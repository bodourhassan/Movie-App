package com.example.android.worldmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class reviewconnect extends AsyncTask<String ,Void,String> {
    String ReviewList = "";
    View rootView ;
    public reviewconnect(View v)
    {
       this.rootView=v;
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

    @Override
    protected void onPostExecute(String s) {
        String Result = "results";
        String Author;
        String content;

           try {
               Log.e("review json ",s);
               JSONObject jsonObject = new JSONObject(s);
               JSONArray jsonArray = jsonObject.getJSONArray(Result);
               for (int i = 0; i < jsonArray.length(); i++) {
                   JSONObject review = jsonArray.getJSONObject(i);
                   Author = review.getString("author");
                   content = review.getString("content");
                   ReviewList += "Author : " + Author + "\n" + "Content :" + content + "\n\n\n";
               }
               TextView review = (TextView) rootView.findViewById(R.id.review);
               review.setText("\n\nReview :\n\n" + ReviewList);
           } catch (JSONException e) {
               e.printStackTrace();
           }


    }
}

