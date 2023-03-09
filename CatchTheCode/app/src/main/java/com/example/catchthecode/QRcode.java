package com.example.catchthecode;



import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QRcode {

    private String url;

    private ImageView qrCodeIV;

    private String qrName;

    private String qrVR;

    private int Score;

    public QRcode(String url, ImageView qrCodeIV) {
        this.url = url;
        this.qrCodeIV = qrCodeIV;
    }

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

    public ImageView getImageview() {
        setImageview();
        return this.qrCodeIV;
    }

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

    public String getqrName(){
        return this.qrName;
    }

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

    public String getQrVR(){
        return this.qrVR;
    }

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

    }

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
    
    public int getqrScore() {
        return this.Score;
    }

}