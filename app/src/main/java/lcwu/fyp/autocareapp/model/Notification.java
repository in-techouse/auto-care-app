package lcwu.fyp.autocareapp.model;

import java.io.Serializable;

public class Notification implements Serializable {
    private String id,userId,providerId,bookingId,message,date , providerText , userText;
    private boolean read;
    public Notification() {
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getProviderText() {
        return providerText;
    }

    public void setProviderText(String driverText) {
        this.providerText = providerText;
    }

    public String getUserText() {
        return userText;
    }

    public void setUserText(String userText) {
        this.userText = userText;
    }



    public Notification(String id, String userId, String providerId, String bookingId, String message, String date, boolean read) {
        this.id = id;
        this.userId = userId;
        this.providerId = providerId;
        this.bookingId = bookingId;
        this.message = message;
        this.date = date;
        this.read = read;
    }

}
