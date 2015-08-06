package com.logcats.fooddonation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    class ViewHolder {
        TextView title;
        TextView postedOn;
        TextView donatorName;
        ImageView foodImage;
    }

    public DonationAdapter(Context context)  {
        mContext = context;
        mDonations = new ArrayList<>();
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
        Picasso.with(mContext).load(currentOffer.getPicUrl()).into(holder.foodImage);

        //TODO: Set the donators name
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
