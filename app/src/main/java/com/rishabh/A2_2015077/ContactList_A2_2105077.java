package com.rishabh.A2_2015077;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.*;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactList_A2_2105077 extends Fragment {

    // Member Vars
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
        // Show FAB
        ((ContactActivity_A2_2015077) getActivity()).showFAB(true);

        // Set title
        getActivity().setTitle("All Contacts");

        // Initialisation
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // Setup progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("All Contacts");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        // Get contactA22015077s from Firebase
        final List<Contact_A2_2015077> contactA22015077s = new ArrayList<>();
        db.collection("contacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Contact_A2_2015077 contactA22015077 = document.toObject(Contact_A2_2015077.class);
                                Log.d(TAG, contactA22015077.getName() + " " + contactA22015077.getEmail() + " " + contactA22015077.getPhone());
                                contactA22015077.setId(document.getId());
                                contactA22015077s.add(contactA22015077);
                            }
                            Collections.sort(contactA22015077s, new Comparator<Contact_A2_2015077>() {
                                @Override
                                public int compare(Contact_A2_2015077 o1, Contact_A2_2015077 o2) {
                                    return o1.getName().compareTo(o2.getName());
                                }
                            });
                            adapter = new ContactsAdapter_A2_2015077(contactA22015077s);
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

        // For rotation purposes
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("All Contacts");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        final List<Contact_A2_2015077> contactA22015077s = new ArrayList<>();
        db.collection("contacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Contact_A2_2015077 contactA22015077 = document.toObject(Contact_A2_2015077.class);
                                Log.d(TAG, contactA22015077.getName() + " " + contactA22015077.getEmail() + " " + contactA22015077.getPhone());
                                contactA22015077.setId(document.getId());
                                contactA22015077s.add(contactA22015077);
                            }
                            Collections.sort(contactA22015077s, new Comparator<Contact_A2_2015077>() {
                                @Override
                                public int compare(Contact_A2_2015077 o1, Contact_A2_2015077 o2) {
                                    return o1.getName().compareTo(o2.getName());
                                }
                            });
                            adapter = new ContactsAdapter_A2_2015077(contactA22015077s);
                            recyclerView.setAdapter(adapter);
                            progressDialog.dismiss();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
