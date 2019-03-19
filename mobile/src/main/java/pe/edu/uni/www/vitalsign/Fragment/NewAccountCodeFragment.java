package pe.edu.uni.www.vitalsign.Fragment;


import android.os.Bundle;
import android.os.Handler;
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

public class NewAccountCodeFragment extends Fragment implements TextWatcher {

    private View view;

    private String username;

    private ApiRequest apiRequest;
    private UserAccount userAccount;

    private ImageView imgViewCode;

    private EditText editTextCode;

    private Button buttonContinue;
    private Button buttonResend;

    private int time = 30;
    private int time_left;
    private Handler contador;
    private Runnable runnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_account_code, container, false);

        Bundle arguments = getArguments();
        username = arguments.getString("username");

        initUI();

        return view;
    }

    public void initUI() {

        apiRequest = ((Globals)getActivity().getApplicationContext()).getApiRequest();
        userAccount = new UserAccount(apiRequest);

        imgViewCode = (ImageView) view.findViewById(R.id.imageViewCode);
        int id = Util.getDrawableInt(getContext(), "verification2");
        Util.setImageToImageView(getContext(), imgViewCode, id);

        editTextCode = (EditText) view.findViewById(R.id.editTextCode);
        editTextCode.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "999999")});
        editTextCode.addTextChangedListener(this);

        buttonContinue = (Button) view.findViewById(R.id.buttonContinue);
        disableButtonContinue();
        buttonContinue.setOnClickListener(view -> {

            Bundle args = new Bundle();
            args.putString("username", username);
            args.putString("code", editTextCode.getText().toString());

            Fragment fragment = new NewAccountInfoFragment();
            fragment.setArguments(args);

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.newAccount_frame, fragment)
                    //.addToBackStack(null)
                    .commit();
        });

        buttonResend = (Button) view.findViewById(R.id.buttonResend);
        buttonResend.setOnClickListener(view -> {
            resend();
        });
    }

    public void resend(){

        disableButtonResend();

        time_left = time;

        runnable=new Runnable() {
            @Override
            public void run() {
                if(time_left==time){
                    userAccount.sendCode(response -> {
                        //Nada
                    },username);
                }

                if (time_left!=0) {
                    buttonResend.setText("RESEND CODE ... "+time_left);
                }else{
                    buttonResend.setText("RESEND CODE");
                }

                time_left--;

                if(time_left>=0){
                    contador.postDelayed(this, 1000);
                }else{
                    buttonResend.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_round_white_background));
                    buttonResend.setEnabled(true);
                }
            }
        };

        contador=new Handler();
        contador.postDelayed(runnable,0000);
    }

    public void disableButtonResend() {
        buttonResend.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_dialog_disable_background));
        buttonResend.setEnabled(false);
    }

    public void disableButtonContinue() {
        buttonContinue.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_dialog_disable_background));
        buttonContinue.setEnabled(false);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        disableButtonContinue();
        if(charSequence.length()==6 ){

            String code = charSequence.toString();

            userAccount.checkCode(response -> {
                if(response){
                    buttonContinue.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_dialog_primary_background));
                    buttonContinue.setEnabled(true);
                    buttonResend.setText("RESEND CODE");
                    if(runnable != null ) contador.removeCallbacks(runnable);
                    disableButtonResend();
                }
            },username,code);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) { }
}
