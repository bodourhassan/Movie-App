package com.example.android.worldmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class ImageAdapter extends BaseAdapter {
    private Context context;
    public List<Movies> moviesList =new ArrayList<>();
    public ImageAdapter(Context context){
      this.context=context;

    }
    public void UpdateMovieList (List<Movies> Movielist)
    {
        this.moviesList=Movielist;
    }
    @Override
    public int getCount() {
        return moviesList.size();
    }

    @Override
    public Object getItem(int position) {
       return moviesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView==null)
        {
           convertView= LayoutInflater.from(context).inflate(R.layout.image_fragment, null);
        }

            imageView = (ImageView) convertView.findViewById(R.id.Image);

        Picasso.with(context).load(moviesList.get(position).getPoster_image()).into(imageView);
        return convertView;
    }
}
