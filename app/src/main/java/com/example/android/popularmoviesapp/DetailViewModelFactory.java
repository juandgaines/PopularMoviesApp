package com.example.android.popularmoviesapp;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;


import com.example.android.popularmoviesapp.data.AppDatabase;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {



    private final AppDatabase mDb;
    private final int mMovieId;

    public DetailViewModelFactory(AppDatabase database, int movieId){
        mDb=database;
        mMovieId= movieId;

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailActivityViewModel(mDb,mMovieId);
    }
}
