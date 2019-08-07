package com.project.travelmantics;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {
    public static FirebaseDatabase myFirebaseDatabase;
    public static DatabaseReference myDatabaseReference;
    private static FirebaseUtil firebaseUtil;
    public static FirebaseAuth myFirebaseAuth;
    public static FirebaseStorage myStorage;
    public static StorageReference myStorageRef;
    public static FirebaseAuth.AuthStateListener myAuthListener;
    public static ArrayList<TravelDeal> myDeals;
    private static final int RC_SIGN_IN = 123;
    private static ListActivity caller;
    private FirebaseUtil(){};
    public static boolean isAdmin;

    public static void openFbReference(String ref, final ListActivity callerActivity){
        if(firebaseUtil == null){
            firebaseUtil = new FirebaseUtil();
            myFirebaseDatabase = FirebaseDatabase.getInstance();
            myFirebaseAuth = FirebaseAuth.getInstance();
            caller = callerActivity;

            myAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(firebaseAuth.getCurrentUser() == null){
                        FirebaseUtil.signIn();
                    }
                    else{
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                    Toast.makeText(callerActivity.getBaseContext(), "Welcome back!", Toast.LENGTH_SHORT).show();
                }
            };
            connectStorage();
        }
        myDeals = new ArrayList<TravelDeal>();
        myDatabaseReference = myFirebaseDatabase.getReference().child(ref);
    }

    private static void connectStorage() {
        myStorage = FirebaseStorage.getInstance();
        myStorageRef = myStorage.getReference().child("deals_pictures");
    }

    private static void checkAdmin(String userId) {
        FirebaseUtil.isAdmin=false;
        DatabaseReference ref = myFirebaseDatabase.getReference().child("administrators")
                .child(userId);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtil.isAdmin=true;
                caller.showMenu();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(listener);
    }

    private static void signIn(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(true)
                        //.setLogo(R.drawable.ic_spa_black_24dp)
                        .build(),
                RC_SIGN_IN);
    }

    //create method to attach listener
    public static void attachListener(){
        myFirebaseAuth.addAuthStateListener(myAuthListener);
    }
    //create method to detach listener
    public static void detachListener(){
        myFirebaseAuth.removeAuthStateListener(myAuthListener);
    }
}
