package mobile.bts.com.viefund.MultiLanguage;

import android.content.Context;
import android.content.SharedPreferences;

import mobile.bts.com.viefund.Application.BTApplication;

/**
 * BT Company
 * Created by Administrator on 3/15/2018.
 */

public class SharedPrefsLanguage {
    private static final String PREFS_NAME = "multi_language_active";
    public static final String LANGUAGE = "langauge";

    private static SharedPrefsLanguage mInstance;
    private SharedPreferences mSharedPreferences;

    private SharedPrefsLanguage() {
        mSharedPreferences = BTApplication.getInstance().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPrefsLanguage getInstance() {
        if (mInstance == null) {
            mInstance = new SharedPrefsLanguage();
        }
        return mInstance;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> anonymousClass) {
        if (anonymousClass == String.class) {
            return (T) mSharedPreferences.getString(key, "");
        } else if (anonymousClass == Boolean.class) {
            return (T) Boolean.valueOf(mSharedPreferences.getBoolean(key, false));
        } else if (anonymousClass == Float.class) {
            return (T) Float.valueOf(mSharedPreferences.getFloat(key, 0));
        } else if (anonymousClass == Integer.class) {
            return (T) Integer.valueOf(mSharedPreferences.getInt(key, 0));
        } else if (anonymousClass == Long.class) {
            return (T) Long.valueOf(mSharedPreferences.getLong(key, 0));
        } else {
            return (T) BTApplication.getInstance()
                    .getGSon()
                    .fromJson(mSharedPreferences.getString(key, ""), anonymousClass);
        }
    }

    public <T> void put(String key, T data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        } else {
            editor.putString(key, BTApplication.getInstance().getGSon().toJson(data));
        }
        editor.apply();
    }

    public void clear() {
        mSharedPreferences.edit().clear().apply();
    }


}
