package com.example.catchthecode;

import android.net.Uri;

public class modelCode {
    private int score;
    private float latitude;
    private float longitude;
    private int hashCode;
    private Uri phto;

    public modelCode(){
        this.score = 0;
        this.latitude = 0;
        this.longitude = 0;
        this.hashCode = 0;
        this.phto = null;
    }

    public modelCode(int score, float latitude, float longitude, int hashCode, Uri photo){
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hashCode = hashCode;
        this.phto = photo;
    }

    public int getScore(){
        return score;
    }

    public float getLatitude(){
        return latitude;
    }

    public float getLongitude(){
        return longitude;
    }

    public int getHashCode(){
        return hashCode;
    }

    public Uri getPhoto(){
        return phto;
    }

    public void setScore(int score){
        this.score = score;
    }

    public void setLatitude(float latitude){
        this.latitude = latitude;
    }

    public void setLongitude(float longitude){
        this.longitude = longitude;
    }

    public void setHashCode(int hashCode){
        this.hashCode = hashCode;
    }

    public void setPhoto(Uri photo){
        this.phto = photo;
    }
}
