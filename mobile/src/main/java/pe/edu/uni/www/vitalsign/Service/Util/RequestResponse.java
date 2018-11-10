package pe.edu.uni.www.vitalsign.Service.Util;

import org.json.JSONException;
import org.json.JSONObject;

public interface RequestResponse {
    void onSuccess(JSONObject result) throws JSONException;
}
