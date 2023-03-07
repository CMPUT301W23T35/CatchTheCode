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

    public ImageView Imageview() {
        return this.qrCodeIV;
    }

    public void setqrName() {

    }

    public String getqrName(){
        return this.qrName;
    }

    public void setQrVR() {

    }

    public String getQrVR(){
        return this.qrVR;
    }
}