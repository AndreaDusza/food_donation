package com.logcats.fooddonation;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by demouser on 8/7/15.
 */
public class MarkerOffer {

    private Offer offer;
    private Marker marker;

    public MarkerOffer(Offer offer, Marker marker) {

        this.offer = offer;
        this.marker = marker;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
