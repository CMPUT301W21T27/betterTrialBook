package com.example.bettertrialbook.dal;

import android.util.Log;

import com.example.bettertrialbook.models.QRCode;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A DAL for handling any QR interactions with the database.
 */
public class QRDAL {
    private static final String TAG = "QRDAL";
    FirebaseFirestore db = Firestore.getInstance();
    CollectionReference collRef = db.collection("QRCodes");

    /**
     * Registers a QR Code into the database based on its id
     * @param qrCode
     * @param onSuccess
     */
    public void registerQRCode(QRCode qrCode, Runnable onSuccess) {
        collRef.document(qrCode.getId())
                .set(qrCode)
                .addOnSuccessListener(doc -> onSuccess.run());
    }

    /**
     * Creates a listener to check for updates related to a QR
     * @param id
     * @param onSuccess
     * @param onFailure
     */
    public void addQRCodeListener(String id, Callback<QRCode> onSuccess, Runnable onFailure) {
        collRef.document(id).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w(TAG, "Error getting qrCode" + error.toString());
                onFailure.run();
                return;
            }
            QRCode qrCode = value.toObject(QRCode.class);
            onSuccess.execute(qrCode);
        });
    }

    /**
     * Retrieves an Id that hasn't been used yet
     * @return String of unused id
     */
    public String getUnusedId() {
        return collRef.document().getId();
    }
}
