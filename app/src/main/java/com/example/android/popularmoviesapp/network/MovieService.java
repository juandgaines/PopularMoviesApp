package com.example.android.popularmoviesapp.network;

import android.arch.lifecycle.LiveData;

import com.example.android.popularmoviesapp.data.ResultReviews;
import com.example.android.popularmoviesapp.data.ResultTrailers;
import com.example.android.popularmoviesapp.data.Results;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

@GET("movie/{preference}")
Call<Results> getMovies(@Path("preference")String orderBy, @Query("api_key") String apiKey);

@GET("movie/{id}/videos")
Call<ResultTrailers>getTrailer(@Path("id")int id, @Query("api_key") String apiKey);

@GET("movie/{id}/reviews")
Call<ResultReviews>getReviews(@Path("id")int id, @Query("api_key") String apiKey);
}


