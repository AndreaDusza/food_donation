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
    private DataCallback mCallback;
    private static DataManager dm;
    private ArrayList<Offer> allOffers =  new ArrayList<Offer>();
    private ArrayList<Offer> allActiveOffers =  new ArrayList<Offer>();
    private ArrayList<User> users =  new ArrayList<User>();
    private Firebase rootRef;

    public static DataManager get(){
        return dm;
    }

    public ArrayList<Offer> getAllOffers() {
        return allOffers;
    }
    public ArrayList<Offer> getAllActiveOffers() {
        return allActiveOffers;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public DataManager(Context context) {
        Firebase.setAndroidContext(context);

        allOffers = new ArrayList<Offer>();
        rootRef = new Firebase("https://intense-inferno-9938.firebaseio.com/fooddonation/users/");

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("FB", snapshot.getValue().toString());

                for (DataSnapshot usersSnapshot : snapshot.getChildren()) {
                    User user = new User();
                    for (DataSnapshot offersSnapshot : usersSnapshot.getChildren()) {
                        for (DataSnapshot oneOfferSnapshot : offersSnapshot.getChildren()) {
                            Offer current = oneOfferSnapshot.getValue(Offer.class);
                            allOffers.add(current);
                            user.offers.add(current);
                            if (current.isActive == true) {
                                allActiveOffers.add(current);
                            }
                        }
                    }

                    users.add(user);
                    if (mCallback != null) {
                        mCallback.onOffersReceived(allOffers);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("FB", "The read failed: " + firebaseError.getMessage());
            }
        });
    }

    public void setCallback(DataCallback callback) {
        mCallback = callback;
    }
}
