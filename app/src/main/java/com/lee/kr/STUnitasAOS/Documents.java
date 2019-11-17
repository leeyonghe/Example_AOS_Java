package com.lee.kr.STUnitasAOS;

import com.google.gson.annotations.SerializedName;

public class Documents {

    @SerializedName("collection")
    public String collection;

    @SerializedName("thumbnail_url")
    public String thumbnail_url;

    @SerializedName("image_url")
    public String image_url;

    @SerializedName("width")
    public String width;

    @SerializedName("height")
    public String height;

    @SerializedName("display_sitename")
    public String display_sitename;

    @SerializedName("doc_url")
    public String doc_url;

    @SerializedName("datetime")
    public String datetime;

    public Documents(String collection, String thumbnail_url, String image_url, String width, String height, String display_sitename, String doc_url, String datetime) {
        this.collection = collection;
        this.thumbnail_url = thumbnail_url;
        this.image_url = image_url;
        this.width = width;
        this.height = height;
        this.display_sitename = display_sitename;
        this.doc_url = doc_url;
        this.datetime = datetime;
    }
}
