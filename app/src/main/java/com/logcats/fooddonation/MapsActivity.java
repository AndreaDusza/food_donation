package com.logcats.fooddonation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements DataCallback {

    public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";
    private SharedPreferences prefs;
    private DataManager dm;
    private boolean loggedIn = false;
    public static String SHARED_PREF_USER_LOGGED_IN = "user_logged_in";
    private String welcome_screen_shown = "welcome_screen_shown";
    private Drawer result;


    public static String USER_MARKER_LOCATION_TITLE = "You are here";
    public static String STARTING_MARKER_LOCATION_TITLE = "Starting Location";
    public static double DEFAULT_LAT = 51.50;
    public static double DEFAULT_LNG = 0;
    public static int MAP_ZOOM = 15;
    public static int DEFAULT_MAP_ZOOM = 5;

    private String userId = "";
    public static String SHARED_PREF_USER_ID = "user_id";



    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    LatLng userLocation;
    Marker userMarker;
    LocationSearch locationSearch;
    Location lastLocation;
    ArrayList<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationSearch = new LocationSearch();
        markers = new ArrayList<>();

        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        locationSearch.getCurrentPosition();
        dm = new DataManager(this);
        dm.setCallback(this);
        prefs = this.getSharedPreferences(PREFERENCES_FILE_NAME, MODE_PRIVATE);

        greetUserAtFirstTime();

        final PrimaryDrawerItem newDonationItem = new PrimaryDrawerItem().withName(R.string.new_donation);
        final PrimaryDrawerItem manageItem = new PrimaryDrawerItem().withName(R.string.manage);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.map_view),
                        new PrimaryDrawerItem().withName(R.string.list_view),
                        newDonationItem,
                        manageItem
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (position == 4) {
                            if (loggedIn) {
                                loggedIn = false;
                                result.removeItem(4);
                                result.addItem(new PrimaryDrawerItem().withName(R.string.action_login));
                                newDonationItem.setEnabled(false);
                                manageItem.setEnabled(false);
                                prefs.edit().putBoolean(SHARED_PREF_USER_LOGGED_IN, loggedIn);
                                prefs.edit().putString(SHARED_PREF_USER_ID, userId);
                                DataManager manager = new DataManager(getApplicationContext());
                                manager.logout();
                                result.closeDrawer();
                            } else {
                                result.removeItem(4);
                                result.addItem(new PrimaryDrawerItem().withName(R.string.action_logout));
                                newDonationItem.setEnabled(true);
                                manageItem.setEnabled(true);
                                loggedIn = true;
                                prefs.edit().putBoolean(SHARED_PREF_USER_LOGGED_IN, loggedIn);
                                prefs.edit().putString(SHARED_PREF_USER_ID, userId);

                                Intent intent = new Intent(MapsActivity.this, UserLoginActivity.class);
                                startActivityForResult(intent, UserLoginActivity.ACTION_LOGIN);

                                result.closeDrawer();
                            }
                        }

                        //TODO: What to do here?
                        if (position==0){
                            Intent i = new Intent(MapsActivity.this, MapsActivity.class);
                            i.putExtra(SHARED_PREF_USER_LOGGED_IN, loggedIn);
                            startActivity(i);
                        } else if (position==1){
                            Intent i = new Intent(MapsActivity.this, DonationListActivity.class);
                            i.putExtra(SHARED_PREF_USER_LOGGED_IN, loggedIn);
                            i.setAction(DonationListActivity.ACTION_DONATION_LIST);
                            startActivity(i);
                        } else if (position==2){
                            Intent i = new Intent(MapsActivity.this, CreateDonationActivity.class);
                            startActivity(i);
                        } else if (position==2){
                            Intent i = new Intent(MapsActivity.this, CreateDonationActivity.class);
                            startActivity(i);
                        } else if (position==3){
                            Intent i = new Intent(MapsActivity.this, DonationListActivity.class);
                            i.setAction(DonationListActivity.ACTION_MANAGE_LIST);
                            startActivity(i);
                        }
                        return true;
                    }
                })
                .build();

        loggedIn = prefs.getBoolean(SHARED_PREF_USER_LOGGED_IN, false);
        userId = prefs.getString(SHARED_PREF_USER_ID, "");
        result.addItem(new PrimaryDrawerItem().withName(R.string.action_login));

        if (!loggedIn) {
            newDonationItem.setEnabled(false);
            manageItem.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null && getSupportFragmentManager()!=null && getSupportFragmentManager().findFragmentById(R.id.map)!=null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        if(userLocation == null) {
            userLocation = new LatLng(DEFAULT_LAT, DEFAULT_LNG);
            userMarker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    .position(userLocation)
                    .title(STARTING_MARKER_LOCATION_TITLE));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,
                    DEFAULT_MAP_ZOOM));
        }
        Log.d("Location", "Marker set: " + userLocation.toString());
    }

    @Override
    public void onOffersReceived(List<Offer> offers) {

        Marker marker;
        for(Offer offer : offers) {
            if(mMap == null) {
                Log.d("Location", "Cannot add markers - null Map");
                return;
            }
            marker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                    .position(new LatLng(offer.getLatitude(), offer.getLongitude()))
                    .title(offer.getTitle()));
            markers.add(marker);
        }
    }

    @Override
    public void onUsersReceived(List<User> users) {
        // do nothing
    }

    @Override
    public void onAuthStateChanged(AuthData authData) {
        if (authData != null) {
            loggedIn = true;
            prefs.edit().putBoolean(SHARED_PREF_USER_LOGGED_IN, loggedIn);
        }
    }

    public class LocationSearch {
        private static final int TWO_MINUTES = 1000 * 60 * 2;
        LatLng userLocation;

        public LatLng getCurrentPosition() {

            // Acquire a reference to the system Location Manager
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            // Define a listener that responds to location updates
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location currentLocation) {

                    if(!locationSearch.isBetterLocation(currentLocation, lastLocation))
                        return;
                    // Called when a new location is found by the network location provider.
                    Log.d("Location", currentLocation.toString());
                    userLocation = new LatLng(currentLocation.getLatitude(),
                            currentLocation.getLongitude());
                    if (mMap != null && lastLocation == null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,
                                MAP_ZOOM));
                    }
                    if (userMarker != null) {
                        Log.d("Location", "Move marker ");
                        userMarker.setPosition(userLocation);
                        userMarker.setTitle(USER_MARKER_LOCATION_TITLE);
                    }
                    lastLocation = currentLocation;
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {}

                public void onProviderEnabled(String provider) {}

                public void onProviderDisabled(String provider) {}
            };

            // Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

            return userLocation;
        }
        /** Determines whether one Location reading is better than the current Location fix
         * @param location  The new Location that you want to evaluate
         * @param currentBestLocation  The current Location fix, to which you want to compare the new one
         */
        protected boolean isBetterLocation(Location location, Location currentBestLocation) {
            if (currentBestLocation == null) {
                // A new location is always better than no location
                return true;
            }

            // Check whether the new location fix is newer or older
            long timeDelta = location.getTime() - currentBestLocation.getTime();
            boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
            boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
            boolean isNewer = timeDelta > 0;

            // If it's been more than two minutes since the current location, use the new location
            // because the user has likely moved
            if (isSignificantlyNewer) {
                return true;
                // If the new location is more than two minutes older, it must be worse
            } else if (isSignificantlyOlder) {
                return false;
            }

            // Check whether the new location fix is more or less accurate
            int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
            boolean isLessAccurate = accuracyDelta > 0;
            boolean isMoreAccurate = accuracyDelta < 0;
            boolean isSignificantlyLessAccurate = accuracyDelta > 200;

            // Check if the old and new location are from the same provider
            boolean isFromSameProvider = isSameProvider(location.getProvider(),
                    currentBestLocation.getProvider());

            // Determine location quality using a combination of timeliness and accuracy
            if (isMoreAccurate) {
                return true;
            } else if (isNewer && !isLessAccurate) {
                return true;
            } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
                return true;
            }
            return false;
        }

        /** Checks whether two providers are the same */
        private boolean isSameProvider(String provider1, String provider2) {
            if (provider1 == null) {
                return provider2 == null;
            }
            return provider1.equals(provider2);
        }
    }


    private void greetUserAtFirstTime() {
        boolean welcomeScreenShown = prefs.getBoolean(welcome_screen_shown, false);
        if (!welcomeScreenShown) {
            showWelcomeScreen();
            prefs.edit().putBoolean(welcome_screen_shown, true).commit();
        }
    }

    private void showWelcomeScreen() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.tutorial);
        // dialog.setTitle(R.string.welcome);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(R.string.tutorial_text);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UserLoginActivity.ACTION_LOGIN) {
            if (resultCode == RESULT_OK) {
                Log.d("MainActivity", "User logged in!");
                User user = (User) data.getSerializableExtra(UserLoginActivity.DATA_USER);
                Log.d("MainActivity", "Got user: " + user.getName());
                userId = user.getId();
                loggedIn = true;
                prefs.edit().putBoolean(SHARED_PREF_USER_LOGGED_IN, loggedIn);
                prefs.edit().putString(SHARED_PREF_USER_ID, userId);
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("MainActivity", "User login was cancelled");
            }
        }
    }

}
