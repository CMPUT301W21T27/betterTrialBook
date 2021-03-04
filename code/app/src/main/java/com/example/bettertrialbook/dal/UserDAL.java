package com.example.bettertrialbook.dal;

import com.example.bettertrialbook.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserDAL {
    FirebaseFirestore db;
    final String TAG = "Sample";
    CollectionReference collectionReference;
    ArrayList<User> users;

    public UserDAL() {
        db = FirebaseFirestore.getInstance();
        collectionReference=db.collection("Users");
        ArrayList<User> users = new ArrayList<>();
    }

    public User getUserByID(int idToFind){
        //search database for this id
        //returns null if not found
        return null;
    }

    //Add user when ID is generated for first time
    //Returns a user object
    public User addUser(int ID){
        User user = new User(ID);
        //add user to database
        //ID = user.getID();
        //username = user.getUsername();
        //email = user.getContactInfo().getEmail();
        //phone = user.getContactInfo().getPhone();

        return user;
    }

    public void addUsername(User user){
        //find user in database
        //add username to document
    }

    public void addContactInfo(User user){
        //find user in database
        //set contact info to users new contact info
    }
}
