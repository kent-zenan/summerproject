package uk.ac.nottingham.ningboport.server.var;

import java.util.ArrayList;

import uk.ac.nottingham.ningboport.server.datamgr.UpdateManager;

public class Global {
	private static ArrayList<UpdateManager> updateTasks = new ArrayList<UpdateManager>();
	

	//TODO: also an ArrayList to save all the sessions
	
	
	public static ArrayList<UpdateManager> getUpdateTasks(){
		return updateTasks;
	}
	
	public static void setUpdateTasks(ArrayList<UpdateManager> up){
		updateTasks = up;
	}
	
	public static void addUpdateTask(UpdateManager um) {
		updateTasks.add(um);
	}	

}
