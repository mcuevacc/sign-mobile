package pe.edu.uni.www.vitalsign.Service.ApiBackend.User;

import org.json.JSONException;
import org.json.JSONObject;

import pe.edu.uni.www.vitalsign.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.Util.Preference;

public class UserAccount{

    public interface booleanResponse{
        void onResponse(boolean response);
    }

    private ApiRequest apiRequest;
    private JSONObject jsonBody;
    private Preference pref;

    public  UserAccount(ApiRequest apiRequest, Preference pref){
        this.apiRequest = apiRequest;
        this.pref = pref;
    }

    public void login(final booleanResponse listener, String username, String password) {

        try {
            jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) { }

        apiRequest.sendPost(new ApiRequest.requestResponse() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String authToken = response.getString("authToken");
                    pref.setDataPref("authToken", authToken);
                    apiRequest.setAuthToken(authToken);

                    JSONObject profile = response.getJSONObject("profile");
                    pref.setDataPref("email", profile.getString("email"));

                    listener.onResponse(true);

                } catch (JSONException e) {
                    listener.onResponse(false);
                }

            }
        }, "/user/account/login", jsonBody);
    }

    public void toking(final booleanResponse listener) {
        apiRequest.setAuthToken(pref.getDataPref("authToken"));
        apiRequest.sendGet(new ApiRequest.requestResponse() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String authToken = response.getString("authToken");
                    pref.setDataPref("authToken", authToken);
                    apiRequest.setAuthToken(authToken);

                    JSONObject profile = response.getJSONObject("profile");
                    pref.setDataPref("email", profile.getString("email"));

                    listener.onResponse(true);

                } catch (JSONException e) {
                    listener.onResponse(false);
                }
            }
        }, "/user/account/toking");
    }
}
