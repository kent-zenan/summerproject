package uk.ac.nottingham.ningboport.planner.Algorithms;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import uk.ac.nottingham.ningboport.planner.Network;
import uk.ac.nottingham.ningboport.planner.Route;
import uk.ac.nottingham.ningboport.planner.RoutingPlanner;
import uk.ac.nottingham.ningboport.planner.Task;

class InsertAction implements Comparable<InsertAction> {
	public int slot;
	public Route r;
	public Task t;
	public double score;
	
	public InsertAction(Route r, Task t) {
		this.r = r;
		this.t = t;
		this.score = -1;
		this.slot = -1;
	}

	public int compareTo(InsertAction o) {
		if (this.score > o.score) 
			return 1;
		
		if (this.score < o.score)
			return -1;
		
		return 0;
		//return Double.compare(this.score, score);
	}
}

public class Insertion {
	private Network nw;
	private boolean mandatoryInsertBest, optionalInsertBest;
	
	int currentPeriodIndex;
	Vector<Route> currentPeriod;
	public Insertion(Network nw, boolean pickBestMan, boolean pickBestOpt) {
		this.nw = nw;
		this.mandatoryInsertBest = pickBestMan;
		this.optionalInsertBest = pickBestOpt;
	}
	
	/*
	 * Initialize EMPTY routes with a most emergent task 
	 */
	private void initializeRoutes(Vector<Task> tasks) {
		Object[] t = tasks.toArray();
		//Task.sortBy = Task.sortingMethod.AVAIL_TIME;
		Task.sortBy = Task.sortingMethod.DEADLINE;
		Arrays.sort(t);
		
		for (int i = 0; i < t.length & i < currentPeriod.size(); i++) { 
			Route r = currentPeriod.elementAt(i);
			if (!r.taskSet.isEmpty())
				continue;
			
			r.insert((Task) t[i], 0, tasks);

			int check = r.check();
			if (check != 1) {
				System.out.println("Failed to give initial node at route " + i + 
						" check code " + check);
				System.out.println("Commodity: " + r.taskSet.get(0).cmdt);
				r.remove(0, tasks);
				break;
			}
		}
	}
	
	/*
	 * 1. choose first node for all routes.
	 *
	 * 2. loop until no more nodes
	 *     2.1 calculate best insertion for all routes. generate a ta list
	 *     2.2 evaluate these insertions by a criteria
	 *     2.3 execute insertion
	 *     2.4 if there are ta not inserted, go back to 2.1
	 */
	public void run() {
		System.out.println("=== Insertion Started ===");
		
		int numberOfShifts = nw.getNumberOfShifts();
		for (int i = 0; i < numberOfShifts; i++) {
			currentPeriodIndex = i;
			currentPeriod = nw.routesOfShifts.elementAt(i);
			//Mandatory tasks for this period first
			Vector<Task> mandatory = nw.getPeriodTaskSet(i, true);
			//nw.combineLightTask(mandatory);
			initializeRoutes(mandatory);
			while (mandatory.size() != 0) {
				if (!insertActionsToRoutes(mandatory, this.mandatoryInsertBest)) {
					break;
				}
			}
			System.out.println("mandatory remainning: " + mandatory.size());
			//nw.uncombineLightTask(mandatory);
			nw.returnPeriodTasks(mandatory);
			
			for (int j = i + 1; j < i + 8 && j <= numberOfShifts; j++) {
				Vector<Task> optional;
				if (j < numberOfShifts) {
					optional = nw.getPeriodTaskSet(j, true);
				} else {
					optional = nw.getPeriodTaskSet(j - 1, false);
				}
				//nw.combineLightTask(optional);
				initializeRoutes(optional);
				while (optional.size() != 0) {
					if (!insertActionsToRoutes(optional, this.optionalInsertBest)) {
						break;
					}
				}
				System.out.println("optional remainning on period " + j + ": " + optional.size());
				//nw.uncombineLightTask(optional);
				nw.returnPeriodTasks(optional);
			} // optional routing complete
			
		}
	}
	
	/*
	 * Try for all commodities on every route.
	 * choose the best insertion slot and get the insertActions
	 * with a score.
	 * 
	 * for each route, do the best insertion
	 */
	private boolean 
	insertActionsToRoutes(Vector<Task> unnassigned, boolean only_best) {
		Vector<InsertAction> ias = new Vector<InsertAction>();
		for (int i = 0; i < unnassigned.size(); i++) {
			for (int j = 0; j < currentPeriod.size(); j++) {
				InsertAction ia = findBestSlot(unnassigned.elementAt(i), currentPeriod.elementAt(j));
				if (ia.slot != -1) {
					if (only_best) {
						if (ias.size() == 0 || Double.compare(ia.score, ias.lastElement().score) < 0) {
							ias.removeAllElements();
							ias.add(ia);
						}
					
					} else {
						ias.add(ia);
					}
				}
			} // currentPeriod.size
		} // uta.size
		
		if (ias.size() == 0) {
			return false;
		}
		
		if (only_best) {
			InsertOneAction(ias, unnassigned);
			
		} else {
			Collections.sort(ias);
			
			while(ias.size() > 0)
				InsertOneAction(ias, unnassigned);
		}
		return true;
	}

	private void InsertOneAction(Vector<InsertAction> ias, Vector<Task> uta) {
		InsertAction ia = ias.elementAt(0);
		ia.r.insert(ia.t, ia.slot, uta);
		//System.out.println("Inserted one." + ia.t.cmdt);
		// remove actions that has same ta and route.
		for (int i = 0; i < ias.size(); i++) {
			InsertAction ia2 = ias.elementAt(i);
			if (ia2.t.equals(ia.t) || ia2.r.equals(ia.r)) {
				ias.remove(i--);
			}
		}
	}
	
	/*
	 * Finds best slot for action t at route.
	 * best slot is the minimum score
	 */
	private InsertAction findBestSlot(Task t, Route r) {
		if (r.check() != 1) { //Failed
			RoutingPlanner.errormsg("find slot: Bad route");
			System.exit(0);
		}
		
		InsertAction ia = new InsertAction(r, t);
		ia.score = 99999;// minimize problem, so give large initial number
		Vector<Task> ta = r.taskSet;
		int taSize = ta.size();
		
		//old Values
		double loadedDistanceO = r.loadedDistance;
		double emptyDistanceO = r.emptyDistance;
		double softTwPenalty = r.softTimeWindowPenalty;
		//double waitTimeO = r.waitTime;
//		Calendar startTO[] = new Calendar[taSize],
//				finishTO[] = new Calendar[taSize];
//		for (int i = 0; i < taSize; i++) {
//			startTO[i] = ta.elementAt(i).startT;
//			finishTO[i] = ta.elementAt(i).finishT;
//		}
		
		//Slot test
		double score;
		for (int i = taSize; i >= 0; i--) {
			if (i != taSize && ta.get(i).finished) {
				continue;
			}
			
			ta.add(i, t);
			if (r.check() != 1) { //Failed
				ta.remove(i);
				continue;
			}
			
			score = (r.emptyDistance + r.softTimeWindowPenalty )/ (r.emptyDistance + r.loadedDistance) 
					- (emptyDistanceO + softTwPenalty) / (emptyDistanceO + loadedDistanceO);
			
			if (score < ia.score) {
				ia.score = score;
				ia.slot = i;
			}
			
			ta.remove(i);
		}
		return ia;
	}
}
