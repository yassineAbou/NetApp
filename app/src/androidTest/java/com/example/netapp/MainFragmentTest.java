package com.example.netapp;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.netapp.Models.GithubUser;
import com.example.netapp.Models.GithubUserInfos;
import com.example.netapp.Utils.GithubStreams;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Yassine Abou on 4/7/2021.
 */
@RunWith(AndroidJUnit4.class)
public class MainFragmentTest {

    @Test
    public void streamFetchUserFollowingTest() {
        //Get the stream
        Observable<List<GithubUser>> observableUser = GithubStreams.streamFetchUserFollowing("JakeWharton");
        //Create a new TestObserver
        TestObserver<List<GithubUser>> testObserver = new TestObserver<>();
        //Launch observable
        observableUser.subscribeWith(testObserver)
                .assertNoErrors() //  Check if no errors
                .assertNoTimeout() //  Check if no Timeout
                .awaitTerminalEvent(); //  Await the stream terminated before continue

        //Get list of user fetched
        List<GithubUser> usersFetched = testObserver.values().get(0);

        //Verify if Jake Wharton follows only 9 users...
        assertThat("Jake Wharton follows only 12 users.",usersFetched.size() == 9);
    }

    @Test
    public void fetchUserInfosTest() throws Exception {
        Observable<GithubUserInfos> observableUser = GithubStreams.streamFetchUserInfos("JakeWharton");
        TestObserver<GithubUserInfos> testObserver = new TestObserver<>();

        observableUser.subscribeWith(testObserver)
                .assertNoErrors()
                .assertNoTimeout()
                .awaitTerminalEvent();

        GithubUserInfos userInfo = testObserver.values().get(0);

        assertThat("Jake Wharton Github's ID is 66577.",userInfo.getId() == 66577);
    }
}
