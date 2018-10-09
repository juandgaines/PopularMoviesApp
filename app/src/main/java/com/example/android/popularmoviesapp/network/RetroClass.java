package com.example.android.popularmoviesapp.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.example.android.popularmoviesapp.MainActivity;
import com.example.android.popularmoviesapp.MovieAdapter;
import com.example.android.popularmoviesapp.data.Result;
import com.example.android.popularmoviesapp.data.Results;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClass {

    private static final String BASE_URL_RETROFIT="http://api.themoviedb.org/3/";
    private static Retrofit getRetroInstance(){
        Gson gson= new GsonBuilder().create();

        return new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
    }

    public static MovieService getMovieService(){
        return getRetroInstance().create(MovieService.class);
    }



    public LiveData<List<Result>> getResultLiveData(String pref, String apikey, final MutableLiveData<List<Result>> mutableLiveData){


        MovieService movieService=RetroClass.getMovieService();


        movieService.getMovies(pref,apikey).enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {

                Log.v("Retrofit",response.toString());
                List<Result> res=response.body().getResults();
                mutableLiveData.setValue(res);

            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {

            }
        });

        return mutableLiveData;
    }
}
