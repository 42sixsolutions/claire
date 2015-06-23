package com._42six.claire.openfda.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Marshals dates using the OpenFDA date format
 */
public class OpenFDADateAdapter extends XmlAdapter<String, Date> {

	@Override
	public String marshal(Date v) throws Exception {
		return new SimpleDateFormat("yyyyMMdd").format(v);
	}

	@Override
	public Date unmarshal(String v) throws Exception {
		return new SimpleDateFormat("yyyyMMdd").parse(v);
	}
}
