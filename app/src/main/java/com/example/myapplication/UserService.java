package com.example.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UserService {

    private static UserService instance;
    private static final String TAG = UserService.class.getSimpleName();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");

    private UserService() {}

    public static UserService getInstance() {
        if (instance == null){
            instance = new UserService();
        }
        return instance;
    }

    public Task<AppUser> getAppUser(String email) {
        return usersRef.whereEqualTo("email", email)
            .get()
            .continueWith(new Continuation<QuerySnapshot, AppUser>() {
                @Override
                public AppUser then(@NonNull Task<QuerySnapshot> task) throws Exception {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        if (documents.isEmpty()) {
                            Log.i(TAG, "couldn't find app user");
                            return null;
                        } else {
                            Log.i(TAG, "app user was successfully found");
                            return documents.get(0).toObject(AppUser.class);
                        }
                    } else {
                        Log.i(TAG, "fetching app user was not successfull");
                        return null;
                    }
                }
            });
    }

    public Task<AppUser> createAppUser(String name, String email) {
        AppUser appUser = new AppUser(name, email);
        return usersRef.add(appUser)
            .continueWithTask(new Continuation<DocumentReference, Task<DocumentSnapshot>>() {
                @Override
                public Task<DocumentSnapshot> then(Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        return task.getResult().get();
                    } else {
                        Log.i(TAG, "Creating appUser was not successfull");
                        return Tasks.forException(task.getException());
                    }
                }
            }).continueWith(new Continuation<DocumentSnapshot, AppUser>() {
                @Override
                public AppUser then(Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        return task.getResult().toObject(AppUser.class);
                    } else {
                        return null;
                    }
                }
            });
    }
}
