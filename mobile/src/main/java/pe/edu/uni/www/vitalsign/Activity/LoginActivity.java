package pe.edu.uni.www.vitalsign.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;


import pe.edu.uni.www.vitalsign.R;
import pe.edu.uni.www.vitalsign.Service.Util.ApiBackend.UserService;
import pe.edu.uni.www.vitalsign.Service.Util.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.Util.Util;

public class LoginActivity extends AppCompatActivity {

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
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if( userService.login(username, password) ){
                    goToMain();
                    saveOnPreferences(username, password);
                }
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
        if (switchRemember.isChecked()) {
            Util.setDataPrefs(prefs,"username", username);
            Util.setDataPrefs(prefs,"password", password);
        }else{
            Util.delDataPrefs(prefs,"username");
            Util.delDataPrefs(prefs,"password");
        }
    }
}
