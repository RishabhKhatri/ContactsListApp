package com.rishabh.contactslist;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<Contact> contacts;
    private Fragment detailsFragment;
    private Bundle bundle;
    private RecyclerView recyclerView;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView header;
        TextView footer;
        public View layout;
        ViewHolder(View v) {
            super(v);
            layout = v;
            header = (TextView) v.findViewById(R.id.name);
            footer = (TextView) v.findViewById(R.id.phone);
        }
    }

    public ContactsAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.contact_layout, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = recyclerView.getChildLayoutPosition(v);
                Contact contact = contacts.get(position);
                detailsFragment = new ContactDetails();
                bundle = new Bundle();
                bundle.putParcelable("contact_selected", contact);
                detailsFragment.setArguments(bundle);
                ContactActivity contactActivity = (ContactActivity) v.getContext();
                contactActivity.changeFragments(detailsFragment);
            }
        });
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Contact contact = contacts.get(position);
        final String name = contact.getName();
        final String phone = contact.getPhone();

        holder.header.setText(name);
        holder.footer.setText(phone);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
}
