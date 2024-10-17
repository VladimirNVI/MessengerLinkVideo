package com.example.messengerlinkvideo;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Message.class}, version = 1, exportSchema = false)
public abstract   class MessageDatabase extends RoomDatabase{
    private static MessageDatabase instance = null;
    private static final String DB_NAME = "message.db";

    public static MessageDatabase getInstance(Application application){
        if (instance == null){
            instance = Room.databaseBuilder(
                    application,
                    MessageDatabase.class,
                    DB_NAME
            ).build();
        }
        return instance;
    }

    abstract MessageDao messageDao ();
}
