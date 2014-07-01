package uk.ac.nottingham.ningboport.planner.Algorithms;

import java.util.Vector;

import uk.ac.nottingham.ningboport.planner.Network;
import uk.ac.nottingham.ningboport.planner.Route;
import uk.ac.nottingham.ningboport.planner.RoutingPlanner;
import uk.ac.nottingham.ningboport.planner.Task;

public class NFunc {
	
	private Network nw;
	
	NFunc(Network nw) {
		this.nw = nw;
	}
	
	/****************************************************
	 * Neighbourhood functions are defined below:       *
	 ****************************************************/
	
	public boolean moveSingleRoute(Route a, int index, int slot) {
		Vector<Task> taskSet = a.taskSet;
		@SuppressWarnings("unchecked")
		Vector<Task> taskSetBak = (Vector<Task>) a.taskSet.clone();
		if (index == slot || slot == index + 1) {
			return false;
		}
		if (index >= taskSet.size() || index < 0) {
			RoutingPlanner.errormsg("Error moving from node index " + index + " from route " + a);
			return false;
		}
		
		if (slot > taskSet.size() || slot < 0) {
			RoutingPlanner.errormsg("Error moving to node index " + slot + " of route " + a);
			return false;
		}
		
		Task t = taskSet.elementAt(index);
		taskSet.add(slot, t);
		if (taskSet.get(index) == t) {
			taskSet.remove(index);
		} else {
			taskSet.remove(index + 1);
		}
		
		if (a.check() > 0) {
			return true;
		} else {
			a.taskSet = taskSetBak;
			return false;
		}
	}
	
	/*
	 * Moving a node from route a to route b
	 */
	public boolean moveTask(Route a, int index_a, Route b, int index_b) {
		if (a.equals(b)) {
			return moveSingleRoute(a, index_a, index_b);
			//return false;
		}
		//System.out.println("before move:" + "\n" + a + "\n" + b);
		
		if (index_a >= a.taskSet.size()) {
			RoutingPlanner.errormsg("Error moving from node index " + a + " from route " + a);
			return false;
		}
		
		if (index_b > b.taskSet.size()) {
			RoutingPlanner.errormsg("Error moving to node index " + b + " of route " + b);
			return false;
		}
		
		Task t = a.taskSet.elementAt(index_a);
		b.taskSet.insertElementAt(t, index_b);
		a.taskSet.remove(index_a);
		if (b.check() > 0 && a.check() > 0) {
			//System.out.println("after move(success): \n" + a + "\n" + b);
			//DON'T move a.ta.remove to here! cuz routes are just checked!
			return true;
		} else {
			a.taskSet.insertElementAt(t, index_a);
			b.taskSet.remove(index_b);
			//System.out.println("after move(fail): \n" + a + "\n" + b);
			return false;
		}
	}
	
	private boolean swapTaskSingleRoute(Route a, int index_a, int index_b) {
		if (index_a == index_b)
			return false;
		
		if (index_a >= a.taskSet.size()) {
			RoutingPlanner.errormsg("Single: Error swapping node index_a " + index_a + " from route " + a);
			return false;
		}
		
		if (index_b >= a.taskSet.size()) {
			RoutingPlanner.errormsg("Single: Error swapping to node index_b " + index_b + " of route " + a);
			return false;
		}
		
		Task t1 = a.taskSet.elementAt(index_a), 
				t2 = a.taskSet.elementAt(index_b);
		
		a.taskSet.set(index_a, t2);
		a.taskSet.set(index_b, t1);
		
		if (a.check() > 0) {
			return true;
		} else {
			a.taskSet.set(index_a, t1);
			a.taskSet.set(index_b, t2);
			return false;
		}
	}
	
	public boolean swapTask(Route a, int index_a, Route b, int index_b) {
		if (a.equals(b)) {
			return swapTaskSingleRoute(a, index_a, index_b);
		}
		if (index_a >= a.taskSet.size()) {
			RoutingPlanner.errormsg("Error swapping node index(a side) " + index_a + " from route " + a);
			return false;
		}
		
		if (index_b >= b.taskSet.size()) {
			RoutingPlanner.errormsg("Error swapping to node index(b side) " + index_b + " of route " + b);
			return false;
		}
		
		Task t1 = a.taskSet.elementAt(index_a), 
				t2 = b.taskSet.elementAt(index_b);
		
		/*a.ta.remove(index_a);
		b.ta.remove(index_b);
		a.ta.add(index_a, t2);
		b.ta.add(index_b, t1);*/
		a.taskSet.set(index_a, t2);
		b.taskSet.set(index_b, t1);
		if (a.check() > 0 && b.check() > 0) {
			return true;
		} else {
			/*a.ta.remove(index_a);
			b.ta.remove(index_b);
			a.ta.add(index_a, t1);
			b.ta.add(index_b, t2);*/
			a.taskSet.set(index_a, t1);
			b.taskSet.set(index_b, t2);
			return false;
		}
		
	}
	
