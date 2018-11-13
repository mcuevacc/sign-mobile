package pe.edu.uni.www.vitalsign.Service.Util;

import android.content.SharedPreferences;

public class Util {

    public static void setDataPrefs(SharedPreferences preferences, String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getDataPrefs(SharedPreferences preferences, String key) {
        return preferences.getString(key, "");
    }

    public static void delDataPrefs(SharedPreferences preferences, String key) {
        SharedPreferences.Editor editor =  preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void delAllPrefs(SharedPreferences preferences) {
        preferences.edit().clear().apply();
    }
}
