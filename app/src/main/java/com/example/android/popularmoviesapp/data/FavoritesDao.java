package com.example.android.popularmoviesapp.data;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.graphics.Movie;

import java.util.List;

@Dao
public interface FavoritesDao {

    @Query("SELECT * FROM favorites ORDER BY id")
    LiveData<List<MovieData>> loadAllFavoritesMovies();

    @Query("SELECT * FROM favorites WHERE movie_id=:id")
    LiveData<MovieData> loadFavoriteItemByName(int id);

    @Insert
    void insertFavoriteMovie(MovieData movieData);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavoriteMovie(MovieData movieData);

    @Delete
    void deleteFavoriteMovie(MovieData movieData);

    @Query("DELETE FROM favorites")
    void deleteAll();

}
