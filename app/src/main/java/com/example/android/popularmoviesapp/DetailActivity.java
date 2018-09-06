package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Property;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.example.android.popularmoviesapp.MovieData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    private static final String LOG_TAG= DetailActivity.class.getName().toString();
    @BindView(R.id.name_movie) TextView mMovieTitleDisplay;
    @BindView(R.id.Overview_view)  TextView mMovieOverviewDisplay;
    @BindView(R.id.rate_view)  TextView mMovieRateDisplay;
    @BindView(R.id.date_view)  TextView mMovieReleaseDisplay;
    @BindView(R.id.movie_picture)  ImageView mMoviePostDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intent=getIntent();


        if(intent!=null && intent.hasExtra(MovieData.PARCELABLE)){

            MovieData movieData = intent.getParcelableExtra(MovieData.PARCELABLE);
            String mTitleStr =movieData.getTitle();
            String mPathStr=movieData.getPath();
            String mOverviewStr=movieData.getOverview();
            String mRateStr= movieData.getRate();
            String mDateStr=movieData.getRelease();

            /*mTitleStr =intent.getStringExtra(MovieData.ID_TITLE);
            mPathStr=intent.getStringExtra(MovieData.ID_PATH);
            mDateStr=intent.getStringExtra(MovieData.ID_RELEASE);
            mRateStr=intent.getStringExtra(MovieData.ID_RATE);
            mOverviewStr=intent.getStringExtra(MovieData.ID_OVERVIEW);*/




            mMovieTitleDisplay.setText(mTitleStr);
            Log.v(LOG_TAG,"value of date "+mDateStr);
            if(mDateStr==null|| mDateStr.equals("")) {
                mDateStr= getResources().getString(R.string.error_date_message);
            }
            mMovieReleaseDisplay.setText(mDateStr);

            Log.v(LOG_TAG,"value of rate "+mRateStr);
            if(mRateStr!=null || mRateStr.equals("")){
                mMovieRateDisplay.setText(mRateStr+"/10");
            }
            else{
                mRateStr= getResources().getString(R.string.error_rate_message);
                mMovieRateDisplay.setText(mRateStr);
            }
            Log.v(LOG_TAG,"value of overview "+mOverviewStr);
            if(mOverviewStr==null|| mOverviewStr.equals("")){
                mOverviewStr= getResources().getString(R.string.error_overview_message);
            }

            mMovieOverviewDisplay.setText(mOverviewStr);

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
