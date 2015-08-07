package com.logcats.fooddonation;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements DataCallback {
    public static String USER_MARKER_LOCATION_TITLE = "You are here";
    public static String STARTING_MARKER_LOCATION_TITLE = "Starting Location";
    public static double DEFAULT_LAT = 51.50;
    public static double DEFAULT_LNG = 0;
    public static int MAP_ZOOM = 15;
    public static int DEFAULT_MAP_ZOOM = 5;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    LatLng userLocation;
    Marker userMarker;
    LocationSearch locationSearch;
    Location lastLocation;
    ArrayList<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataManager dataManager = new DataManager(this);
        dataManager.setCallback(this);

        locationSearch = new LocationSearch();
        markers = new ArrayList<>();

        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        locationSearch.getCurrentPosition();
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
        if (mMap == null) {
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
}
