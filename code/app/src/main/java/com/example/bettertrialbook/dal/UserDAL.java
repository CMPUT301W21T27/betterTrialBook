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

    public User findUserByID(int id){
        //First search database for user
        //If not found return null
        //else return user

        return null;
    }

    public User addUser(int id){
        //Add user when ID is first generated and not in Database
        HashMap<String, Object> data = new HashMap<>();
        data.put("username", "");
        data.put("email", "");
        data.put("phone", "");

        collRef.document(Integer.toString(id)).set(data);
        return new User(id);

    }

    public void editUser(User userToUpdate){
        String id = Integer.toString(userToUpdate.getID());
        String username = userToUpdate.getUsername();

        collRef.document(id).update("username",username);
        editContactInfo(userToUpdate);

    }

    public void editContactInfo(User userToUpdate){
        String id = Integer.toString(userToUpdate.getID());
        String email = userToUpdate.getContact().getEmail();
        String phone = userToUpdate.getContact().getPhone();

        collRef.document(id).update("email",email);
        collRef.document(id).update("phone",phone);

    }

    public boolean userNameTaken(String username){
        return false;
    }
}
