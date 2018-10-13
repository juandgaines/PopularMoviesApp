package com.example.android.popularmoviesapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "favorites")
public class MovieData  implements Parcelable{

    public static final String BASE_LINK = "http://image.tmdb.org/t/p/w185";


    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "movie_id")
    private int movieId;

    @ColumnInfo(name = "rate")
    private Double voteAverage;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "path")
    private String posterPath;

    @ColumnInfo(name = "overview")
    private String overview;

    @ColumnInfo(name = "release")
    private String releaseDate;

    public static  final String PARCELABLE="parcelable";

    @Ignore
    public MovieData(String Title,String Overview, double Rate,String Release, String Path, int movieId){

        this.title=Title;
        this.overview=Overview;
        this.voteAverage=Rate;
        this.releaseDate=Release;
        this.posterPath=Path;
        this.movieId=movieId;


    }

    public MovieData(){

    }

    public MovieData(int id, String Title,String Overview, double Rate,String Release, String Path, int movieId){
        this.id=id;
        this.title=Title;
        this.overview=Overview;
        this.voteAverage=Rate;
        this.releaseDate=Release;
        this.posterPath=Path;
        this.movieId=movieId;
    }

    public int getMovieId(){return movieId;}

    public void setMovieId(int id){
        this.movieId=id;
    }

    public int getId(){return id;}

    public void setId(int id){
        this.id=id;
    }


    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }


    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Integer.toString(movieId));
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(Double.toString(voteAverage));
        dest.writeString(releaseDate);
    }
    //constructor used for parcel
    public MovieData(Parcel parcel){
        //read and set saved values from parcel
        movieId=Integer.parseInt(parcel.readString());
        title= parcel.readString();
        overview=parcel.readString();
        posterPath=parcel.readString();
        voteAverage=Double.parseDouble(parcel.readString()) ;
        releaseDate=parcel.readString();

    }

    //creator - used when un-parceling our parcle (creating the object)
    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>(){

        @Override
        public MovieData createFromParcel(Parcel parcel) {
            return new MovieData(parcel);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[0];
        }
    };

    @Override
    public String toString() {
        StringBuilder movieString=new StringBuilder("");
        movieString
                .append("Id:"+movieId+"\n")
                .append("Title: "+title+"\n")
                .append("Overview: "+overview+"\n")
                .append("Path: "+posterPath+"\n")
                .append("Rate: "+voteAverage+"\n")
                .append("Release: "+releaseDate+"\n");

        return movieString.toString();
    }

}

