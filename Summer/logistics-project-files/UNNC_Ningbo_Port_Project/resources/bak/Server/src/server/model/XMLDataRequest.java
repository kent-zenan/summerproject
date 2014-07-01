package server.model;

import java.util.ArrayList;

/**
 * XML for update from Android device to server.
 * For details about XML format, please see spec.xml
 * 
 * @author Jiaqi LI
 *
 */
public class XMLDataRequest{

	XMLSession session;
	XMLGps gps;
	ArrayList<XMLTask> tasks;
	
	public XMLDataRequest( XMLSession s, XMLGps g) {
		session = s;
		gps = g;
		tasks = new ArrayList<XMLTask>();
	}

	public XMLSession getSession() {
		return session;
	}

	public void setSession(XMLSession session) {
		this.session = session;
	}

	public XMLGps getGps() {
		return gps;
	}

	public void setGps(XMLGps gps) {
		this.gps = gps;
	}	
}
