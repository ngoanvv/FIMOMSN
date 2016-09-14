package com.fimomsn.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fimomsn.R;
import com.fimomsn.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diep_Chelsea on 25/08/2016.
 */
public class CustomAdapter extends ArrayAdapter<Record> {
    private Activity context;
    private int layoutID;
    private List<Record> data;


    public CustomAdapter(Activity context, int resource, List<Record> objects) {
        super(context, resource, objects);
        this.context=context;
        this.layoutID=resource;
        this.data=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= context.getLayoutInflater();
        convertView=inflater.inflate(layoutID, null);
        TextView status = (TextView) convertView.findViewById(R.id.item_status);
        TextView temp = (TextView) convertView.findViewById(R.id.item_temp);
        TextView time = (TextView) convertView.findViewById(R.id.item_time);
        if(data.get(position).getStatus()=="cold") {
            status.setText("Mát");
            status.setTextColor(Color.BLUE);
            temp.setText(data.get(position).getTemp());
            time.setText(data.get(position).getTime());
            temp.setTextColor(Color.BLUE);
            time.setTextColor(Color.BLUE);

        }
        else
        {
            status.setText("Nóng");
            status.setTextColor(Color.RED);
            temp.setText(data.get(position).getTemp());
            time.setText(data.get(position).getTime());
            temp.setTextColor(Color.RED);
            time.setTextColor(Color.RED);

        }

        return convertView;

    }
}
