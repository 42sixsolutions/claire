package com._42six.claire.openfda.util;

import java.util.SortedSet;
import java.util.TreeSet;

public class OpenFDAUtil {
	
	public static final String[] DRUG_NAMES = {"enbrel","hydrochloride","tysabri","methotrexate","prednisone","metformin","avonex","lipitor","lisinopril","acetaminophen","nexium","avandia","simvastatin","folic","metoprolol","lyrica","mirena","synthroid","hydrochlorothiazide","dianeal","levothyroxine","remicade","lasix","furosemide","crestor","chantix","amlodipine","atenolol","seroquel","plavix","spiriva","lantus","advair","dexamethasone","albuterol","celebrex","coumadin","byetta","warfarin","prednisolone","diovan","cymbalta","norvasc","oxycodone","xanax","forteo","premarin","neurontin","chloride","tramadol","zoloft","fentanyl","hydrocodone","gabapentin","morphine","tartrate","paxil","fosamax","allopurinol","cyclophosphamide","niaspan","wellbutrin","ribavirin","effexor","zocor","lorazepam"};

	public static final SortedSet<String> DRUG_NAMES_SET = new TreeSet<String>() {
		
		private static final long serialVersionUID = 967065330831655671L;
	{
		for (int i = 0; i < DRUG_NAMES.length; ++i) {
			add(DRUG_NAMES[i]);
		}
	}};
}
