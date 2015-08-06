package com.logcats.fooddonation;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by demouser on 8/6/15.
 */
public class Offer implements Serializable {
    private String id;
    private String availabilityTime;
    private Date deactivationDate;
    private  Date postCreationDate;
    private double latitude;
    private double longitude;
    private String title;
    private String description;
    private String picUrl;
    private boolean isActive;

    public Offer() {

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

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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
