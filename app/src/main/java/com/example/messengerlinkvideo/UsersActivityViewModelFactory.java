package com.example.messengerlinkvideo;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class UsersActivityViewModelFactory implements ViewModelProvider.Factory {
    private String accessToken;
    private String refreshToken;

    public UsersActivityViewModelFactory(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UsersActivityViewModel(accessToken, refreshToken);
    }
}

