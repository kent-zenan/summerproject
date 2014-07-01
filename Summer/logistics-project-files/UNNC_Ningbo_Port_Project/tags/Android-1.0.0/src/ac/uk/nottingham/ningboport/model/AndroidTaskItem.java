package ac.uk.nottingham.ningboport.model;

import java.util.Date;

/**
 * Task Object.
 * 
 * @author Jiaqi LI
 *
 */
public class AndroidTaskItem {
	
	private int taskID;
	private String from;
	private String to;
	private Date expectedTimeForArrival;
	private Status status;
	
	private enum Status{
		Waiting, Started, Finished, 
	}
	
	public AndroidTaskItem( int id,
			String from,
			String to,
			Date expectedTimeForArrival){
		this.taskID = id;
		this.from = from;
		this.to = to;
		this.expectedTimeForArrival = expectedTimeForArrival;
		
		// TODO: Add if statement here for status!
		this.status = Status.Waiting;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Date getExpectedTimeForArrival() {
		return expectedTimeForArrival;
	}

	public void setExpectedTimeForArrival(Date expectedTimeForArrival) {
		this.expectedTimeForArrival = expectedTimeForArrival;
	}

	public Status getStatus() {
		return status;
	}

	public String getStatusInString(){
		if( status.compareTo(Status.Finished) == 0){
			return "Finished";
		}else if( status.compareTo(Status.Waiting) == 0){
			return "Waiting";
		}else if( status.compareTo(Status.Started) == 0){
			return "In Progress";
		}else{
			return "N/A";
		}
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}
	
	
}