package com.paz.ezloggerview.data;

import android.os.Bundle;

import com.paz.ezloggerview.helpers.TimestampGenerator;

import org.jetbrains.annotations.NotNull;

import static com.paz.ezloggerview.helpers.Constants.CHIP_INDEX;
import static com.paz.ezloggerview.helpers.Constants.COSTUMER_ID;
import static com.paz.ezloggerview.helpers.Constants.DATE_MAX;
import static com.paz.ezloggerview.helpers.Constants.DATE_MIN;
import static com.paz.ezloggerview.helpers.Constants.EZ_LOG_ID;
import static com.paz.ezloggerview.helpers.Constants.MANUFACTURER_NAME;
import static com.paz.ezloggerview.helpers.Constants.PACKAGE_NAME;
import static com.paz.ezloggerview.helpers.Constants.SESSION_COUNTER;

public class SavedQuery {
    private String costumerId;
    private String ezLogId;
    private long timestamp;
    private String manufacturerName;
    private long d1;
    private long d2;
    private String date;
    private int chipIndex;
    private int sessionCounter;
    private String packageName;


    public SavedQuery() {
    }

    public String getCostumerId() {
        return costumerId;
    }

    public SavedQuery setCostumerId(String costumerId) {
        this.costumerId = costumerId;
        return this;
    }

    public String getEzLogId() {
        return ezLogId;
    }

    public SavedQuery setEzLogId(String ezLogId) {
        this.ezLogId = ezLogId;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public SavedQuery setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public SavedQuery setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
        return this;
    }

    public long getD1() {
        return d1;
    }

    public SavedQuery setD1(long d1) {
        this.d1 = d1;
        return this;
    }

    public long getD2() {
        return d2;
    }

    public SavedQuery setD2(long d2) {
        this.d2 = d2;
        return this;
    }

    public String getDate() {
        return date;
    }

    public SavedQuery setDate(String date) {
        this.date = date;
        return this;
    }

    public int getChipIndex() {
        return chipIndex;
    }

    public SavedQuery setChipIndex(int chipIndex) {
        this.chipIndex = chipIndex;
        return this;
    }

    public int getSessionCounter() {
        return sessionCounter;
    }

    public SavedQuery setSessionCounter(int sessionCounter) {
        this.sessionCounter = sessionCounter;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public SavedQuery setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public SavedQuery fromBundle(@NotNull Bundle b) {
        d1 = b.getLong(DATE_MIN);
        d2 = b.getLong(DATE_MAX);
        ezLogId = b.getString(EZ_LOG_ID);
        costumerId = b.getString(COSTUMER_ID);
        manufacturerName = b.getString(MANUFACTURER_NAME);
        chipIndex = b.getInt(CHIP_INDEX, -1);
        sessionCounter = b.getInt(SESSION_COUNTER, -1);
        date = TimestampGenerator.currentTimeStamp();
        timestamp = System.currentTimeMillis();
        packageName = b.getString(PACKAGE_NAME);
        return this;
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putLong(DATE_MIN, d1);
        b.putLong(DATE_MAX, d2);
        b.putString(EZ_LOG_ID, ezLogId);
        b.putString(COSTUMER_ID, costumerId);
        b.putString(MANUFACTURER_NAME, manufacturerName);
        b.putInt(CHIP_INDEX, chipIndex);
        b.putInt(SESSION_COUNTER, sessionCounter);
        b.putString(PACKAGE_NAME, packageName);
        return b;
    }
}
