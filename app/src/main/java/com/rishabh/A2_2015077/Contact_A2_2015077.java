package com.rishabh.A2_2015077;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact_A2_2015077 implements Parcelable{
    private String id;
    private String name;
    private String email;
    private String phone;
    private String image;

    public Contact_A2_2015077() {

    }

    public Contact_A2_2015077(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    protected Contact_A2_2015077(Parcel in) {
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        image = in.readString();
    }

    public static final Creator<Contact_A2_2015077> CREATOR = new Creator<Contact_A2_2015077>() {
        @Override
        public Contact_A2_2015077 createFromParcel(Parcel in) {
            return new Contact_A2_2015077(in);
        }

        @Override
        public Contact_A2_2015077[] newArray(int size) {
            return new Contact_A2_2015077[size];
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
