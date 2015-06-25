package com._42six.claire.web.model;

public class SortableDrug implements Comparable<SortableDrug> {
	
	private String name;
	private Double count;
	
	public SortableDrug(String name, Double count) {
		this.name = name;
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getCount() {
		return count;
	}

	public void setCount(Double count) {
		this.count = count;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		SortableDrug other = (SortableDrug) obj;
		if (count == null) {
			if (other.count != null)
				return false;
		} else if (!count.equals(other.count))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SortableDrug [name=" + name + ", count=" + count + "]";
	}

	@Override
	public int compareTo(SortableDrug o) {
		int c = o.count.compareTo(this.count); // sort descending
		return c != 0 ? c : this.name.compareTo(o.name);
	}

}
