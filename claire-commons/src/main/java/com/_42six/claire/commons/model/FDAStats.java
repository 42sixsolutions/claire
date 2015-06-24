package com._42six.claire.commons.model;

/**
 * POJO for FDA stats
 */
@SuppressWarnings("unused")
public class FDAStats {
    private int totalRecalls;
    private int totalAdverseEvents;

    public int getTotalRecalls() {
        return totalRecalls;
    }

    public FDAStats setTotalRecalls(int totalRecalls) {
        this.totalRecalls = totalRecalls;
        return this;
    }

    public int getTotalAdverseEvents() {
        return totalAdverseEvents;
    }

    public FDAStats setTotalAdverseEvents(int totalAdverseEvents) {
        this.totalAdverseEvents = totalAdverseEvents;
        return this;
    }
}
