package pe.edu.uni.www.vitalsign.Service.Util;

import android.content.Context;
import android.util.Log;

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

import pe.edu.uni.www.vitalsign.Service.Util.RequestCallBack;

public class ApiRequest{

    private String url="http://192.168.1.2/api-sign/web/app_dev.php";
    private Context context;
    private RequestQueue mQueue;


    public ApiRequest(Context context){
        this.context = context;
        initQueue();
    }

    public ApiRequest(Context context, String url) {
        this.context = context;
        this.url = url;
        initQueue();
    }

    public void initQueue(){
        this.mQueue = Volley.newRequestQueue(this.context);
    }

    public void addQueue(JsonObjectRequest jsonObjectRequest){
        this.mQueue.add(jsonObjectRequest);
    }

    public void sendGet(final RequestCallBack callback, String route) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url+route, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            NetworkResponse response = error.networkResponse;

                            String statusCode = String.valueOf(response.statusCode);
                            Log.d("TAG", "Status Code: "+statusCode);

                            String jsonResponse = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            Log.d("TAG", "Json: "+jsonResponse);

                        } catch (UnsupportedEncodingException e) {
                            //hacer algo
                        }
                    }
                }){
                    /*
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("apiKey", "xxxxxxxxxxxxxxx");
                        return headers;
                    }
                    */
                };

                /*
                public void onErrorResponse(VolleyError error){
                    NetworkResponse response = error.networkResponse;
                }
                if (error instanceof ServerError && response != null) {
                try {
                    String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    JSONObject obj = new JSONObject(res);
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                } catch (JSONException e2) {
                    e2.printStackTrace();
                } }
                */




        addQueue(jsonObjectRequest);
    }

    /*

    public void sendRequest(String route, JSONObject jsonBody){


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonBody,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        //Log.d("Response", "Si funca papi");
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                //Log.e("Response", "No funciona perro");
            }
        });


    }
    */

    public interface VolleyCallback{
        void onSuccess(String result);
    }

}


