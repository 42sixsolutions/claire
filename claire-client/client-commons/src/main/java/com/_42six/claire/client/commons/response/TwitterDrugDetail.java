package com._42six.claire.client.commons.response;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

public class TwitterDrugDetail {
	
	private String drugName;
	private int totalTweets;
	private SortedSet<EventsByDate> events;
	
	public TwitterDrugDetail() {
		this.events = new TreeSet<EventsByDate>();
		this.totalTweets = 0;
	}
	
	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public SortedSet<EventsByDate> getEvents() {
		return events;
	}

	public void setEvents(SortedSet<EventsByDate> events) {
		this.events = events;
	}

	public int getTotalTweets() {
		return totalTweets;
	}

	public void setTotalTweets(int totalTweets) {
		this.totalTweets = totalTweets;
	}

	@Override
	public String toString() {
		return "TwitterDrugDetail [drugName=" + drugName + ", events=" + events
				+ "]";
	}

	public static class EventsByDate implements Comparable<EventsByDate> {
		
		private Date date;
		private int positiveCount;
		private int negativeCount;
		private int neutralCount;
		
		public EventsByDate() {
			this.positiveCount = 0;
			this.negativeCount = 0;
			this.neutralCount = 0;
		}
		
		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public int getPositiveCount() {
			return positiveCount;
		}

		public void setPositiveCount(int positiveCount) {
			this.positiveCount = positiveCount;
		}

		public int getNegativeCount() {
			return negativeCount;
		}

		public void setNegativeCount(int negativeCount) {
			this.negativeCount = negativeCount;
		}

		public int getNeutralCount() {
			return neutralCount;
		}

		public void setNeutralCount(int neutralCount) {
			this.neutralCount = neutralCount;
		}

		@Override
		public String toString() {
			return "EventsByDate [date=" + date + ", positiveCount="
					+ positiveCount + ", negativeCount=" + negativeCount
					+ ", neutralCount=" + neutralCount + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((date == null) ? 0 : date.hashCode());
			result = prime * result + negativeCount;
			result = prime * result + neutralCount;
			result = prime * result + positiveCount;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EventsByDate other = (EventsByDate) obj;
			if (date == null) {
				if (other.date != null)
					return false;
			} else if (!date.equals(other.date))
				return false;
			if (negativeCount != other.negativeCount)
				return false;
			if (neutralCount != other.neutralCount)
				return false;
			if (positiveCount != other.positiveCount)
				return false;
			return true;
		}

		@Override
		public int compareTo(EventsByDate o) {
			return this.date.compareTo(o.date);
		}
	}
}
