package pe.edu.uni.www.vitalsign.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import pe.edu.uni.www.vitalsign.App.Globals;
import pe.edu.uni.www.vitalsign.R;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.User.UserAccount;
import pe.edu.uni.www.vitalsign.Service.Util.Preference;

public class LoginActivity extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private Preference pref;
    private ApiRequest apiRequest;
    private UserAccount userAccount;

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button btnLogin;
    private Button btnCreateAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initElement();

        btnLogin.setOnClickListener(view -> {
            final String username = editTextUsername.getText().toString();
            final String password = editTextPassword.getText().toString();

            userAccount.login(resp -> {
                if(resp){
                    //if(checkAndRequestPermissions()) {
                    goToMain();
                    //}
                }
            },username,password);

            saveOnPreferences(username);
        });
        setCredentialsIfExist();

        btnCreateAccountButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), NewAccountActivity.class);
            startActivity(intent);
        });
    }

    private void initElement() {
        pref = new Preference(((Globals)getApplication()).getSharedPref());
        apiRequest = ((Globals)getApplication()).getApiRequest();
        userAccount = new UserAccount(apiRequest,pref);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        btnLogin = (Button) findViewById(R.id.buttonLogin);

        btnCreateAccountButton = (Button) findViewById(R.id.buttonCreateAccountButton);
    }

    private void setCredentialsIfExist() {
        String username = pref.getDataPref("username");
        if (!TextUtils.isEmpty(username)) {
            editTextUsername.setText(username);
        }
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void saveOnPreferences(String username) {
        pref.setDataPref("username", username);
    }
    /*
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
    */
}
