package com.example.android.worldmovies;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static android.R.attr.data;
import static android.R.attr.id;
import static android.R.attr.logo;
import static android.system.Os.remove;
import static java.security.AccessController.getContext;

/**
 * Created by future on 26/10/2016.
 */

public class detail_fragment extends Fragment {
    reviewconnect reviewconnect;
    trailerconnect trailerconnect;
    String baseurl ="http://api.themoviedb.org/3/movie/";
    Set<String> Set = new HashSet<>();
    Set<String> Editedset = new HashSet<>();
    Movies movies_list = new Movies();
    int Movie_id ;
    String original_title;
    String release_date;
    String overview;
    double vote_average;
    String poster;
    private void SavingFavoriteORNot(boolean CheckFavourite) {
        SharedPreferences aSharedPreferenes = getContext().getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        SharedPreferences.Editor aSharedPreferenesEdit = aSharedPreferenes
                .edit();
        aSharedPreferenesEdit.putBoolean("state", CheckFavourite  );
        aSharedPreferenesEdit.commit();
    }


    private boolean ReadingCurrentState() {
        SharedPreferences aSharedPreferenes =getContext().getSharedPreferences(
                "Favourite", Context.MODE_PRIVATE);
        return aSharedPreferenes.getBoolean("state",true);
    }
    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.detail_fragment, container, false);
        Bundle extras = getArguments();
        if (extras != null ) {
            original_title = extras.getString("orignal");
            release_date = extras.getString("release");
            overview = extras.getString("overview");
            vote_average = extras.getDouble("vote_Average");
            poster = extras.getString("poster");
            Movie_id= extras.getInt("id");
            ((TextView) rootView.findViewById(R.id.detail_text)).setText(original_title);
            ((TextView) rootView.findViewById(R.id.release_date)).setText("Realsedate :\n" + release_date);
            TextView textView = (TextView) rootView.findViewById(R.id.average_rate);
            textView.setText(textView.getText() + "\n" + vote_average);
            ((TextView) rootView.findViewById(R.id.overview)).setText("\nOverview :\n\n" + overview);
            ImageView imageView = ((ImageView) rootView.findViewById(R.id.poster_image));
            Picasso.with(getContext()).load(poster).into(imageView);

        }
        poster = poster.substring(32);
        Log.e("settttttttt",poster);
        movies_list.setId(Movie_id);
        movies_list.setPoster_image(poster);
        movies_list.setVote_average(vote_average);
        movies_list.setOverview(overview);
        movies_list.setRelease_date(release_date);
        movies_list.setOrignal_Title(original_title);
        if(isOnline()) {
            reviewconnect = new reviewconnect(rootView);
            reviewconnect.execute(baseurl + Movie_id + "/reviews?");
            trailerconnect = new trailerconnect(this.getContext(), rootView);
            trailerconnect.execute(baseurl + Movie_id + "/videos?");
        }
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                "Fav", Context.MODE_PRIVATE);
        final ImageButton favourite=(ImageButton)rootView.findViewById(R.id.favorite_icon);
        Set=sharedPref.getStringSet("set",new HashSet<String>());
        final Gson gson = new Gson();
        Iterator iter = Set.iterator();
        while (iter.hasNext()) {
            String json= (String) iter.next();
            Movies movies =gson.fromJson(json,Movies.class);
            if(Movie_id == (movies.getId())){
                SavingFavoriteORNot(true);
                favourite.setBackgroundResource(R.drawable.favorite_after);
                break;
            }
        }
        favourite.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View view) {
                boolean isFavourite = ReadingCurrentState();
                SharedPreferences sharedPref = getContext().getSharedPreferences(
                        "Fav", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                String json = gson.toJson(movies_list);

                if (isFavourite) {
                    favourite.setBackgroundResource(R.drawable.favorite_before);
                    isFavourite = false;
                    SavingFavoriteORNot(isFavourite);
                    Editedset=sharedPref.getStringSet("set",new HashSet<String>());
                    Editedset.remove(json);
                    editor.remove("set");
                    editor.commit();
                    editor.putStringSet("set",Editedset);
                    editor.commit();

                } else {
                    favourite.setBackgroundResource(R.drawable.favorite_after);
                    isFavourite = true;
                    SavingFavoriteORNot(isFavourite);
                    Editedset=sharedPref.getStringSet("set",new HashSet<String>());
                    Editedset.add(json);
                    editor.remove("set");
                    editor.commit();
                    editor.putStringSet("set",Editedset);
                    editor.commit();
                }

            }
        });

        return rootView;
    }
}