package com.example.bettertrialbook.dal;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bettertrialbook.You;
import com.example.bettertrialbook.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Data Access Layer
 * Interacts with the Users collection in database
 */

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
                        user = new User(id);
                        user.setContact(document.getString("email"), document.getString("phone"));
                        user.setUsername(document.getString("username"));

                    } else {
                        // User not found, return null
                        user = null;
                    }
                    callback.onCallback(user);
                } else {
                    // Error occurred
                }
            }
        });
    }

    // Interfaces for callbacks

    /**
     * Callback after finding user by their id
     */
    public interface FindUserByIDCallback {
        void onCallback(User user);
    }

    /**
     * Callback after comparing new username to existing usernames
     */
    public interface UsernameTakenCallback {
        void onCallback(boolean isNotTaken);
    }

    /**
     * Callback after retrieving all subscribed experiments for a user
     */
    public interface GetSubscribedCallback {
        void onCallback(List<String> subscribed);
    }

    /**
     * Callback after checking if user is subscribed to a particualr experiment
     */
    public interface IsSubscribedCallback {
        void onCallback(Boolean isSubscribed);
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
     * @return String of device user id
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

    /**
     * Gets all subscribed experiments of a user
     *
     * @param userId for the current user
     * @param callback - return method for firestore queries
     */
    public void getSubscribed(String userId, GetSubscribedCallback callback) {
        final List<String>[] subscribed = new List[]{new ArrayList<String>()};
        collRef.document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Log.d(TAG, "Document has been loaded");
                        subscribed[0] = (List<String>) document.get("subscribed");

                    } else {
                        Log.d(TAG, "No such document exists");
                    }

                } else {
                    Log.d(TAG, "Document could not be loaded");
                }
                callback.onCallback(subscribed[0]);
            }
        });
    }

    /**
     * Adds an experiment to a user's subscriptions
     *
     * @param experimentId - experiment to subscribe to
     * @param userId - user's id
     */
    public void subscribeExperiment(String experimentId, String userId) {
        collRef.document(userId).update("subscribed", FieldValue.arrayUnion(experimentId))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be updated" + e.toString());
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Data has been updated");
            }
        });
    }

    /**
     * Removes an experiment from a user's subscriptions
     *
     * @param experimentId - experiment to subscribe to
     * @param userId - user's id
     */
    public void unsubscribeExperiment(String experimentId, String userId) {
        collRef.document(userId).update("subscribed", FieldValue.arrayRemove(experimentId))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be updated" + e.toString());
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Data has been updated");
            }
        });
    }

    /**
     * Checks if a user is subscribed to an experiment
     *
     * @param experimentId - experiment to subscribe to
     * @param userId - user's id
     */
    public void isSubscribed(String experimentId, String userId, IsSubscribedCallback callback) {
        collRef.document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                boolean isSubscribed = false;
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Log.d(TAG, "Document has been loaded");
                        List<String> subscribed = (List<String>) document.get("subscribed");
                        if (subscribed != null) {
                            for (int i = 0; i < subscribed.size(); i++) {
                                if (subscribed.get(i).equals(experimentId)) {
                                    isSubscribed = true;
                                    break;
                                }
                            }
                        }

                    } else {
                        Log.d(TAG, "No such document exists");
                    }

                } else {
                    Log.d(TAG, "Document could not be loaded");
                }
                callback.onCallback(isSubscribed);
            }
        });
    }
}
