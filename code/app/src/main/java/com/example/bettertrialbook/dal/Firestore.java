package com.example.bettertrialbook.dal;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

/**
 * This class encapsulates access to FirebaseFirestore instances.
 * This way, we can easily switch our entire application between the real cloud firestore and a
 * local emulator used for testing.
 */
public class Firestore {
    private static boolean usingEmulator = false;
    private static boolean firestoreSet = false;
    private static FirebaseFirestore firestore;

    /**
     * All subsequent calls to getInstance will return a FirebaseFirestore that uses the local emulator.
     */
    public static void useEmulator() {
        usingEmulator = true;
    }

    /**
     * Gets a FirebaseFirestore instance and wires it up to use our local emulator if the useEmulator
     * method has been called.
     * @return a FirebaseFirestore instance
     */
    public static FirebaseFirestore getInstance() {

        if(!firestoreSet){

            firestore = FirebaseFirestore.getInstance();
            firestoreSet=true;

            if (usingEmulator) {
                // Connecting app to firestore emulator
                // Google Firebase, 2021-04-08, Apache 2.0, https://firebase.google.com/docs/emulator-suite/connect_and_prototype
                // 10.0.2.2 is the special IP address to connect to the 'localhost' of
                // the host computer from an Android emulator.
                firestore.useEmulator("10.0.2.2", 8080);
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(false)
                        .build();
                firestore.setFirestoreSettings(settings);
            }
        }
        return firestore;
    }
}
