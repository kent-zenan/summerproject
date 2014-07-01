package uk.ac.nottingham.ningboport.network.model;

/**
 * GPS XML Object.
 * For details about XML format, please see spec.xml
 * 
 * @author Jiaqi LI
 *
 */
public class XMLGps {

	private double longitude;
	private double latitude;
	
	public XMLGps(){}
	
	public XMLGps( double log, double lat) {
		longitude = log;
		latitude = lat;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}
