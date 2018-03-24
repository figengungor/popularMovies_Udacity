package com.figengungor.popularmovies.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class TaggedImages {

    @SerializedName("results")
    @Expose
    private List<TaggedImage> taggedImages = null;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;

    public List<TaggedImage> getTaggedImages() {
        return taggedImages;
    }

    public void setTaggedImages(List<TaggedImage> results) {
        this.taggedImages = results;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

}