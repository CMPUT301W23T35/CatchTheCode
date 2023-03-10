package com.example.catchthecode;

import android.location.Location;

/* This class is used to store the location of the QR code.
 * 
 */

public class modelLocation {
    private Location location;
    private modelCode code;

    /* This is the constructor of the class.
     * 
     */
    public modelLocation(){
        this.location = null;
        this.code = null;
    }

    /* This is the constructor of the class.
     * @param location The location of the QR code.
     * @param code The QR code.
     */
    public modelLocation(Location location, modelCode code){
        this.location = location;
        this.code = code;
    }

    /* This method is used to get the location of the QR code.
     * @return The location of the QR code.
     */
    public Location getLocation(){
        return location;
    }

    /* This method is used to get the QR code.
     * @return The QR code.
     */
    public modelCode getCode(){
        return code;
    }

    /* This method is used to set the location of the QR code.
     * @param location The location of the QR code.
     */
    public void setLocation(Location location){
        this.location = location;
    }

    /* This method is used to set the QR code.
     * @param code The QR code.
     */
    public void setCode(modelCode code){
        this.code = code;
    }

}
