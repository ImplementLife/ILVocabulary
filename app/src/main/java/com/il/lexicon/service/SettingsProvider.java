package com.il.lexicon.service;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsProvider {
    private final Context context;
    private final String prefName = "il_settings";

    public SettingsProvider(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public int getIntProperty(String tag, int defaultValue) {
        SharedPreferences sp = getContext().getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sp.getInt(tag, defaultValue);
    }
    public int getIntProperty(String tag) {
        return getIntProperty(tag, 0);
    }

    public boolean getBoolProperty(String tag) {
        SharedPreferences sp = getContext().getSharedPreferences(prefName, Context.MODE_PRIVATE);
        return sp.getBoolean(tag,true);
    }

    public void setProperty(String tag, boolean value) {
        SharedPreferences.Editor editor = getContext().getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
        editor.putBoolean(tag, value);
        editor.apply();
    }
    public void setProperty(String tag, int value) {
        SharedPreferences.Editor editor = getContext().getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
        editor.putInt(tag, value);
        editor.apply();
    }
}
