package com.example.netapp.Controllers.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.netapp.Models.GithubUser;
import com.example.netapp.Models.GithubUserInfos;
import com.example.netapp.R;
import com.example.netapp.Utils.GithubStreams;
import com.example.netapp.Utils.ItemClickSupport;
import com.example.netapp.Utils.NetworkAsyncTask;
import com.example.netapp.Views.GithubUserAdapter;
import com.example.netapp.databinding.FragmentMainBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;


@SuppressWarnings("ALL")
public class MainFragment extends Fragment implements NetworkAsyncTask.Listeners, GithubUserAdapter.Listener {
    //Implement Callbacks

    private FragmentMainBinding mainFragment;
    private static final String TAG = "MainFragment";

    // FOR DATA
    //Declare Subscription
    private Disposable disposable;
    //Declare list of users (GithubUser) & Adapter
    private List<GithubUser> mGithubUsers;
    private GithubUserAdapter mAdapter;

    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainFragment= FragmentMainBinding.inflate(inflater, container, false);
        View view = mainFragment.getRoot();
        this.configureRecyclerView(); //Call during UI creation
        this.configureSwipeRefreshLayout(); //Configure the SwipeRefreshLayout
        this.configureOnclickRecyclerView(); //Calling the method that configuring click on RecyclerView
        this.executeHttpRequestWithretrofit(); //Execute stream after UI creation
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainFragment = null;
        disposeWhenDestroy();
    }

    // -----------------
    // ACTIONS
    // -----------------

    //Configure item click on RecyclerView
    private void configureOnclickRecyclerView() {
        ItemClickSupport.addTo(mainFragment.RecyclerView, R.layout.fragment_main_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        //Get user from adapter
                        GithubUser user = mAdapter.getUser(position);
                        //Show result in a Toast
                        Toast.makeText(getContext(), "You clicked on user : " + user.getLogin(),
                         Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Because of implementing the interface, we have to override its method
    @Override
    public void onClickDeleteButton(int position) {
        GithubUser user = mAdapter.getUser(position);
        Toast.makeText(getContext(), "You are trying to delete user : " + user.getLogin(),
         Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    // ------------------
    //  HTTP REQUEST
    // ------------------

    private void executeHttpRequest() {
        new NetworkAsyncTask(this).execute("https://api.github.com/users/JakeWharton/following");
    }

    @Override
    public void onPreExecute() {
          this.updateUIWhenStartingHttpRequest();
    }

    @Override
    public void doInBackground() {}

    @Override
    public void onPostExecute(String json) {
        this.updateUIWhenStoppingHttpRequest(json);
    }

    // ------------------
    //  HTTP REQUEST (Retrofit Way)
    // ------------------

    // ------------------
    //  UPDATE UI
    // ------------------

    private void updateUIWhenStartingHttpRequest() {

    }

    private void updateUIWhenStoppingHttpRequest(String response) {

    }

    //Update UI showing only name of users
    private void updateUIWithListOfUsers(List<GithubUser> users) {
        StringBuilder stringBuilder = new StringBuilder();
        for (GithubUser user : users) {
            stringBuilder.append("-" + user.getLogin() + "\n");
        }
        updateUIWhenStoppingHttpRequest(stringBuilder.toString());
    }

    private void updateUIWithUserInfos(GithubUserInfos githubUserInfos) {
        updateUIWhenStoppingHttpRequest("The first Following of Jake Wharthon is " +
         githubUserInfos.getName() + " with " + githubUserInfos.getFollowers() + " followers.");
    }

    private void updateUI(List<GithubUser> users) {
        //Stop refreshing and clear actual list of users
        mainFragment.swipeContainer.setRefreshing(false);
        mGithubUsers.clear();
        mGithubUsers.addAll(users);
        mAdapter.notifyDataSetChanged();
    }

    //-------------------------
    //  Reactive X
    //-------------------------

    //Create Observable
    private Observable<String> getObserble() {
        return Observable.just("Cool !");
    }

    //Create Subscriber
    private DisposableObserver<String> getSubscriber() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String s) {

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError: " + Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: !!");
            }
        };
    }

    //Create Stream and execute it
    private void streamShowString() {
        this.disposable = this.getObserble()
                .map(getFunctionUpperCase()) //Apply function
                .flatMap(getSecondObsevable()) //Adding Observable
                .subscribeWith(getSubscriber());
    }

    //Dispose subscription
    private void disposeWhenDestroy() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    //Create function to Uppercase a string
    private Function<String, String> getFunctionUpperCase() {
        return new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return s.toUpperCase();
            }
        };
    }

    //Create a function that will calling a new observable
    private Function<String, Observable<String>> getSecondObsevable() {
        return new Function<String, Observable<String>>() {
            @Override
            public Observable<String> apply(String s) throws Exception {
                return Observable.just(s + " I love Openclassrooms !");
            }
        };
    }

    // --------------------------------------------------------------------
    // HTTP (RxJAVA) 
    // --------------------------------------------------------------------

    //Execute our Stream
    private void executeHttpRequestWithretrofit() {
        //Update UI
        this.updateUIWhenStartingHttpRequest();
        //Execute the stream subscribing to Observable defined inside GithubStream
        this.disposable = GithubStreams.streamFetchUserFollowing("JakeWharton").subscribeWith(new
        DisposableObserver<List<GithubUser>>() {
            @Override
            public void onNext(@NonNull List<GithubUser> users) {
                Log.e(TAG, "onNext: ");
                //Update RecyclerView after getting results from Github API
                updateUI(users);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError: " + Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
            }
        });
    }

    private void executeSecondHttpRequestWithRetrofit() {
            this.updateUIWhenStartingHttpRequest();
            this.disposable = GithubStreams.streamFetchUserFollowingAndFetchFirstUserInfos("JakeWharton").
             subscribeWith(new DisposableObserver<GithubUserInfos>() {
                 @Override
                 public void onNext(@NonNull GithubUserInfos githubUserInfos) {
                     Log.e(TAG, "onNext: ");
                     updateUIWithUserInfos(githubUserInfos);
                 }

                 @Override
                 public void onError(@NonNull Throwable e) {
                     Log.e(TAG, "onError: " + Log.getStackTraceString(e));
                 }

                 @Override
                 public void onComplete() {
                     Log.e(TAG, "onComplete: ");
                 }
             });
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureRecyclerView() {
        //Reset list
        this.mGithubUsers = new ArrayList<>();
        //Create adapter passing the list of users
        this.mAdapter = new GithubUserAdapter(this.mGithubUsers, Glide.with(this), this);
        //Attach the adapter to the recyclerview to populate items
        mainFragment.RecyclerView.setAdapter(this.mAdapter);
        //Set layout manager to position the items
        mainFragment.RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //Configure the SwipeRefreshLayout
    private void configureSwipeRefreshLayout() {
        mainFragment.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeHttpRequestWithretrofit();
            }
        });
    }

}