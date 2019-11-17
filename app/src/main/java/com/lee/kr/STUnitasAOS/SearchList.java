package com.lee.kr.STUnitasAOS;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchList {

    @SerializedName("meta")
    public Meta meta;

    @SerializedName("documents")
    public ArrayList<Documents> documents = new ArrayList<Documents>();

    public SearchList(){

    }

    public SearchList(Meta meta, ArrayList<Documents> documents) {
        this.meta = meta;
        this.documents = documents;
    }

}
