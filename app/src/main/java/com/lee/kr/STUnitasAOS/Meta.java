package com.lee.kr.STUnitasAOS;

import com.google.gson.annotations.SerializedName;

public class Meta {

    @SerializedName("total_count")
    public int total_count;

    @SerializedName("pageable_count")
    public int pageable_count;

    @SerializedName("is_end")
    public boolean is_end;

    public Meta(int total_count, int pageable_count, boolean is_end) {
        this.total_count = total_count;
        this.pageable_count = pageable_count;
        this.is_end = is_end;
    }

}
