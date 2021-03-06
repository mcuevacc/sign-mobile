package pe.edu.uni.www.mutualert.App;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import pe.edu.uni.www.mutualert.Service.ApiBackend.ApiRequest;

public class Globals extends Application {
    private SharedPreferences pref;
    private ApiRequest apiRequest;

    @Override
    public void onCreate() {
        super.onCreate();

        initElement();
    }

    public void initElement(){
        this.pref = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        this.apiRequest = new ApiRequest(getApplicationContext());
    }

    public SharedPreferences getSharedPref(){
        return this.pref;
    }

    public ApiRequest getApiRequest(){
        return this.apiRequest;
    }
}
