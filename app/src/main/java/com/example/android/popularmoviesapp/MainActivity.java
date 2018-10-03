package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;

import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
//LoaderManager.LoaderCallbacks<List<MovieData>>
public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,LoaderManager.LoaderCallbacks<List<MovieData>>
        ,SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String LOG_TAG=MainActivity.class.getName().toString();
    public static final int APP_LOADER_ID=11;
    private static final String SEARCH_PREFERENCE_EXTRA ="preference-query";


    @BindView(R.id.my_recycler_view)  RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    @BindView(R.id.pb_loading_indicator)ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message_display)TextView mErrorMessage;
    private List<MovieData> mMovieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        PreferenceManager.setDefaultValues(this,R.xml.pref_general,false);

        GridLayoutManager layoutManager= new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);


        mRecyclerView.setHasFixedSize(true);


        mMovieAdapter= new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        String syncConnPref = sharedPref.getString(getResources().getString(R.string.pref_order_key),"");
        Bundle queryBundle=new Bundle();

        queryBundle.putString(SEARCH_PREFERENCE_EXTRA,syncConnPref);
        //new GetMovies(this).execute(syncConnPref);


        LoaderManager loaderManager=getSupportLoaderManager();
        Loader<List<MovieData>> moviesAppSearchLoader= loaderManager.getLoader(APP_LOADER_ID);

        loaderManager.initLoader(APP_LOADER_ID,queryBundle,MainActivity.this);


    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String syncConnPref = sharedPref.getString(getResources().getString(R.string.pref_order_key),"");
        Log.v(LOG_TAG,"Preference "+syncConnPref);
        //new GetMovies(this).execute(syncConnPref);
        //
    }

    @Override
    public void onClick(MovieData movieData) {

        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intent= new Intent(context,destinationClass);
        intent.putExtra(MovieData.PARCELABLE,movieData);
        startActivity(intent);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


        if(key.equals(getString(R.string.pref_order_key))){
            LoaderManager manager = getSupportLoaderManager();
            Bundle args = new Bundle();
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            String syncConnPref = sharedPref.getString(getResources().getString(R.string.pref_order_key),"");
            args.putString(SEARCH_PREFERENCE_EXTRA,syncConnPref);

            manager.restartLoader(APP_LOADER_ID,args,this);
        }




    }

    @NonNull
    @Override
    public Loader<List<MovieData>> onCreateLoader(int id, @Nullable final  Bundle args) {
        return new AsyncTaskLoader<List<MovieData>>(this) {
            List<MovieData> mData;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);
                //Do not forget to add this if statement
                if (mData != null) {
                    deliverResult(mData);
                } else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public List<MovieData> loadInBackground() {
                String preference = args.getString(SEARCH_PREFERENCE_EXTRA);
                if (preference == null || TextUtils.isEmpty(preference)) {
                    return null;
                }
                return NetworkUtils.getNetworkResponse(preference);
            }

            //Do not forget to override deliverResult function
            @Override
            public void deliverResult(@Nullable List<MovieData> data) {
                mData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<MovieData>> loader, List<MovieData> movieData) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (movieData != null) {
            showMovieDataView();
            String[] array = new String[movieData.size()];
            for(int i=0; i<array.length;i++){

                array[i]=movieData.get(i).getPath();

            }

            mMovieAdapter.setMovieData(movieData);
            mMovieAdapter.notifyDataSetChanged();
        } else {
            showErrorMessage();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<MovieData>> loader) {
        mMovieAdapter.restartData();
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
        return super.onOptionsItemSelected(item);
    }

    private void showMovieDataView() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
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
