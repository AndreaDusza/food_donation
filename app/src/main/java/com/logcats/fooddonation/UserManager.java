package com.logcats.fooddonation;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

/**
 * Created by malene on 8/6/15.
 */
public class UserManager {

    private final String firebaseUrl;


    public UserManager(String firebaseUrl) {
        this.firebaseUrl = firebaseUrl;

       /* Firebase ref = new Firebase(firebaseUrl);
        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    // user is logged in
                } else {
                    // user is not logged in
                }
            }
        });*/

    }

}
