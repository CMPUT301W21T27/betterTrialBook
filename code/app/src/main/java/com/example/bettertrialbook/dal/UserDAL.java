package com.example.bettertrialbook.dal;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bettertrialbook.You;
import com.example.bettertrialbook.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/*
* Data Access Layer
* Interacts with the Users collection in database
* */
public class UserDAL {

    FirebaseFirestore db;
    CollectionReference collRef;
    final String TAG = "Sample";

    /**
     * Access database's "Users" collection
     */
    public UserDAL() {
        db = Firestore.getInstance();
        collRef = db.collection("Users");
    }

    /**
     * Searches database for given ID Returns user object if found, or null if not
     * found
     * 
     * @param id       - Id to search for
     * @param callback - return method for firestore queries
     */
    public void findUserByID(String id, FindUserByIDCallback callback) {
        // First search database for user
        // If not found return null
        // else return user
        // Uses call back for returns

        collRef.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    User user;
                    if (document.exists()) {
                        // User found, create user object to return
                        Log.d("TEST", "2. User currently exists");
                        user = new User(id);
                        user.setContact(document.getString("email"), document.getString("phone"));
                        user.setUsername(document.getString("username"));

                    } else {
                        // User not found, return null
                        Log.d("TEST", "User does not exist");
                        user = null;
                    }
                    callback.onCallback(user);
                } else {
                    // Error occurred
                    Log.d("TEST", "Failed with:", task.getException());
                }
            }
        });
    }

    // Interfaces for callbacks
    public interface FindUserByIDCallback {
        void onCallback(User user);
    }

    public interface UsernameTakenCallback {
        void onCallback(boolean isNotTaken);
    }

    /**
     * Takes ID and adds to Database Takes ID and creates a user from it
     * 
     * @param id - ID to add
     * @return - new user object with given id
     */
    public User addUser(String id) {
        // Add user when ID is first generated and not in Database
        HashMap<String, Object> data = new HashMap<>();
        data.put("username", "");
        data.put("email", "");
        data.put("phone", "");

        collRef.document(id).set(data);
        return new User(id);

    }

    /**
     * Edits given user object and updates database information
     * 
     * @param userToUpdate - user object to edit
     */
    public void editUser(User userToUpdate) {
        String id = userToUpdate.getID();
        String username = userToUpdate.getUsername();

        collRef.document(id).update("username", username);
        editContactInfo(userToUpdate);
        You.setUser(userToUpdate);

    }

    /**
     * Edits the contact info of a user Updates database information
     * 
     * @param userToUpdate - user to update
     */
    public void editContactInfo(User userToUpdate) {
        String id = userToUpdate.getID();
        String email = userToUpdate.getContact().getEmail();
        String phone = userToUpdate.getContact().getPhone();

        collRef.document(id).update("email", email);
        collRef.document(id).update("phone", phone);

    }

    /**
     * Takes username as input Searches database to see if username already taken
     * Returns true or false in callback
     * 
     * @param username - username to search for
     * @param callback - return method for firestore queries
     */
    public void userNameTaken(String username, UsernameTakenCallback callback) {

        collRef.whereEqualTo("username", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // query successful
                    // check if empty (username not in use)
                    boolean isNotTaken = task.getResult().isEmpty();
                    Log.d("TEST", "Task Success: isNotTaken = " + isNotTaken);
                    callback.onCallback(isNotTaken);

                } else {
                    // query error occurred
                    Log.d("TEST", "Failed with:", task.getException());
                }
            }
        });
    }

    // https://stackoverflow.com/questions/3624280/how-to-use-sharedpreferences-in-android-to-store-fetch-and-edit-values

    /**
     * Retrieves this device's user id.
     * This value will be persistent across sessions.
     * @param context An android context object, such as an Activity
     * @return
     */
    public String getDeviceUserId(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("uniqueID", Context.MODE_PRIVATE);
        return sharedPref.getString("uniqueID", null);
    }

    /**
     * Sets this device's user id.
     * This value will be persistent across sessions
     * @param context An android context object, such as an Activity
     * @param uid
     */
    public void setDeviceUserId(Context context, String uid) {
        if (uid == null)
            throw new NullPointerException("Cannot save a null user id");
        SharedPreferences sharedPref = context.getSharedPreferences("uniqueID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("uniqueID", uid);
        editor.apply();
    }

}
