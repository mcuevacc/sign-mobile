package pe.edu.uni.www.mutualert.Service.ApiBackend.MyAccount;

import org.json.JSONException;
import org.json.JSONObject;

import pe.edu.uni.www.mutualert.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.mutualert.Service.Util.InterfaceService;
import pe.edu.uni.www.mutualert.Service.Util.Preference;

public class MyAccountInfo  {

    private static String ROUTE = "/myaccount/info/";

    private ApiRequest apiRequest;
    private Preference pref;

    public MyAccountInfo(ApiRequest apiRequest){
        this.apiRequest = apiRequest;
    }

    public MyAccountInfo(ApiRequest apiRequest, Preference pref){
        this.apiRequest = apiRequest;
        this.pref = pref;
    }

    public void toking(final InterfaceService.booleanResponse listener) {
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
        },"GET", ROUTE+"toking");
    }

    public void setLocation(final InterfaceService.booleanResponse listener, double latitude, double longitude) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("latitude", latitude);
            jsonBody.put("longitude", longitude);
            jsonBody.put("accuracy", "");
        } catch (JSONException e) {}

        apiRequest.send(response -> {
            listener.onResponse(true);

        },"PATCH", ROUTE+"setlocation", jsonBody);
    }

    public void sendAlert(final InterfaceService.booleanResponse listener) {
        apiRequest.send(response -> {
            listener.onResponse(true);

        },"POST", ROUTE+"sendalert");
    }
}
