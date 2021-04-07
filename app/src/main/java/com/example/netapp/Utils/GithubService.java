package com.example.netapp.Utils;

import com.example.netapp.Models.GithubUser;
import com.example.netapp.Models.GithubUserInfos;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Yassine Abou on 3/31/2021.
 */
@SuppressWarnings("ALL")
public interface GithubService {
    // for GET request
    @GET("users/{username}/following") // specify the sub url for our base url

    Observable<List<GithubUser>> getFollowing(@Path("username") String username);
// GithubUser is a POJO class which receives the response of this API

    @GET("/users/{username}")
    Observable<GithubUserInfos> getUserInfos(@Path("username") String username);
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/") //Setting the Root URL
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

}

