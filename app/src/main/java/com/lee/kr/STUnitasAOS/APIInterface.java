package com.lee.kr.STUnitasAOS;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("image?")
    Call<SearchList> getSearchList(@Query("query") String query, @Query("page") String page);

}