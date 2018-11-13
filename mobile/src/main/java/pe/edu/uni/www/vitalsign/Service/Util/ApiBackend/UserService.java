package pe.edu.uni.www.vitalsign.Service.Util.ApiBackend;

import android.content.SharedPreferences;

import pe.edu.uni.www.vitalsign.Service.Util.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.Util.RequestResponse;
import pe.edu.uni.www.vitalsign.Service.Util.ServiceResponse;
import pe.edu.uni.www.vitalsign.Service.Util.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class UserService {

    private ApiRequest apiRequest;

    private JSONObject jsonBody;

    public UserService(ApiRequest apiRequest){
        this.apiRequest = apiRequest;
    }

    public void login(final ServiceResponse serviceResponse, final SharedPreferences prefs, String username, String password) {

        try {
            jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            serviceResponse.onBoolanResponse(false);
        }

        apiRequest.sendPost(new RequestResponse() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Util.setDataPrefs(prefs,"authToken", response.getString("authToken"));

                    JSONObject profile = response.getJSONObject("profile");
                    Util.setDataPrefs(prefs,"email", profile.getString("email"));

                    serviceResponse.onBoolanResponse(true);

                } catch (JSONException e) {
                    serviceResponse.onBoolanResponse(false);
                }

            }
        }, "/user/login", jsonBody);
    }
}
