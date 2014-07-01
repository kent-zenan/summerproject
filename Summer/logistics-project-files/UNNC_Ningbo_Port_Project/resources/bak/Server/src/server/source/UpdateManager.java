package server.source;

import java.util.ArrayList;

import planner.Route;
import planner.Task;
import server.model.XMLDataRequest;
import server.model.XMLDataResponse;
import server.model.XMLSession;
import server.model.XMLTask;
import server.model.XMLTask.Action;

public class UpdateManager {
	private XMLSession session;
	private ArrayList<SingleUpdateManager> updates;
	
	public UpdateManager(XMLSession session) {
		this.session = session;
		this.updates = new ArrayList<SingleUpdateManager>();
	}


	public XMLSession getSession() {
		return session;
	}


	public void setSession(XMLSession session) {
		this.session = session;
	}


	public ArrayList<SingleUpdateManager> getTasks() {
		return updates;
	}


	public void setTask(ArrayList<SingleUpdateManager> updates) {
		this.updates = updates;
	}

	public void addSingleTask(SingleUpdateManager st) {
		this.updates.add(st);
	}

	public void removeSingleTask(int sn) {
		this.updates.remove(sn);
	}

	public static synchronized XMLDataResponse update(XMLDataRequest dataRequest, ArrayList<UpdateManager> ml) {
		int l = ml.size();
		XMLSession session = dataRequest.getSession();
		XMLDataResponse response = new XMLDataResponse(session);
		ArrayList<XMLTask> tasks = new ArrayList<XMLTask>();
		for (int i = 0; i < l; i++) {
			UpdateManager m = ml.get(i);
			XMLSession session1 = m.getSession();
			if (session1.equals(session)) {
				ArrayList<SingleUpdateManager> updates = m.getTasks();
				int length = tasks.size();
				for (int k = 0; k < length; k ++){
					SingleUpdateManager update = updates.get(k);
					int sn = update.getSequenceNo();
					Action ac = update.getAction();
					XMLTask XTask = new XMLTask(sn, ac);
					// TODO:if add, require details info
					
					// TODO:if update, require the change info
					
					// TODO:if remove, need no info..
					
					tasks.add(XTask);
					updates.remove(k);
				}
				m.setTask(updates);
			}
		}
		response.setTasks(tasks);
		return response;

	}
}
