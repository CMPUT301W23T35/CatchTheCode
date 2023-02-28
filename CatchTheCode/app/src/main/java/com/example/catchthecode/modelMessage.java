package com.example.catchthecode;

import java.util.Date;
import java.util.UUID;

public class modelMessage {
    private Date sendingDate;
    private Date receivingDate;
    private modelUser sender;
    private modelUser receiver;
    private UUID messageID;

    public modelMessage(){
        this.sendingDate = null;
        this.receivingDate = null;
        this.sender = null;
        this.receiver = null;
        this.messageID = null;
    }

    public modelMessage(Date sendingDate, Date receivingDate, modelUser sender, modelUser receiver, UUID messageID){
        this.sendingDate = sendingDate;
        this.receivingDate = receivingDate;
        this.sender = sender;
        this.receiver = receiver;
        this.messageID = messageID;
    }

    public Date getSendingDate(){
        return sendingDate;
    }

    public Date getReceivingDate(){
        return receivingDate;
    }

    public modelUser getSender(){
        return sender;
    }

    public modelUser getReceiver(){
        return receiver;
    }

    public UUID getMessageID(){
        return messageID;
    }

    public void setSendingDate(Date sendingDate){
        this.sendingDate = sendingDate;
    }

    public void setReceivingDate(Date receivingDate){
        this.receivingDate = receivingDate;
    }

    public void setSender(modelUser sender){
        this.sender = sender;
    }

    public void setReceiver(modelUser receiver){
        this.receiver = receiver;
    }

    public void setMessageID(UUID messageID){
        this.messageID = messageID;
    }
}
