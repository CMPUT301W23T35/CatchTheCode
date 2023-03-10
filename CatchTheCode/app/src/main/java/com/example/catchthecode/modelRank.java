package com.example.catchthecode;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.*;

/* This class is used to create a rank object.
 * 
 */

public class modelRank {
    private ArrayList<modelUser> rankList = new ArrayList<modelUser>();

    /* This is the default constructor for the modelRank class.
     * 
     */
    public modelRank(){
        this.rankList = new ArrayList<modelUser>();
    }

    /* This is the constructor for the modelRank class.
     * 
     * @param rankList The list of users in the rank.
     */
    public modelRank(ArrayList<modelUser> rankList){
        this.rankList = rankList;
    }

    /* This method is used to get the rank list.
     * 
     * @return The rank list.
     */
    public List<modelUser> getRankList(){
        return rankList;
    }

    /* This method is used to set the rank list.
     * 
     * @param rankList The rank list.
     */
    public void setRankList(ArrayList<modelUser> rankList){
        this.rankList = rankList;
    }

    /* This method is used to add a user to the rank list.
     * 
     * @param user The user to be added to the rank list.
     */
    public void sortRankList(){

    }
}
