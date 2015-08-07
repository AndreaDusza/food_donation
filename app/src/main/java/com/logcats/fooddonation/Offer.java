package com.logcats.fooddonation;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

/**
 * Created by demouser on 8/6/15.
 */
public class Offer implements Serializable{
    public static final String NO_IMG_AVAILABLE = "http://properties.halpernent.com/property/images/No_Image.png";

    private String userId;
    private String id;
    private String availabilityTime;
    private Date deactivationDate;
    private Date postCreationDate;
    private double latitude;
    private double longitude;
    private String title;
    private String description;
    private String picUrl;
    private boolean active;

    public Offer() {
        userId="";
        id=new BigInteger(130, new SecureRandom()).toString(32);
        availabilityTime="";
        postCreationDate = new Date(System.currentTimeMillis());
        deactivationDate = new Date(System.currentTimeMillis());
        latitude=0;
        longitude=0;
        title="";
        description="";
        picUrl=NO_IMG_AVAILABLE;
        active=true;
    }

    public Offer(String availabilityTime, String title, String description, String picUrl) {
        this.availabilityTime = availabilityTime;
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        active = true;
        postCreationDate = new Date();
        deactivationDate = new Date();
        id=new BigInteger(130, new SecureRandom()).toString(32);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvailabilityTime() {
        return availabilityTime;
    }

    public void setAvailabilityTime(String availabilityTime) {
        this.availabilityTime = availabilityTime;
    }

    public Date getDeactivationDate() {
        return deactivationDate;
    }

    public void setDeactivationDate(Date deactivationDate) {
        this.deactivationDate = deactivationDate;
    }

    public Date getPostCreationDate() {
        return postCreationDate;
    }

    public void setPostCreationDate(Date postCreationDate) {
        this.postCreationDate = postCreationDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
