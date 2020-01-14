package lcwu.fyp.autocareapp.model;

public class Booking {
    private String userId,providerId,status,type,date,id;
    private Double latitude,longitude;

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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Booking(String userId, String providerId, String status, String type, String date, String id, Double latitude, Double longitude) {
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
}
