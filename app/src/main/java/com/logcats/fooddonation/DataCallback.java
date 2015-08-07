package com.logcats.fooddonation;

import com.firebase.client.AuthData;

import java.util.List;

/**
 * Created by demouser on 8/6/15.
 */
public interface DataCallback {
    void onOffersReceived(List<Offer> offers);
    void onUsersReceived(List<User> users);
    void onAuthStateChanged(AuthData authData);
}
