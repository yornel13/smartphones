package com.personal.yornel.androids.model;

/**
 * Created by Yornel on 28/9/2017.
 */

public class Rating {

    private String accountId;
    private String smartphoneId;
    private String smartphoneUrl;
    private Double rating;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSmartphoneId() {
        return smartphoneId;
    }

    public void setSmartphoneId(String smartphoneId) {
        this.smartphoneId = smartphoneId;
    }

    public String getSmartphoneUrl() {
        return smartphoneUrl;
    }

    public void setSmartphoneUrl(String smartphoneUrl) {
        this.smartphoneUrl = smartphoneUrl;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
