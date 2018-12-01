package pe.edu.uni.www.vitalsign.Service.ApiBackend.User;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import pe.edu.uni.www.vitalsign.Model.Contact;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.ApiRequest;

public class UserContact {

    public interface contactsResponse{
        void onResponse(List<Contact> response);
    }

    public interface contactResponse{
        void onResponse(Contact response);
    }

    public interface booleanResponse{
        void onResponse(boolean response);
    }


    private ApiRequest apiRequest;

    public  UserContact(ApiRequest apiRequest){
        this.apiRequest = apiRequest;
    }

    public void list(final UserContact.contactsResponse listener) {

        apiRequest.sendGet(new ApiRequest.requestResponse() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String contactStr = (response.getJSONArray("entidad")).toString();
                    Type listType = new TypeToken<List<Contact>>() {}.getType();
                    List<Contact> contacts = new Gson().fromJson(contactStr, listType);

                    listener.onResponse(contacts);
                } catch (JSONException e) {
                    //listener.onResponse(false);
                }
            }
        }, "/user/contact/l");
    }
}
