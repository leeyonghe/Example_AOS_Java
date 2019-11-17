package com.lee.kr.STUnitasAOS;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static String APPKEY = "77a9306101b5e16ed249a11a6d2a2b39";

    private static Retrofit retrofit = null;

    static Retrofit getClient() {

//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl("https://dapi.kakao.com/v2/search/image")
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(client)
//                .build();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
          @Override
          public Response intercept(Interceptor.Chain chain) throws IOException {
              Request original = chain.request();

              Request request = original.newBuilder()
                      .header("Authorization", "KakaoAK 77a9306101b5e16ed249a11a6d2a2b39")
                      .method(original.method(), original.body())
                      .build();

              return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/v2/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }


}
