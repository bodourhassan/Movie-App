package com.example.android.worldmovies;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import java.util.List;
public class trailerconnect extends AsyncTask<String ,Void,String> {
    Context context;
    View rootview ;
    List<Trailer> Trailerlist = new ArrayList<>();
    TrailerAdapter trailerAdapter ;
    public  trailerconnect (Context context, View rootview)
    {
        this.rootview=rootview;
        this.context =context;
    }
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection UrlConnection = null;
        BufferedReader reader = null;
        String Movie_base_Url = params[0];
        String ReviewJsonString = null;
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
                ReviewJsonString = null;
            } else {
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
            }
            ReviewJsonString = buffer.toString();
        } catch (IOException e) {
            Log.e("Exception connect:", "Error ", e);
            ReviewJsonString = null;
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
        return ReviewJsonString;
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        android.widget.ListAdapter listAdapter=listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = ListView.MeasureSpec.makeMeasureSpec(listView.getWidth(), ListView.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, ListView.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }

    @Override
    protected void onPostExecute(String s) {
        String result ="results";
        String key;
        String name;
        { try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray =jsonObject.getJSONArray(result);
            Trailerlist.clear();
            for (int i=0;i<jsonArray.length();i++)
            {
                Trailer trailer =new Trailer();
                JSONObject TrailerObj = jsonArray.getJSONObject(i);
                key= TrailerObj.getString("key");
                name = TrailerObj.getString("name");
               trailer.setKey(key);
               trailer.setName(name);
               Trailerlist.add(trailer);

            }
             trailerAdapter=new TrailerAdapter(context);
             trailerAdapter.UpdateTrailer(Trailerlist);

            trailerAdapter.notifyDataSetChanged();
            ListView listView = (ListView) rootview.findViewById(R.id.ListView_Trailer);
            listView.setAdapter(trailerAdapter);
            setListViewHeightBasedOnChildren(listView);
        } catch (JSONException e) {
            e.printStackTrace();
        }}
    }
}
