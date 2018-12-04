package pe.edu.uni.www.vitalsign.Service.ApiBackend;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ApiRequest{

    public interface requestResponse {
        void onSuccess(JSONObject result);
    }

    //private static String url="http://18.223.100.180/api-sign/web/app.php";
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

    public void initQueue(){
        this.mQueue = Volley.newRequestQueue(this.context);
    }

    public void addQueue(JsonObjectRequest jsonObjectRequest){
        this.mQueue.add(jsonObjectRequest);
    }

    public void send(final requestResponse listener, String method, String route){
        send(listener,method,route,null);
    }

    public void send(final requestResponse listener, String method, String route, JSONObject jsonBody) {

        int requestMethod;
        if(method.equals("GET")){
            requestMethod = Request.Method.GET;
        }else if(method.equals("POST")){
            requestMethod = Request.Method.POST;
        }else if(method.equals("PUT")){
            requestMethod = Request.Method.PUT;
        }else if(method.equals("DELETE")){
            requestMethod = Request.Method.DELETE;
        }else{
            requestMethod = Request.Method.OPTIONS;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestMethod,url + route, jsonBody,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        listener.onSuccess(response);
                    }else{
                        Toast.makeText(context, response.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    NetworkResponse response = error.networkResponse;
                    /*
                    String statusCode = String.valueOf(response.statusCode);
                    Log.d("TAG", "Status Code: " + statusCode);
                    */
                    String jsonResponse = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                    JSONObject resp = new JSONObject(jsonResponse);
                    Toast.makeText(context, resp.getString("msg"), Toast.LENGTH_SHORT).show();

                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
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