package com._42six.claire.commons.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * POJO for twitter stats
 */
@SuppressWarnings("unused")
@XmlAccessorType(XmlAccessType.NONE)
public class TwitterStats {
    private int totalTweets;
    private double percentPositive;
    private double percentNegative;
    private double percentUnknown;

    @XmlElement
    public int getTotalTweets() {
        return totalTweets;
    }

    public TwitterStats setTotalTweets(int totalTweets) {
        this.totalTweets = totalTweets;
        return this;
    }

    @XmlElement
    public int getPercentPositive() {
        return (int)percentPositive;
    }
    
    public double getPercentPositiveDbl() {
        return percentPositive;
    }

    public TwitterStats setPercentPositive(double percentPositive) {
        this.percentPositive = percentPositive;
        return this;
    }

    @XmlElement
    public int getPercentNegative() {
        return (int)percentNegative;
    }
    
    public double getPercentNegativeDbl() {
        return percentNegative;
    }

    public TwitterStats setPercentNegative(double percentNegative) {
        this.percentNegative = percentNegative;
        return this;
    }

    @XmlElement
    public int getPercentUnknown() {
        return (int)percentUnknown;
    }
    
    public double getPercentUnknownDbl() {
        return percentUnknown;
    }

    public TwitterStats setPercentUnknown(double percentUnknown) {
        this.percentUnknown = percentUnknown;
        return this;
    }
}
