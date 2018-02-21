package com.ksucapstone.gasandgo.Repositories;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesWrapper {

    public final static String FIRST_LOGIN = "FIRST_LOGIN";
    public final static String USERNAME = "USERNAME";

    private final String PREFERENCE_FILE = "PREF_FILE";
    private Context mContext;

    public SharedPreferencesWrapper(Context context){
        mContext = context;
    }

    public String getString(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }

    public void writeString(String key, String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defaultValue){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,defaultValue);
    }

    public void writeBoolean(String key, boolean value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void writeLong(String key, long value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key, long defaultValue){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultValue);
    }
}
