package com.example.vid_me_app;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UserVideos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_videos);
        addFragment();
    }

    private void addFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        UserVideosFragment fragment_user_videos = new UserVideosFragment();
        fragmentTransaction.add(R.id.user_videos_container,fragment_user_videos);
        fragmentTransaction.commit();
    }
}
