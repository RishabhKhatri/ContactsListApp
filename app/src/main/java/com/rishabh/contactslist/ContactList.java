package com.rishabh.contactslist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactList extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FirebaseFirestore db;
    private String TAG = "GetData";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        ((ContactActivity) getActivity()).showFAB(true);

        getActivity().setTitle("All Contacts");

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("All Contacts");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        final List<Contact> contacts = new ArrayList<>();
        db.collection("contacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Contact contact = document.toObject(Contact.class);
                                Log.d(TAG, contact.getName() + " " + contact.getEmail() + " " + contact.getPhone());
                                contact.setId(document.getId());
                                contacts.add(contact);
                            }
                            Collections.sort(contacts, new Comparator<Contact>() {
                                @Override
                                public int compare(Contact o1, Contact o2) {
                                    return o1.getName().compareTo(o2.getName());
                                }
                            });
                            adapter = new ContactsAdapter(contacts);
                            recyclerView.setAdapter(adapter);
                            progressDialog.dismiss();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("All Contacts");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        final List<Contact> contacts = new ArrayList<>();
        db.collection("contacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Contact contact = document.toObject(Contact.class);
                                Log.d(TAG, contact.getName() + " " + contact.getEmail() + " " + contact.getPhone());
                                contact.setId(document.getId());
                                contacts.add(contact);
                            }
                            Collections.sort(contacts, new Comparator<Contact>() {
                                @Override
                                public int compare(Contact o1, Contact o2) {
                                    return o1.getName().compareTo(o2.getName());
                                }
                            });
                            adapter = new ContactsAdapter(contacts);
                            recyclerView.setAdapter(adapter);
                            progressDialog.dismiss();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
