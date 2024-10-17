package com.example.messengerlinkvideo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "message")
public class Message implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    private int id;
    @SerializedName("from")
    private int from;
    @SerializedName("to")
    private int to;
    @SerializedName("message")
    private String message;
    @SerializedName("date")
    private long date;
    @SerializedName("delivered")
    private boolean delivered;

    public Message(int id, int from, int to, String message, long date, boolean delivered) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.delivered = delivered;
    }

    public int getId() {
        return id;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public long getDate() {
        return date;
    }

    public boolean getDelivered() {
        return delivered;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", delivered=" + delivered +
                '}';
    }
}
