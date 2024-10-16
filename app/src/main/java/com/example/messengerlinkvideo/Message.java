package com.example.messengerlinkvideo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Message implements Serializable {

    @SerializedName("id")
    private Integer id;
    @SerializedName("from")
    private Integer from;
    @SerializedName("to")
    private Integer to;
    @SerializedName("message")
    private String message;
    @SerializedName("date")
    private Long date;
    @SerializedName("delivered")
    private Boolean delivered;

    public Message(Integer id, Integer from, Integer to, String message, Long date, Boolean delivered) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        this.delivered = delivered;
    }
    public Message(){

    }

    public Integer getId() {
        return id;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public Long getDate() {
        return date;
    }

    public Boolean getDelivered() {
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
