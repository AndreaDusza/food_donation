package com.logcats.fooddonation;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by demouser on 8/6/15.
 */

public class Offer implements Serializable {
    public static final String NO_IMG_AVAILABLE = "https://lh5.googleusercontent.com/-tHVczjX7COs/AAAAAAAAAAI/AAAAAAAA0Cg/0_Sd9gpSpSI/photo.jpg";
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
    private boolean isActive;

    public Offer() {
        userId="";
        id="";
        availabilityTime="";
        postCreationDate = new Date(System.currentTimeMillis());
        deactivationDate = new Date(System.currentTimeMillis());
        latitude=0;
        longitude=0;
        title="";
        description="";
        picUrl=NO_IMG_AVAILABLE;
        isActive=true;
    }

    public Offer(String availabilityTime, String title, String description, String picUrl) {
        this.availabilityTime = availabilityTime;
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        isActive = true;
        postCreationDate = new Date();
        deactivationDate = new Date();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
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

    public boolean isPicturePresent() {
        return picUrl != null && !picUrl.equals("");
    }
}
