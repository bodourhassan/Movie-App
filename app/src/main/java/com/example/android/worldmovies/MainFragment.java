package com.example.android.worldmovies;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
public class MainFragment extends Fragment{
    ImageAdapter imageadapter ;
    List<Movies> Movielist=new ArrayList<>();
    Movies movies = new Movies() ;
    String resultsArray [];
    boolean popular =false;
    boolean top =false;
    boolean favorite =false;
    public communcation  communcation ;

    public MainFragment()
    {
        setHasOptionsMenu(true);
    }

    public void  setLisner (communcation comm)
    {
        communcation=comm;
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
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public  void clickFavorite ()
    {
        Set<String> list= new HashSet<String>();
        ArrayList<Movies> FavoriteMovies=new ArrayList<Movies>();
        SharedPreferences sharedPref = getContext().getSharedPreferences(
                "Fav", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        list=sharedPref.getStringSet("set",new HashSet<String>());
        Iterator iter =list.iterator();
        while (iter.hasNext()) {
            String json= (String) iter.next();
            Movies movies=gson.fromJson(json,Movies.class);
            FavoriteMovies.add(movies);
        }

        imageadapter.UpdateMovieList(FavoriteMovies);
        imageadapter.notifyDataSetChanged();

}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.Top)
        {
            top=true;
            favorite=false;
            popular=false;
            update("http://api.themoviedb.org/3/movie/top_rated?");
        }
        else if(id==R.id.Popular)
        {
            top=false;
            favorite=false;
            popular=true;
           update("http://api.themoviedb.org/3/movie/popular?");
        }
        else if (id==R.id.Favourit) {
            top = false;
            favorite = true;
            popular = false;
            clickFavorite();
        }


        return super.onOptionsItemSelected(item);
    }
    public void update (String base_url)
    {
        if(isOnline())
        {MovieData movieData = new MovieData();
        movieData.execute(base_url);}
    }

    @Override
    public void onStart() {
        super.onStart();

         if(top==true)
        {
            update("http://api.themoviedb.org/3/movie/top_rated?");
        }
        else if (favorite==true)
        {
            clickFavorite();
        }
        else
             update("http://api.themoviedb.org/3/movie/popular?");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        imageadapter = new ImageAdapter(this.getContext());
        gridView.setAdapter(imageadapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                movies = (Movies) imageadapter.getItem(position);
                 communcation.setData(movies);
            }
        });

        return rootView;
    }



    public class MovieData extends AsyncTask<String ,Void, List<Movies>> {

        @Override
        protected List<Movies> doInBackground(String... Base_Url) {
            HttpURLConnection UrlConnection = null;
            BufferedReader reader = null;
            String Movie_base_Url = Base_Url[0];
            String MovieJsonString = null;

            try {
                Uri builtUri = Uri.parse(Movie_base_Url).buildUpon()
                        .appendQueryParameter("api_key", BuildConfig.Movie_API).build();
                URL url = new URL(builtUri.toString());
                UrlConnection = (HttpURLConnection) url.openConnection();
                UrlConnection.setRequestMethod("GET");
                UrlConnection.connect();

                InputStream inputStream = UrlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    MovieJsonString = null;
                } else {
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                }
                MovieJsonString = buffer.toString();
            } catch (IOException e) {
                Log.e("Exception connect:", "Error ", e);
                MovieJsonString = null;
            } finally {
                if (UrlConnection != null) {
                    UrlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("Exception error:", "Error closing stream", e);
                    }
                }
            }

            try {

                    return getMovie_detail(MovieJsonString);
            } catch (JSONException e) {
                e.printStackTrace();

            }
            return null;
        }


        @Override
        protected void onPostExecute(List<Movies> List_of_Movies) {


              imageadapter.UpdateMovieList(List_of_Movies);
              imageadapter.notifyDataSetChanged();
        }


        public List<Movies> getMovie_detail(String JsonString) throws JSONException {
            String Result = "results";
            String Poster_path = "poster_path";
            String original_title = "original_title";
            String overview = "overview";
            String vote_average = "vote_average";
            String release_date = "release_date";
            String id ="id";
            JSONObject MovieObj = new JSONObject(JsonString);
            JSONArray MovieResult = MovieObj.getJSONArray(Result);
            resultsArray = new String[MovieResult.length()];
             Movielist.clear();
            for (int i = 0; i < MovieResult.length(); i++) {
                Movies movies = new Movies();
                JSONObject MovieNum = MovieResult.getJSONObject(i);
                String Poster = MovieNum.getString(Poster_path);
                String original = MovieNum.getString(original_title);
                String over = MovieNum.getString(overview);
                double vote = MovieNum.getDouble(vote_average);
                String release = MovieNum.getString(release_date);
                int id_define = MovieNum.getInt(id);
                movies.setPoster_image(Poster);
                movies.setOrignal_Title(original);
                movies.setOverview(over);
                movies.setVote_average(vote);
                movies.setRelease_date(release);
                movies.setId(id_define);
                Movielist.add(movies);
            }
            return Movielist;
        }
    }
}

