package com.example.android.popularmoviesapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "favorites")
public class MovieData  implements Parcelable{

    public static final String BASE_LINK = "http://image.tmdb.org/t/p/w185";


    @PrimaryKey(autoGenerate = true)
    private int id;

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
    public MovieData(String Title,String Overview, double Rate,String Release, String Path){

        this.title=Title;
        this.overview=Overview;
        this.voteAverage=Rate;
        this.releaseDate=Release;
        this.posterPath=Path;

    }

    public MovieData(){

    }

    public MovieData(int id, String Title,String Overview, double Rate,String Release, String Path){
        this.id=id;
        this.title=Title;
        this.overview=Overview;
        this.voteAverage=Rate;
        this.releaseDate=Release;
        this.posterPath=Path;
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
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(Double.toString(voteAverage));
        dest.writeString(releaseDate);
    }
    //constructor used for parcel
    public MovieData(Parcel parcel){
        //read and set saved values from parcel
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
                .append("Title: "+title+"\n")
                .append("Overview: "+overview+"\n")
                .append("Path: "+posterPath+"\n")
                .append("Rate: "+voteAverage+"\n")
                .append("Release: "+releaseDate+"\n");

        return movieString.toString();
    }

}



/*
* @Entity(tableName = "favorites")
public class MovieData  implements Parcelable{

    public static final String BASE_LINK = "http://image.tmdb.org/t/p/w185";


    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("title")
    @Expose
    @ColumnInfo(name = "title")
    private String Title;

    @SerializedName("overview")
    @Expose
    @ColumnInfo(name = "overview")
    private String Overview;

    @SerializedName("poster_path")
    @Expose
    @ColumnInfo(name = "path")
    private String Path;

    @SerializedName("vote_average")
    @Expose
    @ColumnInfo(name = "rate")
    private String Rate;

    @SerializedName("release_date")
    @Expose
    @ColumnInfo(name = "release")
    private String Release;



    public static  final String PARCELABLE="parcelable";

    @Ignore
    public MovieData(String Title,String Overview, String Rate,String Release, String Path){

        this.Title=Title;
        this.Overview=Overview;
        this.Rate=Rate;
        this.Release=Release;
        this.Path=Path;

    }

    public MovieData(int id, String Title,String Overview, String Rate,String Release, String Path){
        this.id=id;
        this.Title=Title;
        this.Overview=Overview;
        this.Rate=Rate;
        this.Release=Release;
        this.Path=Path;
    }

    public int getId(){return id;}
    public String getTitle(){
        return Title;
    }
    public String getOverview(){
        return Overview;
    }
    public String getRate(){
        return Rate;
    }
    public String getRelease(){
        return Release;
    }
    public String getPath(){
        return Path;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setRelease(String release) {
        Release = release;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public void setPath(String path) {
        Path = path;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRate(String rate) {
        Rate = rate;
    }


    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Title);
        dest.writeString(Overview);
        dest.writeString(Path);
        dest.writeString(Rate);
        dest.writeString(Release);
    }
    //constructor used for parcel
    public MovieData(Parcel parcel){
        //read and set saved values from parcel
        Title= parcel.readString();
        Overview=parcel.readString();
        Path=parcel.readString();
        Rate=parcel.readString();
        Release=parcel.readString();

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
                .append("Title: "+Title+"\n")
                .append("Overview: "+Overview+"\n")
                .append("Path: "+Path+"\n")
                .append("Rate: "+Rate+"\n")
                .append("Release: "+Release+"\n");

        return movieString.toString();
    }

}
*
* */
