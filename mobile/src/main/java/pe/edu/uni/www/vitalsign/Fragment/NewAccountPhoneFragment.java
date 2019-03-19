package pe.edu.uni.www.vitalsign.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import pe.edu.uni.www.vitalsign.App.Globals;
import pe.edu.uni.www.vitalsign.R;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.User.UserAccount;
import pe.edu.uni.www.vitalsign.Service.Util.InputFilterMinMax;
import pe.edu.uni.www.vitalsign.Service.Util.Util;

public class NewAccountPhoneFragment extends Fragment implements View.OnClickListener, TextWatcher {

    private View view;

    private ApiRequest apiRequest;
    private UserAccount userAccount;

    private ImageView imgViewPhone;

    private EditText editTextCode;
    private EditText editTextNumber;
    private Button buttonContinue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_account_phone, container, false);

        initUI();
        return view;
    }

    private void initUI() {

        apiRequest = ((Globals)getActivity().getApplicationContext()).getApiRequest();
        userAccount = new UserAccount(apiRequest);

        imgViewPhone = (ImageView) view.findViewById(R.id.imageViewPhone);
        int id = Util.getDrawableInt(getContext(), "verification1");
        Util.setImageToImageView(getContext(), imgViewPhone, id);

        editTextCode = (EditText) view.findViewById(R.id.editTextCode);
        editTextCode.setEnabled(false);

        editTextNumber = (EditText) view.findViewById(R.id.editTextNumber);
        editTextNumber.setFilters(new InputFilter[]{ new InputFilterMinMax("9", "999999999")});
        editTextNumber.addTextChangedListener(this);

        buttonContinue = (Button) view.findViewById(R.id.buttonContinue);
        buttonContinue.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_dialog_disable_background));
        buttonContinue.setEnabled(false);
        buttonContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String username = editTextNumber.getText().toString();

        userAccount.sendCode(response -> {

            if(response){
                Bundle args = new Bundle();
                args.putString("username", username);

                Fragment fragment = new NewAccountCodeFragment();
                fragment.setArguments(args);

                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.newAccount_frame, fragment)
                        //.addToBackStack(null)
                        .commit();
            }

        },username);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(charSequence.length()==9 ){
            buttonContinue.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_dialog_primary_background));
            buttonContinue.setEnabled(true);

        }else{
            buttonContinue.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_dialog_disable_background));
            buttonContinue.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) { }
}
