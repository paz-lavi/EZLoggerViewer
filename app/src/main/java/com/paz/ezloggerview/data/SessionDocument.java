package com.paz.ezloggerview.data;

import android.content.Context;


public class SessionDocument {
    private String logs; // logs as json string
    private String costumerId;
    private String ezLogId;
    private int sessionCounter;
    private Context context;
    private Float batteryLevel;
    private int brightness;
    private String ringerMode;
    private String manufacturerName;
    private boolean btEnable;
    private boolean connectedToWifi;
    private boolean gpsEnable;
    private String deviceLang;
    private String date;
    private long timestamp;
    private String packageName;


    public SessionDocument() {
    }

    public String getLogs() {
        return logs;
    }

    public SessionDocument setLogs(String logs) {
        this.logs = logs;
        return this;
    }

    public String getCostumerId() {
        return costumerId;
    }

    public SessionDocument setCostumerId(String costumerId) {
        this.costumerId = costumerId;
        return this;
    }

    public String getEzLogId() {
        return ezLogId;
    }

    public SessionDocument setEzLogId(String ezLogId) {
        this.ezLogId = ezLogId;
        return this;
    }

    public int getSessionCounter() {
        return sessionCounter;
    }

    public SessionDocument setSessionCounter(int sessionCounter) {
        this.sessionCounter = sessionCounter;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public SessionDocument setContext(Context context) {
        this.context = context;
        return this;
    }

    public Float getBatteryLevel() {
        return batteryLevel;
    }

    public SessionDocument setBatteryLevel(Float batteryLevel) {
        this.batteryLevel = batteryLevel;
        return this;
    }

    public int getBrightness() {
        return brightness;
    }

    public SessionDocument setBrightness(int brightness) {
        this.brightness = brightness;
        return this;
    }

    public String getRingerMode() {
        return ringerMode;
    }

    public SessionDocument setRingerMode(String ringerMode) {
        this.ringerMode = ringerMode;
        return this;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public SessionDocument setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
        return this;
    }

    public boolean isBtEnable() {
        return btEnable;
    }

    public SessionDocument setBtEnable(boolean btEnable) {
        this.btEnable = btEnable;
        return this;
    }

    public boolean isConnectedToWifi() {
        return connectedToWifi;
    }

    public SessionDocument setConnectedToWifi(boolean connectedToWifi) {
        this.connectedToWifi = connectedToWifi;
        return this;
    }

    public boolean isGpsEnable() {
        return gpsEnable;
    }

    public SessionDocument setGpsEnable(boolean gpsEnable) {
        this.gpsEnable = gpsEnable;
        return this;
    }

    public String getDeviceLang() {
        return deviceLang;
    }

    public SessionDocument setDeviceLang(String deviceLang) {
        this.deviceLang = deviceLang;
        return this;
    }

    public String getDate() {
        return date;
    }

    public SessionDocument setDate(String date) {
        this.date = date;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public SessionDocument setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public SessionDocument setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    @Override
    public String toString() {
        return "SessionDocument{" +
                "logs='" + logs + '\'' +
                ", costumerId='" + costumerId + '\'' +
                ", ezLogId='" + ezLogId + '\'' +
                ", sessionCounter=" + sessionCounter +
                ", context=" + context +
                ", batteryLevel=" + batteryLevel +
                ", brightness=" + brightness +
                ", ringerMode='" + ringerMode + '\'' +
                ", manufacturerName='" + manufacturerName + '\'' +
                ", btEnable=" + btEnable +
                ", connectedToWifi=" + connectedToWifi +
                ", gpsEnable=" + gpsEnable +
                ", deviceLang='" + deviceLang + '\'' +
                ", date='" + date + '\'' +
                ", timestamp=" + timestamp +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}
