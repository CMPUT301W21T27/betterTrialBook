/*
Currently not implemented.
 */

package com.example.bettertrialbook.dal;

import android.util.Log;

import com.example.bettertrialbook.models.QRCode;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class QRDAL {
    private static final String TAG = "QRDAL";
    FirebaseFirestore db = Firestore.getInstance();
    CollectionReference collRef = db.collection("QRCodes");

    public void registerQRCode(QRCode qrCode, Runnable onSuccess) {
        collRef.document(qrCode.getId())
                .set(qrCode)
                .addOnSuccessListener(aVoid -> onSuccess.run());
    }

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
}
