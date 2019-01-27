package com.example.android.worldmovies;

import android.util.Log;


public class Movies  {
String orignal_Title ;
String poster_image;
String overview;
double vote_average;
String release_date;
int id;
    public void setOrignal_Title(String orignal_Title)
    {
        this.orignal_Title=orignal_Title;
    }
    public void setPoster_image(String poster_image)
    {
        String Base_url = "http://image.tmdb.org/t/p/w185/";
           this.poster_image = Base_url.concat(poster_image);

    }
    
    public void setOverview(String overview)
    {
        this.overview=overview;
    }
    public void setVote_average(double vote_average)
    {
        this.vote_average=vote_average;
    }
    public void setRelease_date(String release_date)
    {
        this.release_date=release_date;
    }
    public void setId(int id)
    {this.id=id;
    }
    public String getOrignal_Title()
    {
        return orignal_Title;
    }
    public String getPoster_image()
    {
        return  poster_image;
    }
    public String getOverview()
    {
        return overview;
    }
    public double getVote_average()
    {
        return vote_average;
    }
    public  String getRelease_date()
    {
        return release_date;
    }
    public  int getId() {
        return id;
    }

}
