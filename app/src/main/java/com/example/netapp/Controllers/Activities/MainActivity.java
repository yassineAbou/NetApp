package com.example.netapp.Controllers.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.netapp.Controllers.Fragments.MainFragment;
import com.example.netapp.R;
import com.example.netapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mMainBinding;
    private MainFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view =mMainBinding.getRoot();
        setContentView(view);

        this.configureAndSowMainFragment();
    }

    //------------------
    //CONFIGURATION
    //-------------------

    private void configureAndSowMainFragment() {

       mMainFragment= (MainFragment) getSupportFragmentManager().findFragmentById(R.id.Main_fragment);

        if (mMainFragment == null) {
            mMainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.Main_fragment, mMainFragment)
                    .commit();
        }
    }
}