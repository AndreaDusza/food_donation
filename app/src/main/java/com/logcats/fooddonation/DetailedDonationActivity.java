package com.logcats.fooddonation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DetailedDonationActivity extends Activity {
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_donation);

        mTitle = (TextView) findViewById(R.id.title);

        Intent intent = getIntent();

        if(intent != null) {
            Offer offer = (Offer) intent.getSerializableExtra(DonationListActivity.OFFER_EXTRA_KEY);

            if(offer != null){
                mTitle.setText(offer.getTitle());
            }
        }
    }
}
