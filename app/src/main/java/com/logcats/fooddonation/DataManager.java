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
    private ArrayList<User> users =  new ArrayList<User>();
    private Firebase usersRootRef;
    private Firebase offersRootRef;

    public void registerNewOffer(Offer o){
        offersRootRef.push().setValue(o);
        Log.d("FB", "New offer registered");
    }

    public User getUserForOffer(Offer offer){
        //TODO
        return null;
    }

    public DataManager(Context context) {
        Firebase.setAndroidContext(context);
        usersRootRef = new Firebase("https://intense-inferno-9938.firebaseio.com/fooddonation/users/");
        offersRootRef = new Firebase("https://intense-inferno-9938.firebaseio.com/fooddonation/offers/");
        offersRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("FB", snapshot.getValue().toString());
                allOffers =  new ArrayList<Offer>();
                for (DataSnapshot offerSnapshot : snapshot.getChildren()) {

                    Offer current = offerSnapshot.getValue(Offer.class);
                    allOffers.add(current);
                }
                if (mCallback != null) {
                    mCallback.onOffersReceived(allOffers);
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
