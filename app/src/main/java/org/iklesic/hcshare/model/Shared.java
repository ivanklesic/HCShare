package org.iklesic.hcshare.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class Shared {

    private String id;
    private String sellerId;
    private String buyerId;
    private String itemId;
    private String itemImage;
    private String itemTitle;
    private boolean isSold;
    private float rating;
    private @ServerTimestamp
    Timestamp timestamp;

    public Shared() {
    }

    public Shared(String id, String sellerId, String buyerId, String itemId, String itemImage, String itemTitle, boolean isSold, Timestamp timestamp) {
        this.id = id;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.itemId = itemId;
        this.itemImage = itemImage;
        this.itemTitle = itemTitle;
        this.isSold = isSold;
        this.timestamp = timestamp;
    }

    public Shared(String id, String sellerId, String buyerId, String itemId, String itemImage, String itemTitle, boolean isSold, float rating, Timestamp timestamp) {
        this.id = id;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.itemId = itemId;
        this.itemImage = itemImage;
        this.itemTitle = itemTitle;
        this.isSold = isSold;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
