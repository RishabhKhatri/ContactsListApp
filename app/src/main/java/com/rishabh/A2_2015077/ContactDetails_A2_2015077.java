package com.rishabh.A2_2015077;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ContactDetails_A2_2015077 extends Fragment {

    // Constants
    private static final int CALL_PHONE_CODE = 273, RESUME_FRAG=1001;
    private static final String TAG = "Delete";

    // Member Vars
    private Contact_A2_2015077 contactA22015077;
    private FirebaseFirestore db;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Remove FAB
        ((ContactActivity_A2_2015077) getActivity()).showFAB(false);

        // Initialisation
        Context context = getActivity().getApplicationContext();
        final Context wrapper = new ContextThemeWrapper(getActivity(), R.style.CustomTheme);
        LayoutInflater layoutInflater = inflater.cloneInContext(wrapper);
        view = layoutInflater.inflate(R.layout.fragment_contact_details, container, false);
        ImageView profileImage = (ImageView) view.findViewById(R.id.showImage);
        TextView showName = (TextView) view.findViewById(R.id.showName);
        TextView showEmail = (TextView) view.findViewById(R.id.showEmail);
        TextView showPhone = (TextView) view.findViewById(R.id.showPhone);
        com.github.clans.fab.FloatingActionButton phone_fab = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.phone_fab);
        com.github.clans.fab.FloatingActionButton mail_fab = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.mail_fab);

        // Get selected contactA22015077 from ViewAdapter
        Bundle bundle = getArguments();
        contactA22015077 = bundle.getParcelable("contact_selected");

        // Fill details
        if (contactA22015077 !=null) {
            if (contactA22015077.getImage()!=null) {

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading profile image...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);

                Picasso.with(context).load(contactA22015077.getImage()).into(profileImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError() {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Error loading profile image!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            // Set title
            getActivity().setTitle(contactA22015077.getName());
            showName.setText(contactA22015077.getName());
            showEmail.setText(contactA22015077.getEmail());
            showPhone.setText(contactA22015077.getPhone());
        }

        // For MenuBar
        setHasOptionsMenu(true);

        // Set onClickListeners
        phone_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ contactA22015077.getPhone()));
                startActivity(intent);
            }
        });

        mail_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + contactA22015077.getEmail()));
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_layout, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESUME_FRAG && resultCode== Activity.RESULT_OK) {

            // Refresh contactA22015077 variable after editing it
            db.collection("contacts").document(contactA22015077.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            contactA22015077 = document.toObject(Contact_A2_2015077.class);
                        } else {
                            Log.d(TAG, "No such document");
                        }

                        ImageView profileImage = (ImageView) view.findViewById(R.id.showImage);
                        TextView showName = (TextView) view.findViewById(R.id.showName);
                        TextView showEmail = (TextView) view.findViewById(R.id.showEmail);
                        TextView showPhone = (TextView) view.findViewById(R.id.showPhone);

                        // Fill new details
                        if (contactA22015077 !=null) {
                            if (contactA22015077.getImage()!=null) {

                                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Loading profile image...");
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.show();
                                progressDialog.setCancelable(false);

                                Picasso.with(getActivity()).load(contactA22015077.getImage()).into(profileImage, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onError() {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Error loading profile image!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            // Set title
                            getActivity().setTitle(contactA22015077.getName());
                            showName.setText(contactA22015077.getName());
                            showEmail.setText(contactA22015077.getEmail());
                            showPhone.setText(contactA22015077.getPhone());
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Bundle bundle = new Bundle();
                bundle.putParcelable("selected_contact", contactA22015077);
                Intent intent = new Intent(getActivity(), EditActivity_A2_2015077.class);
                intent.putExtra("selected_contact", bundle);
                intent.putExtra("selected_id", contactA22015077.getId());
                startActivityForResult(intent, RESUME_FRAG);
                break;
            case R.id.action_delete:

                // Delete contactA22015077
                final ProgressDialog dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Deleting contact...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setCancelable(false);
                dialog.show();
                db.collection("contacts").document(contactA22015077.getId()).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Contact deleted!", Toast.LENGTH_SHORT).show();
                                Intent intent = getActivity().getIntent();
                                getActivity().finish();
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Error while deleting contact!", Toast.LENGTH_LONG).show();
                            }
                        });
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_refresh).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}
