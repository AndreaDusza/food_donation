package com.logcats.fooddonation;

import android.content.Context;
import com.firebase.client.utilities.Base64;

import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by demouser on 8/6/15.
 */
public class DonationAdapter extends BaseAdapter {
    public static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    private Context mContext;
    private List<Offer> mDonations;
    private DataManager mManager;

    class ViewHolder {
        TextView title;
        TextView postedOn;
        TextView donatorName;
        ImageView foodImage;
    }

    public DonationAdapter(Context context, DataManager manager)  {
        mContext = context;
        mDonations = new ArrayList<>();
        mManager = manager;
    }

    @Override
    public int getCount() {
        return mDonations.size();
    }

    @Override
    public Object getItem(int position) {
        return mDonations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder = null;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.donation_item_view, null);

            holder = new ViewHolder();
            holder.donatorName = (TextView) view.findViewById(R.id.donator);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.postedOn = (TextView) view.findViewById(R.id.postedOn);
            holder.foodImage = (ImageView) view.findViewById(R.id.image);

            view.setTag(holder);
        } else {
            view  = convertView;
            holder = (ViewHolder) view.getTag();
        }

        Offer currentOffer = mDonations.get(position);

        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        holder.postedOn.setText(dateFormat.format(currentOffer.getPostCreationDate()));
        holder.title.setText(currentOffer.getTitle());
        PictureUtil.diplayPhoto(mContext, currentOffer.getPicUrl(), holder.foodImage);

        User user = mManager.getUserForOffer(currentOffer);
        if(user != null) {
            holder.donatorName.setText(user.getName());
        }

        return view;
    }

    public void clear() {
        mDonations = new ArrayList<>();
    }

    public void add(Offer donation) {
        mDonations.add(donation);
    }

    public void setData(List<Offer> donations) {
        mDonations = donations;
    }
}
