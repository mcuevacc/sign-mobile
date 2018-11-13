package pe.edu.uni.www.vitalsign.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import pe.edu.uni.www.vitalsign.R;
import pe.edu.uni.www.vitalsign.Service.Util.ApiBackend.UserService;
import pe.edu.uni.www.vitalsign.Service.Util.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.Util.ServiceResponse;
import pe.edu.uni.www.vitalsign.Service.Util.Util;

public class LoginActivity extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private SharedPreferences prefs;
    private ApiRequest apiRequest;
    private UserService userService;

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Switch switchRemember;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initElement();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = editTextUsername.getText().toString();
                final String password = editTextPassword.getText().toString();

                userService.login(new ServiceResponse() {
                    @Override
                    public void onBoolanResponse(boolean resp) {
                        if(resp){
                            saveOnPreferences(username, password);
                            if(checkAndRequestPermissions()) {
                                goToMain();
                            }
                        }
                    }
                },prefs,username,password);
            }
        });
        setCredentialsIfExist();
    }


    private void initElement() {
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        apiRequest = new ApiRequest(getApplicationContext());
        userService = new UserService(apiRequest);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        switchRemember = (Switch) findViewById(R.id.switchRemember);
        btnLogin = (Button) findViewById(R.id.buttonLogin);
    }

    private void setCredentialsIfExist() {
        String username = Util.getDataPrefs(prefs,"username");
        String password = Util.getDataPrefs(prefs,"password");
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            editTextUsername.setText(username);
            editTextPassword.setText(password);
            switchRemember.setChecked(true);
        }
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void saveOnPreferences(String username, String password) {
        if( switchRemember.isChecked() ) {
            Util.setDataPrefs(prefs,"username", username);
            Util.setDataPrefs(prefs,"password", password);
        }else{
            Util.delDataPrefs(prefs,"username");
            Util.delDataPrefs(prefs,"password");
        }
    }

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}
