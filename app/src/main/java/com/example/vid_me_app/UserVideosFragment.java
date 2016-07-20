package com.example.vid_me_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserVideosFragment extends Fragment{
    RecyclerViewAdapter userVideosAdapter;
    public static final String ROOT_URL = "https://api.vid.me/";
    public List<Video> videos;
    RecyclerView userVideosList;
    SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_videos, container, false);
        userVideosList = (RecyclerView) rootView.findViewById(R.id.user_videos_list);
        setHasOptionsMenu(true);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.user_videos_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    refreshItems();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            public void refreshItems() throws IOException {
                getVideos();
                OnItemLoadComplete();
            }
            public void OnItemLoadComplete(){
                userVideosAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);


            }
        });
        userVideosList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        userVideosList.setLayoutManager(llm);


        try {
            getVideos();
        } catch (IOException e) {
            e.printStackTrace();

        }

        return rootView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void getVideos() throws IOException {
        Retrofit retrofitAdapter = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ROOT_URL)
                .build();
        final VideoApi videoApi = retrofitAdapter.create(VideoApi.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);
        Log.d("token in user videos",token);
        Call<Videos> call = videoApi.getFollowingVideo(token);
        call.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, final Response<Videos> response) {


                videos = response.body().videos;
                userVideosAdapter = new RecyclerViewAdapter(videos);
                userVideosAdapter.SetOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String video_url = response.body().videos.get(position).getComplete_url();
                        Intent intent = new Intent(getActivity(),Videoplayer.class);
                        intent.putExtra("video_url",video_url);
                        startActivity(intent);
                    }
                });

                userVideosList.setAdapter(userVideosAdapter);
            }

            @Override
            public void onFailure(Call<Videos> call, Throwable t) {

            }


        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

inflater.inflate(R.menu.menu_logout,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.LogOut_button:
                SharedPreferences failPreferences = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = failPreferences.edit();
                edit.putString("username",null);
                edit.putString("password",null);
                edit.commit();
                Intent logout = new Intent(getActivity(),MainActivity.class);
                startActivity(logout);
                break;

        }
        return true;

    }
}

