package com.example.catchthecode;

import java.util.ArrayList;
import java.util.List;

/**

 The modelUser class is used to store the user's information including their ID, name, contact information,
 total score, and the list of codes that they have scanned. The class provides methods to get and set these
 attributes as well as to add and remove codes from the list and update the user's total score and number of
 scanned codes.
 */

public class modelUser {
    private String userID;
    private String userName;
    private String userContact;
    private int totalScore;
    private int numCodes;

    private ArrayList<modelCode> codes = new ArrayList<modelCode>();

    /**
     * Default constructor for the modelUser class.
     */
    public modelUser(){

    }

    /**
     * Constructor for the modelUser class.
     * @param androidID - the android ID of the user.
     * @param name - the name of the user.
     */
    public modelUser(String androidID, String name){
        this.userID = androidID;
        this.userName = name;
    }

    /**
     * Constructor for the modelUser class.
     * @param androidID - the android ID of the user.
     * @param name - the name of the user.
     * @param contact - the contact information of the user.
     */
    public modelUser(String androidID, String name, String contact){
        this.userID = androidID;
        this.userName = name;
        this.userContact = contact;
    }

    /**
     * This method returns the list of codes that the user has scanned.
     * @return codes - the list of codes that the user has scanned.
     */

    public List<modelCode> getCodes(){
        return codes;
    }

    /**
     * This method returns the android ID of the user.
     * @return userID - the android ID of the user.
     */
    public String getUserID(){
        return userID;
    }

    /**
     * This method returns the name of the user.
     * @return userName - the name of the user.
     */
    public String getUserName(){
        return userName;
    }

    /**
     * This method returns the contact information of the user.
     * @return userContact - the contact information of the user.
     */
    public String getUserContact(){
        return userContact;
    }

    /**
     * This method returns the total score of the user.
     * @return totalScore - the total score of the user.
     */
    public int getTotalScore(){
        return totalScore;
    }

    /**
     * This method returns the number of codes that the user has scanned.
     * @return numCodes - the number of codes that the user has scanned.
     */
    public int getNumCodes(){
        return numCodes;
    }

    /**
     * This method sets the android ID of the user.
     * @param androidID - the android ID of the user.
     */
    public void setUserID(String androidID){
        this.userID = androidID;
    }

    /**
     * This method sets the name of the user.
     * @param name - the name of the user.
     */
    public void setUserName(String name){
        this.userName = name;
    }

    /**
     * This method sets the contact information of the user.
     * @param contact - the contact information of the user.
     */
    public void setUserContact(String contact){
        this.userContact = contact;
    }

    /**
     * This method sets the total score of the user.
     * @param score - the total score of the user.
     */
    public void setTotalScore(int score){
        this.totalScore = score;
    }

    /**
     * This method sets the number of codes that the user has scanned.
     *  @param num - the number of codes that the user has scanned
     */
    public void setNumCodes(int num){
        this.numCodes = num;
    }

    /**
     * This method adds a code to the list of codes that the user has scanned.
     *  @param code - the code to be added to the list of codes that the user has scanned
     */
    public void addCode(modelCode code){
        codes.add(code);
    }

    /**
     * This method removes a code from the list of codes that the user has scanned.
     *  @param code - the code to be removed from the list of codes that the user has scanned
     */
    public void removeCode(modelCode code){
        codes.remove(code);
    }

    /**
     * This method adds a score to the total score of the user.
     *  @param score - the score to be added to the total score of the user
     */
    public void addCodeScore(int score){
        this.totalScore += score;
    }

    /**
     * This method adds one to the number of codes that the user has scanned.
     */
    public void addCodeNum(){
        this.numCodes++;
    }
}
