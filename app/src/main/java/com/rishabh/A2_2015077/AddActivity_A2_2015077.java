package com.rishabh.A2_2015077;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddActivity_A2_2015077 extends AppCompatActivity {

    // Constants
    private static final int PICK_PHOTO = 123;

    // Member Vars
    private EditText editName, editEmail, editPhone;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private Uri profileImage, contactURL = null;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Set title
        getSupportActionBar().setTitle("Add New Contact");

        // Initialisation
        db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        editName = (EditText) findViewById(R.id.editName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPhone = (EditText) findViewById(R.id.editPhone);
        profileImageView = (ImageView) findViewById(R.id.profileImage);
        Button uploadImageButton = (Button) findViewById(R.id.uploadButton);
        Button submitContact = (Button) findViewById(R.id.submitContact);

        // Set onClickListeners
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(v);
            }
        });

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(v);
            }
        });

        submitContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, email, phone;
                name = editName.getText().toString();
                email = editEmail.getText().toString();
                phone = editPhone.getText().toString();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(phone)) {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        if (Patterns.PHONE.matcher(phone).matches()) {
                            Contact_A2_2015077 contactA22015077 = new Contact_A2_2015077(name, email, phone);
                            if (contactURL!=null) {
                                contactA22015077.setImage(contactURL.toString());
                            }
                            db.collection("contacts").add(contactA22015077);
                            Toast.makeText(getBaseContext(), "New contact created!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            editPhone.setError("Invalid phone number!");
                        }
                    } else {
                        editEmail.setError("Invalid email address!");
                    }
                } else {
                    if (TextUtils.isEmpty(name)) {
                        editName.setError("All fields are necessary!");
                    }
                    if (TextUtils.isEmpty(email)) {
                        editEmail.setError("All fields are necessary!");
                    }
                    if (TextUtils.isEmpty(phone)) {
                        editPhone.setError("All fields are necessary!");
                    }
                }
            }
        });
    }

    public void pickImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Image selected!", Toast.LENGTH_SHORT).show();
                    profileImage = data.getData();
                }
        }
    }

    public void uploadImage(View view) {

        if (profileImage==null) {
            Toast.makeText(this, "Please pick a photo by clicking the image!", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference imageReference = storageReference.child("profileImages/" + profileImage.getLastPathSegment());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        progressDialog.setCancelable(false);

        UploadTask uploadTask = imageReference.putFile(profileImage);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.incrementProgressBy((int)progress);
            }
        });

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getBaseContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                contactURL = taskSnapshot.getDownloadUrl();
                progressDialog.dismiss();
                Picasso.with(getBaseContext()).load(contactURL).transform(new CircleTransform_A2_2015077()).into(profileImageView);
            }
        });
    }
}
