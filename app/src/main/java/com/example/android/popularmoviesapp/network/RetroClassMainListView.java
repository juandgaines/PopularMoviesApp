package com.example.android.popularmoviesapp.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.android.popularmoviesapp.BuildConfig;
import com.example.android.popularmoviesapp.data.AppExecutors;
import com.example.android.popularmoviesapp.data.Result;
import com.example.android.popularmoviesapp.data.Results;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClassMainListView {

    private static final Object LOCK =new Object();
    private static Retrofit sInstance;



    private static final String BASE_URL_RETROFIT="http://api.themoviedb.org/3/";

    private static Retrofit getRetroInstance(){
        if(sInstance==null){
            synchronized (LOCK){
                Gson gson= new GsonBuilder().create();
                sInstance=new Retrofit.Builder()
                        .baseUrl(BASE_URL_RETROFIT)
                        .addConverterFactory(GsonConverterFactory.create(gson)).build();
            }
        }
        return sInstance;
    }

    public static MovieService getMovieService(){
        return getRetroInstance().create(MovieService.class);
    }




}
