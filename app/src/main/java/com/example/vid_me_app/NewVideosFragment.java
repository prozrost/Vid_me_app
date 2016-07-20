package com.example.vid_me_app;


import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.io.IOException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewVideosFragment extends Fragment {
    RecyclerViewAdapter newVideosAdapter;
    public static final String ROOT_URL = "https://api.vid.me/";
    public List<Video> videos;
    SwipeRefreshLayout refreshNew;
    RecyclerView newVideosList;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new, container, false);
        newVideosList = (RecyclerView) rootView.findViewById(R.id.new_List);
       newVideosList.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        newVideosList.setLayoutManager(llm);

        refreshNew = (SwipeRefreshLayout)rootView.findViewById(R.id.refresh_new);
        refreshNew.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                newVideosAdapter.notifyDataSetChanged();
                refreshNew.setRefreshing(false);


            }
        });

            try {
                getVideos();
            } catch (IOException e) {
                e.printStackTrace();

        }

        return rootView;
    }
    private void getVideos() throws IOException {
        Retrofit retrofitAdapter = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ROOT_URL)
                .build();
        final VideoApi videoApi = retrofitAdapter.create(VideoApi.class);
        Call<Videos> call = videoApi.getNewVideo();
        call.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, final Response<Videos> response) {
                videos = response.body().videos;
                newVideosAdapter = new RecyclerViewAdapter(videos);
                newVideosAdapter.SetOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String video_url = response.body().videos.get(position).getComplete_url();
                        Intent intent = new Intent(getActivity(),Videoplayer.class);
                        intent.putExtra("video_url",video_url);
                        startActivity(intent);
                    }
                });

                newVideosList.setAdapter(newVideosAdapter);
            }

            @Override
            public void onFailure(Call<Videos> call, Throwable t) {

            }


        });
    }


}