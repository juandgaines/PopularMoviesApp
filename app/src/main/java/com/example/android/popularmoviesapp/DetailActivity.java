package com.example.android.popularmoviesapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.graphics.Movie;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.data.AppDatabase;
import com.example.android.popularmoviesapp.data.AppExecutors;
import com.example.android.popularmoviesapp.data.ResultReviews;
import com.example.android.popularmoviesapp.data.ResultTrailers;
import com.example.android.popularmoviesapp.data.Review;
import com.example.android.popularmoviesapp.data.Trailer;
import com.example.android.popularmoviesapp.databinding.ActivityDetailBinding;
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
    ActivityDetailBinding mBinding;
    private ReviewsAdapter reviewsAdapter;
    private TrailersAdapter trailersAdapter;


    AppDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intent=getIntent();

        mBinding=DataBindingUtil.setContentView(this,R.layout.activity_detail);

        //mBinding.

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

                        mBinding.favoriteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                    }
                    else{
                        mBinding.favoriteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
                    }

                    populateUI(id_movie,mTitleStr,mPathStr,mOverviewStr,mRateStr,mDateStr);
                }
            });



            viewModel.getTrailersLiveData(id_movie).observe(this, new Observer<List<Trailer>>() {
                @Override
                public void onChanged(@Nullable List<Trailer> trailers) {

                    trailersAdapter= new TrailersAdapter(DetailActivity.this,trailers);
                    //trailersAdapter.setEmptyView(R.id.empty_view_reviews);
                    mBinding.trailerListview.setAdapter(trailersAdapter);

                    if(trailersAdapter.getItemCount()!=0){
                        mBinding.trailerListview.setVisibility(View.VISIBLE);
                        mBinding.emptyViewTrailers.setVisibility(View.GONE);
                    }
                    else{
                        mBinding.trailerListview.setVisibility(View.GONE);
                        mBinding.emptyViewTrailers.setVisibility(View.VISIBLE);

                    }

                }
            });

            viewModel.getReviewLiveData(id_movie).observe(this, new Observer<List<Review>>() {
                @Override
                public void onChanged(@Nullable List<Review> reviews) {

                    reviewsAdapter= new ReviewsAdapter(reviews);
                    mBinding.reviewsListview.setAdapter(reviewsAdapter);

                    if(reviewsAdapter.getItemCount()!=0){
                        mBinding.reviewsListview.setVisibility(View.VISIBLE);
                        mBinding.emptyViewReviews.setVisibility(View.GONE);
                    }
                    else{
                        mBinding.reviewsListview.setVisibility(View.GONE);
                        mBinding.emptyViewReviews.setVisibility(View.VISIBLE);

                    }


                }
            });


            int orientation = getResources().getConfiguration().orientation;
            LinearLayoutManager layoutManager;



            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // In landscape
                layoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            }
            else {
                // In portrait
                layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            }

            LinearLayoutManager layoutManager2= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            mBinding.reviewsListview.setLayoutManager(layoutManager2);
            mBinding.trailerListview.setLayoutManager(layoutManager);



            mBinding.trailerListview.setHasFixedSize(true);
            mBinding.reviewsListview.setHasFixedSize(true);

            mBinding.favoriteButton.setOnClickListener(new View.OnClickListener() {
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
    protected void onResume() {
        super.onResume();
        mBinding.scrollviewDetail.scrollTo(0,0);
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
        mBinding.nameMovie.setText(mTitleStr);

        Log.v(LOG_TAG,"value of date "+mDateStr);
        if(mDateStr==null|| mDateStr.equals("")) {
            //mDateStr= getResources().getString(R.string.error_date_message);
            mBinding.dateView.setText(getResources().getString(R.string.error_date_message));
        }
        else{
            mBinding.dateView.setText(mDateStr);
        }

        Log.v(LOG_TAG,"value of rate "+mRateStr);
        if(mRateStr!=null || mRateStr.equals("")){
            mBinding.rateView.setText(mRateStr+"/10");
        }
        else{
            mBinding.rateView.setText(getResources().getString(R.string.error_rate_message));
        }
        Log.v(LOG_TAG,"value of overview "+mOverviewStr);
        if(mOverviewStr==null|| mOverviewStr.equals("")){

            ((TextView)mBinding.overviewParts.findViewById(R.id.Overview_view)).setText(getResources().getString(R.string.error_overview_message));
        }
        else{
            ((TextView)mBinding.overviewParts.findViewById(R.id.Overview_view)).setText(mOverviewStr);
        }


        Log.v(LOG_TAG,"value of post: "+mPathStr);


        if(!mPathStr.equals("null")){
            Picasso.with(this)
                    .load(MovieData.BASE_LINK +mPathStr)
                    .resize(600,1000)
                    .centerInside()
                    .into(mBinding.moviePicture);
        }
        else{
            Picasso.with(this)
                    .load(R.drawable.not_found)
                    .resize(600,800)
                    .into(mBinding.moviePicture);
        }
    }
}
