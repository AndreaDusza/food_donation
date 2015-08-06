package com.logcats.fooddonation;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by demouser on 8/6/15.
 */
public class DataManager {
    private static DataManager dm;

  /*  public ArrayList<Offer> getAllOffers() {
        return allOffers;
    }

    private ArrayList<Offer> allOffers;
    private ArrayList<Offer> allActiveOffers;
    private ArrayList<User> users;
    private Firebase rootRef;
    //private Firebase rootRef;

    public static DataManager get(){
        return dm;
    }

    public DataManager(Context context) {
        Firebase.setAndroidContext(context);
        allOffers = new ArrayList<Offer>();
        //rootRef = new Firebase("https://intense-inferno-9938.firebaseio.com/fooddonation/users/");
        rootRef = new Firebase("https://intense-inferno-9938.firebaseio.com/fooddonation/users/user1/offers/");

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("FB", snapshot.getValue().toString());

                for (DataSnapshot msgSnapshot : snapshot.getChildren()){
                    Offer current = msgSnapshot.getValue(Offer.class);
                    allOffers.add(current);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("FB", "The read failed: " + firebaseError.getMessage());
            }
        });
    }*/
}
