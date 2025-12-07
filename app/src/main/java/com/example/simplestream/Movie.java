package com.example.simplestream;

import java.io.Serializable;

public class Movie implements Serializable {
    private String title;
    private String posterUrl;
    private String detailUrl;
    private String quality;
    private String rating;

    public Movie() { }

    public Movie(String title, String posterUrl, String detailUrl, String quality, String rating) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.detailUrl = detailUrl;
        this.quality = quality;
        this.rating = rating;
    }

    public String getTitle() { return title; }
    public String getPosterUrl() { return posterUrl; }
    public String getDetailUrl() { return detailUrl; }
    public String getQuality() { return quality == null ? "" : quality; }
    public String getRating() { return rating == null ? "N/A" : rating; }
}
