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

    /**
     * All subsequent calls to getInstance will return a FirebaseFirestore that uses the local emulator.
     */
    public void useEmulator() {
        usingEmulator = true;
    }

    /**
     * Gets a FirebaseFirestore instance and wires it up to use our local emulator if the useEmulator
     * method has been called.
     * @return a FirebaseFirestore instance
     */
    FirebaseFirestore getInstance() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        if (usingEmulator) {
            // Connecting app to firestore emulator
            // https://firebase.google.com/docs/emulator-suite/connect_and_prototype
            // 10.0.2.2 is the special IP address to connect to the 'localhost' of
            // the host computer from an Android emulator.
            firestore.useEmulator("10.0.2.2", 8080);
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(false)
                    .build();
            firestore.setFirestoreSettings(settings);
        }
        return firestore;
    }
}
