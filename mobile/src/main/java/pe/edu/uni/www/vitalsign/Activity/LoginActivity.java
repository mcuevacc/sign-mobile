package pe.edu.uni.www.vitalsign.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import pe.edu.uni.www.vitalsign.R;
import pe.edu.uni.www.vitalsign.Service.Util.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.Util.RequestCallBack;
import pe.edu.uni.www.vitalsign.Service.Util.Util;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private ApiRequest apiRequest;

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Switch switchRemember;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindUI();

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        apiRequest = new ApiRequest(getApplicationContext());

        setCredentialsIfExist();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if (login(username, password)) {
                    goToMain();
                    saveOnPreferences(username, password);
                }
            }
        });
    }


    private void bindUI() {
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

    private boolean login(String email, String password) {
        /*
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Email is not valid, please try again", Toast.LENGTH_LONG).show();
            return false;
        } else if (!isValidPassword(password)) {
            Toast.makeText(this, "Password is not valid, 4 characters or more, please try again", Toast.LENGTH_LONG).show();
            return false;
        }
        */

        apiRequest.sendGet(new RequestCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d("TAG", response.toString());
            }
        }, "/user/p");




        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", "mcueva");
            jsonBody.put("password", "mcueva");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        /*
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonBody,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        //Log.d("Response", "Si funca papi");
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                        //Log.e("Response", "No funciona perro");
                    }
                });

        mQueue.add(jsonObjectRequest);
        */
        return true;
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

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 4;
    }
}
