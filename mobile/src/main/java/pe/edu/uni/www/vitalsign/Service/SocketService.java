package pe.edu.uni.www.vitalsign.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.Collections;

import pe.edu.uni.www.vitalsign.App.Globals;
import pe.edu.uni.www.vitalsign.Model.User;
import pe.edu.uni.www.vitalsign.Service.Util.Preference;

public class SocketService extends Service {

    private Socket mSocket;
    private Preference pref;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        pref = new Preference(((Globals)getApplication()).getSharedPref());
        String authToken =  pref.getDataPref("authToken");

        try {
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.query = "auth_token="+authToken;
            mSocket = IO.socket("http://192.168.1.2:3000",opts);
            mSocket.on("message", onMessage);
            mSocket.connect();
        } catch (URISyntaxException e) { }
    }

    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String type;
                    try {
                        type = data.getString("type");
                        if(type.equals("alert")){
                            sendAlert(data);
                        }
                    } catch (JSONException e) {
                        return;
                    }
                    //Toast.makeText(getApplicationContext(),"mensaje recibido",Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    public void sendAlert(JSONObject data){
        try {
            String userStr = (data.getJSONObject("user")).toString();
            Type listType = new TypeToken<User>() {}.getType();
            User user = new Gson().fromJson(userStr, listType);
        } catch (JSONException e) {
            return;
        }

        /*
        Intent intent = new Intent("location_update");
        Bundle extras = new Bundle();
        extras.putString("latitude", String.valueOf(latitude));
        extras.putString("longitude", String.valueOf(longitude));
        intent.putExtras(extras);
        sendBroadcast(intent);

        setLocation(latitude, longitude);
        */
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("message", onMessage);
    }
}
