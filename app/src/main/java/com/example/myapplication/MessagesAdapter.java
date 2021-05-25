package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private List<CommunicationMessage> messages;
    private MainActivity.MessageType messageType;

    public MessagesAdapter(List<CommunicationMessage> messages, MainActivity.MessageType messageType) {
        this.messages = messages;
        this.messageType = messageType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_item_message, viewGroup, false);

        return new ViewHolder(this, viewGroup.getContext(), view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.bindTo(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    protected MainActivity.MessageType getMessageType() {
        return this.messageType;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private MessageService messageService = MessageService.getInstance();

        private Context context;
        private MessagesAdapter adapter;

        private CardView container;
        private ImageView letterIcon;
        private TextView senderName;
        private TextView receiverName;
        private TextView subject;
        private TextView content;
        private TextView date;
        private ViewGroup contentHolder;
        private Button answerButton;
        private Button deleteButton;

        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy. MM. dd. HH:mm");

        public ViewHolder(MessagesAdapter adapter, Context context, View view) {
            super(view);

            this.context = context;
            this.adapter = adapter;

            container = view.findViewById(R.id.messageViewItem_containerCardView);
            letterIcon = view.findViewById(R.id.messageViewItem_letterImageView);
            senderName = view.findViewById(R.id.messageViewItem_senderTextView);
            receiverName = view.findViewById(R.id.messageViewItem_receiverTextView);
            subject = view.findViewById(R.id.messageViewItem_subjectTextView);
            content = view.findViewById(R.id.messageViewItem_contentTextView);
            date = view.findViewById(R.id.messageViewItem_dateTextView);
            contentHolder = view.findViewById(R.id.messageViewItem_contentHolder);

            answerButton = view.findViewById(R.id.messageViewItem_answerButton);
            deleteButton = view.findViewById(R.id.messageViewItem_deleteButton);
        }

        public void bindTo(CommunicationMessage message) {
            senderName.setText(message.getSender().getName());
            receiverName.setText(message.getReceiver().getName());
            subject.setText(message.getSubject());
            content.setText(message.getContent());
            date.setText(dateFormat.format(message.getSendTime()));
            contentHolder.setVisibility(View.GONE);

            toggleLetterIcon(message);

            container.setOnClickListener(v -> {
                if (contentHolder.getVisibility() != View.GONE) {
                    contentHolder.setVisibility(View.GONE);
                } else {
                    contentHolder.setVisibility(View.VISIBLE);
                    if (message.getState() == CommunicationMessage.CommunicationMessageStateType.inProgress) {
                        message.setState(CommunicationMessage.CommunicationMessageStateType.completed);
                        messageService.updateState(message);
                        toggleLetterIcon(message);
                    }
                }
            });

            answerButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra(MessageActivity.RECIPIENT_NAME_KEY, message.getSender().getName());
                intent.putExtra(MessageActivity.RECIPIENT_EMAIL_KEY, message.getSender().getEmail());
                intent.putExtra(MessageActivity.SUBJECT_KEY, message.getSubject());
                context.startActivity(intent);
            });

            deleteButton.setOnClickListener(v -> {
                messageService.delete(message).addOnSuccessListener(task -> {
                    adapter.messages.remove(message);
                    adapter.notifyDataSetChanged();
                });
            });
        }

        private void toggleLetterIcon(CommunicationMessage message) {
            if (adapter.getMessageType() == MainActivity.MessageType.SENT) {
                return;
            }

            Drawable unreadMail = context.getDrawable(R.drawable.ic_baseline_mark_email_unread_24);
            Drawable readMail = context.getDrawable(R.drawable.ic_baseline_email_24);

            if (message.getState() == CommunicationMessage.CommunicationMessageStateType.inProgress) {
                letterIcon.setImageDrawable(unreadMail);
            } else {
                letterIcon.setImageDrawable(readMail);
            }
        }
    }
}
