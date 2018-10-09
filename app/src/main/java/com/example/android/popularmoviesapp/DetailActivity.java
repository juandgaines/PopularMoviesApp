package com.example.android.popularmoviesapp;

import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.data.AppDatabase;
import com.example.android.popularmoviesapp.data.AppExecutors;
import com.squareup.picasso.Picasso;

import com.example.android.popularmoviesapp.data.MovieData;

import butterknife.BindView;
import butterknife.ButterKnife;

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
            final String mTitleStr =movieData.getTitle();
            String mPathStr=movieData.getPosterPath();
            String mOverviewStr=movieData.getOverview();
            String mRateStr= Double.toString(movieData.getVoteAverage()) ;
            String mDateStr=movieData.getReleaseDate();

            Log.v(LOG_TAG,"Path"+ mPathStr);

            AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    final MovieData movie =mDb.favoritesDao().loadFavoriteItemByName(mTitleStr);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(movie!=null){
                                mFavortite.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                            }
                            else{
                                mFavortite.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                            }
                        }
                    });
                }
            });




            mFavortite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final String mTitleStr =movieData.getTitle();
                    String mPathStr=movieData.getPosterPath();
                    String mOverviewStr=movieData.getOverview();
                    String mRateStr= Double.toString(movieData.getVoteAverage());
                    String mDateStr=movieData.getReleaseDate();

                    final MovieData movie=new MovieData(mTitleStr,mOverviewStr,Double.parseDouble(mRateStr) ,mDateStr, mPathStr);

                    AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            final MovieData prueba=mDb.favoritesDao().loadFavoriteItemByName(mTitleStr);

                            if(prueba!=null && mTitleStr.equals(prueba.getTitle())){
                                mDb.favoritesDao().deleteFavoriteMovie(prueba);
                            }
                            else {
                                mDb.favoritesDao().insertFavoriteMovie(movie);
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(prueba!=null && mTitleStr.equals(prueba.getTitle())){
                                        mFavortite.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                                        Toast.makeText(getApplicationContext(),"Removed from favorites",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        mFavortite.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                                        Toast.makeText(getApplicationContext(),"Added to favorites",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }
                    });

                    //mDb.favoritesDao().deleteAll();
                    //Log.v(LOG_TAG,prueba.toString());



                }
            });

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
