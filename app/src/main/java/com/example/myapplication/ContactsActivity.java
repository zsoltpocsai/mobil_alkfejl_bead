package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends BaseActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView recyclerView;

    private ContactsAdapter adapter;
    private CollectionReference contactsRef;
    private List<AppUser> contacts;

    public void onContactClick(View view) {
        Intent intent = new Intent(this, MessageActivity.class);
        TextView nameInput = view.findViewById(R.id.contactsViewItem_nameTextView);
        TextView emailInput = view.findViewById(R.id.contactsViewItem_emailTextView);
        intent.putExtra(MessageActivity.RECIPIENT_NAME_KEY, nameInput.getText());
        intent.putExtra(MessageActivity.RECIPIENT_EMAIL_KEY, emailInput.getText());
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contactsRef = db.collection("users");
    }

    @Override
    protected void onStart() {
        super.onStart();

        recyclerView = findViewById(R.id.contacts_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contacts = new ArrayList<>();
        adapter = new ContactsAdapter(contacts);
        recyclerView.setAdapter(adapter);

        setAppBarTitle("Contacts");

        getContacts();
    }

    private void getContacts() {
        contactsRef.whereNotEqualTo("email", getLoggedInUser().getEmail())
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    AppUser contact = documentSnapshot.toObject(AppUser.class);
                    contacts.add(contact);
                }
                adapter.notifyDataSetChanged();
            });
    }
}
