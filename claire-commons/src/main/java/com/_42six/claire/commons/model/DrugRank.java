package com._42six.claire.commons.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO for drug rankings
 */
@XmlRootElement
@SuppressWarnings("unused")
public class DrugRank {
    private String brandName;
    private int ranking;
    private boolean currentDrug;

    public String getBrandName() {
        return brandName;
    }

    public DrugRank setBrandName(String brandName) {
        this.brandName = brandName;
        return this;
    }

    public int getRanking() {
        return ranking;
    }

    public DrugRank setRanking(int ranking) {
        this.ranking = ranking;
        return this;
    }

    public boolean isCurrentDrug() {
        return currentDrug;
    }

    public DrugRank setCurrentDrug(boolean currentDrug) {
        this.currentDrug = currentDrug;
        return this;
    }
}
