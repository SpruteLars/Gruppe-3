package no.hiof.emilie.efinder.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseAccessModel {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference eventdataReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    public FirebaseAccessModel() {
    }

    public FirebaseDatabase getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public DatabaseReference getEventdataReference() {
        return eventdataReference;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public FirebaseAuth.AuthStateListener getFirebaseAuthStateListener() {
        return firebaseAuthStateListener;
    }
}
