package com.paz.ezloggerview.data;

public class LogLevel {
    public static final int DEBUG = 0;
    public static final int INFO = 1;
    public static final int WARN = 2;
    public static final int ERROR = 3;
    public static final int VERBOSE = 4;
    public static final int ASSERTS = 5;

    public static String getLevelText(int level) {
        switch (level) {
            case DEBUG:
                return "debug";
            case INFO:
                return "info";
            case WARN:
                return "warn";
            case ERROR:
                return "error";
            case VERBOSE:
                return "verbose";
            case ASSERTS:
                return "asserts";
            default:
                return "unknown";
        }
    }
}
