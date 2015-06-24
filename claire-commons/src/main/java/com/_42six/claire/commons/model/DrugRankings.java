package com._42six.claire.commons.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO for drug rankings
 */
@XmlRootElement
@SuppressWarnings("unused")
public class DrugRankings {
    private int positiveTweetsRank;
    private int negativeTweetsRank;
    private int unknownTweetsRank;
    private int adverseEventsRank;
    private int recallsRank;

    public int getPositiveTweetsRank() {
        return positiveTweetsRank;
    }

    public DrugRankings setPositiveTweetsRank(int positiveTweetsRank) {
        this.positiveTweetsRank = positiveTweetsRank;
        return this;
    }

    public int getNegativeTweetsRank() {
        return negativeTweetsRank;
    }

    public DrugRankings setNegativeTweetsRank(int negativeTweetsRank) {
        this.negativeTweetsRank = negativeTweetsRank;
        return this;
    }

    public int getUnknownTweetsRank() {
        return unknownTweetsRank;
    }

    public DrugRankings setUnknownTweetsRank(int unknownTweetsRank) {
        this.unknownTweetsRank = unknownTweetsRank;
        return this;
    }

    public int getAdverseEventsRank() {
        return adverseEventsRank;
    }

    public DrugRankings setAdverseEventsRank(int adverseEventsRank) {
        this.adverseEventsRank = adverseEventsRank;
        return this;
    }

    public int getRecallsRank() {
        return recallsRank;
    }

    public DrugRankings setRecallsRank(int recallsRank) {
        this.recallsRank = recallsRank;
        return this;
    }
}
