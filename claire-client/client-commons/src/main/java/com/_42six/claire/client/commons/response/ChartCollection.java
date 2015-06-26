package com._42six.claire.client.commons.response;

import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Collection of Chart reponse objects
 */
@XmlRootElement
public class ChartCollection {
	
	public Collection<Chart> charts;

}
