package pe.edu.uni.www.vitalsign.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Dictionary;
import java.util.Hashtable;

import pe.edu.uni.www.vitalsign.Activity.LoginActivity;
import pe.edu.uni.www.vitalsign.Activity.MainActivity;
import pe.edu.uni.www.vitalsign.App.Globals;
import pe.edu.uni.www.vitalsign.R;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.User.UserAccount;
import pe.edu.uni.www.vitalsign.Service.Util.DesingService;
import pe.edu.uni.www.vitalsign.Service.Util.Preference;

public class NewAccountInfoFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private View view;

    private static int MIN_LENGTH=4;

    private boolean isNewUser = true;
    private String username;
    private String code;

    private Preference pref;
    private ApiRequest apiRequest;
    private UserAccount userAccount;
    private DesingService desingService;

    private TextInputLayout etPasswordLayout;
    private TextInputLayout etApPatLayout;
    private TextInputLayout etApMatLayout;
    private TextInputLayout etNombresLayout;

    private TextInputEditText editTextPassword;
    private TextInputEditText editTextApPat;
    private TextInputEditText editTextApMat;
    private TextInputEditText editTextNombres;

    private Button buttonRegister;

    private Dictionary info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_account_info, container, false);

        Bundle arguments = getArguments();
        if(arguments!=null){
            isNewUser = arguments.getBoolean("isNewUser");
            username = arguments.getString("username");
            code = arguments.getString("code");
        }

        initUI();

        return view;
    }

    private void initUI() {
        apiRequest = ((Globals)getActivity().getApplicationContext()).getApiRequest();
        userAccount = new UserAccount(apiRequest);
        pref = new Preference(((Globals)getActivity().getApplicationContext()).getSharedPref());
        desingService = new DesingService(getContext());

        info =  new Hashtable();
        info.put("username", username);
        info.put("code", code);

        editTextPassword = (TextInputEditText) view.findViewById(R.id.editTextPassword);
        editTextPassword.addTextChangedListener(this);

        editTextApPat = (TextInputEditText) view.findViewById(R.id.editTextApPat);
        editTextApPat.addTextChangedListener(this);

        editTextApMat = (TextInputEditText) view.findViewById(R.id.editTextApMat);
        editTextApMat.addTextChangedListener(this);

        editTextNombres = (TextInputEditText) view.findViewById(R.id.editTextNombres);
        editTextNombres.addTextChangedListener(this);

        buttonRegister = (Button) view.findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);
        desingService.ButtonDefaultDisable(buttonRegister);

        if(!isNewUser){
            etPasswordLayout = (TextInputLayout) view.findViewById(R.id.etPasswordLayout);
            etPasswordLayout.setHint("New Password");
            editTextPassword.setHint("New Password");

            etApPatLayout = (TextInputLayout) view.findViewById(R.id.etApPatLayout);
            etApPatLayout.setVisibility(View.GONE);

            etApMatLayout = (TextInputLayout) view.findViewById(R.id.etApMatLayout);
            etApMatLayout.setVisibility(View.GONE);

            etNombresLayout = (TextInputLayout) view.findViewById(R.id.etNombresLayout);
            etNombresLayout.setVisibility(View.GONE);

            buttonRegister.setText("CONFIRM");
        }
    }

    @Override
    public void onClick(View view) {

        desingService.ButtonDefaultDisable(buttonRegister);
        if(isNewUser){
            userAccount.create(response -> {
                goToLogin();
            },info);
        }else{
            userAccount.changeNewPassword(response -> {
                goToLogin();
            },info);
        }
    }

    public void goToLogin(){
        pref.setDataPref("username", username);

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(formValid())
            desingService.ButtonDefaultEnable(buttonRegister);
        else
            desingService.ButtonDefaultDisable(buttonRegister);
    }

    @Override
    public void afterTextChanged(Editable editable) { }

    public boolean formValid(){
        if(isNewUser){

            info.put("apPat", editTextApPat.getText().toString());
            if( ((String) info.get("apPat")).length()<MIN_LENGTH )
                return false;

            info.put("apMat", editTextApMat.getText().toString());
            if( ((String) info.get("apMat")).length()<MIN_LENGTH )
                return false;

            info.put("nombres", editTextNombres.getText().toString());
            if( ((String) info.get("nombres")).length()<MIN_LENGTH )
                return false;
        }

        info.put("password", editTextPassword.getText().toString());
        if( ((String) info.get("password")).length()<MIN_LENGTH+2 )
            return false;

        return true;
    }
}
