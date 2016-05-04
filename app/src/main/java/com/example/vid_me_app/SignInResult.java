package com.example.vid_me_app;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SignInResult {

    @SerializedName("status")
    private Boolean status;
    @SerializedName("auth")
    private Auth auth;


    public Boolean getStatus() {
        return status;
    }


    public Auth getAuth() {
        return auth;
    }

    public static class Auth {

        @SerializedName("token")
        private String token;
        @SerializedName("expires")
        private String expires;
        @SerializedName("user_id")
        private String userId;

        public String getToken() {
            return token;
        }

    }
}