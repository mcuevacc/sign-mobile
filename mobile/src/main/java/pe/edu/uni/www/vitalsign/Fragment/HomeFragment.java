package pe.edu.uni.www.vitalsign.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import pe.edu.uni.www.vitalsign.App.Globals;
import pe.edu.uni.www.vitalsign.R;
import pe.edu.uni.www.vitalsign.Service.Util.Preference;

public class HomeFragment extends Fragment {

    private Preference pref;

    private View view;
    private Button button;
    private TextView heartText;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        initElement();

        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(messageReceiver, newFilter);
        return view;
    }

    private void initElement() {
        pref = new Preference(((Globals)getActivity().getApplicationContext()).getSharedPref());
        button = (Button) view.findViewById(R.id.button);
        heartText = (TextView) view.findViewById(R.id.heartText);
    }

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            if (bundle != null && bundle.getString("message") != null) {
                String message = bundle.getString("message");
                heartText.setText(message);
            }
        }
    }
}



