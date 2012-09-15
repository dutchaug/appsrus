package nl.appsrus.vhack2012.api;

import android.content.Context;

public class ApiFactory {

	private static AbcApi sInstance;

	private ApiFactory() {
		// Hiding constructor of utility class
	}
	
	public static void initialize (Context c) {
		if (sInstance == null) {
			sInstance = new AbcApiImpl(c);
		}
	}
	
	public static AbcApi getInstance () {
		return sInstance;
	}
}
