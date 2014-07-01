package planner;

import java.util.Vector;

public class Solution {
	public int numberOfTasksCompleted = 0;
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
		int numberOfShifts = routesOfShifts.size();
		this.routesOfShifts = new Vector<Vector <Route>>(numberOfShifts);
		for (int i = 0; i < numberOfShifts; i++) {
			// create a new period for this solution
			Vector<Route> solutionRoutesOfPeriodI = new Vector<Route>();
			this.routesOfShifts.add(solutionRoutesOfPeriodI);
			
			Vector<Route> networkRoutesInShiftI = routesOfShifts.elementAt(i);
			int numberOfRoutes = networkRoutesInShiftI.size();
			for (int j = 0; j < numberOfRoutes; j++) {
				Route current = networkRoutesInShiftI.elementAt(j);
				current.check();
				emptyDistance += current.emptyDistance;
				loadedDistance += current.loadedDistance;
				numberOfTasksCompleted++;
				solutionRoutesOfPeriodI.add(current.clone());
			}
		}
	}
	
	public int compareTo(Solution b) {
		return Double.compare(b.emptyDistance, this.emptyDistance);
	}
	
	public boolean betterThan(Solution b) {
		if (numberOfTasksCompleted > b.numberOfTasksCompleted) return true;
		else if (numberOfTasksCompleted < b.numberOfTasksCompleted) return false;
		
		double empty_rate_a = this.emptyDistance / (this.loadedDistance + this.emptyDistance);
		double empty_rate_b = b.emptyDistance / (b.loadedDistance + b.emptyDistance);
		if (Double.compare(empty_rate_a, empty_rate_b) < 0)
			return true;
		else
			return false;
	}
}
