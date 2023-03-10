package com.example.catchthecode;

import java.util.Date;
import java.util.UUID;

/* This class is used to create a message object.
 * 
 */

public class modelMessage {
    private Date sendingDate;
    private Date receivingDate;
    private modelUser sender;
    private modelUser receiver;
    private UUID messageID;

    /* This is the default constructor for the modelMessage class.
     * 
     */
    public modelMessage(){
        this.sendingDate = null;
        this.receivingDate = null;
        this.sender = null;
        this.receiver = null;
        this.messageID = null;
    }

    /* This is the constructor for the modelMessage class.
     * 
     * @param sendingDate The date the message was sent.
     * @param receivingDate The date the message was received.
     * @param sender The user who sent the message.
     * @param receiver The user who received the message.
     * @param messageID The unique ID of the message.
     */
    public modelMessage(Date sendingDate, Date receivingDate, modelUser sender, modelUser receiver, UUID messageID){
        this.sendingDate = sendingDate;
        this.receivingDate = receivingDate;
        this.sender = sender;
        this.receiver = receiver;
        this.messageID = messageID;
    }

    /* This method is used to get the sending date of the message.
     * 
     * @return The sending date of the message.
     */
    public Date getSendingDate(){
        return sendingDate;
    }

    /* This method is used to get the receiving date of the message.
     * 
     * @return The receiving date of the message.
     */
    public Date getReceivingDate(){
        return receivingDate;
    }

    /* This method is used to get the sender of the message.
     * 
     * @return The sender of the message.
     */
    public modelUser getSender(){
        return sender;
    }

    /* This method is used to get the receiver of the message.
     * 
     * @return The receiver of the message.
     */
    public modelUser getReceiver(){
        return receiver;
    }

    /* This method is used to get the unique ID of the message.
     * 
     * @return The unique ID of the message.
     */
    public UUID getMessageID(){
        return messageID;
    }

    /* This method is used to set the sending date of the message.
     * 
     * @param sendingDate The sending date of the message.
     */
    public void setSendingDate(Date sendingDate){
        this.sendingDate = sendingDate;
    }

    /* This method is used to set the receiving date of the message.
     * 
     * @param receivingDate The receiving date of the message.
     */
    public void setReceivingDate(Date receivingDate){
        this.receivingDate = receivingDate;
    }

    /* This method is used to set the sender of the message.
     * 
     * @param sender The sender of the message.
     */
    public void setSender(modelUser sender){
        this.sender = sender;
    }

    /* This method is used to set the receiver of the message.
     * 
     * @param receiver The receiver of the message.
     */
    public void setReceiver(modelUser receiver){
        this.receiver = receiver;
    }

    /* This method is used to set the unique ID of the message.
     * 
     * @param messageID The unique ID of the message.
     */
    public void setMessageID(UUID messageID){
        this.messageID = messageID;
    }
}
