package com._42six.claire.client.commons.response;

import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ChartArrayResponseCollection {
	
	public Collection<ChartArrayResponse> charts;

}
