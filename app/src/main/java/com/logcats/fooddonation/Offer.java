package com.logcats.fooddonation;


import java.sql.Date;

/**
 * Created by demouser on 8/6/15.
 */
public class Offer {
    public String id="";
    public String availabilityTime = new String("");
    public Date deactivationDate = new Date(System.currentTimeMillis());
    public Date postCreationDate= new Date(System.currentTimeMillis());
    public double latitude;
    public double longitude;

    public String title = "";
    public String description ="";
    public String picUrl = "";

    public boolean isActive;
}
