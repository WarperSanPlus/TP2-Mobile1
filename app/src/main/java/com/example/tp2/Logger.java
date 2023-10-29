package com.example.tp2;

import android.util.Log;

public abstract class Logger {
    public static void log(String message) {
        if (Config.ENABLE_LOG)
            Log.d("LOG-TP2", message);
    }
}
