package uk.ac.nottingham.ningboport.server.datamgr;

import uk.ac.nottingham.ningboport.network.model.XMLTask.Action;
import uk.ac.nottingham.ningboport.planner.Task;

public class SingleUpdateManager {
	private String action;
	private int sequenceNo;
	private Task task;
	
	public SingleUpdateManager(Action ac, Task t) {
		this.action = ac.toString();
		this.task = t;
	}

	public SingleUpdateManager(Action ac, Task t, int sn) {
		this.action = ac.toString();
		this.task = t;
		this.sequenceNo = sn;
	}

	public SingleUpdateManager(Action ac, int sn) {
		this.action = ac.toString();
		this.sequenceNo = sn;
	}
	
	public int getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public Action getAction() {
		if (this.action == "add")
			return Action.add;
		if (this.action == "delete")
			return Action.delete;
		else 
			return Action.update;
	}


	public void setOperation(String operation) {
		this.action = operation;
	}

	public Task getTask() {
		return task;
	}


	public void setTask(Task task) {
		this.task = task;
	}

}