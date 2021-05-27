package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class MessageActivity extends Activity {

    public static final String RECIPIENT_NAME_KEY = "recipient_name";
    public static final String RECIPIENT_EMAIL_KEY = "recipient_email";
    public static final String SUBJECT_KEY = "subject";

    FirebaseAuth auth = FirebaseAuth.getInstance();
    UserService userService = UserService.getInstance();
    MessageService messageService = MessageService.getInstance();

    private Sender sender;
    private Receiver recipient;
    private EditText recipientView;
    private EditText subjectInput;
    private EditText contentInput;

    public void onSendMessage(View view) {
        if (validateFields()) {
            sendMessage();
        }
    }

    public void onCancel(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Intent intent = getIntent();

        recipient = new Receiver(
            intent.getStringExtra(RECIPIENT_NAME_KEY),
            intent.getStringExtra(RECIPIENT_EMAIL_KEY)
        );
        recipientView = findViewById(R.id.messageActivity_recipientEditText);
        subjectInput = findViewById(R.id.messageActivity_subjectEditText);
        contentInput = findViewById(R.id.messageActivity_contentEditText);

        if (auth.getCurrentUser() != null) {
            userService.getAppUser(auth.getCurrentUser().getEmail()).addOnSuccessListener(appUser -> {
                if (appUser != null) {
                    sender = new Sender(appUser.getName(), appUser.getEmail());
                }
            });
        }

        if (intent.getStringExtra(SUBJECT_KEY) != null) {
            subjectInput.setText(intent.getStringExtra(SUBJECT_KEY));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        recipientView.setText(recipient.getName() + " <" + recipient.getEmail() + ">");
    }

    private void sendMessage() {
        CommunicationMessage message = new CommunicationMessage();
        message.setSender(sender);
        message.setReceiver(recipient);
        message.setSubject(subjectInput.getText().toString());
        message.setContent(contentInput.getText().toString());
        message.setSendTime(Calendar.getInstance().getTime());

        messageService.save(message).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Message is sent successfully", Toast.LENGTH_SHORT).show();
                MessageActivity.this.finish();
            } else {
                Toast.makeText(this, "Sending message is failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateFields() {
        if (subjectInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Subject can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else if (contentInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Content can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
