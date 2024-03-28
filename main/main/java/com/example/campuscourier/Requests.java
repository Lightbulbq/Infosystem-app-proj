package com.example.campuscourier;

import java.io.Serializable;

public class Requests implements Serializable {
    private String item, description, imageStorageUri, date, time, location, urgency, userId;

    public Requests() {
    }

    public Requests(String item, String description, String imageStorageUri, String date, String time, String location, String urgency, String userId) {
        this.item = item;
        this.description = description;
        this.imageStorageUri = imageStorageUri;
        this.date = date;
        this.time = time;
        this.location = location;
        this.urgency = urgency;
        this.userId = userId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageStorageUri() {
        return imageStorageUri;
    }

    public void setImageStorageUri(String imageStorageUri) {
        this.imageStorageUri = imageStorageUri;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
