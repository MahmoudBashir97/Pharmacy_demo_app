package com.mahmoudbashir.pharmacy_app.models;

public class Messages {

    private String From,Message,Type,to,messageID,time,date,name;

    public Messages() {
    }

    public String getFrom() {
        return this.From;
    }

    public void setFrom(final String from) {
        this.From = from;
    }

    public String getMessage() {
        return this.Message;
    }

    public void setMessage(final String message) {
        this.Message = message;
    }

    public String getType() {
        return this.Type;
    }

    public void setType(final String type) {
        this.Type = type;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(final String to) {
        this.to = to;
    }

    public String getMessageID() {
        return this.messageID;
    }

    public void setMessageID(final String messageID) {
        this.messageID = messageID;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(final String time) {
        this.time = time;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
