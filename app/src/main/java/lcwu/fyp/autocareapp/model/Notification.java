package lcwu.fyp.autocareapp.model;

import java.io.Serializable;

public class Notification implements Serializable {
    private String id,userId,providerId,bookingId,message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Notification(String id, String userId, String providerId, String bookingId, String message) {
        this.id = id;
        this.userId = userId;
        this.providerId = providerId;
        this.bookingId = bookingId;
        this.message = message;
    }

    public Notification() {
    }
}
