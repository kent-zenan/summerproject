package server.model;



/**
 * Task XML Object.
 * For details about XML format, please see spec.xml
 * 
 * @author Haoyue Zhu, Jiaqi LI
 *
 */
public class XMLTask {
	
	private int sequenceNo = 0;
	private Action action = null; 
	
	private String declareID = null;
	private String src = null;
	private String dest = null;
	private long availableTime = 0;
	private long deadline = 0;
	private int size = 0;
	private int weight = 0;
	private int quantity = 0;
	private long plannedStartTime = 0;
	private long plannedLoadTime = 0;
	private long plannedTravelTime = 0;
	private long plannedUnloadTime = 0;
	private long plannedFinishTime = 0;
	private long actualStartTime = 0;
	private long actualLoadTime = 0;
	private long actualTravelTime = 0;
	private long actualUnloadTime = 0;
	private long actualFinishTime = 0;
	private Status status = null;
	
	public enum Status {planned, started, finished};
	public enum Action {add, update, delete, start, finish, none};
	
	public XMLTask( int sn, Action a) {
		sequenceNo = sn;
		action = a;
	}

	
	public Action getAction() {
		return action;
	}


	public void setAction(Action action) {
		this.action = action;
	}


	public String getDeclareID() {
		return declareID;
	}

	public void setDeclareID(String declareID) {
		this.declareID = declareID;
	}

	public int getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public long getAvailableTime() {
		return availableTime;
	}

	public void setAvailableTime(long availableTime) {
		this.availableTime = availableTime;
	}

	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getPlannedStartTime() {
		return plannedStartTime;
	}

	public void setPlannedStartTime(long plannedStartTime) {
		this.plannedStartTime = plannedStartTime;
	}

	public long getPlannedLoadTime() {
		return plannedLoadTime;
	}

	public void setPlannedLoadTime(long plannedLoadTime) {
		this.plannedLoadTime = plannedLoadTime;
	}

	public long getPlannedTravelTime() {
		return plannedTravelTime;
	}

	public void setPlannedTravelTime(long plannedTravelTime) {
		this.plannedTravelTime = plannedTravelTime;
	}

	public long getPlannedUnloadTime() {
		return plannedUnloadTime;
	}

	public void setPlannedUnloadTime(long plannedUnloadTime) {
		this.plannedUnloadTime = plannedUnloadTime;
	}

	public long getPlannedFinishTime() {
		return plannedFinishTime;
	}

	public void setPlannedFinishTime(long plannedFinishTime) {
		this.plannedFinishTime = plannedFinishTime;
	}

	public long getActualStartTime() {
		return actualStartTime;
	}

	public void setActualStartTime(long actualStartTime) {
		this.actualStartTime = actualStartTime;
	}

	public long getActualLoadTime() {
		return actualLoadTime;
	}

	public void setActualLoadTime(long actualLoadTime) {
		this.actualLoadTime = actualLoadTime;
	}

	public long getActualTravelTime() {
		return actualTravelTime;
	}

	public void setActualTravelTime(long actualTravelTime) {
		this.actualTravelTime = actualTravelTime;
	}

	public long getActualUnloadTime() {
		return actualUnloadTime;
	}

	public void setActualUnloadTime(long actualUnloadTime) {
		this.actualUnloadTime = actualUnloadTime;
	}

	public long getActualFinishTime() {
		return actualFinishTime;
	}

	public void setActualFinishTime(long actualFinishTime) {
		this.actualFinishTime = actualFinishTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	
}
