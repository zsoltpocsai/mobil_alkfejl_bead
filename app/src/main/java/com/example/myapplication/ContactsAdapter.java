package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<AppUser> contacts;

    public ContactsAdapter(List<AppUser> contacts) {
        this.contacts = contacts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_item_contacts, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.bindTo(contacts.get(position));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView contactName;
        private TextView contactEmail;

        public ViewHolder(View view) {
            super(view);
            contactName = view.findViewById(R.id.contactsViewItem_nameTextView);
            contactEmail = view.findViewById(R.id.contactsViewItem_emailTextView);
        }

        public void bindTo(AppUser user) {
            contactName.setText(user.getName());
            contactEmail.setText(user.getEmail());
        }
    }
}
