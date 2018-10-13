package com.example.android.popularmoviesapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Movie;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements TrailersAdapter.TrailerAdapterOnClickHandler{
    private static final String LOG_TAG= DetailActivity.class.getName().toString();
    @BindView(R.id.name_movie) TextView mMovieTitleDisplay;
    @BindView(R.id.Overview_view)  TextView mMovieOverviewDisplay;
    @BindView(R.id.rate_view)  TextView mMovieRateDisplay;
    @BindView(R.id.date_view)  TextView mMovieReleaseDisplay;
    @BindView(R.id.movie_picture)  ImageView mMoviePostDisplay;
    @BindView(R.id.favoriteButton)  ImageView mFavortite;
    @BindView(R.id.trailer_listview) RecyclerView mTrailerListView;
    @BindView(R.id.reviews_listview) RecyclerView mReviewsListView;

    private ReviewsAdapter reviewsAdapter;
    private TrailersAdapter trailersAdapter;


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

            DetailViewModelFactory factory =new DetailViewModelFactory(mDb,id_movie);

            final DetailActivityViewModel viewModel=
                    ViewModelProviders.of(this,factory).get(DetailActivityViewModel.class);


            viewModel.getmCurrentMovie().observe(this, new Observer<MovieData>() {
                @Override
                public void onChanged(@Nullable MovieData movieData) {
                    if(viewModel.getmCurrentMovie().getValue()!=null){
                        mFavortite.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                    }
                    else{
                        mFavortite.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                    }

                    populateUI(id_movie,mTitleStr,mPathStr,mOverviewStr,mRateStr,mDateStr);
                }
            });



            viewModel.getTrailersLiveData(id_movie).observe(this, new Observer<List<Trailer>>() {
                @Override
                public void onChanged(@Nullable List<Trailer> trailers) {

                    trailersAdapter= new TrailersAdapter(DetailActivity.this,trailers);
                    mTrailerListView.setAdapter(trailersAdapter);

                }
            });

            viewModel.getReviewLiveData(id_movie).observe(this, new Observer<List<Review>>() {
                @Override
                public void onChanged(@Nullable List<Review> reviews) {

                    reviewsAdapter= new ReviewsAdapter(reviews);
                    mReviewsListView.setAdapter(reviewsAdapter);

                }
            });


            LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            LinearLayoutManager layoutManager2= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            mReviewsListView.setLayoutManager(layoutManager);
            mTrailerListView.setLayoutManager(layoutManager2);
            mTrailerListView.setHasFixedSize(true);
            mReviewsListView.setHasFixedSize(true);

            mFavortite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    final MovieData movieCache=new MovieData(mTitleStr,mOverviewStr,Double.parseDouble(mRateStr) ,mDateStr, mPathStr,id_movie);

                    AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if(viewModel.getmCurrentMovie().getValue()!=null){
                                mDb.favoritesDao().deleteFavoriteMovie(viewModel.getmCurrentMovie().getValue());
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

        }

    }


    @Override
    public void onClick(Trailer trailerData) {
        Uri url =Uri.parse("https://www.youtube.com/watch?v="+ trailerData.getKey());

        Intent intent =new Intent(Intent.ACTION_VIEW,url);

        intent.putExtra(Intent.EXTRA_TEXT, url.toString());


        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }


    }

    private void  populateUI(int id_movie,String mTitleStr,String mPathStr,String mOverviewStr,String mRateStr, String mDateStr){
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
