package com.example.android.popularmoviesapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.popularmoviesapp.data.Result;
import com.example.android.popularmoviesapp.data.Results;
import com.example.android.popularmoviesapp.network.MovieService;
import com.example.android.popularmoviesapp.network.RetroClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchViewModel extends ViewModel{

    private List<Result> results;
    private RetroClass retroClass=new RetroClass();
    private MutableLiveData<List<Result>> resultsLiveData;
    private MutableLiveData<BooleanJ> mNetworkProblem;


    public LiveData<BooleanJ> getmNetworkProblem() {

        if( mNetworkProblem==null){
            mNetworkProblem=new MutableLiveData<>();
        }
        return mNetworkProblem;
    }

    public LiveData<List<Result>> getResultsLiveData(String pref, String apiKey){
        if (resultsLiveData==null ){
            mNetworkProblem=new MutableLiveData<>();

            resultsLiveData=new MutableLiveData<>();
            loadLiveData(pref,apiKey);
            //resultsLiveData=retroClass.getResultLiveData(pref,apiKey);
        }

        return resultsLiveData;
    }

    public void initNetLiveData(){
        if( mNetworkProblem==null){
            mNetworkProblem=new MutableLiveData<>();
        }
    }

    public void loadLiveData(String pref,String apiKey){

        MovieService movieService=RetroClass.getMovieService();
        initNetLiveData();


        movieService.getMovies(pref,apiKey).enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {

                Log.v("Retrofit",response.toString());
                List<Result> res=response.body().getResults();
                resultsLiveData.setValue(res);

                BooleanJ booleanJ= new BooleanJ();
                booleanJ.setStatus(false);
                mNetworkProblem.setValue(booleanJ);

            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Log.v("Retrofit","No internet connection");

                BooleanJ booleanJ= new BooleanJ();
                booleanJ.setStatus(true);
                mNetworkProblem.setValue(booleanJ);

            }
        });


    }





}
