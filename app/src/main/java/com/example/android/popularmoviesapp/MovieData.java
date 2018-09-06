package com.example.android.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Property;

class MovieData  implements Parcelable{
    public static final String BASE_LINK = "http://image.tmdb.org/t/p/w185";
    private String Title;
    private String Overview;
    private String Path;
    private String Rate;
    private String Release;

    public static  final String PARCELABLE="parcelable";
    /*public static final String ID_TITLE="title";
    public static final String ID_OVERVIEW="overview";
    public static final String ID_PATH="path";
    public static final String ID_RATE="rate";
    public static final String ID_RELEASE="release";*/

    public MovieData(String Title,String Overview, String Rate,String Release, String Path){

        this.Title=Title;
        this.Overview=Overview;
        this.Rate=Rate;
        this.Release=Release;
        this.Path=Path;

    }

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
}
