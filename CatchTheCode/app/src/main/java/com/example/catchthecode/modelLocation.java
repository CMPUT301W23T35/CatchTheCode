package com.example.catchthecode;
import android.location.Location;


/* This class is used to store the location of the QR code.
 * 
 */
/**
 This class is used to store the location of the QR code along with the QR code itself.
 */
public class modelLocation {
    private Location location;
    private modelCode code;

    /**
     * Constructs an empty instance of modelLocation.
     */
    public modelLocation(){
        this.location = null;
        this.code = null;
    }

    /**
     * Constructs an instance of modelLocation with a location and a QR code.
     * @param location The location of the QR code.
     * @param code The QR code.
     */
    public modelLocation(Location location, modelCode code){
        this.location = location;
        this.code = code;
    }

    /**
     * Returns the location of the QR code.
     * @return The location of the QR code.
     */
    public Location getLocation(){
        return location;
    }

    /**
     * Returns the QR code.
     * @return The QR code.
     */
    public modelCode getCode(){
        return code;
    }

    /**
     * Sets the location of the QR code.
     * @param location The location of the QR code.
     */
    public void setLocation(Location location){
        this.location = location;
    }

    /**
     * Sets the QR code.
     * @param code The QR code.
     */
    public void setCode(modelCode code){
        this.code = code;
    }

}
