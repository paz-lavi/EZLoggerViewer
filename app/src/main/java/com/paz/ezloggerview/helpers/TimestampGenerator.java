package com.paz.ezloggerview.helpers;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

@SuppressLint("SimpleDateFormat")
public class TimestampGenerator {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss:ms");

    public static String currentTimeStamp() {
        return timestampFormat.format(System.currentTimeMillis());
    }

    public static String getDateAsString(long timestamp) {
        return dateFormat.format(timestamp);
    }

}
