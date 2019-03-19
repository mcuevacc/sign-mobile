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
    private Preference pref;

    public UserAccount(ApiRequest apiRequest){
        this.apiRequest = apiRequest;
        this.pref = pref;
    }

    public UserAccount(ApiRequest apiRequest, Preference pref){
        this.apiRequest = apiRequest;
        this.pref = pref;
    }

    public void login(final booleanResponse listener, String username, String password) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);
        } catch (JSONException e) {}

        apiRequest.send(response -> {
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

        },"POST", "/user/account/login", jsonBody);
    }

    public void toking(final booleanResponse listener) {
        apiRequest.setAuthToken(pref.getDataPref("authToken"));
        apiRequest.send(response -> {
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
        },"GET", "/user/account/toking");
    }

    public void sendCode(final booleanResponse listener, String username) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
        } catch (JSONException e) {}

        apiRequest.send(response -> {
                listener.onResponse(true);

        },"POST", "/user/account/sendcode", jsonBody);
    }

    public void checkCode(final booleanResponse listener, String username, String code) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("code", code);
        } catch (JSONException e) {}

        apiRequest.send(response -> {
            listener.onResponse(true);

        },"POST", "/user/account/checkcode", jsonBody);
    }

    public void create(final booleanResponse listener, String username, String code) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("code", code);
        } catch (JSONException e) {}

        apiRequest.send(response -> {
            listener.onResponse(true);

        },"POST", "/user/account/checkcode", jsonBody);
    }
}
