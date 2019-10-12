package pe.edu.uni.www.vitalsign.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import pe.edu.uni.www.vitalsign.App.Globals;
import pe.edu.uni.www.vitalsign.R;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.User.UserAccount;
import pe.edu.uni.www.vitalsign.Service.Util.DesingService;
import pe.edu.uni.www.vitalsign.Service.Util.InputFilterMinMax;
import pe.edu.uni.www.vitalsign.Service.Util.Util;

public class NewAccountPhoneFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private View view;

    private boolean isNewUser = true;

    private ApiRequest apiRequest;
    private UserAccount userAccount;

    private ImageView imgViewPhone;

    private EditText editTextCode;
    private EditText editTextNumber;
    private Button buttonContinue;

    private DesingService desingService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_account_phone, container, false);

        Bundle arguments = getArguments();
        if(arguments!=null)
            isNewUser = arguments.getBoolean("isNewUser");

        initUI();
        return view;
    }

    private void initUI() {

        apiRequest = ((Globals)getActivity().getApplicationContext()).getApiRequest();
        userAccount = new UserAccount(apiRequest);
        
        desingService = new DesingService(getContext());

        imgViewPhone = (ImageView) view.findViewById(R.id.imageViewPhone);
        int id = Util.getDrawableInt(getContext(), "verification1");
        Util.setImageToImageView(getContext(), imgViewPhone, id);

        editTextCode = (EditText) view.findViewById(R.id.editTextCode);
        editTextCode.setEnabled(false);

        editTextNumber = (EditText) view.findViewById(R.id.editTextNumber);
        editTextNumber.setFilters(new InputFilter[]{ new InputFilterMinMax("9", "999999999")});
        editTextNumber.addTextChangedListener(this);

        buttonContinue = (Button) view.findViewById(R.id.buttonContinue);
        desingService.ButtonDefaultDisable(buttonContinue);
        buttonContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        desingService.ButtonDefaultDisable(buttonContinue);
        String username = editTextNumber.getText().toString();
        userAccount.exist(response -> {
            if( (isNewUser&&!response) || (!isNewUser&&response) ){
                Bundle args = new Bundle();
                args.putString("username", username);
                args.putBoolean("isNewUser", isNewUser);

                Fragment fragment = new NewAccountCodeFragment();
                fragment.setArguments(args);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.newAccount_frame, fragment)
                        //.addToBackStack(null)
                        .commit();
            }else{
                if(isNewUser)
                    Toast.makeText(getContext(), "Usuario ya está registrado", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Usuario no está registrado", Toast.LENGTH_SHORT).show();
            }
        },username);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.length()==9 )
            desingService.ButtonDefaultEnable(buttonContinue);
        else
            desingService.ButtonDefaultDisable(buttonContinue);
    }

    @Override
    public void afterTextChanged(Editable editable) { }
}
