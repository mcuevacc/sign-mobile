package pe.edu.uni.www.mutualert.Service.ApiBackend;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import pe.edu.uni.www.mutualert.Service.Util.InterfaceService;

public class ApiRequest{

    private static final String TAG = ApiRequest.class.getSimpleName();

    //private static String url="http://3.14.141.227/api-sign/web/app.php";
    private static String url="http://192.168.1.3/api-sign/web/app_dev.php";
    private Context context;
    private RequestQueue mQueue;

    private String authToken=null;

    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }

    public ApiRequest(Context context){
        this.context = context;
        initQueue();
    }

    public ApiRequest(Context context, String authToken){
        this.context = context;
        this.authToken = authToken;
        initQueue();
    }

    public void initQueue(){
        this.mQueue = Volley.newRequestQueue(this.context);
    }

    public void addQueue(JsonObjectRequest jsonObjectRequest){
        this.mQueue.add(jsonObjectRequest);
    }

    public void send(final InterfaceService.requestResponse listener, String method, String route){
        send(listener,method,route,null,false);
    }

    public void send(final InterfaceService.requestResponse listener, String method, String route, JSONObject jsonBody){
        send(listener,method,route,jsonBody,false);
    }

    public void send(final InterfaceService.requestResponse listener, String method, String route, JSONObject jsonBody, Boolean showMsg) {

        int requestMethod;
        if(method.equals("GET")){
            requestMethod = Request.Method.GET;
        }else if(method.equals("POST")){
            requestMethod = Request.Method.POST;
        }else if(method.equals("PUT")) {
            requestMethod = Request.Method.PUT;
        }else if(method.equals("PATCH")){
            requestMethod = Request.Method.PATCH;
        }else if(method.equals("DELETE")){
            requestMethod = Request.Method.DELETE;
        }else{
            requestMethod = Request.Method.OPTIONS;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestMethod,url + route, jsonBody,
                response -> {
                    try {
                        if(response.getBoolean("success"))
                            listener.onSuccess(response);
                        else {
                            if( showMsg )
                                Toast.makeText(context, response.getString("msg"), Toast.LENGTH_SHORT).show();
                            else
                                Log.e(TAG, response.getString("msg"));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }, error -> {
                    try {
                        NetworkResponse response = error.networkResponse;

                        String statusCode = String.valueOf(response.statusCode);

                        //Log.e(TAG, "Status Code: " + statusCode);

                        String jsonResponse = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                        JSONObject resp = new JSONObject(jsonResponse);
                        if(statusCode.contentEquals("401"))
                            listener.onSuccess(resp);
                        else{
                            if( showMsg )
                                Toast.makeText(context, resp.getString("msg"), Toast.LENGTH_SHORT).show();
                            else
                                Log.e(TAG, resp.getString("msg"));
                        }
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, e.getMessage());
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                if (authToken != null && !authToken.isEmpty()) {
                    headers.put("authToken", authToken);
                }
                return headers;
            }
        };
        addQueue(jsonObjectRequest);
    }
}