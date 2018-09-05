package com.example.android.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    public static final String LOG_TAG=MainActivity.class.getName().toString();


    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    private List<MovieData> mMovieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this,R.xml.pref_general,false);

        mRecyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);
        mErrorMessage =(TextView) findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager= new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);


        mRecyclerView.setHasFixedSize(true);


        mMovieAdapter= new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String syncConnPref = sharedPref.getString(getResources().getString(R.string.pref_order_key),"");
        new GetMovies(this).execute(syncConnPref);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String syncConnPref = sharedPref.getString(getResources().getString(R.string.pref_order_key),"");
        Log.v(LOG_TAG,"Preference "+syncConnPref);
        new GetMovies(this).execute(syncConnPref);
        //




    }

    @Override
    public void onClick(MovieData movieData) {
        String title= movieData.getTitle();
        String overview= movieData.getOverview();
        String path=movieData.getPath();
        String rate=movieData.getRate();
        String release=movieData.getRelease();

        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intent= new Intent(context,destinationClass);

        intent.putExtra(MovieData.ID_TITLE,title);
        intent.putExtra(MovieData.ID_OVERVIEW,overview);
        intent.putExtra(MovieData.ID_PATH,path);
        intent.putExtra(MovieData.ID_RATE,rate);
        intent.putExtra(MovieData.ID_RELEASE,release);

        startActivity(intent);



    }




    public class GetMovies extends AsyncTask<String, Void, List<MovieData> > {
        private final String LOG_TAG = GetMovies.class.getSimpleName();
        private Context mContext;

        public GetMovies(Context context){

            mContext= context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<MovieData> doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                String sort = sharedPrefs.getString(getString(R.string.pref_order_key),
                        getString(R.string.pref_order_defalult));

                /*SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                        mMovieData.clear();
                        mMovieAdapter.notifyDataSetChanged();

                    }
                };*//*

                sharedPrefs.registerOnSharedPreferenceChangeListener(listener);*/


                String apiKey = BuildConfig.OPEN_THE_MOVIE_DB_API_KEY;
                //String sort="popularity.desc";
                //vote_average.desc





                final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";

                final String SORT_PARAM = "sort_by";

                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, strings[0])
                        .appendQueryParameter(APPID_PARAM, apiKey)
                        .build();


                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Link: " + builtUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();

                // Log.v(LOG_TAG, "Cadena" + movieJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            try {
                return getDescriptionsDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        private List<MovieData> getDescriptionsDataFromJson(String forecastJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String OWM_TITLE = "original_title";
            final String OWM_OVERVIEW = "overview";
            final String OWM_VOTE = "vote_average";
            final String OWM_RELEASE = "release_date";
            final String OWM_RESULTS = "results";
            final String OWM_POSTER = "poster_path";


            JSONObject movieJson = new JSONObject(forecastJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(OWM_RESULTS);
            String originalTitle;
            String overview;
            String voteAverage;
            String releaseDate;
            String posterPath;
            List<MovieData> temp = new ArrayList<>();

            for (int i = 0; i < movieArray.length(); i++) {

                JSONObject objeto = movieArray.getJSONObject(i);
                originalTitle = objeto.getString(OWM_TITLE);
                overview = objeto.getString(OWM_OVERVIEW);
                voteAverage = objeto.getString(OWM_VOTE);
                releaseDate = objeto.getString(OWM_RELEASE);
                posterPath = objeto.getString(OWM_POSTER);

                MovieData objetoTemporal = new MovieData(originalTitle,
                        overview,
                        voteAverage,
                        releaseDate,
                        posterPath);

                temp.add(objetoTemporal);
                //Resultados.add(0,objetoTemporal);
                //Log.v(LOG_TAG,"Title: "+ originalTitle);
                //Log.v(LOG_TAG,"overview: "+ overview);
                //Log.v(LOG_TAG,"Vote: "+voteAverage);
                //Log.v(LOG_TAG,"Release: "+releaseDate);
                //Log.v(LOG_TAG,"Post: "+ posterPath);
            }


            return temp;

        }

        @Override
        protected void onPostExecute(List<MovieData> movieData) {


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

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
}
