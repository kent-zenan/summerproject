package server.model;

import java.util.ArrayList;

/**
 * XML for update from server to android device.
 * For details about XML format, please see spec.xml
 * 
 * @author Jiaqi LI
 *
 */
public class XMLDataResponse {

	XMLSession session;
	ArrayList<XMLTask> tasks;
	
	public XMLDataResponse( XMLSession s){
		session = s;
		tasks = new ArrayList<XMLTask>();
	}

	public XMLSession getSession() {
		return session;
	}

	public void setSession(XMLSession session) {
		this.session = session;
	}

	public ArrayList<XMLTask> getTasks() {
		return tasks;
	}

	public void setTasks(ArrayList<XMLTask> tasks) {
		this.tasks = tasks;
	}
	
	
}
