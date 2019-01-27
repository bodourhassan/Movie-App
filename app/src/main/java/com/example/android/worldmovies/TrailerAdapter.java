package com.example.android.worldmovies;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by future on 20/11/2016.
 */

public class TrailerAdapter extends BaseAdapter {
    private Context context;
    public List<Trailer> TrailerList =new ArrayList<>();
    public TrailerAdapter ()
    {
    }
    public TrailerAdapter (Context context)
    {
        this.context=context;
    }
    public void UpdateTrailer (List<Trailer> TrailerList)
    {
        this.TrailerList=TrailerList;
    }
    @Override
    public int getCount() {
        return TrailerList.size();
    }



    @Override
    public Object getItem(int position) {
        return TrailerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView textView=null;
        ImageButton imageButton;
        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.list_item_content, null);
        }
        textView = (TextView) convertView.findViewById(R.id.trailer);
        textView.setText(TrailerList.get(position).getName());
        imageButton =(ImageButton)convertView.findViewById(R.id.Clickbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(("https://www.youtube.com/watch?v=")
                        .concat(TrailerList.get(position).getKey()))));
            }
        });

        return convertView;
    }
}
