package com.example.catchthecode;

import android.net.Uri;

/**
 This class represents the data of a QR code, including the score, latitude, longitude, hash code, and photo.
 */
public class modelCode {
    private int score;
    private float latitude;
    private float longitude;
    private int hashCode;
    private Uri phto;

    /**
     * Default constructor for the modelCode class.
     * Initializes all member variables to zero or null.
     */
    public modelCode(){
        this.score = 0;
        this.latitude = 0;
        this.longitude = 0;
        this.hashCode = 0;
        this.phto = null;
    }

    /**
     * Constructor for the modelCode class that takes in initial values for all member variables.
     * @param score The score of the QR code.
     * @param latitude The latitude of the QR code.
     * @param longitude The longitude of the QR code.
     * @param hashCode The hash code of the QR code.
     * @param photo The photo of the QR code.
     */
    public modelCode(int score, float latitude, float longitude, int hashCode, Uri photo){
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hashCode = hashCode;
        this.phto = photo;
    }

    /**
     * Retrieves the score of the QR code.
     * @return The score of the QR code.
     */
    public int getScore(){
        return score;
    }

    /**
     * Retrieves the latitude of the QR code.
     * @return The latitude of the QR code.
     */
    public float getLatitude(){
        return latitude;
    }

    /**
     * Retrieves the longitude of the QR code.
     * @return The longitude of the QR code.
     */
    public float getLongitude(){
        return longitude;
    }

    /**
     * Retrieves the hash code of the QR code.
     * @return The hash code of the QR code.
     */
    public int getHashCode(){
        return hashCode;
    }

    /**
     * Retrieves the photo of the QR code.
     * @return The photo of the QR code.
     */
    public Uri getPhoto(){
        return phto;
    }

    /**
     * Sets the score of the QR code.
     * @param score The score of the QR code.
     */
    public void setScore(int score){
        this.score = score;
    }

    /**
     * Sets the latitude of the QR code.
     * @param latitude The latitude of the QR code.
     */
    public void setLatitude(float latitude){
        this.latitude = latitude;
    }

    /**
     * Sets the longitude of the QR code.
     * @param longitude The longitude of the QR code.
     */
    public void setLongitude(float longitude){
        this.longitude = longitude;
    }

    /**
     * Sets the hash code of the QR code.
     * @param hashCode The hash code of the QR code.
     */
    public void setHashCode(int hashCode){
        this.hashCode = hashCode;
    }

    /**
     * Sets the photo of the QR code.
     * @param photo The photo of the QR code.
     */
    public void setPhoto(Uri photo){
        this.phto = photo;
    }
}
