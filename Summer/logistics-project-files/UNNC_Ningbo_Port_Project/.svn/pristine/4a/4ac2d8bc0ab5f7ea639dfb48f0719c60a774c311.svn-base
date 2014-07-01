package uk.ac.nottingham.ningboport.planner;

import java.util.Vector;

public class Solution {
	public double emptyDistance = 0;
	public double loadedDistance = 0;
	public Vector<Vector <Route>> routesOfShifts;
	public Vector<Task> taskSet;
	
	public Solution(Network nw, Vector<Task> unassignedTask) {
		taskSet = new Vector<Task>();
		unassignedTask.addAll(nw.taskSet);
		
		int nwTaskSetSize = unassignedTask.size();
		for (int i = 0; i < nwTaskSetSize; i++) {
			taskSet.add(unassignedTask.get(i).clone());
		}
		
		Vector<Vector <Route>> routesOfShifts = nw.routesOfShifts;
		int nPeriods = routesOfShifts.size();
		this.routesOfShifts = new Vector<Vector <Route>>(nPeriods);
		for (int i = 0; i < nPeriods; i++) {
			// create a new period for this solution
			Vector<Route> solutionRoutesOfPeriodI = new Vector<Route>();
			this.routesOfShifts.add(solutionRoutesOfPeriodI);
			
			Vector<Route> networkRoutesOfPeriodI = routesOfShifts.elementAt(i);
			int numberOfRoutes = networkRoutesOfPeriodI.size();
			for (int j = 0; j < numberOfRoutes; j++) {
				Route current = networkRoutesOfPeriodI.elementAt(j);
				current.check();
				emptyDistance += current.emptyDistance;
				loadedDistance += current.loadedDistance;
				solutionRoutesOfPeriodI.add(current.clone());
			}
		}
	}
	
	public Vector<Vector <Route>> getRoutes() {
		return routesOfShifts;
	}
	
	public int compareTo(Solution b) {
		return Double.compare(b.emptyDistance, this.emptyDistance);
	}
	
	public boolean betterThan(Solution b) {
		double empty_rate_a = this.emptyDistance / (this.loadedDistance + this.emptyDistance);
		double empty_rate_b = b.emptyDistance / (b.loadedDistance + b.emptyDistance);
		if (Double.compare(empty_rate_a, empty_rate_b) < 0)
			return true;
		else
			return false;
	}
}
