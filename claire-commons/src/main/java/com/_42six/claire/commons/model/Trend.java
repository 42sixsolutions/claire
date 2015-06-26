package com._42six.claire.commons.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO for Trends
 */
@XmlRootElement
@SuppressWarnings("unused")
public class Trend implements Comparable<Trend>{

	private String brandName;
	private double slope;
	
	public Trend() {
		
	}
	
	public Trend(String brandName, double slope) {
		this.brandName = brandName;
		this.slope = slope;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public double getSlope() {
		return slope;
	}

	public void setSlope(double slope) {
		this.slope = slope;
	}

	@Override
	public int compareTo(Trend o) {
		int c = new Double(o.slope).compareTo(new Double(this.slope)); //descending order
		return c != 0 ? c : this.brandName.compareTo(o.brandName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((brandName == null) ? 0 : brandName.hashCode());
		long temp;
		temp = Double.doubleToLongBits(slope);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Trend other = (Trend) obj;
		if (brandName == null) {
			if (other.brandName != null)
				return false;
		} else if (!brandName.equals(other.brandName))
			return false;
		if (Double.doubleToLongBits(slope) != Double
				.doubleToLongBits(other.slope))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Trend [brandName=" + brandName + ", slope=" + slope + "]";
	}
}
