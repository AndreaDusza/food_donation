package com.logcats.fooddonation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;


public class DetailedDonationActivity extends Activity {
    public static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String MESSAGE_TYPE = "message/rfc822";

    private TextView mTitle;
    private ImageView mImageView;
    private TextView mDescription;
    private TextView mPostCreation;
    private TextView mDeactivationDate;
    private TextView mAvailabilityTime;
    private Button mConnectButton;
    private String recipient;
    private boolean isLoggedIn;

    public static final String PREFERENCES_FILE_NAME = "MyAppPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_donation);

        mTitle = (TextView) findViewById(R.id.title);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mDescription = (TextView) findViewById(R.id.description);
        mPostCreation = (TextView) findViewById(R.id.creationDate);
        mDeactivationDate = (TextView) findViewById(R.id.deactivationDate);
        mAvailabilityTime = (TextView) findViewById(R.id.availabilityTime);

        DataManager dataManager = new DataManager(this);

        Intent intent = getIntent();

        if (intent != null) {
            Offer offer = (Offer) intent.getSerializableExtra(DonationListActivity.OFFER_EXTRA_KEY);
            isLoggedIn = intent.getBooleanExtra(MapsActivity.SHARED_PREF_USER_LOGGED_IN, false);

            if (offer != null) {
                mTitle.setText(offer.getTitle());
                mDescription.setText(offer.getDescription());

                SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                mPostCreation.setText(dateFormat.format(offer.getPostCreationDate()));
                mDeactivationDate.setText(dateFormat.format(offer.getDeactivationDate()));
                mAvailabilityTime.setText(getString(R.string.avail_time) + offer.getAvailabilityTime());
                PictureUtil.diplayPhoto(this, offer.getPicUrl(), mImageView);

                User user = dataManager.getUserForOffer(offer);
                if (user != null) {
                    recipient = user.getEmail();
                }
            }
        }

        mConnectButton = (Button) findViewById(R.id.connectButton);
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType(MESSAGE_TYPE);

                if (recipient != null) {
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
                }
                else{
                    intent.putExtra(Intent.EXTRA_EMAIL, "malene.soeholm@gmail.com");
                }

                try {
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DetailedDonationActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (!isLoggedIn) {
            Log.d("Det.DonationAct.", "User not logged in");
            mConnectButton.setVisibility(View.GONE);
        } else {
            Log.d("Det.DonationAct.", "User logged in");
            mConnectButton.setVisibility(View.VISIBLE);
        }

    }
}
