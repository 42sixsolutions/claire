package com._42six.claire.client.commons.response;

public class DataPoint implements Comparable<DataPoint> {
	
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
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
		DataPoint other = (DataPoint) obj;
		if (count != other.count)
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}
	
}