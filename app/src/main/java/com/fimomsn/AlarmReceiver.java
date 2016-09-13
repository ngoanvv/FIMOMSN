package com.fimomsn;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.fimomsn.model.Record;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    public void onReceive(Context context, Intent intent) {
        makeNotification(context,0,"Alarm : "+Calendar.getInstance().get(Calendar.MINUTE)+"");
        if(isConnected(context)) {
            requestDatachange(context);
        }
    }


    private void requestDatachange(final Context context)
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("status");
        Query query = databaseReference.orderByKey().limitToLast(10);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("query",dataSnapshot.getChildrenCount()+"");
                ArrayList<Record> records = new ArrayList<Record>();
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    Record record = new Record();
                    record.setTemp(messageSnapshot.child("temp").getValue().toString()+(char) 0x00B0 +"C");
                    record.setTime(messageSnapshot.child("time").getValue().toString()+" , "+messageSnapshot.child("date").getValue().toString());
                    records.add(record);
                }
                if(Integer.valueOf(records.get(9).getTemp().substring(0,2))>22)
                    makeNotification(context,12121,"Alarm : Chú ý, nhiệt độ là "+records.get(9).getTemp()+" lúc : "+records.get(9).getTime());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private boolean isConnected(Context context)
    {

        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

        if (ni != null && ni.isConnected())
        {
            return true;
        }
            return false;
    }
    private void makeNotification(Context context,int id,String content)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("FIMO MSN")
                        .setContentText(content);
        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT  );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(id, mBuilder.build());
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1000);
        }
        else
        {
        }
    }
}
