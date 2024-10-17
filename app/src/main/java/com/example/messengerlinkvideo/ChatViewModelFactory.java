package com.example.messengerlinkvideo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChatViewModelFactory extends ViewModelProvider.AndroidViewModelFactory{
    private final Application application;
    private final int currentUserId;
    private final int otherUserId;
    private String accessToken;
    private String refreshToken;

    public ChatViewModelFactory(@NonNull Application application,
                                int currentUserId,
                                int otherUserId,
                                String accessToken,
                                String refreshToken
    ) {
        super(application);
        this.application = application;
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChatViewModel.class)) {
            return (T) new ChatViewModel(application, currentUserId, otherUserId, accessToken, refreshToken);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
