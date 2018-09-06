package com.example.android.popularmoviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;


public class MovieAdapter  extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public final static String LOG_TAG= MovieAdapter.class.getName().toString();
    private Context context;
    private List<MovieData> mMovieData;
    private final MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(MovieData movieData);
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView mMoviePosterView;


        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            mMoviePosterView= (ImageView) itemView.findViewById(R.id.tv_movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition=getAdapterPosition();
            MovieData movieData = mMovieData.get(adapterPosition);
            mClickHandler.onClick(movieData);


        }


    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position ) {
        String movieImage = mMovieData.get(position).getPath();

        Log.v(LOG_TAG,"movie image"+movieImage);

        if (!movieImage.equals("null")){
            Picasso.with(context).load ("http://image.tmdb.org/t/p/w185/"+movieImage)
                    .resize(600,1000)
                    .centerInside()
                    .into(movieViewHolder.mMoviePosterView);
        }else{
            Picasso.with(context)
                    .load(R.drawable.not_found)
                    .resize(600,1000)

                    .into(movieViewHolder.mMoviePosterView);
        }



    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }
    public void setMovieData(List<MovieData> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }



}
