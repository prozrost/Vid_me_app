package com.example.vid_me_app;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class User_videos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_videos);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment_user_videos fragment_user_videos = new Fragment_user_videos();
        fragmentTransaction.add(R.id.user_videos_container,fragment_user_videos);
        fragmentTransaction.commit();
    }
}
