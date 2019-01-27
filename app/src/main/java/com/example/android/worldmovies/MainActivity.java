package com.example.android.worldmovies;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements communcation {
    boolean Istablet =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainFragment mainFragment =new MainFragment();
        mainFragment.setLisner(this);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .commit();

        if(findViewById(R.id.Ffragment)!=null)
        {
            Istablet = true;
        }
        else
        {
            Istablet=false;
        }


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setData(Movies movies) {
        //case one pane
        if(!Istablet)
        {
            Bundle extras = new Bundle();
            Intent intent = new Intent(this,detaildActivity.class);
            extras.putString("orignal",movies.getOrignal_Title());
            extras.putString("release",movies.getRelease_date());
            extras.putString("overview",movies.getOverview());
            extras.putDouble("vote_Average",movies.getVote_average());
            extras.putString("poster",movies.getPoster_image());
            extras.putInt("id",movies.getId());
            intent.putExtras(extras);
            startActivity(intent);
        }
        //case two pane
        else
        {

            detail_fragment detail_fragment = new detail_fragment();
            Bundle extras = new Bundle();
            extras.putString("orignal",movies.getOrignal_Title());
            extras.putString("release",movies.getRelease_date());
            extras.putString("overview",movies.getOverview());
            extras.putDouble("vote_Average",movies.getVote_average());
            extras.putString("poster",movies.getPoster_image());
            extras.putInt("id",movies.getId());
            detail_fragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction().replace(R.id.Ffragment,detail_fragment).commit();
        }
    }
}