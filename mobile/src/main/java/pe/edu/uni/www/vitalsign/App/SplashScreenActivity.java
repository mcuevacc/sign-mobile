package pe.edu.uni.www.vitalsign.App;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import pe.edu.uni.www.vitalsign.Activity.LoginActivity;
import pe.edu.uni.www.vitalsign.Activity.MainActivity;

import pe.edu.uni.www.vitalsign.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.User.UserAccount;
import pe.edu.uni.www.vitalsign.Service.Util.Preference;

public class SplashScreenActivity extends AppCompatActivity {

    private Preference pref;
    private ApiRequest apiRequest;
    private UserAccount userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intentLogin = new Intent(this, LoginActivity.class);
        final Intent intentMain = new Intent(this, MainActivity.class);

        pref = new Preference(((Globals)getApplication()).getSharedPref());
        String authToken = pref.getDataPref("authToken");

        if( !TextUtils.isEmpty(authToken) ){
            apiRequest = ((Globals)getApplication()).getApiRequest();
            userAccount = new UserAccount(apiRequest,pref);

            userAccount.toking(new UserAccount.booleanResponse() {
                @Override
                public void onResponse(boolean resp) {
                    if(resp){
                        //if(checkAndRequestPermissions()) {
                        startActivity(intentMain);
                        //}
                    } else {
                        startActivity(intentLogin);
                    }
                    finish();
                }
            });
        }else{
            startActivity(intentLogin);
            finish();
        }
    }

}