package com.logcats.fooddonation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.AuthData;

import java.util.List;


public class DonationListActivity extends Activity  implements AdapterView.OnItemClickListener, DataCallback {

    public static final String ACTION_DONATION_LIST = "donation";
    public static final String ACTION_MANAGE_LIST = "manage";
    private boolean isManageList = true;

    public static final String OFFER_EXTRA_KEY = "offer";
    private ListView mListView;
    private DonationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_list);

        Intent intent = getIntent();
        if (intent != null) {
            isManageList = intent.getAction().equals(ACTION_MANAGE_LIST);
            Log.d("DonationListActivity", "Value isManageList: " + isManageList);
        }

        DataManager dataManager = new DataManager(this);
        dataManager.setCallback(this);

        mAdapter = new DonationAdapter(this);
        mListView = (ListView) findViewById(R.id.donationList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Offer donation = (Offer) mAdapter.getItem(position);

        Intent detailedActivityIntent = new Intent(this, DetailedDonationActivity.class);
        detailedActivityIntent.putExtra(OFFER_EXTRA_KEY, donation);
        startActivity(detailedActivityIntent);
    }

    @Override
    public void onOffersReceived(List<Offer> offers) {
        mAdapter.setData(offers);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUsersReceived(List<User> users) {
        // do nothing
    }

    @Override
    public void onAuthStateChanged(AuthData authData) {
        // Do nothing
    }
}
