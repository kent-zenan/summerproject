package planner;

import java.util.Calendar;
import java.util.Vector;
import planner.Task;
import server.model.XMLSession;

public class Route implements Identical<Route>{
	public int id;
	private XMLSession session;
	public int period; // The period it belongs to.
	public Vector<Task> taskSet;
	public double loadedDistance;
	public double emptyDistance;
	public double softTimeWindowPenalty;
	public double waitTime;
	public enum status {UNASSIGNED, ASSIGNED, SESSION_SUSPENDED, SESSION_ENDED}; 
	public status currentStatus = status.UNASSIGNED;
	
	private Network nw;
	public Route(Network nw, int id, int period) {
		taskSet = new Vector<Task>();
		this.nw = nw;
		this.id = id;
		this.period = period;
	}
	
	public void insert(int ta_index, int route_slot, Vector<Task> unassignedTaskSet) {
		route_slot = route_slot > taskSet.size()? taskSet.size() : route_slot;
		this.taskSet.add(route_slot, unassignedTaskSet.remove(ta_index));
	}
	
	public void insert(Task t, int route_slot, Vector<Task> unassignedTaskSet) {
		insert(unassignedTaskSet.indexOf(t), route_slot, unassignedTaskSet);
	}
	
	public void remove(int ta_index, Vector<Task> unassignedTaskSet) {
		unassignedTaskSet.add(this.taskSet.remove(ta_index));
	}

	
	/*
	 * If there is already one session assigned, it will fail
	 */
	public boolean setSession(XMLSession s) {
		if (this.session != null) {
			return false;
		}
		
		this.session = s;
		this.currentStatus = status.ASSIGNED;
		return true;
	}
	
	public XMLSession getSession() {
		return session;
	}
	
	public status getStatus() {
		return this.currentStatus;
	}
	
	public void setStatus(status s) {
		currentStatus = s;
	}
	
	public String outputPortSequence() {
		String out = "";
		for (int i = 0; i < taskSet.size(); i++) {
			Task t = taskSet.elementAt(i);
			
			out += "[" + t.cmdt.src.id + " " + t.cmdt.dest.id + "] ";
		}
		
		return out;
	}
	
	/*
	 * how much time the first task can be postponed 
	 * without affecting the whole route plan. (inversed way of checking route).
	 */
	public int getTightness() {
		//TODO implementation
		return 0;
	}
	
	public String toString() {
		String statistics = new String(),
				ports = "Ports: ",
				tasks = "Tasks: ",
				times = "Times: ";
		if (check() <= 0) {
			statistics += "*";
			nw.updateObjectiveValues(this);
		}

		for (int i = 0; i < taskSet.size(); i++) {
			Task t = taskSet.elementAt(i);
			
			if (i == 0) {
				statistics += "Route: " + this.id + " driver: " + t.driver + " vehicle: " + t.vehicleID;
				statistics += " ED: " + this.emptyDistance + " LD: " + this.loadedDistance;
			}
			
			ports += "[" + t.cmdt.src.id + " " + t.cmdt.dest.id + "] ";
			
			tasks += t.cmdt.id + "(";
			if (t.size == 1)
				tasks += "s";
			else // large
				tasks += "b";
			
			if (t.gT != null)
				tasks += "s";
			
			if (t.finished)
				tasks += "f";
			
			tasks += ") ";
			
			times += "<";
			if (t.startT != null && t.finishT != null)
				times += t.startT.getTime() + " " + t.finishT.getTime();
			else
				times += "Infeasible";
			
			times += " " +t.getLateness() + " ";
			times +=  "> ";
			
		}
		return statistics + "\n" + ports + "\n" + tasks + "\n" + times + "\n";
	}
	
	/*
	 * set finished tag for all ta in this route
	 * according to the given ctime.
	 * The running job is also set to finished (even)
	 */
	public void setFinishedTA(Calendar ctime) {
		if (nw.periodStartTimes[period].after(ctime)) {
			return;
		}
		
		int size = taskSet.size();
		for (int i = 0; i < size; i++) {
			Task t = taskSet.get(i);
			Calendar startTime;
			if (i == 0)
				startTime = nw.periodStartTimes[period];
			else
				startTime = taskSet.get(i - 1).startT;
			
			if (ctime.after(startTime))
				t.finished = true;
			
			if (ctime.before(t.finishT))
				break;
			
			// if ctime > startTime
			// set this to finished
			// if ctime < finishTime
			// break;
		}
	}
	
	public boolean setRouteDelay(int delayInMinutes) {
		int size = taskSet.size();
		//System.out.println("Set delay on route: " + this.id);
		//How do you know a delay if it is not finished?
		if (!taskSet.get(0).finished) {
			System.out.println("Delay not set, incorrect delay.");
			return false;
		}
		int lastIndex = 0;
		for (int i = 0; i < size; i++) {
			if (taskSet.get(i).finished) {
				lastIndex = i;
			} else {
				break;
			}
		}
		taskSet.get(lastIndex).setLateness(delayInMinutes);
		//System.out.println("Delay set on " + lastIndex + " later: " + ta.get(lastIndex + 1).finished);
		return true;
	}
	
