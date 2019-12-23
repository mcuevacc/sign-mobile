package pe.edu.uni.www.mutualert.Activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import pe.edu.uni.www.mutualert.App.Globals;
import pe.edu.uni.www.mutualert.R;
import pe.edu.uni.www.mutualert.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.mutualert.Service.ApiBackend.User.UserAccount;
import pe.edu.uni.www.mutualert.Service.Util.Preference;

public class LoginActivity extends AppCompatActivity {

    private Preference pref;
    private ApiRequest apiRequest;
    private UserAccount userAccount;

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button btnLogin;
    private Button btnCreateAccount;
    private Button btnForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null)
            initUI();
    }

    private void initUI() {
        pref = new Preference(((Globals)getApplication()).getSharedPref());
        apiRequest = ((Globals)getApplication()).getApiRequest();
        userAccount = new UserAccount(apiRequest,pref);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        btnLogin = (Button) findViewById(R.id.buttonLogin);
        btnLogin.setOnClickListener(view -> {
            final String username = editTextUsername.getText().toString();
            final String password = editTextPassword.getText().toString();

            userAccount.login(resp -> {
                if(resp) goToMain();
            },username,password);

            saveOnPreferences(username);
        });

        btnCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
        btnCreateAccount.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), NewAccountActivity.class);
            intent.putExtra("isNewUser", true);
            startActivity(intent);
        });

        btnForgot = (Button) findViewById(R.id.buttonForgot);
        btnForgot.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), NewAccountActivity.class);
            intent.putExtra("isNewUser", false);
            startActivity(intent);
        });

        setCredentialsIfExist();
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
}
