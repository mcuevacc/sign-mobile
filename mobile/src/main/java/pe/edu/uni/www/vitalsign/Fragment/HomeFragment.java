package pe.edu.uni.www.vitalsign.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import pe.edu.uni.www.vitalsign.Activity.MyLocation;
import pe.edu.uni.www.vitalsign.App.Globals;
import pe.edu.uni.www.vitalsign.R;
import pe.edu.uni.www.vitalsign.Service.Util.Preference;

public class HomeFragment extends Fragment {

    private Preference pref;

    int increment = 4;
    MyLocation myLocation = new MyLocation();

    private View view;
    private Button button;
    private TextView heartText;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        initElement();

        /*
        myLocation.getLocation(getContext(), locationResult);

        boolean r = myLocation.getLocation(getContext(),
                locationResult);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });
        */

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



    private void sendSms() {
        String contenido = "Help me!\nhttp://maps.google.com/?q="+(pref.getDataPref("latitude"))+","+(pref.getDataPref("longitude"));

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("952164666", null, contenido, null, null);
            Toast.makeText(getContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getContext(),e.toString(), Toast.LENGTH_LONG).show();
        }


        /*
        // Comprobar si ha aceptado, no ha aceptado, o nunca se le ha preguntado
        if (CheckPermission(Manifest.permission.SEND_SMS)) {
            // Ha aceptado

            Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
            // Set the data for the intent as the phone number.
            smsIntent.setData(Uri.parse("smsto: 952164666"));
            // Add the message (sms) with the key ("sms_body").
            smsIntent.putExtra("sms_body", "Hola caracola");

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                return;

            startActivity(smsIntent);

        } else {
            // Ha denegado o es la primera vez que se le pregunta
            if (!shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                // No se le ha preguntado aún
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_CODE);
            } else {
                // Ha denegado
                Toast.makeText(getContext(), "Please, enable the request permission", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setData(Uri.parse("package:" + getActivity().getPackageName()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(i);
            }
        }
        */
    }
    /*
    private boolean CheckPermission(String permission){
        int result = getActivity().checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    */

    public MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {

        @Override
        public void gotLocation(Location location) {

            double Longitude = location.getLongitude();
            double Latitude = location.getLatitude();

            Toast.makeText(getContext(), "Got Location",
                    Toast.LENGTH_LONG).show();

            try {
                pref.setDataPref("latitude", ""+Latitude);
                pref.setDataPref("longitude", ""+Longitude);

            } catch (Exception e) {
                Toast.makeText(getContext(),e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    };


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



