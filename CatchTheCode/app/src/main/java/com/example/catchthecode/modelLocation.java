package com.example.catchthecode;

import android.location.Location;

public class modelLocation {
    private Location location;
    private modelCode code;

    public modelLocation(){
        this.location = null;
        this.code = null;
    }

    public modelLocation(Location location, modelCode code){
        this.location = location;
        this.code = code;
    }

    public Location getLocation(){
        return location;
    }

    public modelCode getCode(){
        return code;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public void setCode(modelCode code){
        this.code = code;
    }

}
