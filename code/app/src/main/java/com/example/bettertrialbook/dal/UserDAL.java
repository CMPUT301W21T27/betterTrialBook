package com.example.bettertrialbook.dal;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bettertrialbook.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class UserDAL {

    FirebaseFirestore db;
    CollectionReference collRef;
    ArrayList<User> users;
    final String TAG = "Sample";

    public UserDAL() {
        db = FirebaseFirestore.getInstance();
        collRef = db.collection("Users");
    }

    public void findUserByID(String id, FindUserByIDCallback callback){
        //First search database for user
        //If not found return null
        //else return user
        collRef.document(id).get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        User user;
                        if (document.exists()) {
                            Log.d("TEST", "2. User currently exists");
                            user = new User(id);
                            user.setContact(document.getString("email"), document.getString("phone"));
                            user.setUsername(document.getString("username"));

                        } else {
                            Log.d("TEST", "User does not exist");
                            user = null;
                        }
                        callback.onCallback(user);
                    } else {
                        Log.d("TEST", "Failed with:", task.getException());
                    }
                }
            });
    }

    public interface FindUserByIDCallback {
        void onCallback(User user);
    }

    public User addUser(String id){
        //Add user when ID is first generated and not in Database
        HashMap<String, Object> data = new HashMap<>();
        data.put("username", "");
        data.put("email", "");
        data.put("phone", "");

        collRef.document(id).set(data);
        return new User(id);

    }

    public void editUser(User userToUpdate){
        String id = userToUpdate.getID();
        String username = userToUpdate.getUsername();

        collRef.document(id).update("username",username);
        editContactInfo(userToUpdate);

    }

    public void editContactInfo(User userToUpdate){
        String id = userToUpdate.getID();
        String email = userToUpdate.getContact().getEmail();
        String phone = userToUpdate.getContact().getPhone();

        collRef.document(id).update("email",email);
        collRef.document(id).update("phone",phone);

    }

    public boolean userNameTaken(String username){
        return false;
    }
}
