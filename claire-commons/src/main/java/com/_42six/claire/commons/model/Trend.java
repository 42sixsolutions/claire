package com._42six.claire.commons.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO for Trends
 */
@XmlRootElement
@SuppressWarnings("unused")
public class Trend {
    private String brandName;
    private double percentPositive;
    private double percentNegative;
    private int count;

    public String getBrandName() {
        return brandName;
    }

    public Trend setBrandName(String brandName) {
        this.brandName = brandName;
        return this;
    }

    public double getPercentPositive() {
        return percentPositive;
    }

    public Trend setPercentPositive(double percentPositive) {
        this.percentPositive = percentPositive;
        return this;
    }

    public double getPercentNegative() {
        return percentNegative;
    }

    public Trend setPercentNegative(double percentNegative) {
        this.percentNegative = percentNegative;
        return this;
    }

    public int getCount() {
        return count;
    }

    public Trend setCount(int count) {
        this.count = count;
        return this;
    }
}
