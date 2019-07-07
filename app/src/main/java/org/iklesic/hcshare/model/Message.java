package org.iklesic.hcshare.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class Message {

    private String id;
    private String text;
    @ServerTimestamp private Timestamp timestamp;
    private String userFrom;
    private String userTo;
    private String nameFrom;

    public Message() {
    }

    public Message(String id, String text, Timestamp timestamp, String userFrom, String userTo, String nameFrom) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.nameFrom = nameFrom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getUserTo() {
        return userTo;
    }

    public void setUserTo(String userTo) {
        this.userTo = userTo;
    }

    public String getNameFrom() {
        return nameFrom;
    }

    public void setNameFrom(String nameFrom) {
        this.nameFrom = nameFrom;
    }
}
