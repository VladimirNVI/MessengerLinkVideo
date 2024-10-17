package com.example.messengerlinkvideo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
@Dao
public interface MessageDao {
    @Query("SELECT * FROM message WHERE (`from` = :fromUserId AND `to` = :toUserId) OR (`from` = :toUserId AND `to` = :fromUserId)")
    LiveData<List<Message>> getAllMassage(int fromUserId, int toUserId);
    @Insert
    Completable insertMassage(Message message);
}
