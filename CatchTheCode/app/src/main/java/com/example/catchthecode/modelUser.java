package com.example.catchthecode;

import java.util.ArrayList;
import java.util.List;

public class modelUser {
    private String userID;
    private String userName;
    private String userContact;
    private int totalScore;
    private int numCodes;

    private ArrayList<modelCode> codes = new ArrayList<modelCode>();

    public modelUser(){

    }

    public modelUser(String androidID, String name){
        this.userID = androidID;
        this.userName = name;
    }

    public modelUser(String androidID, String name, String contact){
        this.userID = androidID;
        this.userName = name;
        this.userContact = contact;
    }

    public List<modelCode> getCodes(){
        return codes;
    }

    public String getUserID(){
        return userID;
    }

    public String getUserName(){
        return userName;
    }

    public String getUserContact(){
        return userContact;
    }

    public int getTotalScore(){
        return totalScore;
    }

    public int getNumCodes(){
        return numCodes;
    }

    public void setUserID(String androidID){
        this.userID = androidID;
    }

    public void setUserName(String name){
        this.userName = name;
    }

    public void setUserContact(String contact){
        this.userContact = contact;
    }

    public void setTotalScore(int score){
        this.totalScore = score;
    }

    public void setNumCodes(int num){
        this.numCodes = num;
    }

    public void addCode(modelCode code){
        codes.add(code);
    }

    public void removeCode(modelCode code){
        codes.remove(code);
    }

    public void addCodeScore(int score){
        this.totalScore += score;
    }

    public void addCodeNum(){
        this.numCodes++;
    }
}
