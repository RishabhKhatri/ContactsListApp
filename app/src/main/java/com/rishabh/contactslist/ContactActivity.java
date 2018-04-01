package com.rishabh.contactslist;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ContactActivity extends AppCompatActivity {

    private static final String TAG = "Delete";
    private FloatingActionButton fab;
    private ViewGroup home_frag, details_frag;
    private static final int ASK_PERMISSIONS = 273;
    private FirebaseFirestore db;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.CustomTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        db = FirebaseFirestore.getInstance();

        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AddActivity.class);
                startActivity(intent);
            }
        });

        home_frag = (ViewGroup) findViewById(R.id.home_frame_layout);
        details_frag = (ViewGroup) findViewById(R.id.details_frame_layout);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS}, ASK_PERMISSIONS);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("firstTime", false)) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.home_frame_layout, new ContactList());
            transaction.commit();
        }
    }

    public void changeFragments(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (details_frag!=null) {
            fragmentTransaction.replace(R.id.details_frame_layout, fragment, fragment.toString());
        } else {
            fragmentTransaction.replace(R.id.home_frame_layout, fragment, fragment.toString());
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0,0);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ASK_PERMISSIONS: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    if (!prefs.getBoolean("firstTime", false)) {

                        final ProgressDialog dialog = new ProgressDialog(this);
                        dialog.setMessage("Adding contacts from phone...");
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.show();
                        dialog.setCancelable(false);

                        db.collection("contacts")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                db.collection("contacts").document(document.getId()).delete();
                                            }
                                            dialog.dismiss();
                                            getContentProviders();
                                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                            transaction.add(R.id.home_frame_layout, new ContactList());
                                            transaction.commit();
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });

                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("firstTime", true);
                        editor.apply();
                    }
                } else {
                    Toast.makeText(this, "LOL WTF!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void showFAB(Boolean flag) {
        if (flag) {
            fab.show();
        } else {
            fab.hide();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getContentProviders() {

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null,
                null, null,
                ContactsContract.Contacts.DISPLAY_NAME+" ASC ");

        if (cursor.moveToFirst())
        {

            do {
                long id = cursor.getLong(cursor.getColumnIndex("_ID"));
                Uri data_uri = ContactsContract.Data.CONTENT_URI;

                Cursor data_cursor = getContentResolver().query(data_uri, null,
                        ContactsContract.Data.CONTACT_ID+"="+id, null,
                        null);
                String name = "", email="", phone="";


                if(data_cursor.moveToFirst())
                {
                    name = data_cursor.getString(data_cursor.
                            getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                    do {

                        if (data_cursor.getString(data_cursor.getColumnIndex(
                                "mimetype"
                        )).equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE))
                        {
                            switch (data_cursor.getInt(data_cursor
                                    .getColumnIndex("data2")))
                            {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    phone = data_cursor.getString(data_cursor.getColumnIndex(
                                            "data1"
                                    ));
                                    break;
                                default:
                                    break;
                            }
                        }

                        if (data_cursor.getString(data_cursor.getColumnIndex(
                                "mimetype"
                        )).equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE))
                        {
                            switch (data_cursor.getInt(data_cursor
                                    .getColumnIndex("data2")))
                            {
                                case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                                    email = data_cursor.getString(data_cursor.getColumnIndex(
                                            "data1"
                                    ));
                                    break;
                                default:
                                    break;
                            }
                        }
                    } while (data_cursor.moveToNext());
                }
                if (!TextUtils.isEmpty(phone)) {
                    Contact contact = new Contact(name, email, phone);
                    db.collection("contacts").add(contact);
                }
            } while (cursor.moveToNext());
        }
        Toast.makeText(this, "Contacts added from phone!", Toast.LENGTH_LONG).show();
    }
}
