package com.example.messengerlinkvideo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChatViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> messageSent = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MessageDao messageDao;

    private static final String TAG = "ChatViewModel";
    private final int currentUserId;
    private final int otherUserId;
    private String accessToken;
    private String refreshToken;


    public ChatViewModel(@NonNull Application application, int currentUserId, int otherUserId, String accessToken, String refreshToken) {
        super(application);
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        messageDao = MessageDatabase.getInstance(application).messageDao();
        getMessage();
    }

    public LiveData<Boolean> getMessageSent() {
        return messageSent;
    }

    public LiveData<String> getError() {
        return error;
    }

    private void getMessage() {
        Disposable disposable = ApiFactory.apiService.getMessages(otherUserId, "application/json", accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Message>>() {
                    @Override
                    public void accept(List<Message> messagesResponse) throws Throwable {
                        List<Message> sortMessages = new ArrayList<>(messagesResponse);
                        sortMessages.sort(Comparator.comparing(Message::getDate));

                        for (Message message:sortMessages) {
                            insertMessage(message);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        error.setValue(throwable.toString());
                        refreshAccessToken(refreshToken);
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void insertMessage (Message message){
        Disposable disposable = messageDao.insertMassage(message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG,throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    public LiveData <List <Message>> getMessagesFromDb(int fromUserId, int toUserId ){
        return messageDao.getAllMassage(fromUserId, toUserId);
    }


    private void refreshAccessToken(String refresh) {

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
                        Log.d(TAG, throwable.toString());
                    }
                });

        compositeDisposable.add(disposable);
    }

    public void sendMessage(String message) {

        JsonObject messageRequest = new JsonObject();
        messageRequest.addProperty("from", currentUserId);
        messageRequest.addProperty("message", message);
        messageRequest.addProperty("to", otherUserId);

        Disposable disposable = ApiFactory.apiService.sendMessage("application/json", "application/json", accessToken, messageRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {
                        messageSent.setValue(true);
                        getMessage();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        messageSent.setValue(true);
                        getMessage();
                    }
                });

        compositeDisposable.add(disposable);


    }

    //Работа с WebSocet





    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
    }


}
