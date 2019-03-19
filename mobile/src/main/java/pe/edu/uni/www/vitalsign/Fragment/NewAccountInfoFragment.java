package pe.edu.uni.www.vitalsign.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import pe.edu.uni.www.vitalsign.App.Globals;
import pe.edu.uni.www.vitalsign.R;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.User.UserAccount;

public class NewAccountInfoFragment extends Fragment implements View.OnClickListener {

    private View view;

    private String username;
    private String code;

    private ApiRequest apiRequest;
    private UserAccount userAccount;

    private EditText editTextApPat;
    private EditText editTextApMat;
    private EditText editTextNombres;

    private Button buttonRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_account_info, container, false);

        Bundle arguments = getArguments();
        username = arguments.getString("username");
        code = arguments.getString("code");

        initUI();

        return view;
    }

    private void initUI() {
        apiRequest = ((Globals)getActivity().getApplicationContext()).getApiRequest();
        userAccount = new UserAccount(apiRequest);

        editTextApPat = (EditText) view.findViewById(R.id.editTextApPat);
        editTextApMat = (EditText) view.findViewById(R.id.editTextApMat);
        editTextNombres = (EditText) view.findViewById(R.id.editTextNombres);

        buttonRegister = (Button) view.findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
