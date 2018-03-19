package com.figengungor.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Videos {

    @SerializedName("id")
    private Integer id;
    @SerializedName("results")
    private List<Video> videos = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

}