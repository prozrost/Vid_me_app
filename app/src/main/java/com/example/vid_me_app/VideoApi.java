package com.example.vid_me_app;



import retrofit2.Call;


import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface VideoApi {

    @GET("/videos/featured")
    Call<Videos> getFeaturedVideo();

    @GET("/videos/new")
    Call<Videos> getNewVideo();
    
@Headers("Content-Type:application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("/auth/create")
   Call<ConnectionResult>insertUser(@Field("username") String username,
                                    @Field("password") String password
                           );

    @GET("/videos/following")
    Call<Videos> getFollowingVideo(@Header("AccessToken")String token );
}