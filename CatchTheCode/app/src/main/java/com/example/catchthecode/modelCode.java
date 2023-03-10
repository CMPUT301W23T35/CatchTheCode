package com.example.catchthecode;

import android.net.Uri;

/*  This class is used to store the data of the QR code.
 *  It stores the score, the latitude, the longitude, the hash code and the photo.
 */

public class modelCode {
    private int score;
    private float latitude;
    private float longitude;
    private int hashCode;
    private Uri phto;

    /* This is the constructor of the class.
     * 
     */
    public modelCode(){
        this.score = 0;
        this.latitude = 0;
        this.longitude = 0;
        this.hashCode = 0;
        this.phto = null;
    }

    /* This is the constructor of the class.
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

    /*  This method is used to get the score of the QR code.
     *  @return The score of the QR code.
     */
    public int getScore(){
        return score;
    }

    /* This method is used to get the latitude of the QR code.
     * @return The latitude of the QR code.
     */
    public float getLatitude(){
        return latitude;
    }

    /* This method is used to get the longitude of the QR code.
     * @return The longitude of the QR code.
     */
    public float getLongitude(){
        return longitude;
    }

    /* This method is used to get the hash code of the QR code.
     * @return The hash code of the QR code.
     */
    public int getHashCode(){
        return hashCode;
    }

    /* This method is used to get the photo of the QR code.
     * @return The photo of the QR code.
     */
    public Uri getPhoto(){
        return phto;
    }

    /* This method is used to set the score of the QR code.
     * @param score The score of the QR code.
     */
    public void setScore(int score){
        this.score = score;
    }

    /* This method is used to set the latitude of the QR code.
     * @param latitude The latitude of the QR code.
     */
    public void setLatitude(float latitude){
        this.latitude = latitude;
    }

    /* This method is used to set the longitude of the QR code.
     * @param longitude The longitude of the QR code.
     */
    public void setLongitude(float longitude){
        this.longitude = longitude;
    }

    /* This method is used to set the hash code of the QR code.
     * @param hashCode The hash code of the QR code.
     */
    public void setHashCode(int hashCode){
        this.hashCode = hashCode;
    }

    /* This method is used to set the photo of the QR code.
     * @param photo The photo of the QR code.
     */
    public void setPhoto(Uri photo){
        this.phto = photo;
    }
}
