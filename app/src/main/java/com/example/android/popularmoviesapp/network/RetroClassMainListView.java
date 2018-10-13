package com.example.android.popularmoviesapp.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
