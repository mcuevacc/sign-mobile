package pe.edu.uni.www.vitalsign.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private ContactAdapter adapter;
    private List<Contact> contacts;

    private ApiRequest apiRequest;
    private UserContact userContact;

    public ContactFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_contact, container, false);

        initElement();

        adapter = new ContactAdapter(contacts, R.layout.recycler_view_contact_item, getActivity(), new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Contact contact, int position) {
                Toast.makeText(getContext(), "Nombre:"+contact.getName()+" Number:"+contact.getNumber(), Toast.LENGTH_SHORT).show();
                adapter.notifyItemChanged(position);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void initElement() {
        apiRequest = ((Globals)getActivity().getApplicationContext()).getApiRequest();
        userContact = new UserContact(apiRequest);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewContact);
        layoutManager = new LinearLayoutManager(getContext());

        contacts = new ArrayList<Contact>();
        getAllContacts();
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_fruit:
                // Rescatamos el número de frutas para saber en que posición insertaremos
                int position = contacts.size();
                contacts.add(position, new Contact("name", "number"));
                adapter.notifyItemInserted(position);
                layoutManager.scrollToPosition(position);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
