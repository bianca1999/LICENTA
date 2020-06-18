package com.example.licenta.model;

public class Chat {
    private String sender, receiver, message,time;

    public Chat(String sender, String receiver, String message,String time) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time=time;
    }

    public Chat() {

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class ChatViewHolder {
    }
}