	public Route clone() {
		Route r = new Route(this.nw, this.id, this.period);
		r.emptyDistance = this.emptyDistance;
		r.loadedDistance = this.loadedDistance;
		r.waitTime = this.waitTime;
		//This clone copies the TA as well not just reference.
		int taskSetSize = taskSet.size();
		for (int i = 0; i < taskSetSize; i++) {
			r.taskSet.add(taskSet.get(i).clone());
		}
		return r;
	}

	@Override
	public boolean geographicallyIdenticalTo(Route t) {
		if (this.taskSet.size() != t.taskSet.size())
			return false;
		
		int tsize = taskSet.size();
		Vector<Task> t2 = t.taskSet;
		for (int i = 0; i < tsize; i++) {
			Commodity c1 = taskSet.get(i).cmdt,
					c2 = t2.get(i).cmdt;
			if (!c1.geographicallyIdenticalTo(c2))
				return false;
		}
		return true;
	}
	
	/*
	 * Checks Deadline, total Time
	 * return 3 => succeed (last task emergent)
	 * return 2 => succeed (soft time window)
	 * return 1 => succeed (strict time window)
	 * return 0 => exceed return deadline
	 * return -1 => time window conflict
	 * 
	 * after check, the startT and finishT is updated correctly.
	 */
	public int check() {
		Calendar currentTime = (Calendar) nw.periodStartTimes[this.period].clone();
		Calendar endTime = nw.periodEndTimes[this.period];
		loadedDistance = emptyDistance = waitTime = softTimeWindowPenalty = 0;
		
		int taskSetSize = taskSet.size();
		for (int i = 0; i < taskSetSize; i++) {
			Task currentTa = taskSet.elementAt(i);
			currentTa.emergent = false;
			Commodity c = currentTa.cmdt;
			int srcI = c.src.index, 
					destI = c.dest.index, 
					depotI = nw.depot.index;
			
			//dead heading to source node.
			if (i == 0) { //depot to src
				currentTime.add(Calendar.MINUTE, (int) nw.travelingTimes[depotI][srcI]);
				emptyDistance += nw.travelingDistances[depotI][srcI];
				
			} else {//dest to src
				Commodity c2 = this.taskSet.elementAt(i - 1).cmdt;
				if (!c.src.equals(c2.dest)) {
					currentTime.add(Calendar.MINUTE, (int) nw.travelingTimes[c2.dest.index][srcI]);
					emptyDistance += nw.travelingDistances[c2.dest.index][srcI];
				}
			}
			
			//Carrier just arrived at source node.
			if (currentTime.compareTo(c.availTime) < 0) { // arrive too early
				this.waitTime += (c.availTime.getTimeInMillis() - currentTime.getTimeInMillis())
						/ 1000 / 60; 
				currentTime = (Calendar) c.availTime.clone();
			}
			currentTa.startT = (Calendar) currentTime.clone();
			
			// load time of commodity c
			currentTa.emergent = (int) ((c.deadline.getTimeInMillis() - currentTime.getTimeInMillis()) / 1000 / 60 - 120) // should arrive earlier, not just in time!! 
					< nw.travelingTimesWithQueue[srcI][destI];
			//if (!currentTa.emergent) {
			currentTime.add(Calendar.MINUTE, c.src.loadTime); 
			//}
			
			//src -> dest
			currentTime.add(Calendar.MINUTE, (int) nw.travelingTimes[srcI][destI]);
			loadedDistance += nw.travelingDistances[srcI][destI];
			
			// unload ta1, task(s) finished
			//if (!currentTa.emergent) {
			currentTime.add(Calendar.MINUTE, c.dest.unloadTime);
			//}
			
			if (currentTime.compareTo(c.deadline) > 0) {
				//System.out.println("Too late to unload!");
				return -1;
			}
			currentTa.finishT = (Calendar) currentTime.clone();

			// if last task, go back to depot
			if (i == taskSetSize - 1) {  //dest to depot
				currentTime.add(Calendar.MINUTE, (int) nw.travelingTimes[destI][depotI]);
				this.emptyDistance += nw.travelingDistances[destI][depotI];
			}
			
			if (currentTime.compareTo(endTime) > 0) {
				if (i == taskSetSize - 1) { // emergent and it is last task
					if (currentTa.emergent)
						return 3;
					
					//Soft time window
//					double lateness = currentTime.getTimeInMillis() - endTime.getTimeInMillis();
//					lateness = lateness / 1000 / 60;
//					
//					this.softTimeWindowPenalty = lateness / 2;
//					return 2;
				} else {
					return 0;
				}
			}
			
			//The delay is calculated, but not included in finish time;
			//This is put here because once a job is found to be delayed,
			//Only later jobs should be reconsidered, it is already to late
			//to change it if period deadline is missed.

			currentTime.add(Calendar.MINUTE, currentTa.getLateness());
		}
		return 1;
	}
}
