package com.example.messengerlinkvideo;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChatViewModelFactory  implements ViewModelProvider.Factory{
    private final int currentUserId;
    private final int otherUserId;
    private String accessToken;
    private String refreshToken;

    public ChatViewModelFactory(int currentUserId, int otherUserId, String accessToken, String refreshToken) {
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChatViewModel(currentUserId, otherUserId, accessToken, refreshToken);
    }
}
