package com.logcats.fooddonation;

import java.util.List;

/**
 * Created by demouser on 8/6/15.
 */
public interface DataCallback {
    void onOffersReceived(List<Offer> offers);
}
