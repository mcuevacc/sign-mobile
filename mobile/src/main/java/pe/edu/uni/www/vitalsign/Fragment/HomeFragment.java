package pe.edu.uni.www.vitalsign.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import pe.edu.uni.www.vitalsign.Activity.MainActivity;
import pe.edu.uni.www.vitalsign.App.Globals;
import pe.edu.uni.www.vitalsign.R;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.MyAccount.MyAccountInfo;

public class HomeFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener, MainActivity.PulseListener {

    private View rootView;

    private ApiRequest apiRequest;
    private MyAccountInfo myAccountInfo;

    private Button buttonAlert;
    private TextView heartText;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initUI();

        return rootView;
    }

    private void initUI() {
        MainActivity activity = (MainActivity) getActivity();
        activity.setPulseListener(this);

        apiRequest = ((Globals)getActivity().getApplicationContext()).getApiRequest();
        myAccountInfo = new MyAccountInfo(apiRequest);

        buttonAlert = (Button) rootView.findViewById(R.id.buttonAlert);
        buttonAlert.setOnClickListener(this);
        buttonAlert.setOnLongClickListener(this);

        heartText = (TextView) rootView.findViewById(R.id.heartText);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(),"Debe mantener presionado el boton",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {
        myAccountInfo.sendAlert(resp -> {
        });

        new AlertDialog.Builder(getContext())
                .setTitle("Alert")
                .setMessage("Your alert was sent to all your contacts.")
                .setNeutralButton("OK", null)
                .show();

        return false;
    }

    @Override
    public void changePulse(int pulse) {
        heartText.setText(""+pulse);
    }
}



