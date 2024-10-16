package com.example.messengerlinkvideo;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CurrentUser implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("login")
    private String login;
    @SerializedName("token")
    private Token token;

    public CurrentUser(int id, String login, Token token) {
        this.id = id;
        this.login = login;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public Token getToken() {
        return token;
    }

}
