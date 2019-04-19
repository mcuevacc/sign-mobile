package pe.edu.uni.www.vitalsign.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class SmsReceiver extends BroadcastReceiver {

    public interface SmsReceiveListener {
        void onCodeReceived(String otp);
    }

    private SmsReceiveListener smsListener;

    public void setSmsListener(SmsReceiveListener smsListener) {
        this.smsListener = smsListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    int index = message.lastIndexOf(":")+2;
                    String code = message.substring(index,index+6);

                    if (smsListener != null) {
                        smsListener.onCodeReceived(code);
                    }
                    break;

                case CommonStatusCodes.TIMEOUT:
                    //Toast.makeText(context, "OTP Time out", Toast.LENGTH_SHORT).show();
                    break;

                case CommonStatusCodes.API_NOT_CONNECTED:
                    //Toast.makeText(context, "API NOT CONNECTED", Toast.LENGTH_SHORT).show();
                    break;

                case CommonStatusCodes.NETWORK_ERROR:
                    //Toast.makeText(context, "NETWORK ERROR", Toast.LENGTH_SHORT).show();
                    break;

                case CommonStatusCodes.ERROR:
                    //Toast.makeText(context, "SOME THING WENT WRONG", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}