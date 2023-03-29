package com.example.catchthecode;


import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.firestore.Exclude;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
/**
 * represent the QR code and correpsonding information
 */
public class QRcode {
    private String url;

    private ImageView qrCodeIV;

    private String qrName;

    private String qrVR;

    private int Score;

    private String latitude;

    private String longitude;

    private Bitmap image;

    /**
     Constructs a new QRcode object with the specified URL and image view.
     Generates the QR code's name, VR, score, and image view.
     @param url the URL to encode in the QR code
     @param qrCodeIV the ImageView in which to display the QR code
     @throws NoSuchAlgorithmException if SHA-256 algorithm is not available
     */
    public QRcode(String url, ImageView qrCodeIV) throws NoSuchAlgorithmException {
        this.url = url;
        this.qrCodeIV = qrCodeIV;
        setScore();
        setqrName();
        setQrVR();
        setImageview();
        setLocation("noLat", "noLon");
    }

    /**

     Constructs a new QRcode object with the specified URL.
     Generates the QR code's name, VR, and score.
     @param url the URL to encode in the QR code
     @throws NoSuchAlgorithmException if SHA-256 algorithm is not available
     */
    public QRcode(String url) throws NoSuchAlgorithmException {
        this.url = url;
        setScore();
        setqrName();
        setQrVR();
        setLocation("noLat", "noLon");
    }
    /**

     Sets the image view of the QR code by generating a new QR code image
     with the specified URL and dimensions.
     */
    public void setImageview() {
        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        QRGEncoder qrgEncoder = new QRGEncoder(this.url, null, QRGContents.Type.TEXT, 810);
        qrgEncoder.setColorBlack(Color.BLACK);
        qrgEncoder.setColorWhite(Color.WHITE);
        // Getting QR-Code as Bitmap
        Bitmap bitmap = qrgEncoder.getBitmap(0);
        // Setting Bitmap to ImageView
        this.qrCodeIV.setImageBitmap(bitmap);
    }

    /**

     Returns the image view of the QR code.
     @return the ImageView object representing the QR code
     */
    public ImageView getImageview() {
        return this.qrCodeIV;
    }

    /**

     Sets the name of the QR code by generating a string from the hash of the URL.
     */
    public void setqrName() {
        int hash = this.gethash();
        this.qrName = "";
        this.qrName += ((hash>>9) %2 == 0) ? "cool":"hot";
        this.qrName += ((hash>>8) %2 == 0) ? "Fro": "Glo";
        this.qrName += ((hash>>7) %2 == 0) ? "Mo": "Lo";
        this.qrName += ((hash>>6) %2 == 0) ? "Mega":"Ultra";
        this.qrName += ((hash>>5) %2 == 0) ? "Spectral":"Sonic";
        this.qrName += ((hash>>4) %2 == 0) ? "Crab":"Shark";
    }

    /**

     Returns the name of the QR code.
     @return the name of the QR code as a string
     */
    public String getqrName(){
        return this.qrName;
    }
    /**

     Set the ASCII art representation of the QR code.
     */
    public void setQrVR() {
        int hash = this.gethash();
        this.qrVR = "";
        this.qrVR += ((hash>>9) %2 == 0) ? "   ____\n  /    \\\n": "  ______\n  |     |\n";
        this.qrVR += ((hash>>8) %2 == 0) ? " | _  _ |\n": " |      | \\n";
        this.qrVR += ((hash>>7) %2 == 0) ? "\\| ,  , |/\n": "\\| o  o |/\n";
        this.qrVR += ((hash>>6) %2 == 0) ? "@|      |@\n":"&|      |&\n";
        this.qrVR += ((hash>>5) %2 == 0) ? "/| ^  ^ |\\\n":" |  ``  |\n";
        this.qrVR += ((hash>>4) %2 == 0) ? " | `--` |\n":" | .--. |\n";
        this.qrVR += ((hash>>9) %2 == 0) ? "  \\____/\n": "  |____|\n";
    }

    /**

     Get the ASCII art representation of the QR code.
     @return ASCII art representation of the QR code.
     */
    public String getQrVR(){
        return this.qrVR;
    }

    /**

     Set the score of the QR code based on its URL.
     @throws NoSuchAlgorithmException if SHA-256 algorithm is not available.
     */
    public void setScore() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(
                (this.url+'\n').getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
        for (int i = 0; i < encodedhash.length; i++) {
            String hex = Integer.toHexString(0xff & encodedhash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        this.Score = 0;
        String s = hexString.toString();
        int sLen =  s.length();
        for (int i = 0; i < sLen-1; i++){
            if (s.charAt(i) == s.charAt(i+1) || s.charAt(i) == '0'){
                int j = 0;
                while (s.charAt(i) == s.charAt(i+j+1)){
                    j++;
                }
                Score += convertScore(s.charAt(i), j);
                i += j;
            }
        }
        Log.d(TAG, "setScore: " + String.valueOf(this.Score));

    }

    /**

     Convert the score based on hash.
     @param c character in hexadecimal representation of the hash.
     @param l number of occurrences of the character in the hash.
     @return score of the character based on its frequency in the hash.
     */
    public int convertScore(char c, int l) {
        switch (c) {
            case '0':
                return (int) Math.pow(20, l);
            case '1':
                return (int) Math.pow(1, l);
            case '2':
                return (int) Math.pow(2, l);
            case '3':
                return (int) Math.pow(3, l);
            case '4':
                return (int) Math.pow(4, l);
            case '5':
                return (int) Math.pow(5, l);
            case '6':
                return (int) Math.pow(6, l);
            case '7':
                return (int) Math.pow(7, l);
            case '8':
                return (int) Math.pow(8, l);
            case '9':
                return (int) Math.pow(9, l);
            case 'a':
                return (int) Math.pow(10, l);
            case 'b':
                return (int) Math.pow(11, l);
            case 'c':
                return (int) Math.pow(12, l);
            case 'd':
                return (int) Math.pow(13, l);
            case 'e':
                return (int) Math.pow(14, l);
            case 'f':
                return (int) Math.pow(15, l);
        }
        return 0;
    }

    /**

     Returns the current score of the QR code.
     @return the score of the QR code
     */
    public int getqrScore() {
        return this.Score;
    }

    /**

     Calculates and returns a hash code based on the URL of the QR code.
     @return the hash code of the QR code
     */
    public int gethash(){
        int hash = 7;
        for (int i = 0; i < this.url.length(); i++) {
            hash = hash * 31 + this.url.charAt(i);
            hash = hash % 1001;
        }
        return hash;
    }

    public void setLocation(String lat, String lon){
        this.latitude = lat;
        this.longitude = lon;
    }

    public String getLatitude(){
        return this.latitude;
    }

    public String getLongitude(){
        return this.longitude;
    }

    public void setImage(Bitmap image){
        this.image = image;
    }

    public Bitmap getImage(){
        return this.image;
    }

    @Exclude
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("url", this.url);
        //result.put("ImageView", this.qrCodeIV);
        result.put("readable_name", this.qrName);
        result.put("repr", this.qrVR);
        result.put("score", this.Score);
        result.put("latitude", this.latitude);
        result.put("longitude", this.longitude);
        //result.put("image", this.image);

        return result;
    }
}
