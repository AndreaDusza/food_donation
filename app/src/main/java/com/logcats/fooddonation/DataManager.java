package com.logcats.fooddonation;

import android.content.Context;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.plus.Plus;

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

    public ArrayList<Offer> getAllOffers() {
        return allOffers;
    }

    public void registerNewOffer(Offer o){
        offersRootRef.push().setValue(o);
        Log.d("FB", "New offer registered");
    }

    public void registerNewUser(User user){
        boolean userExists = false;
        for (User u: users){
            if (u.getId().equals(user.getId())){
                userExists = true;
                break;
            }
        }
        if (!userExists) {
            usersRootRef.push().setValue(user);
            Log.d("FB", "New user registered");
        }
    }

    public User getUserForOffer(Offer offer){
        String userId = offer.getUserId();
        for (User u : users){
            if (userId.equals(u.getId())){
                return u;
            }
        }
        return null;
    }

    public DataManager(Context context) {
        Firebase.setAndroidContext(context);
        usersRootRef = new Firebase("https://intense-inferno-9938.firebaseio.com/fooddonation/users/");
        offersRootRef = new Firebase("https://intense-inferno-9938.firebaseio.com/fooddonation/offers/");
        offersRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                allOffers =  new ArrayList<Offer>();
                if (snapshot.getValue() != null) {
                    Log.d("FB", snapshot.getValue().toString());
                    for (DataSnapshot offerSnapshot : snapshot.getChildren()) {

                        Offer current = offerSnapshot.getValue(Offer.class);
                        allOffers.add(current);
                    }
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

        usersRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                users =  new ArrayList<User>();
                if (snapshot.getValue() != null) {
                    Log.d("FB", snapshot.getValue().toString());
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                        User current = userSnapshot.getValue(User.class);
                        users.add(current);
                    }
                }
                if (mCallback != null) {
                    mCallback.onUsersReceived(users);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("FB", "The read failed: " + firebaseError.getMessage());
            }
        });

        usersRootRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                Log.d("DatabaseManager", "Authentication state changed");
                if (mCallback != null) {
                    mCallback.onAuthStateChanged(authData);
                }
            }
        });
    }

    public void setCallback(DataCallback callback) {
        mCallback = callback;
    }

    public void logout() {
        usersRootRef.unauth();
    }
}
