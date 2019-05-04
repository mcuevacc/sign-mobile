package pe.edu.uni.www.vitalsign.Fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pe.edu.uni.www.vitalsign.Adapter.ContactAdapter;
import pe.edu.uni.www.vitalsign.App.Globals;
import pe.edu.uni.www.vitalsign.Model.Contact;
import pe.edu.uni.www.vitalsign.R;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.MyAccount.MyAccountContact;

public class ContactFragment extends Fragment implements DialogInterface.OnClickListener{

    private View rootView;

    private ApiRequest apiRequest;
    private MyAccountContact myAccountContact;

    private List<Contact> contacts;
    private ContactAdapter adapter;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FloatingActionButton fab;
    private AlertDialog.Builder builder;

    private EditText editTextName;
    private EditText editTextNumber;

    /////////////////////////////////////
    private char crudContact;
    private int positionContact;

    public ContactFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_contact, container, false);

        initUI();

        adapter = new ContactAdapter(contacts, R.layout.recycler_view_contact_item, getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        initActions();

        return rootView;
    }

    private void initUI() {
        apiRequest = ((Globals)getActivity().getApplicationContext()).getApiRequest();
        myAccountContact = new MyAccountContact(apiRequest);

        fab = rootView.findViewById(R.id.fab);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewContact);
        layoutManager = new LinearLayoutManager(getContext());

        contacts = new ArrayList<Contact>();
        getAllContacts();
    }

    public void initActions(){

        fab.setOnClickListener(view -> {
            crudContact='c';
            showDialogCUD();
        });

        adapter.setOnMenuItemClickListener(new ContactAdapter.OnMenuItemClickListener() {
            @Override
            public void onEditClick(Contact contact, int position) {
                crudContact='u';
                positionContact=position;
                showDialogCUD();
            }

            @Override
            public void onDeleteClick(Contact contact, int position) {
                crudContact='d';
                positionContact=position;
                showDialogCUD();
            }
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

    public void showDialogCUD(){
        builder = new AlertDialog.Builder(getContext());
        if(crudContact=='c')
            builder.setTitle("Create Contact");
        else if(crudContact=='u')
            builder.setTitle("Edit Contact");
        else if(crudContact=='d') {
            builder.setTitle("Delete Contact");
        }

        if(crudContact=='c' || crudContact=='u') {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_fragment_contact_cu, null);
            editTextName = (EditText) dialogView.findViewById(R.id.editTextUsername);
            editTextNumber = (EditText) dialogView.findViewById(R.id.editTextNumber);
            builder.setView(dialogView);
        }

        if(crudContact=='u' || crudContact=='d') {
            Contact contact = contacts.get(positionContact);
            if(crudContact=='u'){
                editTextName.setText(contact.getName());
                editTextNumber.setText(contact.getNumber());
            }else if(crudContact=='d'){
                builder.setMessage("Name: "+contact.getName()+"\nNumber: "+contact.getNumber());
            }
        }
        builder.setPositiveButton("OK", this);
        builder.setNegativeButton("Cancel", this);
        builder.show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            if(crudContact=='c'){
                //Create
                Contact newContact = new Contact(editTextName.getText().toString(), editTextNumber.getText().toString());
                myAccountContact.add(resp -> {
                    if(resp){
                        contacts.add(0,newContact);
                        adapter.notifyItemInserted(0);
                        layoutManager.scrollToPosition(0);
                    }
                },newContact);
            }else if(crudContact=='u'){
                //Update
                Contact editContact = contacts.get(positionContact);
                editContact.setName(editTextName.getText().toString());
                editContact.setNumber(editTextNumber.getText().toString());

                myAccountContact.update(resp -> {
                    if(resp){
                        adapter.notifyItemChanged(positionContact);
                        //layoutManager.scrollToPosition(0);
                    }
                },editContact);
            }else if(crudContact=='d'){
                //Delete
                Contact deleteContact = contacts.get(positionContact);
                myAccountContact.delete(resp -> {
                    if(resp){
                        contacts.remove(positionContact);
                        adapter.notifyItemRemoved(positionContact);
                    }
                },deleteContact.getId());
            }
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            dialogInterface.cancel();
        }
    }

    private void getAllContacts() {
        myAccountContact.list(list -> {
            contacts=list;
            adapter.updateList(contacts);
        });
    }
}