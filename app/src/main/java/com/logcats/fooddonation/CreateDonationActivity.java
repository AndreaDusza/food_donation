package com.logcats.fooddonation;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.firebase.client.utilities.Base64;
import java.io.ByteArrayOutputStream;


public class CreateDonationActivity extends Activity implements View.OnClickListener {
    private static final int TAKE_PICTURE_RESPONSE_CODE = 0;
    private Button mOpenCameraButton;
    private Button mSaveButton;
    private EditText mTitle;
    private EditText mDescription;
    private EditText mAvailability;
    private DataManager mDataManager;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_donation);
        mOpenCameraButton = (Button) findViewById(R.id.takePictureButton);
        mTitle = (EditText) findViewById(R.id.title);
        mDescription = (EditText) findViewById(R.id.description);
        mAvailability = (EditText) findViewById(R.id.availabilityTime);
        mSaveButton = (Button) findViewById(R.id.saveButton);

        mSaveButton.setOnClickListener(this);

        mDataManager = new DataManager(this);
        mOpenCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PICTURE_RESPONSE_CODE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        CharSequence title = mTitle.getText();
        CharSequence description = mDescription.getText();
        CharSequence availability = mAvailability.getText();

        Offer offer = new Offer();

        if(title != null) {
            offer.setTitle(title.toString());
        }

        if(description != null) {
            offer.setDescription(description.toString());
        }

        if(availability != null) {
            offer.setAvailabilityTime(availability.toString());
        }

        if(encodedImage != null) {
            offer.setPicUrl(encodedImage);
        }

        mDataManager.registerNewOffer(offer);

        startActivity(new Intent(this, DonationListActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE_RESPONSE_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, output);
                byte[] array = output.toByteArray();
                encodedImage = Base64.encodeBytes(array);
            }
        }
    }
}
