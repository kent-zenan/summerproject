package uk.ac.nottingham.ningboport.planner;

import java.util.Calendar;
import java.util.Vector;


public class Commodity implements Identical<Commodity>{
	public String id; //申报单号
	public Calendar availTime, deadline; 
	public int small, large;
	public Node src, dest;
	// All ta related to this commodity
	public Vector<Task> tasks = new Vector<Task>();
	// The periods when commodity is available or must be finished.
	public Vector<Integer> completableShifts = new Vector<Integer>();
	public int latestPeriod; // index of deadline period
	
	public Commodity (String id, Calendar avail, Calendar deadline, Node src, Node dest, int small, int large) {
		this.id = id;
		this.availTime = avail;
		this.deadline = deadline;
		this.small = small;
		this.large = large;
		this.src = src;
		this.dest = dest;
	}
	
	public String toString() {
		return id + " " + 
				src + "(" + availTime.getTime() + ") " + 
				dest + "(" + deadline.getTime() + ") " + 
				small + " " + large;
	}

	/*
	 * just compare the src and dest node
	 */
	public boolean geographicallyIdenticalTo(Commodity t) {
		if (this.src == t.src && this.dest == t.dest)
			return true;
		else
			return false;
	}
}
