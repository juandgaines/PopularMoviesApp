package com.example.android.popularmoviesapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Movie;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.data.AppDatabase;
import com.example.android.popularmoviesapp.data.AppExecutors;
import com.example.android.popularmoviesapp.data.ResultReviews;
import com.example.android.popularmoviesapp.data.ResultTrailers;
import com.example.android.popularmoviesapp.data.Review;
import com.example.android.popularmoviesapp.data.Trailer;
import com.example.android.popularmoviesapp.network.MovieService;
import com.example.android.popularmoviesapp.network.RetroClassMainListView;
import com.squareup.picasso.Picasso;

import com.example.android.popularmoviesapp.data.MovieData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private static final String LOG_TAG= DetailActivity.class.getName().toString();
    @BindView(R.id.name_movie) TextView mMovieTitleDisplay;
    @BindView(R.id.Overview_view)  TextView mMovieOverviewDisplay;
    @BindView(R.id.rate_view)  TextView mMovieRateDisplay;
    @BindView(R.id.date_view)  TextView mMovieReleaseDisplay;
    @BindView(R.id.movie_picture)  ImageView mMoviePostDisplay;
    @BindView(R.id.favoriteButton)  ImageView mFavortite;

    AppDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intent=getIntent();

        mDb= AppDatabase.getsInstance(getApplicationContext());

        if(intent!=null && intent.hasExtra(MovieData.PARCELABLE)){

            final MovieData movieData = intent.getParcelableExtra(MovieData.PARCELABLE);
            final int id_movie=movieData.getMovieId();
            final String mTitleStr =movieData.getTitle();
            final String mPathStr=movieData.getPosterPath();
            final String mOverviewStr=movieData.getOverview();
            final String mRateStr= Double.toString(movieData.getVoteAverage()) ;
            final String mDateStr=movieData.getReleaseDate();

            Log.v(LOG_TAG,"MovieData"+ movieData.toString());
            final LiveData<MovieData> movie =mDb.favoritesDao().loadFavoriteItemByName(id_movie);

            movie.observe(this, new Observer<MovieData>() {
                @Override
                public void onChanged(@Nullable MovieData movieData) {
                    if(movie.getValue()!=null){
                        mFavortite.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                    }
                    else{
                        mFavortite.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                    }
                }
            });



            MovieService movieService= RetroClassMainListView.getMovieService();

            movieService.getTrailer(id_movie,BuildConfig.OPEN_THE_MOVIE_DB_API_KEY).enqueue(new Callback<ResultTrailers>() {
                @Override
                public void onResponse(Call<ResultTrailers> call, Response<ResultTrailers> response) {

                    ResultTrailers cache= response.body();
                    List<Trailer> movieTrailers=cache.getResults();

                    for(Trailer trailer:movieTrailers){

                        Log.v( "TRAILER", "https://www.youtube.com/watch?v="+trailer.getKey());
                    }


                }

                @Override
                public void onFailure(Call<ResultTrailers> call, Throwable t) {

                }
            });

            movieService.getReviews(id_movie,BuildConfig.OPEN_THE_MOVIE_DB_API_KEY).enqueue(new Callback<ResultReviews>() {
                @Override
                public void onResponse(Call<ResultReviews> call, Response<ResultReviews> response) {
                    ResultReviews cache= response.body();
                    List<Review> movieReviews=cache.getResults();

                    for(Review review:movieReviews){

                        Log.v( "REVIEWS", review.getAuthor()+ ": "+ review.getContent());
                    }

                }

                @Override
                public void onFailure(Call<ResultReviews> call, Throwable t) {

                }
            });

            mFavortite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final MovieData movieCache=new MovieData(mTitleStr,mOverviewStr,Double.parseDouble(mRateStr) ,mDateStr, mPathStr,id_movie);

                    AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {

                            if(movie.getValue()!=null){
                                mDb.favoritesDao().deleteFavoriteMovie(movie.getValue());
                                Log.v(LOG_TAG,"MovieData removed");
                            }
                            else {
                                mDb.favoritesDao().insertFavoriteMovie(movieCache);
                                Log.v(LOG_TAG,"MovieData inserted");
                            }
                        }
                    });

                }
            });

            mMovieTitleDisplay.setText(mTitleStr);
            Log.v(LOG_TAG,"value of date "+mDateStr);
            if(mDateStr==null|| mDateStr.equals("")) {
                //mDateStr= getResources().getString(R.string.error_date_message);
                mMovieReleaseDisplay.setText(getResources().getString(R.string.error_date_message));
            }
            else{
                mMovieReleaseDisplay.setText(mDateStr);
            }


            Log.v(LOG_TAG,"value of rate "+mRateStr);
            if(mRateStr!=null || mRateStr.equals("")){
                mMovieRateDisplay.setText(mRateStr+"/10");
            }
            else{
                //mRateStr= getResources().getString(R.string.error_rate_message);
                mMovieRateDisplay.setText(getResources().getString(R.string.error_rate_message));
            }
            Log.v(LOG_TAG,"value of overview "+mOverviewStr);
            if(mOverviewStr==null|| mOverviewStr.equals("")){
                mMovieOverviewDisplay.setText(getResources().getString(R.string.error_overview_message));
            }
            else{
                mMovieOverviewDisplay.setText(mOverviewStr);
            }


            Log.v(LOG_TAG,"value of post: "+mPathStr);


            if(!mPathStr.equals("null")){
                Picasso.with(this)
                        .load(MovieData.BASE_LINK +mPathStr)
                        .resize(600,1000)
                        .centerInside()
                        .into(mMoviePostDisplay);
            }
            else{
                Picasso.with(this)
                        .load(R.drawable.not_found)
                        .resize(600,800)
                        .into(mMoviePostDisplay);
            }

        }

    }
}
