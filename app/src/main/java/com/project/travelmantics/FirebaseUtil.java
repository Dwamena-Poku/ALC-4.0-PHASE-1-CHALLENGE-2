package com.project.travelmantics;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseUtil {
    public static FirebaseDatabase myFirebaseDatabase;
    public static DatabaseReference myDatabaseReference;
    private static FirebaseUtil firebaseUtil;
    public static ArrayList<TravelDeal> myDeals;

    private FirebaseUtil(){};

    public static void openFbReference(String ref){
        if(firebaseUtil == null){
            firebaseUtil = new FirebaseUtil();
            myFirebaseDatabase = FirebaseDatabase.getInstance();

        }
        myDeals = new ArrayList<TravelDeal>();
        myDatabaseReference = myFirebaseDatabase.getReference().child(ref);
    }
}
