package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import com.example.android.popularmoviesapp.MovieData;

public class DetailActivity extends AppCompatActivity {
    private static final String LOG_TAG= DetailActivity.class.getName().toString();
    private TextView mMovieTitleDisplay;
    private TextView mMovieOverviewDisplay;
    private TextView mMovieRateDisplay;
    private TextView mMovieReleaseDisplay;
    private ImageView mMoviePostDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent=getIntent();


        if(intent!=null && intent.hasExtra(MovieData.ID_TITLE)
                && intent.hasExtra(MovieData.ID_OVERVIEW)
                && intent.hasExtra(MovieData.ID_RATE)
                && intent.hasExtra(MovieData.ID_RELEASE)
                && intent.hasExtra(MovieData.ID_PATH)){
            String mTitleStr;
            String mPathStr;
            String mOverviewStr;
            String mRateStr;
            String mDateStr;
            mTitleStr =intent.getStringExtra(MovieData.ID_TITLE);
            mPathStr=intent.getStringExtra(MovieData.ID_PATH);
            mDateStr=intent.getStringExtra(MovieData.ID_RELEASE);
            mRateStr=intent.getStringExtra(MovieData.ID_RATE);
            mOverviewStr=intent.getStringExtra(MovieData.ID_OVERVIEW);

            mMovieTitleDisplay= (TextView)findViewById(R.id.name_movie);
            mMovieReleaseDisplay= (TextView)findViewById(R.id.date_view);
            mMovieRateDisplay=(TextView)findViewById(R.id.rate_view);
            mMovieOverviewDisplay=(TextView)findViewById(R.id.Overview_view);
            mMoviePostDisplay =(ImageView)findViewById(R.id.movie_picture);


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
                        //I used both cases because with placeholder is not depicting any image

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
