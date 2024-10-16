package com.example.messengerlinkvideo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UsersActivityViewModel extends ViewModel {
    private String accessToken;
    private String refreshToken;
    private final MutableLiveData<List<User>> users = new MutableLiveData<List<User>>();

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final String TAG = "UsersActivityViewModel";




    public UsersActivityViewModel(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        loadUsers();
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }

    private void loadUsers(){
        Disposable disposable = ApiFactory.apiService.getUsers(accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> usersResponse) throws Throwable {
                        users.setValue(usersResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        refreshAccessToken(refreshToken);
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void refreshAccessToken (String refresh){

        Disposable disposable = ApiFactory.apiService.refreshAccessToken(
                "application/json",
                refresh)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Token>() {
                    @Override
                    public void accept(Token token) throws Throwable {
                        accessToken = token.getAccess();
                        refreshToken = token.getRefresh();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG,throwable.toString());
                    }
                });

        compositeDisposable.add(disposable);
    }


    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }


}
