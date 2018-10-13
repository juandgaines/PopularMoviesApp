package com.example.android.popularmoviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.popularmoviesapp.data.Result;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MovieAdapter  extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public final static String LOG_TAG= MovieAdapter.class.getName().toString();
    private Context context;
    private List<Result> mMovieData;
    private final MovieAdapterOnClickHandler mClickHandler;
    private int mWidth;
    private int mHeight;

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler, List<Result> movieFetchedData,int width,int height) {
        mClickHandler =  clickHandler;
        setMovieData(movieFetchedData);
        mWidth=width;
        mHeight=height;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(Result movieData);
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.favorite_movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position ) {
        String movieImage = mMovieData.get(position).getPosterPath();
        String mTitle =mMovieData.get(position).getTitle();

        Log.v(LOG_TAG,"movie image"+movieImage);


        if (!movieImage.equals("null")){
            Picasso.with(context).load ("http://image.tmdb.org/t/p/w185/"+movieImage)
                    .resize((int) Math.round(mWidth/2.3),(int) Math.round(mHeight/2.3) )
                    .centerInside()
                    .into(movieViewHolder.mMoviePosterView);
        }else{
            Picasso.with(context)
                    .load(R.drawable.not_found)
                    .resize(600,1000)
                    .into(movieViewHolder.mMoviePosterView);
        }
        movieViewHolder.mTitleMovie.setText(mTitle);


    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }
    public void setMovieData(List<Result> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public void restartData(){
        mMovieData.clear();
        notifyDataSetChanged();
    }



    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView mMoviePosterView;
        public final TextView mTitleMovie;


        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            mMoviePosterView= (ImageView) itemView.findViewById(R.id.favorite_movie_poster);
            mTitleMovie=(TextView) itemView.findViewById(R.id.favorite_movie_title) ;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition=getAdapterPosition();
            Result movieData = mMovieData.get(adapterPosition);
            mClickHandler.onClick(movieData);
        }


    }





}
