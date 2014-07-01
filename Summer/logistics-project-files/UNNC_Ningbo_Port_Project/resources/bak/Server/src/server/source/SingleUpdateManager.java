package server.source;

import planner.Task;
import server.model.XMLTask.Action;

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