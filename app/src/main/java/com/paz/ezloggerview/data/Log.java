package com.paz.ezloggerview.data;

public class Log {
    String msg;
    String date;
    int level;

    public Log() {
    }

    public String getMsg() {
        return msg;
    }

    public Log setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Log setDate(String date) {
        this.date = date;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public Log setLevel(int level) {
        this.level = level;
        return this;
    }
}
