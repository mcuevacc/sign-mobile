package pe.edu.uni.www.vitalsign.Fragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;

import pe.edu.uni.www.vitalsign.App.Globals;
import pe.edu.uni.www.vitalsign.R;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.User.UserAccount;
import pe.edu.uni.www.vitalsign.Service.Util.DesingService;
import pe.edu.uni.www.vitalsign.Service.SmsReceiver;
import pe.edu.uni.www.vitalsign.Service.Util.InputFilterMinMax;
import pe.edu.uni.www.vitalsign.Service.Util.Util;

public class NewAccountCodeFragment extends Fragment implements TextWatcher, SmsReceiver.SmsReceiveListener {

    private View view;

    private boolean isNewUser = true;
    private String username;

    private ApiRequest apiRequest;
    private UserAccount userAccount;

    private ImageView imgViewCode;

    private EditText editTextCode;

    private Button buttonContinue;
    private Button buttonResend;

    private int time = 59;
    private int time_left;
    private Handler contador;
    private Runnable runnable;

    private SmsReceiver smsReceiver;

    private DesingService desingService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_account_code, container, false);

        Bundle arguments = getArguments();
        if(arguments!=null){
            isNewUser = arguments.getBoolean("isNewUser");
            username = arguments.getString("username");
        }

        initUI();

        return view;
    }

    public void initUI() {

        apiRequest = ((Globals)getActivity().getApplicationContext()).getApiRequest();
        userAccount = new UserAccount(apiRequest);

        desingService = new DesingService(getContext());

        imgViewCode = (ImageView) view.findViewById(R.id.imageViewCode);
        int id = Util.getDrawableInt(getContext(), "verification2");
        Util.setImageToImageView(getContext(), imgViewCode, id);

        editTextCode = (EditText) view.findViewById(R.id.editTextCode);
        editTextCode.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "999999")});
        editTextCode.addTextChangedListener(this);

        buttonContinue = (Button) view.findViewById(R.id.buttonContinue);
        desingService.ButtonDefaultDisable(buttonContinue);
        buttonContinue.setOnClickListener(view -> {

            Bundle args = new Bundle();
            args.putString("username", username);
            args.putString("code", editTextCode.getText().toString());

            Fragment fragment;
            if(isNewUser)
                fragment = new NewAccountInfoFragment();
            else
                fragment = new NewAccountPasswordFragment();

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
        resend();

        startSMSListener();
    }

    private void startSMSListener() {
        try {
            smsReceiver = new SmsReceiver();
            smsReceiver.setSmsListener(this);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            getActivity().registerReceiver(smsReceiver, intentFilter);

            SmsRetrieverClient client = SmsRetriever.getClient(getActivity());

            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(aVoid -> {
                // API successfully started
            });
            task.addOnFailureListener(e -> {
                // Fail to start API
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCodeReceived(String code) {

        editTextCode.setText(code);
        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(smsReceiver);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (smsReceiver != null)
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(smsReceiver);
    }

    // Funciones de los botones y validaciones
    public void resend(){

        desingService.ButtonDefaultDisable(buttonResend);

        time_left = time;

        runnable=new Runnable() {
            @Override
            public void run() {
                if(time_left==time){

                    if(isNewUser){
                        userAccount.sendCode(response -> {
                            //Nada
                        },username);
                    }else{
                        userAccount.sendCodeUser(response -> {
                            //Nada
                        },username);
                    }
                }

                if (time_left!=0) {
                    buttonResend.setText("RESEND CODE ... "+time_left);
                }else{
                    buttonResend.setText("RESEND CODE");
                }

                time_left--;

                if(time_left>=0)
                    contador.postDelayed(this, 1000);
                else
                    desingService.ButtonSecondEnable(buttonResend);
            }
        };

        contador=new Handler();
        contador.postDelayed(runnable,0000);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        desingService.ButtonDefaultDisable(buttonContinue);
        if(charSequence.length()==6 ){

            String code = charSequence.toString();

            userAccount.checkCode(response -> {
                if(response){
                    desingService.ButtonDefaultEnable(buttonContinue);
                    buttonResend.setText("RESEND CODE");
                    if(runnable != null ) contador.removeCallbacks(runnable);
                    desingService.ButtonDefaultDisable(buttonResend);
                }
            },username,code);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) { }
}