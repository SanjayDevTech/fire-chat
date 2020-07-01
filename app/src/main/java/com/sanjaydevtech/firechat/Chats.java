package com.sanjaydevtech.firechat;

public class Chats {
    public Chats(String key, String name, String uid, String msg) {
        this.key = key;
        this.name = name;
        this.uid = uid;
        this.msg = msg;
    }

    public Chats() {
    }

    private String key, name, uid, msg;

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getMsg() {
        return msg;
    }
}
