package com.example.vid_me_app;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("clip_url")
    @Expose
    private String clip_url;

    public String getComplete_url() {
        return complete_url;
    }


    @SerializedName("complete_url")
    @Expose
    private String complete_url;
    @SerializedName("title")
    @Expose
    private String title;

    public String getThumbnail_url() {
        return thumbnail_url;
    }



    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnail_url;


    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("likes_count")
    @Expose
    private Integer likes_count;


    public String getClipUrl() {
        return clip_url;
    }




    public String getTitle() {
        return title;
    }





    public Integer getScore() {
        return likes_count;
    }




}
