package com.example.android.popularmoviesapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.android.popularmoviesapp.data.AppDatabase;
import com.example.android.popularmoviesapp.data.MovieData;
import com.example.android.popularmoviesapp.data.ResultReviews;
import com.example.android.popularmoviesapp.data.ResultTrailers;
import com.example.android.popularmoviesapp.data.Review;
import com.example.android.popularmoviesapp.data.Trailer;
import com.example.android.popularmoviesapp.network.MovieService;
import com.example.android.popularmoviesapp.network.RetroClassMainListView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class DetailActivityViewModel extends ViewModel{

    private static final String API_KEY = BuildConfig.OPEN_THE_MOVIE_DB_API_KEY;
    private static final String LOG_TAG= DetailActivityViewModel.class.getSimpleName();

    private RetroClassMainListView retroClass=new RetroClassMainListView();
    private MutableLiveData<List<Trailer>>  trailerLiveData;
    private MutableLiveData<List<Review>> reviewsLiveData;


    private MutableLiveData<BooleanJ> mNetworkProblem;
    private MutableLiveData<BooleanJ> mNetworkProblem2;

    LiveData<MovieData> mCurrentMovie;


    public DetailActivityViewModel(AppDatabase mDb, int mMovieId) {

        mCurrentMovie=mDb.favoritesDao().loadFavoriteItemByName(mMovieId);
    }

    public LiveData<MovieData> getmCurrentMovie() {
        return mCurrentMovie;
    }

    public void initNetLiveData(){
        if( mNetworkProblem==null){
            mNetworkProblem=new MutableLiveData<>();
        }
    }

    public void initNetLiveData2(){
        if( mNetworkProblem2==null){
            mNetworkProblem2=new MutableLiveData<>();
        }
    }

    public LiveData<List<Review>> getReviewLiveData(int id){
        if (reviewsLiveData==null ){
            mNetworkProblem2=new MutableLiveData<>();

            reviewsLiveData=new MutableLiveData<>();
            loadLiveDataReviews(id);
            //resultsLiveData=retroClass.getResultLiveData(pref,apiKey);
        }

        return reviewsLiveData;
    }

    public LiveData<List<Trailer>> getTrailersLiveData(int id){
        if (trailerLiveData==null ){
            mNetworkProblem=new MutableLiveData<>();

            trailerLiveData=new MutableLiveData<>();
            loadLiveDataTrailers(id);
            //resultsLiveData=retroClass.getResultLiveData(pref,apiKey);
        }

        return trailerLiveData;
    }

    public void loadLiveDataTrailers(int id){

        MovieService movieService= RetroClassMainListView.getMovieService();
        initNetLiveData();

        movieService.getTrailer(id,API_KEY).enqueue(new Callback<ResultTrailers>() {
            @Override
            public void onResponse(Call<ResultTrailers> call, Response<ResultTrailers> response) {
                Log.v("RetrofitDetailTrailer",response.toString());
                List<Trailer> res=response.body().getResults();
                trailerLiveData.setValue(res);

                BooleanJ booleanJ= new BooleanJ();
                booleanJ.setStatus(false);
                mNetworkProblem.setValue(booleanJ);
            }

            @Override
            public void onFailure(Call<ResultTrailers> call, Throwable t) {
                Log.v("RetrofitDetailTrailer","No internet connection");
                BooleanJ booleanJ= new BooleanJ();
                booleanJ.setStatus(true);
                mNetworkProblem.setValue(booleanJ);
            }
        });

    }
    public void loadLiveDataReviews(int id){

        MovieService movieService= RetroClassMainListView.getMovieService();
        initNetLiveData2();

        movieService.getReviews(id,API_KEY).enqueue(new Callback<ResultReviews>() {
            @Override
            public void onResponse(Call<ResultReviews> call, Response<ResultReviews> response) {
                Log.v("RetrofitDetailReviews",response.toString());
                List<Review> res=response.body().getResults();
                reviewsLiveData.setValue(res);

                BooleanJ booleanJ= new BooleanJ();
                booleanJ.setStatus(false);
                mNetworkProblem2.setValue(booleanJ);
            }

            @Override
            public void onFailure(Call<ResultReviews> call, Throwable t) {
                Log.v("RetrofitDetailReviews","No internet connection");
                BooleanJ booleanJ= new BooleanJ();
                booleanJ.setStatus(true);
                mNetworkProblem2.setValue(booleanJ);
            }
        });

    }
}
