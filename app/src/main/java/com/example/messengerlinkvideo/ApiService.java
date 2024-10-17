package com.example.messengerlinkvideo;

import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {

    @GET("user/v1/users")
    Single<List<User>> getUsers(
            @Header("access_token") String accessToken
    );

    @POST("user/v1/login")
    Single<CurrentUser> login(
            @Header("accept") String acceptHeader,
            @Header("Content-Type") String contentTypeHeader,
            @Body JsonObject loginRequest
    );

    @POST("user/v1/refresh")
    Single<Token> refreshAccessToken(
            @Header("accept") String acceptHeader,
            @Header("refresh_token") String refreshToken
    );

    @GET("messenger/v1/messages")
    Single<List<Message>> getMessages(
            @Query("from") int from,
            @Header("accept") String acceptHeader,
            @Header("access_token") String accessToken
    );

    @POST("messenger/v1/send")
    Completable sendMessage(
            @Header("accept") String acceptHeader,
            @Header("Content-Type") String contentTypeHeader,
            @Header("access_token") String accessToken,
            @Body JsonObject messageRequest
    );

    @PUT("user/v1/registration")
    Single<CurrentUser> registerUser(
            @Header("accept") String acceptHeader,
            @Header("Content-Type") String contentTypeHeader,
            @Body JsonObject registrationRequest
    );






}
