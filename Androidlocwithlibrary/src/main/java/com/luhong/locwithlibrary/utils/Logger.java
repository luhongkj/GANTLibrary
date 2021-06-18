package com.luhong.locwithlibrary.utils;

import android.util.Log;

public class Logger {
    private static boolean DEBUG = true;

    public static void error(String msg) {
        print(Log.ERROR, msg);
    }

    public static void warn(String msg) {
        print(Log.WARN, msg);
    }

    public static void verbose(String msg) {
        print(Log.VERBOSE, msg);
    }

    public static void info(String msg) {
        print(Log.INFO, msg);
    }

    public static void debug(String msg) {
        print(Log.DEBUG, msg);
    }


    private static void print(int level, String msg) {
        if (DEBUG) {
            String tag = getClassName();
            String line = getLineIndicator();
            Log.println(level, tag, line + msg);
        }
    }

    private static String getClassName() {
        StackTraceElement element = ((new Exception()).getStackTrace())[3];
        return element.getFileName();
    }

    private static String getLineIndicator() {
        StackTraceElement element = ((new Exception()).getStackTrace())[3];
        return "(" + element.getFileName() + ":" + element.getLineNumber() + ")." + element.getMethodName() + ":";
    }
}
