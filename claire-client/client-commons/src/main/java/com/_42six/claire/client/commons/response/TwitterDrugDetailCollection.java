package com._42six.claire.client.commons.response;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TwitterDrugDetailCollection {
	
	private Collection<TwitterDrugDetail> drugs;
	
	public TwitterDrugDetailCollection() {
		this.drugs = new ArrayList<TwitterDrugDetail>();
	}

	public Collection<TwitterDrugDetail> getDrugs() {
		return drugs;
	}

	public void setDrugs(Collection<TwitterDrugDetail> drugs) {
		this.drugs = drugs;
	}

	@Override
	public String toString() {
		return "TwitterDrugDetailCollection [drugs=" + drugs + "]";
	}
	
}
