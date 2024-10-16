package com.example.messengerlinkvideo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivityViewModel extends AndroidViewModel {

    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<CurrentUser> currentUser = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<Boolean> isLogin = new MutableLiveData<>();

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);

    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<CurrentUser> getCurrentUser() {
        return currentUser;
    }

    public LiveData<Boolean> getIsLogin() {
        return isLogin;
    }

    public void login(String login, String password) {

        JsonObject loginRequest = new JsonObject();
        loginRequest.addProperty("login", login);
        loginRequest.addProperty("password", password);

        Disposable disposable = ApiFactory.apiService.login("application/json", "application/json", loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CurrentUser>() {
                    @Override
                    public void accept(CurrentUser currentUserResponse) throws Throwable {
                        currentUser.setValue(currentUserResponse);
                        isLogin.setValue(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        error.setValue(throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);


    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }
}
