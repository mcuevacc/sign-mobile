package pe.edu.uni.www.vitalsign.App;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import pe.edu.uni.www.vitalsign.Activity.LoginActivity;
import pe.edu.uni.www.vitalsign.Activity.MainActivity;

import pe.edu.uni.www.vitalsign.Service.Util.Util;

public class SplashScreenActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        Intent intentLogin = new Intent(this, LoginActivity.class);
        Intent intentMain = new Intent(this, MainActivity.class);

        if (!TextUtils.isEmpty(Util.getDataPrefs(prefs,"username")) &&
                !TextUtils.isEmpty(Util.getDataPrefs(prefs,"password"))) {
            startActivity(intentMain);
        } else {
            startActivity(intentLogin);
        }
        finish();
    }

}