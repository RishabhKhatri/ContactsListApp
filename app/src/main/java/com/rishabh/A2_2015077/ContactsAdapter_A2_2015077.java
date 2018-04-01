package com.rishabh.A2_2015077;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ContactsAdapter_A2_2015077 extends RecyclerView.Adapter<ContactsAdapter_A2_2015077.ViewHolder> {

    // Member Vars
    private List<Contact_A2_2015077> contactA22015077s;
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

    public ContactsAdapter_A2_2015077(List<Contact_A2_2015077> contactA22015077s) {
        this.contactA22015077s = contactA22015077s;
    }

    @NonNull
    @Override
    public ContactsAdapter_A2_2015077.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.contact_layout, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = recyclerView.getChildLayoutPosition(v);
                Contact_A2_2015077 contactA22015077 = contactA22015077s.get(position);
                detailsFragment = new ContactDetails_A2_2015077();
                bundle = new Bundle();
                bundle.putParcelable("contact_selected", contactA22015077);
                detailsFragment.setArguments(bundle);
                ContactActivity_A2_2015077 contactActivityA22015077 = (ContactActivity_A2_2015077) v.getContext();
                contactActivityA22015077.changeFragments(detailsFragment);
            }
        });
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Contact_A2_2015077 contactA22015077 = contactA22015077s.get(position);
        final String name = contactA22015077.getName();
        final String phone = contactA22015077.getPhone();

        holder.header.setText(name);
        holder.footer.setText(phone);
    }

    @Override
    public int getItemCount() {
        return contactA22015077s.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
}
