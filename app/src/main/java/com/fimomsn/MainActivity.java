package com.fimomsn;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fimomsn.adapter.CustomAdapter;
import com.fimomsn.model.Record;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends Activity implements  ChildEventListener {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView tv_temperature,tv_time;
    private String TAG="MSN";
    private ListView listView;
    private ArrayList<Record> records;
    private ArrayList<Record> listData;
    private CustomAdapter adapter;
    private Context context;
    private int alarmID=111;
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        requestDatachange();
        cancelAlarm();
    }
    private void initView()
    {
        tv_temperature = (TextView) findViewById(R.id.tv_temperature);
        tv_time = (TextView) findViewById(R.id.tv_time);
        listView = (ListView) findViewById(R.id.listview);
        tv_temperature.setText("?? "+(char) 0x00B0 );

    }
    private void cancelAlarm()
    {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }
    private void setAlarm()
    {
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 8000;
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
    }
    private boolean isConnected(Context context)
    {

        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

        if (ni != null && ni.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"onPAuse");
        if(isConnected(MainActivity.this)) setAlarm();
        super.onPause();
    }

    @Override
    protected void onResume() {
        cancelAlarm();
        Log.d(TAG,"onResume");
        super.onResume();
    }


    private void makeNotification(String content)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("FIMO MSN")
                        .setContentText(content);
        Intent resultIntent = new Intent(MainActivity.this, MainActivity.class);
        TaskStackBuilder stackBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder = TaskStackBuilder.create(MainActivity.this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT  );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(10, mBuilder.build());
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1000);
        }
        else
        {

        }
    }


    private void requestDatachange()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("status");
        databaseReference.addChildEventListener(this);
        Query query = databaseReference.orderByKey().limitToLast(10);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                records = new ArrayList<>(8);
                listData = new ArrayList<Record>();
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    Record record = new Record();
                    if( Integer.valueOf(messageSnapshot.child("temp").getValue().toString().substring(0,2)) > 29 ) {
                        record.setStatus("hot");
                    }
                    else {
                        record.setStatus("cold");
                    }
                    record.setTemp(messageSnapshot.child("temp").getValue().toString()+(char) 0x00B0 +"C");
                    record.setTime(messageSnapshot.child("time").getValue().toString()+" , "+messageSnapshot.child("date").getValue().toString());
                    records.add(record);
                }
                if(Integer.valueOf(records.get(9).getTemp().substring(0,2))>29)
                    makeNotification("Chú ý, nhiệt độ là "+records.get(9).getTemp()+" lúc : "+records.get(9).getTime());
                Log.d(TAG,"size:"+records.size()+"");
                updateDate(records);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void updateDate(ArrayList<Record> data)
    {
        adapter = new CustomAdapter(MainActivity.this,R.layout.item_listview,data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        tv_temperature.setText(dataSnapshot.child("temp").getValue().toString().substring(0,2)+(char) 0x00B0 );
        tv_time.setText(dataSnapshot.child("time").getValue().toString()+", "+dataSnapshot.child("date").getValue().toString());

    }


    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }


}

