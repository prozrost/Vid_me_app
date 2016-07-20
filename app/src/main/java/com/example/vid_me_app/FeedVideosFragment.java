package com.example.vid_me_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FeedVideosFragment extends Fragment {
    EditText username;
    EditText password;
    String sharedPreferencesUsername;
    String sharedPreferencesPassword;
    String usernameValue, passwordValue;
    String inSharedPreferenceUsername;
    String inSharedPreferencePassword;
    Call<ConnectionResult> call;
    public static final String ROOT_URL = "https://api.vid.me/";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        username = (EditText) rootView.findViewById(R.id.user_name_field);
        password = (EditText) rootView.findViewById(R.id.password_field);
        LinearLayout linearLayout = (LinearLayout)rootView.findViewById(R.id.my_button);
        SharedPreferences UsernameAndPasswordValues = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
        inSharedPreferenceUsername = UsernameAndPasswordValues.getString("username",null);
        inSharedPreferencePassword = UsernameAndPasswordValues.getString("password",null);

        if(inSharedPreferenceUsername!=null & inSharedPreferencePassword!=null){
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

         usernameValue = username.getText().toString();
         passwordValue = password.getText().toString();

        SharedPreferences SavedUsernameAndPasswordValues = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
        sharedPreferencesUsername = SavedUsernameAndPasswordValues.getString("username",null);
        sharedPreferencesPassword = SavedUsernameAndPasswordValues.getString("password",null);
        Log.d("Shared preference", sharedPreferencesUsername + sharedPreferencesPassword);
        if(sharedPreferencesUsername ==null || sharedPreferencesPassword ==null) {
            call = videoApi.insertUser(usernameValue, passwordValue);
        }
        else{
            call = videoApi.insertUser(sharedPreferencesUsername, sharedPreferencesPassword);
        }
        call.enqueue(new Callback<ConnectionResult>() {


            @Override
            public void onResponse(Call<ConnectionResult> call, Response<ConnectionResult> response) {
if(response.body()==null){
    buildDialog(getActivity()).show();
    SharedPreferences failConnectionValues = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
    SharedPreferences.Editor edit = failConnectionValues.edit();
    edit.putString("username",null);
    edit.putString("password",null);
    edit.commit();
}
                else {
    Boolean i = response.body().getStatus();
    if (i) {

        SharedPreferences successfulConnectionValues = getActivity().getSharedPreferences("Pref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = successfulConnectionValues.edit();
        editor.putString("token", response.body().getAuth().getToken());
        editor.putString("username", usernameValue);
        editor.putString("password", passwordValue);
        editor.commit();
        Intent user_activity_intent = new Intent(getActivity(), UserVideos.class);
        startActivity(user_activity_intent);

    }
}



            }

            @Override
            public void onFailure(Call<ConnectionResult> call, Throwable t) {

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