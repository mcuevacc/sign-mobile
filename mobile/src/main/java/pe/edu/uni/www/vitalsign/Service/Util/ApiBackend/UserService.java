package pe.edu.uni.www.vitalsign.Service.Util.ApiBackend;

import android.util.Log;

import pe.edu.uni.www.vitalsign.Service.Util.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.Util.RequestResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class UserService {

    private ApiRequest apiRequest;
    private JSONObject jsonBody;

    public UserService(ApiRequest apiRequest){
        this.apiRequest = apiRequest;
    }

    public boolean login(String username, String password){

            try {
                jsonBody = new JSONObject();
                jsonBody.put("username", username);
                jsonBody.put("password", password);

                apiRequest.sendPost(new RequestResponse() {
                        @Override
                        public void onSuccess(JSONObject response){
                            //Log.d("TAG", response.toString());
                            try {
                                String token = response.getString("authToken");
                                JSONObject profile = response.getJSONObject("profile");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, "/user/login", jsonBody);

                return true;

        } catch (JSONException e) {
            Log.d("TAG", e.toString());
            return false;
        }
        try{
                int a =0;
        } catch ()

    }
}
