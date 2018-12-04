package pe.edu.uni.www.vitalsign.Service.ApiBackend.User;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collections;
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

        apiRequest.send(response -> {
            try {
                String contactStr = (response.getJSONArray("entidad")).toString();
                Type listType = new TypeToken<List<Contact>>() {}.getType();
                List<Contact> contacts = new Gson().fromJson(contactStr, listType);
                Collections.reverse(contacts);

                listener.onResponse(contacts);
            } catch (JSONException e) {
                //listener.onResponse(false);
            }
        },"GET", "/user/contact/l");
    }

    public void add(final UserContact.booleanResponse listener, Contact contact) {

        JSONObject  jsonBody = new JSONObject();
        try {
            jsonBody.put("name", contact.getName());
            jsonBody.put("number", contact.getNumber());
        } catch (JSONException e) { }

        apiRequest.send(response -> {
            try {
                int id = response.getInt("id");
                contact.setId(id);

                //listener.onResponse(id);
                listener.onResponse(true);

            } catch (JSONException e) {
                //listener.onResponse(0);
                listener.onResponse(false);
            }
        },"POST", "/user/contact/c", jsonBody);
    }

    public void update(final UserContact.booleanResponse listener, Contact contact) {

        JSONObject  jsonBody = new JSONObject();
        try {
            jsonBody.put("name", contact.getName());
            jsonBody.put("number", contact.getNumber());
        } catch (JSONException e) { }

        apiRequest.send(response -> {
                listener.onResponse(true);

        },"PUT", "/user/contact/u/"+contact.getId(), jsonBody);
    }

    public void delete(final UserContact.booleanResponse listener, int id) {

        apiRequest.send(response -> {
                listener.onResponse(true);

        },"DELETE", "/user/contact/d/"+id);
    }
}
