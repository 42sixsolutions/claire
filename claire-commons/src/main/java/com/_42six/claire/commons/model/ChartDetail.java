package com._42six.claire.commons.model;

import java.util.Date;
import java.util.List;

/**
 * POJO for the chart details
 */
@SuppressWarnings("unused")
public class ChartDetail {
    private List<ChartDetailDataPoint> positiveTweets;
    private List<ChartDetailDataPoint> negativeTweets;
    private List<ChartDetailDataPoint> unknownTweets;
    private List<ChartDetailDataPoint> adverseEvents;
    private List<ChartDetailDataPoint> recalls;
    private List<ChartDetailDataPoint> positiveTweetSpikes;
    private List<ChartDetailDataPoint> negativeTweetSpikes;

    public List<ChartDetailDataPoint> getPositiveTweets() {
        return positiveTweets;
    }

    public ChartDetail setPositiveTweets(List<ChartDetailDataPoint> positiveTweets) {
        this.positiveTweets = positiveTweets;
        return this;
    }

    public List<ChartDetailDataPoint> getNegativeTweets() {
        return negativeTweets;
    }

    public ChartDetail setNegativeTweets(List<ChartDetailDataPoint> negativeTweets) {
        this.negativeTweets = negativeTweets;
        return this;
    }

    public List<ChartDetailDataPoint> getUnknownTweets() {
        return unknownTweets;
    }

    public ChartDetail setUnknownTweets(List<ChartDetailDataPoint> unknownTweets) {
        this.unknownTweets = unknownTweets;
        return this;
    }

    public List<ChartDetailDataPoint> getAdverseEvents() {
        return adverseEvents;
    }

    public ChartDetail setAdverseEvents(List<ChartDetailDataPoint> adverseEvents) {
        this.adverseEvents = adverseEvents;
        return this;
    }

    public List<ChartDetailDataPoint> getRecalls() {
        return recalls;
    }

    public ChartDetail setRecalls(List<ChartDetailDataPoint> recalls) {
        this.recalls = recalls;
        return this;
    }

	public List<ChartDetailDataPoint> getPositiveTweetSpikes() {
		return positiveTweetSpikes;
	}

	public void setPositiveTweetSpikes(
			List<ChartDetailDataPoint> positiveTweetSpikes) {
		this.positiveTweetSpikes = positiveTweetSpikes;
	}

	public List<ChartDetailDataPoint> getNegativeTweetSpikes() {
		return negativeTweetSpikes;
	}

	public void setNegativeTweetSpikes(
			List<ChartDetailDataPoint> negativeTweetSpikes) {
		this.negativeTweetSpikes = negativeTweetSpikes;
	}

	@Override
	public String toString() {
		return "ChartDetail [positiveTweets=" + positiveTweets
				+ ", negativeTweets=" + negativeTweets + ", unknownTweets="
				+ unknownTweets + ", adverseEvents=" + adverseEvents
				+ ", recalls=" + recalls + ", positiveTweetSpikes="
				+ positiveTweetSpikes + ", negativeTweetSpikes="
				+ negativeTweetSpikes + "]";
	}

	public static class ChartDetailDataPoint {
        private Date date;
        private double percentMax;

        public Date getDate() {
            return date;
        }

        public ChartDetailDataPoint setDate(Date date) {
            this.date = date;
            return this;
        }

        public double getPercentMax() {
            return percentMax;
        }

        public ChartDetailDataPoint setPercentMax(double percentMax) {
            this.percentMax = percentMax;
            return this;
        }

		@Override
		public String toString() {
			return "ChartDetailDataPoint [date=" + date + ", percentMax="
					+ percentMax + "]";
		}
    }
}
