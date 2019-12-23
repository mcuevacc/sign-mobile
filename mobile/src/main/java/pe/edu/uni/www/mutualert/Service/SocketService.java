package pe.edu.uni.www.mutualert.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.Nullable;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URISyntaxException;

import pe.edu.uni.www.mutualert.App.Globals;
import pe.edu.uni.www.mutualert.Model.User;
import pe.edu.uni.www.mutualert.Service.Util.Preference;

public class SocketService extends Service {

    private Socket mSocket;
    private Preference pref;

    private static String ALERT_INIT = "alert_init";
    private static String ALERT_UPDATE = "alert_update";
    private static String ALERT_END = "alert_end";

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
            mSocket = IO.socket("http://192.168.1.4:3000",opts);
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
                        if(type.equals(ALERT_INIT)){
                            sendAlertInit(data);
                        }else if(type.equals(ALERT_UPDATE)){
                            sendAlertInit(data);
                        }else if(type.equals(ALERT_END)){
                            sendAlertEnd(data);
                        }
                    } catch (JSONException e) {
                        return;
                    }
                    //Toast.makeText(getApplicationContext(),"mensaje recibido",Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    public void sendAlertInit(JSONObject data){
        try {
            String userStr = (data.getJSONObject("user")).toString();
            Type listType = new TypeToken<User>() {}.getType();
            User user = new Gson().fromJson(userStr, listType);

            Intent intent = new Intent("socket_alert_init");
            intent.putExtra("pe.edu.uni.www.mutualert.Model.User",user);
            sendBroadcast(intent);
        } catch (JSONException e) {
            return;
        }
    }

    public void sendAlertEnd(JSONObject data){
        try {
            Long id = data.getLong("id");
            Intent intent = new Intent("socket_alert_end");
            intent.putExtra("id",id);
            sendBroadcast(intent);
        } catch (JSONException e) {
            return;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("message", onMessage);
    }
}
