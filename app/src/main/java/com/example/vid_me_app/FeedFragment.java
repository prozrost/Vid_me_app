package com.example.vid_me_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FeedFragment extends Fragment {
    EditText username;
    EditText password;
    String sharedpref_username;
    String sharedpref_password;
    String username_value,password_value;
    Call<SignInResult> call;
    public static final String ROOT_URL = "https://api.vid.me/";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        username = (EditText) rootView.findViewById(R.id.user_name_field);
        password = (EditText) rootView.findViewById(R.id.password_field);
        LinearLayout linearLayout = (LinearLayout)rootView.findViewById(R.id.my_button);
        SharedPreferences sharedPreferencesm = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
        String prefs_username = sharedPreferencesm.getString("username",null);
        String prefs_password = sharedPreferencesm.getString("password",null);

        if(prefs_username!=null & prefs_password!=null){
            Authorize();
        }
        else {
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Authorize();
                }
            });
        }
        return rootView;
    }

    public void Authorize() {
        Retrofit retrofitAdapter = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ROOT_URL)
                .build();
        final VideoApi videoApi = retrofitAdapter.create(VideoApi.class);

         username_value = username.getText().toString();
         password_value = password.getText().toString();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
        sharedpref_username = sharedPreferences.getString("username",null);
        sharedpref_password= sharedPreferences.getString("password",null);
        Log.d("Shared preference",sharedpref_username+sharedpref_password);
        if(sharedpref_username==null || sharedpref_password==null) {
            call = videoApi.insertUser(username_value, password_value);
        }
        else{
            call = videoApi.insertUser(sharedpref_username,sharedpref_password);
        }
        call.enqueue(new Callback<SignInResult>() {


            @Override
            public void onResponse(Call<SignInResult> call, Response<SignInResult> response) {
if(response.body()==null){
    buildDialog(getActivity()).show();
    SharedPreferences failpreferences = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
    SharedPreferences.Editor edit = failpreferences.edit();
    edit.putString("username",null);
    edit.putString("password",null);
    edit.commit();
}
                else {
    Boolean i = response.body().getStatus();
    if (i == true) {

        SharedPreferences sharedPref = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token", response.body().getAuth().getToken());
        editor.putString("username", username_value);
        editor.putString("password",password_value);
        editor.commit();
        Intent user_activity_intent = new Intent(getActivity(), User_videos.class);
        startActivity(user_activity_intent);

    }
}



            }

            @Override
            public void onFailure(Call<SignInResult> call, Throwable t) {

                call.cancel();
                buildDialog(getActivity());
            }
        });
}
    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Invalid password ");
        builder.setMessage("The password you entered was not valid");
        builder.setIcon(R.drawable.ic_block_black_18dp);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return builder;
    }
}