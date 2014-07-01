package uk.ac.nottingham.ningboport.server.datamgr;

import java.util.ArrayList;
import java.util.Calendar;

import uk.ac.nottingham.ningboport.client.test.TaskTestData;
import uk.ac.nottingham.ningboport.network.model.XMLDataRequest;
import uk.ac.nottingham.ningboport.network.model.XMLDataResponse;
import uk.ac.nottingham.ningboport.network.model.XMLSession;
import uk.ac.nottingham.ningboport.network.model.XMLTask;
import uk.ac.nottingham.ningboport.network.model.XMLTask.Action;
import uk.ac.nottingham.ningboport.network.model.XMLTask.Status;
import uk.ac.nottingham.ningboport.planner.Commodity;
import uk.ac.nottingham.ningboport.planner.Task;
import uk.ac.nottingham.ningboport.server.var.Global;



public class UpdateManager {
	/* -----------Testing part--------
	public static void main(String args[]){
		XMLSession s = new XMLSession("Alex","9SADF");
		XMLGps g = new XMLGps(123.44,55.3);
		XMLDataRequest dataRequest = new XMLDataRequest(s,g);
		XMLDataResponse r = update(dataRequest);
		
		//System.out.println(Global.getUpdateTasks().get(0).getSession().getDriverName());
		System.out.println(r.getTasks().get(0).getSequenceNo());
	}
	-----------Testing part-------- */
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

	public static synchronized XMLDataResponse update(XMLDataRequest dataRequest) {
		//RECEIVE FROM THE GLOBLE...!

		XMLSession session = dataRequest.getSession();
		
		//------------Testing Part-------------------
		TaskTestData.generateTestData(session);
		
		//------------Testing Part-------------------
		

		XMLDataResponse response = new XMLDataResponse(session);
		ArrayList<XMLTask> tasks = new ArrayList<XMLTask>();
		
		ArrayList<UpdateManager> ml = Global.getUpdateTasks();
		int l = ml.size();	
		
		for (int i = 0; i < l; i++) {
			UpdateManager m = ml.get(i);
			XMLSession session1 = m.getSession();
			if (session1.equals(session)) {			
				ArrayList<SingleUpdateManager> updates = m.getTasks();
				int length = updates.size();
				
				for (int k = 0; k < length; k ++){
					SingleUpdateManager update = updates.get(0);
					int sn = update.getSequenceNo();
					Action ac = update.getAction();
					XMLTask XTask = new XMLTask(sn, ac);
					// TODO:if add, require details info

					if ( ac == Action.add) {
						Task t = update.getTask();
						Commodity c = t.cmdt;
						
						if (t != null) {
							XTask.setActualFinishTime(checkNull(t.actualFinishT));
							//XTask.setActualLoadTime();
							XTask.setActualStartTime(checkNull(t.actualStartT));
							//XTask.setActualTravelTime();
							//XTask.setActualUnloadTime();
							XTask.setPlannedFinishTime(checkNull(t.startT));
							XTask.setPlannedStartTime(checkNull(t.finishT));
							
							if(t.finished == true) {
								XTask.setStatus(Status.finished);
							}
							else {
								XTask.setStatus(Status.planned);
							}
							
							if(t.gT != null) {
								XTask.setSize(1);
								XTask.setQuantity(2);
							}
							else {
								XTask.setSize(t.size);
								XTask.setQuantity(1);							
							}
						}
						
						if(c != null) {
						
							XTask.setAvailableTime(checkNull(c.availTime));
							XTask.setDeadline(checkNull(c.deadline));
							XTask.setDeclareID(c.id);
							XTask.setPlannedLoadTime(c.src.loadTime);
							
							//XTask.setPlannedTravelTime();
							XTask.setPlannedUnloadTime(c.src.unloadTime);
							XTask.setDest(c.dest.id);
							XTask.setSrc(c.src.id);
							//XTask.setWeight();
						}
					}
					
					// TODO:if update, require the change info
					
					// if remove, need no info..
					
					tasks.add(XTask);
					
					updates.remove(0);
				}
				m.setTask(updates);
				ml.set(i, m);
				Global.setUpdateTasks(ml);
			}
		}
		response.setTasks(tasks);
		return response;
	}
	
	public static long checkNull(Calendar c){
		if (c == null) {
			return 0;
		}
		else {
			return c.getTimeInMillis();
		}
	}
}
