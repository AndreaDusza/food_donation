package com.logcats.fooddonation;

import android.content.Context;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by demouser on 8/6/15.
 */
public class DataManager {
    private DataCallback mCallback;
    private static DataManager dm;
    private ArrayList<Offer> allOffers =  new ArrayList<Offer>();
    private ArrayList<User> users =  new ArrayList<User>();
    private Firebase rootRef;

    public ArrayList<Offer> getAllOffers() {
        return allOffers;
    }
    public ArrayList<Offer> getAllActiveOffers() {
        ArrayList<Offer> allActiveOffers =  new ArrayList<Offer>();
        for (Offer o: allOffers){
          //TODO
        }
        return allActiveOffers;
    }

    public ArrayList<Offer> getAllPassiveOffers() {
        ArrayList<Offer> allPassiveOffers =  new ArrayList<Offer>();

        return getAllPassiveOffers();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void registerNewOffer(Offer o){
        rootRef.child("offers/").push().setValue(o);
        Log.d("FB", "New offer registered");
    }

    public User getUserForOffer(Offer offer){
        for (User u : users){
            for (Offer o : u.offers){
                //TODO Use offer ID instead of == to compare
                if (o == offer){
                    return u;
                }
            }
        }
        return null;
    }

    public DataManager(Context context) {
        Firebase.setAndroidContext(context);
        rootRef = new Firebase("https://intense-inferno-9938.firebaseio.com/fooddonation/users/");
        //rootRef = new Firebase("https://intense-inferno-9938.firebaseio.com/fooddonation/");

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
