package pe.edu.uni.www.vitalsign.Fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pe.edu.uni.www.vitalsign.Adapter.ContactAdapter;
import pe.edu.uni.www.vitalsign.App.Globals;
import pe.edu.uni.www.vitalsign.Model.Contact;
import pe.edu.uni.www.vitalsign.R;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.User.UserContact;

public class ContactFragment extends Fragment {

    private View view;

    private ApiRequest apiRequest;
    private UserContact userContact;

    private List<Contact> contacts;
    private ContactAdapter adapter;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton fab;

    public ContactFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_contact, container, false);

        initUI();

        adapter = new ContactAdapter(contacts, R.layout.recycler_view_contact_item, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        initActions();

        return view;
    }

    private void initUI() {
        apiRequest = ((Globals)getActivity().getApplicationContext()).getApiRequest();
        userContact = new UserContact(apiRequest);

        fab = view.findViewById(R.id.fab);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewContact);
        layoutManager = new LinearLayoutManager(getContext());

        contacts = new ArrayList<Contact>();
        getAllContacts();
    }

    public void initActions(){

        fab.setOnClickListener(view -> {
            Contact newContact = new Contact("name", "number",25);

            userContact.add(new UserContact.booleanResponse() {
                @Override
                public void onResponse(boolean resp) {
                    if(resp){
                        contacts.add(0,newContact);
                        adapter.notifyItemInserted(0);
                        layoutManager.scrollToPosition(0);
                    }
                }
            },newContact);
        });

        adapter.setOnItemClickListener((view, contact, position) -> {
            Toast.makeText(getContext(), "Id:"+contact.getId()+"Nombre:"+contact.getName()+" Number:"+contact.getNumber(), Toast.LENGTH_SHORT).show();
            //adapter.notifyItemChanged(position);
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });
    }

    private void getAllContacts() {
        userContact.list(new UserContact.contactsResponse() {
            @Override
            public void onResponse(List<Contact> list) {
                contacts=list;
                adapter.updateList(contacts);
            }
        });
    }
}