package pe.edu.uni.www.mutualert.Service.Util;

import org.json.JSONObject;

import java.util.List;

import pe.edu.uni.www.mutualert.Model.Contact;

public class InterfaceService {
    /**************  GLOBALS  *********************/
    public interface booleanResponse{
        void onResponse(boolean response);
    }

    public interface requestResponse {
        void onSuccess(JSONObject result);
    }

    /**************  CONTACTS  *********************/
    public interface contactsResponse{
        void onResponse(List<Contact> response);
    }

    public interface contactResponse{
        void onResponse(Contact response);
    }
}
