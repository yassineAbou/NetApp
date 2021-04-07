package com.example.netapp.Utils;

import com.example.netapp.Models.GithubUser;
import com.example.netapp.Models.GithubUserInfos;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Yassine Abou on 4/3/2021.
 */
@SuppressWarnings("ALL")
public class GithubStreams {

    public static Observable<List<GithubUser>> streamFetchUserFollowing(String userName) {
        GithubService githubService = GithubService.retrofit.create(GithubService.class);
        return githubService.getFollowing(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    //Create a stream that will get user infos on Github API
    public static Observable<GithubUserInfos> streamFetchUserInfos(String username) {
        GithubService githubService = GithubService.retrofit.create(GithubService.class);
        return githubService.getUserInfos(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    //Create a stream that will :
    //  A. Fetch all users followed by "username"
    //  B. Return the first user of the list
    //  C. Fetch details of the first user
    public static Observable<GithubUserInfos> streamFetchUserFollowingAndFetchFirstUserInfos(String username) {
        return streamFetchUserFollowing(username) // A
        .map(new Function<List<GithubUser>, GithubUser>() {
            @Override
            public GithubUser apply(@NonNull List<GithubUser> users) throws Exception {
                return users.get(0); //B
            }
        })
        .flatMap(new Function<GithubUser, Observable<GithubUserInfos>>() {
            @Override
            public Observable<GithubUserInfos> apply(@NonNull GithubUser githubUser) throws Exception {
                //C
                return streamFetchUserInfos(githubUser.getLogin());
            }
        });
    }
}
