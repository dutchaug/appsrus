package nl.appsrus.vhack2012.api;

public class ApiFactory {

	private static AbcApi sInstance;

	private ApiFactory() {
		// Hiding constructor of utility class
	}
	
	public static AbcApi getInstance () {
		if (sInstance == null) {
			sInstance = new AbcApiImpl();
		}
		return sInstance;
	}
}
