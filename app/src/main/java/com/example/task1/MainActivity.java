package com.example.task1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task1.adapter.ContactsAdapter;
import com.example.task1.db.DatabaseHelper;
import com.example.task1.db.entity.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ContactsAdapter adapter;
    private ArrayList<Contact> contactsArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DatabaseHelper dbhelper;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //recycler view
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbhelper =new DatabaseHelper(this);

        contactsArrayList.addAll(dbhelper.getAllContacts());
        adapter = new ContactsAdapter(this,contactsArrayList,MainActivity.this);
        recyclerView.setAdapter(adapter);

        fab =findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAndEditContacts(false,null,-1);
            }
        });


    }

    public void addAndEditContacts(final boolean isUpdated, final Contact contact, final int position) {
        View view =  LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_add_contact,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(view);

        TextView contactTitle = view.findViewById(R.id.new_contact_title);
        final EditText newContactName = view.findViewById(R.id.edt_name);
        final EditText newContactEmail = view.findViewById(R.id.edt_email);

        contactTitle.setText(!isUpdated ? "Add New Contact" : "Edit Contact");


        if (isUpdated && contact !=null){
            newContactName.setText(contact.getName());
            newContactEmail.setText(contact.getEmail());
        }

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(isUpdated ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(!isUpdated ? "Cancel" : "Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (isUpdated){
                                    deleteContact(contact,position);
                                }else {
                                    dialog.cancel();
                                }

                            }
                        });


        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(newContactName.getText().toString()) || TextUtils.isEmpty(newContactEmail.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Empty Fields are not allowed!", Toast.LENGTH_SHORT).show();
                }else {
                    alertDialog.dismiss();

                    if (isUpdated && contact!=null){
                        updateContact(newContactName.getText().toString(),newContactEmail.getText().toString(),position);
                    }else {
                        createContact(newContactName.getText().toString(),newContactEmail.getText().toString());
                    }
                }

            }
        });
    }

    private void createContact(String name, String email) {
        long id = dbhelper.insertContact(name,email);
        Contact contact = dbhelper.getContact(id);
        if (contact!= null){
            contactsArrayList.add(0,contact);
            adapter.notifyDataSetChanged();
        }
    }

    private void updateContact(String name, String email, int position) {
        Contact contact = contactsArrayList.get(position);
        contact.setName(name);
        contact.setEmail(email);
        dbhelper.updateContact(contact);

        contactsArrayList.set(position,contact);
        adapter.notifyDataSetChanged();
    }

    private void deleteContact(Contact contact, int position) {
        contactsArrayList.remove(position);
        dbhelper.deleteContact(contact);
        adapter.notifyDataSetChanged();
    }
}