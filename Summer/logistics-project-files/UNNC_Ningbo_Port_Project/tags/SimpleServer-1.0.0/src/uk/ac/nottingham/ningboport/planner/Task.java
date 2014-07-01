package uk.ac.nottingham.ningboport.planner;

import java.util.Calendar;

public class Task implements Comparable<Task> {
	public Commodity cmdt;
	public int size;
	public double fuel; // fuel cost of this action.
	public Calendar startT, finishT;
	public Calendar actualStartT, actualFinishT;
	public boolean emergent; // updated by chkroute;
	// The delay in real time; negative means early finish.
	// Note that this delay is not used in deadline checking.
	private int lateness;
	public String driver = "none";
	public String vehicleID = "none";
	
	public enum sortingMethod {AVAIL_TIME, DEADLINE, SHORTEST_DISTANCE, LONGEST_DISTANCE, ACTUAL_STARTING_TIME}; // static?
	public static sortingMethod sortBy = sortingMethod.DEADLINE;
	public Task gT; // 2 small boxes can put together
	public boolean finished; // whether the action is already done
	
	public int getLateness() {
		if (finished)
			return lateness;
		else
			return 0;
	}
	
	public boolean setLateness(int delay) {
		System.out.println("set delay:" + this.cmdt.id);
		if (!finished)
			return false;
			
		lateness = delay;
		return true;
	}
/*	public boolean grab(Vector<TransportAction> ta, int index) {
		if (index >= ta.size() || index < 0) {
			RoutingPlanner.errormsg("Grab: bad index");
			System.exit(0);
		}
		this.gT = ta.elementAt(index);
		ta.remove(index);
		return true;
	}
	
	public boolean grab(Vector<TransportAction> ta, TransportAction t) {
		if (!ta.contains(t)) {
			RoutingPlanner.errormsg("Grab: no such ta");
			System.exit(0);
		}
		this.gT = t;
		ta.remove(t);
		return true;
	}*/

	public int compareTo(Task that) {
		if (sortBy == sortingMethod.DEADLINE) {
			if (this.cmdt.deadline.before(that.cmdt.deadline))
				return -1;
			else if (this.cmdt.deadline.after(that.cmdt.deadline))
				return 1;
			else
				return 0;
			
		} else if (sortBy == sortingMethod.AVAIL_TIME) {
			if (this.cmdt.availTime.before(that.cmdt.availTime))
				return -1;
			else if (this.cmdt.availTime.after(that.cmdt.availTime))
				return 1;
			else
				return 0;
			
		} else if (sortBy == sortingMethod.ACTUAL_STARTING_TIME) {
			return this.actualStartT.compareTo(that.actualStartT);
		}
		return 0;
	} 
	
	public String toString() {
		String res = cmdt.id + 
				": " + cmdt.src.id +
				"->" + cmdt.dest.id + " (" +
				cmdt.availTime.getTime() + " - " + cmdt.deadline.getTime() +
				") weight:" + size;
		if (this.gT != null) {
			res += " with " + gT.cmdt.id + " " + gT.size;
		}
		return res;
	}
	
	public String toDisplayString() {
		String res = cmdt.id + 
				": \n" + cmdt.src.id +
				"->" + cmdt.dest.id + " \n(" +
				cmdt.availTime.getTime() + " - " + cmdt.deadline.getTime() +
				") \nweight:" + size;
		if (this.gT != null) {
			res += " with " + gT.cmdt.id + " " + gT.size;
		}
		return res;
	}
	
	public Task clone() {
		Task t = new Task();
		//TODO: cmdt should be cloned, but not by clone(). what should we do?
		t.cmdt = this.cmdt;
		t.size = this.size;
		if (this.gT != null) {
			t.gT = this.gT.clone();
		}
		
		return t;
	}
}
