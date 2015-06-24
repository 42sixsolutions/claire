package com._42six.claire.client.commons.response;

import java.util.SortedSet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Chart {

	public String name;
	public SortedSet<DataPoint> points;
	
	@Override
	public String toString() {
		return "Chart [name=" + name + ", points=" + points + "]";
	}

	//TODO:
	public String toChartResponse() {
		/*
		 * 		StringBuilder builder = new StringBuilder();
		builder.append("[ ");
		boolean first = true;
		for (Result result : countByDay.results) {
			Date date = this.dateAdapter.unmarshal(result.time);
			cal.setTime(date);
			if (cal.getTimeInMillis() == startCal.getTimeInMillis()
					|| cal.getTimeInMillis() == endCal.getTimeInMillis() 
					|| (cal.after(startCal) && cal.before(endCal))) {
				if (first) {
					first = false;
				}
				else {
					builder.append(",");
				}
				builder.append("[");
				builder.append(result.time);
				builder.append(",");
				builder.append(result.count);
				builder.append("]");
			}
		}
		builder.append("]");
		 */
		return null;
	}
	
	public static class DataPoint implements Comparable<DataPoint> {
		
		public String label;
		public int count;
		@Override
		public int compareTo(DataPoint o) {
			return this.label.compareTo(o.label);
		}
		@Override
		public String toString() {
			return "DataPoint [label=" + label + ", count=" + count + "]";
		}
	}
}