	private boolean grabTASingleRoute(Route a, int index_a, int index_b) {
		if (index_a == index_b) {
			return false;
		}
		Task ta1 = a.taskSet.elementAt(index_a),
				ta2 = a.taskSet.elementAt(index_b);
		//already grabbed, skip
		if (ta1.gT != null || ta2.gT != null) {
			return false;
		}
		//check weight
		if (ta1.size == 2 || ta2.size == 2) {
			//System.out.println("grabTA: overloaded, skip");
			return false;
		}
		//grab
		ta1.gT = ta2;
		a.taskSet.remove(index_b);
		
		if (a.check() > 0) {
			return true;
		} else {
			ta1.gT = null;
			a.taskSet.insertElementAt(ta2, index_b);
			return false;
		}
	}
	
	public boolean grabTA(Route a, int index_a, Route b, int index_b) {
		if (a.equals(b)) {
			return grabTASingleRoute(a, index_a, index_b);
		}
		Task ta1 = a.taskSet.elementAt(index_a),
				ta2 = b.taskSet.elementAt(index_b);
		//already grabbed, skip
		if (ta1.gT != null || ta2.gT != null) {
			return false;
		}
		//check weight
		if (ta1.size == 2 || ta2.size == 2) {
			//System.out.println("grabTA: overloaded, skip");
			return false;
		}
		//grab
		ta1.gT = ta2;
		b.taskSet.remove(index_b); // remove grabbed
		//check route.
		if (a.check() > 0 && b.check() > 0) {
			return true;
		} else {
			// restore grabbed
			ta1.gT = null;
			b.taskSet.insertElementAt(ta2, index_b);
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean twoOpt(Route a, int slotA, Route b, int slotB, boolean execute) {
		if (a.equals(b)) {
			return false;
		}
		
		Vector<Task> oldA = (Vector<Task>) a.taskSet.clone(),
				oldB = (Vector<Task>) b.taskSet.clone();
		
		Vector<Task> newA = new Vector<Task>(),
				newB = new Vector<Task>();

		newA.addAll(a.taskSet.subList(0, slotA + 1));
		newA.addAll(b.taskSet.subList(slotB + 1, b.taskSet.size()));
		newB.addAll(b.taskSet.subList(0, slotB + 1));
		newB.addAll(a.taskSet.subList(slotA + 1, a.taskSet.size()));
		a.taskSet = newA;
		b.taskSet = newB;
		if (a.check() > 0 && b.check() > 0) {
			if (!execute) {
				a.taskSet = oldA;
				b.taskSet = oldB;
			}
			
			return true;
		} else {
			a.taskSet = oldA;
			b.taskSet = oldB;
			return false;
		}
	}
	
	public void optimizeAllRoutes() {
		int numberOfShifts = nw.getNumberOfShifts();
		for (int i = 0; i < numberOfShifts; i++) {
			this.optimizeRouteInPeriod(i);
		}
	}
	
	public void optimizeRouteInPeriod(int periodIndex) {
		Vector<Route> routesOfPeriod = nw.routesOfShifts.get(periodIndex);
		for (int i = 0; i < routesOfPeriod.size(); i++) {
			optimizeRoute(routesOfPeriod.get(i));
		}
	}
	
	/*
	 * find the best sequence of the task for a give route
	 */
	public void optimizeRoute(Route r) {
		if (r.check() < 1) {
			System.out.println("Route optimizer: You gave me a bad route to optimize");
		}
		Route empty = r.clone();
		empty.taskSet.clear();
		findFullRoutePerm(empty, r.taskSet, r);
	}
	
	@SuppressWarnings("unchecked")
	private void findFullRoutePerm(Route r, Vector<Task> taskSet, Route bestR) {
		for (int i = 0; i < taskSet.size(); i++) {
			Route nextRoute = r.clone();
			Vector<Task> nextTaskSet = (Vector<Task>) taskSet.clone();
			nextRoute.taskSet.add(nextTaskSet.remove(i));
			
			if (nextRoute.check() > 0) {
				if (nextTaskSet.size() == 0) { 
					if (Double.compare(nextRoute.emptyDistance, bestR.emptyDistance) < 0) {
						bestR.taskSet = nextRoute.taskSet;
						bestR.emptyDistance = nextRoute.emptyDistance;
						//System.out.println("new best route found");
					}
				} else {
					findFullRoutePerm(nextRoute, nextTaskSet, bestR);
				}
			}
		}
		
	}
	
	/*
	 * Optimizes a sub route,
	 * starts with task at 'start', and ends at task at 'end'
	 * [start, end]
	 */
	public void optimizePartialRoute(Route r, int start, int end) {
		if (r.check() < 1) {
			System.out.println("Route optimizer: You gave me a bad route to optimize");
			return;
		}
		
		int r_size = r.taskSet.size();
		
		if (r_size == 0)
			return;
		
		if (start < 0 || start > r_size ||
				end < 0 || end > r_size || start > end) {
			System.out.println("optimize partial route: bad index");
			return;
		}
		
		Route initial = r.clone();
		//initial.taskSet.clear();
		Vector<Task> taskSet = new Vector<Task>();
		Vector<Task> tail = new Vector<Task>();
		
		for (int i = r_size - 1; i > end; i--) {
			tail.add(initial.taskSet.remove(i));
		}
		
		for (int i = start; i <= end; i++) {
			taskSet.add(initial.taskSet.remove(start));
		}
		
		findRoutePerm(initial, taskSet, tail, r);
	}
	
	/*
	 * find all possible sequences of tasks in a route
	 * task in 'tail' is added to the end of the route when checking route
	 */
	@SuppressWarnings("unchecked")
	private void findRoutePerm(Route initial, Vector<Task> taskSet,
			Vector<Task> tail, Route bestR) {
		for (int i = 0; i < taskSet.size(); i++) {
			Route testRoute = initial.clone(), nextTestRoute = initial.clone();
			Vector<Task> nextTaskSet = (Vector<Task>) taskSet.clone();
			nextTestRoute.taskSet.add(nextTaskSet.get(i));
			testRoute.taskSet.add(nextTaskSet.remove(i));
			if (tail != null) {
				testRoute.taskSet.addAll(tail);
			}
			
			if (testRoute.check() > 0) {
				if (nextTaskSet.size() == 0) { 
					if (Double.compare(testRoute.emptyDistance, bestR.emptyDistance) < 0) {
						bestR.taskSet = testRoute.taskSet;
						bestR.emptyDistance = testRoute.emptyDistance;
						//System.out.println("new best route found");
					}
				} else {
					findRoutePerm(nextTestRoute, nextTaskSet, tail, bestR);
				}
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public boolean cross(Route a, int startA, int lengthA, Route b, int startB, int lengthB, boolean executeCross) {
		if (a == b) return false;
//		String out = "Before:\n";
//		out += a + "\n" + b + "\n";
		
		Vector<Task> taskSetA = a.taskSet, taskSetB = b.taskSet;
		Vector<Task> backUpA = (Vector<Task>) taskSetA.clone(), backUpB = (Vector<Task>) taskSetB.clone();
		
		Vector<Task> aMid = new Vector<Task>(),
				bMid = new Vector<Task>();
		for (int i = 0; i < lengthA; i++) {
			aMid.add(taskSetA.remove(startA));
		}
		
		for (int i = 0; i < lengthB; i++) {
			bMid.add(taskSetB.remove(startB));
		}
		//switch segment
		taskSetA.addAll(startA, bMid);

		taskSetB.addAll(startB, aMid);
		
		
		//check route
		if(a.check() > 0 && b.check() > 0) {
			if (!executeCross) {
				a.taskSet = backUpA;
				b.taskSet = backUpB;
				return true;
			}

//			out += "After\n";
//			out += a + "\n" + b + "\n";
//			System.out.println(out);
//			System.out.println(startA + " " + lengthA + " " + startB + " " + lengthB);
			return true;
		} else {
			a.taskSet = backUpA;
			b.taskSet = backUpB;
			return false;
		}
	}
}