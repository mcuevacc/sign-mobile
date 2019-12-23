package pe.edu.uni.www.mutualert.Service.ApiBackend.MyAccount;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import pe.edu.uni.www.mutualert.Model.Contact;
import pe.edu.uni.www.mutualert.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.mutualert.Service.Util.InterfaceService;

public class MyAccountContact {

    private static String ROUTE = "/myaccount/contact/";

    private ApiRequest apiRequest;

    public MyAccountContact(ApiRequest apiRequest){
        this.apiRequest = apiRequest;
    }

    public void list(final InterfaceService.contactsResponse listener) {

        apiRequest.send(response -> {
            try {
                String contactsStr = (response.getJSONArray("entidad")).toString();
                Type listType = new TypeToken<List<Contact>>() {}.getType();
                List<Contact> contacts = new Gson().fromJson(contactsStr, listType);
                Collections.reverse(contacts);

                listener.onResponse(contacts);
            } catch (JSONException e) {
                //listener.onResponse(false);
            }
        },"GET", ROUTE+"l");
    }

    public void add(final InterfaceService.booleanResponse listener, Contact contact) {

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
        },"POST", ROUTE+"c", jsonBody);
    }

    public void update(final InterfaceService.booleanResponse listener, Contact contact) {

        JSONObject  jsonBody = new JSONObject();
        try {
            jsonBody.put("name", contact.getName());
            jsonBody.put("number", contact.getNumber());
        } catch (JSONException e) { }

        apiRequest.send(response -> {
                listener.onResponse(true);

        },"PUT", ROUTE+"u/"+contact.getId(), jsonBody);
    }

    public void delete(final InterfaceService.booleanResponse listener, int id) {

        apiRequest.send(response -> {
                listener.onResponse(true);

        },"DELETE", ROUTE+"d/"+id);
    }
}
