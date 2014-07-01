package uk.ac.nottingham.ningboport.client.test;

import java.util.ArrayList;
import java.util.Calendar;

import uk.ac.nottingham.ningboport.network.model.XMLSession;
import uk.ac.nottingham.ningboport.network.model.XMLTask.Action;
import uk.ac.nottingham.ningboport.planner.Commodity;
import uk.ac.nottingham.ningboport.planner.Node;
import uk.ac.nottingham.ningboport.planner.Task;
import uk.ac.nottingham.ningboport.server.datamgr.SingleUpdateManager;
import uk.ac.nottingham.ningboport.server.datamgr.UpdateManager;
import uk.ac.nottingham.ningboport.server.var.Global;

public class TaskTestData {
	public static void generateTestData(XMLSession s){
		ArrayList<UpdateManager> updateTasks = new ArrayList<UpdateManager>();
		UpdateManager um = new UpdateManager(s);
		Task t = new Task();
		t.driver = s.getDriverName();
		t.vehicleID = s.getVehicleID();
		t.startT = Calendar.getInstance();
		t.finishT = Calendar.getInstance();
		
		Node src = new Node("sss", 1, 1);
		Node dest = new Node("ddd", 1, 1);
		Commodity c = new Commodity("DeclareID", Calendar.getInstance(), Calendar.getInstance(), src, dest, 6, 8);
		
		t.cmdt = c;
		SingleUpdateManager a = new SingleUpdateManager(Action.add, t);
		//SingleUpdateManager b = new SingleUpdateManager(Action.delete, t);
		
		ArrayList<SingleUpdateManager> sl = new ArrayList<SingleUpdateManager>();
		sl.add(a);
		//sl.add(b);
		
		
		um.setTask(sl);
		updateTasks.add(um);
		
		Global.setUpdateTasks(updateTasks);
	}
}
