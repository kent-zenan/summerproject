package ac.uk.nottingham.ningboport.model;


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
	XMLTask task;
	
	public XMLDataRequest( XMLSession s, XMLGps g) {
		session = s;
		gps = g;
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
	
	public XMLTask getTask() {
		return task;
	}

	public void setTask(XMLTask task) {
		this.task = task;
	}	
	
}
