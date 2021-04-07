package com.example.netapp.Views;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.netapp.Models.GithubUser;
import com.example.netapp.R;

import java.lang.ref.WeakReference;


/**
 * Created by Yassine Abou on 4/4/2021.
 */
@SuppressWarnings("ALL")
public class GithubUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @SuppressWarnings("CanBeFinal")
    TextView mTextView, mTextViewWebsite;
    ImageView mImageView;
    //Declare our ImageButton
    ImageButton mImageButton;
    //Declare a Weak Reference to our Callback
    private WeakReference<GithubUserAdapter.Listener> mListenerWeakReference;

    public GithubUserViewHolder(@NonNull View itemView) {
        super(itemView);
       mTextView = itemView.findViewById(R.id.fragment_main_item_title);
       mTextViewWebsite = itemView.findViewById(R.id.fragment_main_item_website);
       mImageView = itemView.findViewById(R.id.fragment_main_item_image);
       mImageButton = itemView.findViewById(R.id.fragment_main_item_delete);

    }

    public void updateWithGithubUser(GithubUser githubUser, RequestManager glide, GithubUserAdapter.Listener callback) {
        mTextView.setText(githubUser.getLogin());
        mTextViewWebsite.setText(githubUser.getHtmlUrl());

        glide.load(githubUser.getAvatarUrl()).apply(RequestOptions.circleCropTransform()).into(mImageView);

        //Implement Listener on ImageButton
        this.mImageButton.setOnClickListener(this);
        //Create a new weak Reference to our Listener
        this.mListenerWeakReference = new WeakReference<GithubUserAdapter.Listener>(callback);
    }


    @Override
    public void onClick(View v) {
        //When a click happens, we fire our listener.
        GithubUserAdapter.Listener callback = mListenerWeakReference.get();
        if (callback != null) callback.onClickDeleteButton(getAdapterPosition());
    }
}
