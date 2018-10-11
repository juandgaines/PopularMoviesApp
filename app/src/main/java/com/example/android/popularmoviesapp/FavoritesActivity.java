package com.example.android.popularmoviesapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        mRecyclerView.setLayoutManager(layoutManager2);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);




    }

    @Override
    public void onClick(MovieData movieData) {

    }
}
