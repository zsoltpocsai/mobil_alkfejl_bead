package com.example.myapplication;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MessageService {

    private static MessageService instance;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messagesRef = db.collection("messages");

    private MessageService() {}

    public static MessageService getInstance() {
        if (instance == null) {
            instance = new MessageService();
        }
        return instance;
    }

    public Task<List<CommunicationMessage>> getReceivedMessages(String email) {
        return getMessages(FieldPath.of("receiver", "email"), email);
    }

    public Task<List<CommunicationMessage>> getSentMessages(String email) {
        return getMessages(FieldPath.of("sender", "email"), email);
    }

    public Task<DocumentReference> save(CommunicationMessage message) {
        return messagesRef.add(message);
    }

    public Task<Void> updateState(CommunicationMessage message) {
        return getDocumentReference(message).continueWithTask(task -> {
            if (task.getResult() != null) {
                return task.getResult().update("state", message.getState());
            } else {
                return Tasks.forException(new Exception("Couldn't find the message"));
            }
        });
    }

    public Task<Void> delete(CommunicationMessage message) {
        return getDocumentReference(message).continueWithTask(task -> {
            if (task.getResult() != null) {
                return task.getResult().delete();
            } else {
                return Tasks.forException(new Exception("Couldn't find the message"));
            }
        });
    }

    private Task<List<CommunicationMessage>> getMessages(FieldPath where, String email) {
        List<CommunicationMessage> messages = new ArrayList<>();
        return messagesRef.whereEqualTo(where, email).orderBy("sendTime", Query.Direction.DESCENDING).get()
            .continueWith(task -> {
                QuerySnapshot querySnapshot = task.getResult();
                for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                    messages.add(documentSnapshot.toObject(CommunicationMessage.class));
                }
                return messages;
            });
    }

    private Task<DocumentReference> getDocumentReference(CommunicationMessage message) {
        return messagesRef
            .whereEqualTo(FieldPath.of("sender", "email"), message.getSender().getEmail())
            .whereEqualTo(FieldPath.of("receiver", "email"), message.getReceiver().getEmail())
            .whereEqualTo("sendTime", message.getSendTime())
            .get()
            .continueWith(task -> {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.getDocuments().isEmpty()) {
                    return querySnapshot.getDocuments().get(0).getReference();
                } else {
                    return null;
                }
            });
    }
}
