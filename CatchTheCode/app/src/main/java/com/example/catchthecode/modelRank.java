package com.example.catchthecode;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.*;

public class modelRank {
    private ArrayList<modelUser> rankList = new ArrayList<modelUser>();

    public modelRank(){
        this.rankList = new ArrayList<modelUser>();
    }

    public modelRank(ArrayList<modelUser> rankList){
        this.rankList = rankList;
    }

    public List<modelUser> getRankList(){
        return rankList;
    }

    public void setRankList(ArrayList<modelUser> rankList){
        this.rankList = rankList;
    }

    public void sortRankList(){

    }
}
