package com.example.android.popularmoviesapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.android.popularmoviesapp.data.MovieData;
import com.example.android.popularmoviesapp.data.Result;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler
        ,SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String LOG_TAG=MainActivity.class.getName().toString();
    private FetchViewModel fetchViewModel;


    @BindView(R.id.my_recycler_view)  RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private FavoriteMovieAdapter mMovieAdapter2;
    @BindView(R.id.pb_loading_indicator)ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message_display)TextView mErrorMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        FetchMode();
    }


    void FetchMode(){
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);

        fetchViewModel= ViewModelProviders.of(this).get(FetchViewModel.class);

        PreferenceManager.setDefaultValues(this,R.xml.pref_general,false);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        String syncConnPref = sharedPref.getString(getResources().getString(R.string.pref_order_key),"");
        String apiKey = BuildConfig.OPEN_THE_MOVIE_DB_API_KEY;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        final int height = size.y;

        Log.v("Width",Integer.toString(width));

        fetchViewModel.getResultsLiveData(syncConnPref,apiKey).observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(@Nullable List<Result> results) {

                mMovieAdapter = new MovieAdapter(MainActivity.this,results, width,height);

                mRecyclerView.setAdapter(mMovieAdapter);

            }
        });

        fetchViewModel.getmNetworkProblem().observe(this, new Observer<BooleanJ>() {
            @Override
            public void onChanged(@Nullable BooleanJ booleanJ) {
                boolean status=booleanJ.getStatus();
                if(status){
                    showErrorMessage();
                }
                else{

                    showMovieDataView();
                }
            }
        });


        GridLayoutManager layoutManager= new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);


    }


    @Override
    public void onClick(Result movieData) {

        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intent= new Intent(context,destinationClass);

        String title = movieData.getTitle();
        String overview = movieData.getOverview();
        String release = movieData.getReleaseDate();
        Log.v("Release:", release);
        double rate = movieData.getVoteAverage();
        String path = movieData.getPosterPath();
        int id_movie=movieData.getId();

        MovieData movie = new MovieData(title,overview,rate,release, path,id_movie);
        intent.putExtra(MovieData.PARCELABLE,movie);
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals(getString(R.string.pref_order_key))){

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            String syncConnPref = sharedPref.getString(getResources().getString(R.string.pref_order_key),"");
            fetchViewModel.loadLiveData(syncConnPref);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startIntentSettings = new Intent(this, SettingsActivity.class);
            startActivity(startIntentSettings);
            return true;
        }

        else if (id == R.id.action_favorites) {
            Intent startIntentFavorites = new Intent(this, FavoritesActivity.class);
            startActivity(startIntentFavorites);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMovieDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.unregisterOnSharedPreferenceChangeListener(this);
    }


}
