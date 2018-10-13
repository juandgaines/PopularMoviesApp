package com.example.android.popularmoviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.popularmoviesapp.data.Trailer;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerMovieViewHolder> {
    public final static String LOG_TAG= TrailersAdapter.class.getName().toString();
    private Context context;
    private List<Trailer> mMovieData;
    private final TrailerAdapterOnClickHandler mClickHandler;

    public TrailersAdapter(TrailerAdapterOnClickHandler mClickHandler, List<Trailer> ReviewsFetchedData){
        mMovieData=ReviewsFetchedData;
        this.mClickHandler=mClickHandler;
    }


    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailerData);
    }

    @NonNull
    @Override
    public TrailersAdapter.TrailerMovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_movie_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TrailerMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersAdapter.TrailerMovieViewHolder holder, int position) {

        String movieReview = mMovieData.get(position).getName();
        //String movieAuthor= mMovieData.get(position).getAuthor();

        holder.mReviewIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_movie_filter_24px));
        holder.mReviewContent.setText(movieReview);
        //holder.mReviewAuthor.setText(movieAuthor);

    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }

    public void setMovieData(List<Trailer> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public void restartData(){
        mMovieData.clear();
        notifyDataSetChanged();
    }


    public class TrailerMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView mReviewIcon;
        public final TextView mReviewContent;


        public TrailerMovieViewHolder(View itemView) {
            super(itemView);

            mReviewIcon= (ImageView) itemView.findViewById(R.id.trailer_movie_icon);

            mReviewContent=(TextView)itemView.findViewById(R.id.trailer_movie_content);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int adapterPosition=getAdapterPosition();
            Trailer movieData = mMovieData.get(adapterPosition);
            mClickHandler.onClick(movieData);
        }
    }
}