package com._42six.claire.client.commons.response;

import java.util.SortedSet;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Chart response object
 */
@XmlRootElement
public class Chart {

	private String name;
	private SortedSet<DataPoint> points;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SortedSet<DataPoint> getPoints() {
		return points;
	}

	public void setPoints(SortedSet<DataPoint> points) {
		this.points = points;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((points == null) ? 0 : points.hashCode());
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
		Chart other = (Chart) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (points == null) {
			if (other.points != null)
				return false;
		} else if (!points.equals(other.points))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Chart [name=" + name + ", points=" + points + "]";
	}
}
