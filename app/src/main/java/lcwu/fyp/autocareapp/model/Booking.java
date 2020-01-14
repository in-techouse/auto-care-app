package lcwu.fyp.autocareapp.model;

import java.io.Serializable;

public class Booking implements Serializable {
    private String userId,providerId,status,type,date,id;
    private double latitude,longitude;

    public Booking(String userId, String providerId, String status, String type, String date, String id, double latitude, double longitude) {
        this.userId = userId;
        this.providerId = providerId;
        this.status = status;
        this.type = type;
        this.date = date;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Booking() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
