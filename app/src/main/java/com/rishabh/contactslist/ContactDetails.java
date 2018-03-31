package com.rishabh.contactslist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ContactDetails extends Fragment {

    private Contact contact;
    private static final int CALL_PHONE_CODE = 273;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((ContactActivity) getActivity()).showFAB(false);

        getActivity().setTitle("Contact");

        Context context = getActivity().getApplicationContext();
        final Context wrapper = new ContextThemeWrapper(getActivity(), R.style.CustomTheme);
        LayoutInflater layoutInflater = inflater.cloneInContext(wrapper);
        View view = layoutInflater.inflate(R.layout.fragment_contact_details, container, false);

        ImageView profileImage = (ImageView) view.findViewById(R.id.showImage);
        TextView showName = (TextView) view.findViewById(R.id.showName);
        TextView showEmail = (TextView) view.findViewById(R.id.showEmail);
        TextView showPhone = (TextView) view.findViewById(R.id.showPhone);
        com.github.clans.fab.FloatingActionButton phone_fab = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.phone_fab);
        com.github.clans.fab.FloatingActionButton mail_fab = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.mail_fab);

        Bundle bundle = getArguments();
        contact = bundle.getParcelable("contact_selected");

        phone_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+contact.getPhone()));
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[] {android.Manifest.permission.CALL_PHONE}, CALL_PHONE_CODE);
                } else {
                    try {
                        startActivity(intent);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mail_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + contact.getEmail()));
                startActivity(intent);
            }
        });

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading profile image...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);

        if (contact!=null) {
            if (contact.getImage()!=null) {
                Picasso.with(context).load(contact.getImage()).into(profileImage, new Callback() {
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
            showName.setText(contact.getName());
            showEmail.setText(contact.getEmail());
            showPhone.setText(contact.getPhone());
        }
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CALL_PHONE_CODE: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact.getPhone()));
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Are you a fucking idiot!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_layout, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Bundle bundle = new Bundle();
                bundle.putParcelable("selected_contact", contact);
                Intent intent = new Intent(getActivity(), EditActivity.class);
                intent.putExtra("selected_contact", bundle);
                intent.putExtra("selected_id", contact.getId());
                startActivity(intent);
                break;
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
