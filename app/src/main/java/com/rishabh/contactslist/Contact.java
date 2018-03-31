package com.rishabh.contactslist;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String image;

    public Contact() {

    }

    public Contact(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    protected Contact(Parcel in) {
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        image = in.readString();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(image);
    }
}
