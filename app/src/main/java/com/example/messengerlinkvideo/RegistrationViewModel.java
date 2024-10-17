package com.example.messengerlinkvideo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RegistrationViewModel extends AndroidViewModel {
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<CurrentUser> currentUser = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<Boolean> isRegistration = new MutableLiveData<>();

    public RegistrationViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<CurrentUser> getCurrentUser() {
        return currentUser;
    }

    public LiveData<Boolean> getIsRegistration() {
        return isRegistration;
    }

    public void registration(String login, String password) {

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("login", login);
        requestBody.addProperty("password", password);

        Disposable disposable = ApiFactory.apiService.registerUser("application/json", "application/json", requestBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CurrentUser>() {
                    @Override
                    public void accept(CurrentUser currentUserResponse) throws Throwable {
                        currentUser.setValue(currentUserResponse);
                        isRegistration.setValue(true);
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
