package com.example.android.popularmoviesapp;

class MovieData {
    public static final String BASE_LINK = "http://image.tmdb.org/t/p/w185";
    private String Title;
    private String Overview;
    private String Path;
    private String Rate;
    private String Release;

    public static final String ID_TITLE="title";
    public static final String ID_OVERVIEW="overview";
    public static final String ID_PATH="path";
    public static final String ID_RATE="rate";
    public static final String ID_RELEASE="release";

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
}
