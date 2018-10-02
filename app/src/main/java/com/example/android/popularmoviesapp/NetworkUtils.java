package com.example.android.popularmoviesapp;

import android.arch.lifecycle.ViewModelProvider;
import android.net.Uri;
import android.util.Log;

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

public class NetworkUtils {

    public static final String LOG_TAG = NetworkUtils.class.getName().toString();

    public static List<MovieData> getNetworkResponse(String preference) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String movieJsonStr = null;
        try {


            String apiKey = BuildConfig.OPEN_THE_MOVIE_DB_API_KEY;

            final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/";

            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendPath(preference)
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

                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {

                return null;
            }
            movieJsonStr = buffer.toString();


        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);

            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
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

    private static List<MovieData> getDescriptionsDataFromJson(String forecastJsonStr)
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

        }


        return temp;
    }
}

