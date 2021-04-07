package com.example.netapp.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.netapp.Models.GithubUser;
import com.example.netapp.R;

import java.util.List;

/**
 * Created by Yassine Abou on 4/5/2021.
 */
public class GithubUserAdapter extends RecyclerView.Adapter<GithubUserViewHolder> {

    // FOR DATA
    private List<GithubUser> mGithubUsers;
    //Declaring a Glide object
    private RequestManager glide;

    //Updating our constructor adding a Glide Object
    //Passing an instance of callback through constructor
    public GithubUserAdapter(List<GithubUser> githubUsers, RequestManager glide, Listener callback) {
        mGithubUsers = githubUsers;
        this.glide = glide;
        this.callback = callback;
    }

    public GithubUser getUser(int position) {
        return this.mGithubUsers.get(position);
    }

    //Create interface for callback
    public interface Listener {
        void onClickDeleteButton(int position);
    }

    //Declaring callback
    private final Listener callback;

    @NonNull
    @Override
    public GithubUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_main_item, parent, false);

        return new GithubUserViewHolder(view);
    }

    // UPDATE VIEW HOLDER WITH A GITHUBUSER
    @Override
    public void onBindViewHolder(@NonNull GithubUserViewHolder holder, int position) {
        //Passing the Glide object to each ViewHolder
        //Passing an instance of callback through each ViewHolder
       holder.updateWithGithubUser(this.mGithubUsers.get(position), this.glide, this.callback);
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.mGithubUsers.size();
    }
}
