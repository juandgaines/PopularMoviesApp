package com.example.android.popularmoviesapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.example.android.popularmoviesapp.data.AppDatabase;
import com.example.android.popularmoviesapp.data.MovieData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesActivity extends AppCompatActivity implements  FavoriteMovieAdapter.MovieAdapterOnClickHandler{
    AppDatabase mDb;

    @BindView(R.id.my_favortire_recycler_view) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int width = size.x;
        final int height = size.y;
        Log.v("Width favorites",Integer.toString(width));

        mDb= AppDatabase.getsInstance(getApplicationContext());
        final LiveData<List<MovieData>> mListFavoriteLiveData= mDb.favoritesDao().loadAllFavoritesMovies();

        mListFavoriteLiveData.observe(this, new Observer<List<MovieData>>() {
            @Override
            public void onChanged(@Nullable List<MovieData> movieData) {
                FavoriteMovieAdapter adapter =new FavoriteMovieAdapter(FavoritesActivity.this, mListFavoriteLiveData.getValue(), width,height);

                mRecyclerView.setAdapter(adapter);
            }
        });

        GridLayoutManager layoutManager2;

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            layoutManager2= new GridLayoutManager(this,5,GridLayoutManager.VERTICAL,false);
        }
        else {
            // In portrait
            layoutManager2= new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        }

        if( orientation != Configuration.ORIENTATION_LANDSCAPE && dpWidth>=600) {
            layoutManager2= new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        }
        mRecyclerView.setLayoutManager(layoutManager2);
        mRecyclerView.setHasFixedSize(true);


    }

    @Override
    public void onClick(MovieData movieData) {

        Intent intent =new Intent(this, DetailActivity.class);

        intent.putExtra(MovieData.PARCELABLE,movieData);
        startActivity(intent);

    }
}
