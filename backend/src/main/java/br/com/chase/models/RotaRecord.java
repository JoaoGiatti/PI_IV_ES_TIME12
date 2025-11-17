package br.com.chase.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RotaRecord {
    private String uid;
    private String userName;
    private String photoUrl;
    private String timeString;
    private double averageSpeed;

    public RotaRecord() {}

    public RotaRecord(String uid, String userName, String photoUrl, String timeString, double averageSpeed) {
        this.uid = uid;
        this.userName = userName;
        this.photoUrl = photoUrl;
        this.timeString = timeString;
        this.averageSpeed = averageSpeed;
    }

    public RotaRecord(String uid, String timeString) {
    }

    // getters e setters
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getTimeString() { return timeString; }
    public void setTimeString(String timeString) { this.timeString = timeString; }

    public double getAverageSpeed() { return averageSpeed; }
    public void setAverageSpeed(double averageSpeed) { this.averageSpeed = averageSpeed; }
}
