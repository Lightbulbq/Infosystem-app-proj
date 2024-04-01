package com.example.campuscourier;

import android.annotation.SuppressLint;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirebaseHelper extends Home{

    static FirebaseAuth mAuth;
    @SuppressLint("StaticFieldLeak")
    static FirebaseFirestore db;

    public static void getPosts(ArrayList<Requests> requestsArrayList, RequestAdapter requestAdapter){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        // below line is use to get the data from Firebase Firestore.
        db.collection("users").document(userId).collection("posts").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are
                            // hiding our progress bar and adding
                            // our data in a list.
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.
                                Requests r = d.toObject(Requests.class);

                                // and we will pass this object class
                                // inside our arraylist which we have
                                // created for recycler view.
                                requestsArrayList.add(r);
                            }
                            // after adding the data to recycler view.
                            // we are calling recycler view notifyDataSetChanged
                            // method to notify that data has been changed in recycler view.
                            requestAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
