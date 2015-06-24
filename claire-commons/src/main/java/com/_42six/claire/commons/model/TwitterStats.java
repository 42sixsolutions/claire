package com._42six.claire.commons.model;

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
        return percentPositive;
    }

    public TwitterStats setPercentPositive(double percentPositive) {
        this.percentPositive = percentPositive;
        return this;
    }

    public double getPercentNegative() {
        return percentNegative;
    }

    public TwitterStats setPercentNegative(double percentNegative) {
        this.percentNegative = percentNegative;
        return this;
    }

    public double getPercentUnknown() {
        return percentUnknown;
    }

    public TwitterStats setPercentUnknown(double percentUnknown) {
        this.percentUnknown = percentUnknown;
        return this;
    }
}
