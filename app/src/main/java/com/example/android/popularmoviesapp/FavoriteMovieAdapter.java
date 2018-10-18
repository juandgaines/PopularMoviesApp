package com.example.android.popularmoviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.data.MovieData;
import com.example.android.popularmoviesapp.data.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder>{

    public final static String LOG_TAG= MovieAdapter.class.getName().toString();
    private Context context;
    private List<MovieData> mMovieData;
    private int mWidth;
    private int mHeight;
    private final MovieAdapterOnClickHandler mClickHandler;

    public FavoriteMovieAdapter(MovieAdapterOnClickHandler clickHandler, List<MovieData> movieFetchedData, int width,int height){
        mMovieData=movieFetchedData;
        mClickHandler=clickHandler;
        mWidth=width;
        mHeight=height;
    }


    public interface MovieAdapterOnClickHandler {
        void onClick(MovieData movieData);
    }

    @NonNull
    @Override
    public FavoriteMovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.favorite_movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new FavoriteMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteMovieViewHolder holder, int position) {

        String movieImage = mMovieData.get(position).getPosterPath();
        String movieTitle =mMovieData.get(position).getTitle();
        TypedValue outValue = new TypedValue();

        context.getResources().getValue(R.dimen.picasso_image_divisor,outValue,true);

        float divisor= outValue.getFloat();

        if (!movieImage.equals("null")){
            Picasso.with(context).load ("http://image.tmdb.org/t/p/w185/"+movieImage)
                    .resize((int) Math.round(mWidth/divisor),(int) Math.round(mHeight/divisor) )
                    .centerInside()
                    .into(holder.mFavoritePoster);
        }else{
            Picasso.with(context)
                    .load(R.drawable.not_found)
                    .resize(600,1000)

                    .into(holder.mFavoritePoster);
        }

        holder.mFavoriteTitle.setText(movieTitle);

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

    public void restartData(){
        mMovieData.clear();
        notifyDataSetChanged();
    }


    public class FavoriteMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView mFavoritePoster;
        public final TextView mFavoriteTitle;


        public FavoriteMovieViewHolder(View itemView) {
            super(itemView);

            mFavoritePoster= (ImageView) itemView.findViewById(R.id.favorite_movie_poster);
            mFavoriteTitle=(TextView)itemView.findViewById(R.id.favorite_movie_title);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition=getAdapterPosition();
            MovieData movieData = mMovieData.get(adapterPosition);
            mClickHandler.onClick(movieData);
        }
    }
}
