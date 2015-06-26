package com._42six.claire.commons.model;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * POJO for twitter stats
 */
@SuppressWarnings("unused")
public class TwitterStats {
    private int totalTweets;
    private double percentPositive;
    private double percentNegative;
    private double percentUnknown;

    public int getTotalTweets() {
        return totalTweets;
    }

    public TwitterStats setTotalTweets(int totalTweets) {
        this.totalTweets = totalTweets;
        return this;
    }

    public double getPercentPositive() {
        return round(percentPositive, 1);
    }

    public TwitterStats setPercentPositive(double percentPositive) {
        this.percentPositive = percentPositive;
        return this;
    }

    public double getPercentNegative() {
        return round(percentNegative, 1);
    }

    public TwitterStats setPercentNegative(double percentNegative) {
        this.percentNegative = percentNegative;
        return this;
    }

    public double getPercentUnknown() {
        return round(percentUnknown, 1);
    }

    public TwitterStats setPercentUnknown(double percentUnknown) {
        this.percentUnknown = percentUnknown;
        return this;
    }
    
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
