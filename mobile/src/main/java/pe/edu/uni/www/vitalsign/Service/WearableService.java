package pe.edu.uni.www.vitalsign.Service;

import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class WearableService extends WearableListenerService {

    private static String PULSE_CHANGE = "pulse_change";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if( messageEvent.getPath().equals("/smartwatch") ){

            final String message = new String( messageEvent.getData() );

            Intent messageIntent = new Intent();
            messageIntent.setAction(PULSE_CHANGE);
            messageIntent.putExtra("pulse", Integer.parseInt(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }
}
