package pe.edu.uni.www.mutualert.Service.Util;

import android.content.SharedPreferences;

public class Preference{

    private SharedPreferences preferences;

    public Preference(SharedPreferences prefs){
        this.preferences = prefs;
    }

    public void setDataPref(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getDataPref(String key) {
        return preferences.getString(key, "");
    }

    public void delDataPref(String key) {
        SharedPreferences.Editor editor =  preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void delAllPref() {
        preferences.edit().clear().apply();
    }
}
