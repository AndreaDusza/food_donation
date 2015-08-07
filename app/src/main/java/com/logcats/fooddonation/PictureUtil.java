package com.logcats.fooddonation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.firebase.client.utilities.Base64;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by demouser on 8/7/15.
 */
public class PictureUtil {
    public static void diplayPhoto(Context context, String imageUrl, ImageView imageView) {
        try {
            byte[] data = Base64.decode(imageUrl);
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            Picasso.with(context).load(imageUrl).into(imageView);
        }
    }
}
