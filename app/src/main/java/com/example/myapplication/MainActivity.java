package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    public enum MessageType {
        RECEIVED,
        SENT
    }

    public static final String MESSAGE_TYPE_KEY = "message_type";

    private MessageService messageService = MessageService.getInstance();

    private RecyclerView recyclerView;
    private MessagesAdapter messagesAdapter;
    private List<CommunicationMessage> messages;

    private MessageType messageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        recyclerView = findViewById(R.id.mainActivity_messagesView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messages = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(messages);
        recyclerView.setAdapter(messagesAdapter);

        Intent intent = getIntent();
        messageType = (MessageType) intent.getSerializableExtra(MESSAGE_TYPE_KEY);

        getLoggedInAppUser().addOnSuccessListener(appUser -> {
            if (appUser != null) {
                pullMessages(appUser);
            }
        });

        if (messageType == MessageType.SENT) {
            setAppBarTitle("Sent messages");
            messagesAdapter.setMessageType(MessageType.SENT);
        } else {
            setAppBarTitle("Received messages");
            messagesAdapter.setMessageType(MessageType.RECEIVED);
        }
    }

    private void pullMessages(AppUser user) {
        if (messageType == MessageType.SENT) {
            messageService.getSentMessages(user.getEmail()).addOnSuccessListener(messages -> {
                this.messages.clear();
                for (CommunicationMessage message : messages) {
                    this.messages.add(message);
                }
                messagesAdapter.notifyDataSetChanged();
                toggleEmptyMessage();
            });
        } else {
            messageService.getReceivedMessages(user.getEmail()).addOnSuccessListener(messages -> {
                this.messages.clear();
                for (CommunicationMessage message : messages) {
                    this.messages.add(message);
                }
                messagesAdapter.notifyDataSetChanged();
                toggleEmptyMessage();
            });
        }
    }

    private void toggleEmptyMessage() {
        TextView emptyMessage = findViewById(R.id.mainActivity_emptyMessageTextView);
        if (messages.isEmpty()) {
            emptyMessage.setVisibility(View.VISIBLE);
        } else {
            emptyMessage.setVisibility(View.GONE);
        }
    }
}
