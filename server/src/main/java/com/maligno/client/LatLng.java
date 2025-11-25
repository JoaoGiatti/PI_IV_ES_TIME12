package com.maligno.client;

public class LatLng {
    private double latitude;
    private double longitude;

    public LatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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

    @Override
    public String toString() {
        return this.latitude + ", " + this.longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;

        LatLng other = (LatLng) obj;

        return this.latitude == other.latitude && this.longitude == other.longitude;
    }

    @Override
    public int hashCode() {
        int ret = 1;

        ret = ret * 5 + ((Double)this.latitude).hashCode();
        ret = ret * 5 + ((Double)this.longitude).hashCode();

        if(ret < 0) ret = -ret;
        return ret;
    }
}