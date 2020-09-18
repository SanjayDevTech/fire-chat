package com.sanjaydevtech.firechat;

public class Chats {

    public String getImg() {
        return img;
    }

    public Chats(String key, String name, String uid, String msg, String img) {
        this.key = key;
        this.name = name;
        this.uid = uid;
        this.msg = msg;
        this.img = img;
    }

    public Chats() {
    }

    private String key, name, uid, msg, img;

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
