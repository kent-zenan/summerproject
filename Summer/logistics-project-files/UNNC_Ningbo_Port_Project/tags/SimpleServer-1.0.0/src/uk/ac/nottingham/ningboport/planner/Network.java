package uk.ac.nottingham.ningboport.planner;

import java.util.Calendar;
import java.util.Vector;

import uk.ac.nottingham.ningboport.network.model.XMLSession;
import uk.ac.nottingham.ningboport.planner.Route.status;






public class Network {
	//Network definition
	public Vector<Node> nodes;
	public Node depot;
	private double travelingTimes[][];
	private double travelingTimesWithQueue[][];
	private double travelingDistances[][];
	public Vector<Vector <Route>> routesOfShifts;
	public int loadedVehilceFuelCost = 0, emptyVehicleFuelCost = 0;
	
	/*They are initialized together in createTimes()*/
	public Calendar periodStartTimes[];
	public Calendar periodEndTimes[];
	public Vector<Vector<Commodity>> mandatoryCommodities; // vector for each period
	public Vector<Vector<Commodity>> optionalCommodities;
	public int periodLength = -1;
	//public int nShifts = -1;
	/*Because the length of array is only known after network and parameter is read*/
	
	//----Commodity Settings----//
	// Contains all task generated from list
	public Vector<Task> taskSet; 
	public Vector<Commodity> commodities; // kept for quick linking
	
	//algorithm related
	public Calendar alg_stime;
	
	// ------------------------ Methods for initializing network ----------------------//
	public Network() {
		nodes = new Vector<Node>();
		taskSet = new Vector<Task>();
		commodities = new Vector<Commodity>();
		alg_stime = Calendar.getInstance();
		//commodities = new Vector<Commodity>();
	}
	
	/*
	 * Create starting/ending times for different periods
	 */
	public void createPeriodBoundaries(Calendar stime, int pLengthInHour, int nperiods) {
		mandatoryCommodities = new Vector<Vector<Commodity>>(nperiods);
		optionalCommodities = new Vector<Vector<Commodity>>(nperiods);
		periodLength = pLengthInHour;
		periodStartTimes = new Calendar[nperiods];
		periodEndTimes = new Calendar[nperiods];
		for (int i = 0; i < nperiods; i++) {
			periodStartTimes[i] = (Calendar) stime.clone();
			stime.add(Calendar.HOUR_OF_DAY, pLengthInHour);
			periodEndTimes[i] = (Calendar) stime.clone();
			mandatoryCommodities.add(new Vector<Commodity>());
			optionalCommodities.add(new Vector<Commodity>());
			//System.out.printf("period %d:\n\tStart:%s\n\tEnd:%s\n",
			//		i, periodStartTimes[i].getTime(), periodEndTimes[i].getTime());
		}
	}
	
	public void createEmptyRoutes(int numberOfRoutes) {
		routesOfShifts = new Vector<Vector <Route>>();
		for (int i = 0; i < periodStartTimes.length; i++) {
			Vector<Route> r = new Vector<Route>();
			routesOfShifts.add(r);
			for (int j = 0; j < numberOfRoutes; j++) {
				r.add(new Route(this, j, i));
			}
		}
		//System.out.printf("There are %d periods with %d routes in each \n", 
		//		routesOfPeriods.size(), routesOfPeriods.elementAt(0).size());
	}
	
	// -------------------------------- Shift tool -------------------------------------------/
	public int getNumberOfShifts() {
		if (routesOfShifts == null)
			return 0;
		
		return routesOfShifts.size();
	}
	
	// -------------------------------- Node / arc tool -------------------------------------------/
	public Node getNodeById(String id) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.elementAt(i).id.equals(id))
				return nodes.elementAt(i);
		}
		
		return null;
	}
	
	// traveling time
	public double getTravelingTime(Node a, Node b) {
		return getTravelingTime(a.index, b.index);
	}

	public double getTravelingTime(int indexA, int indexB) {
		return travelingTimes[indexA][indexB];
	}
	
	public void setTravelingTimesMatrix(double matrix[][]) {
		this.travelingTimes = matrix;
	}
	
	public void setTravelingTime(int indexA, int indexB, double value) {
		this.travelingTimes[indexA][indexB] = value;
	}
	
	// traveling time with queue
	public double getTravelingTimeWithQueue(Node a, Node b) {
		return getTravelingTimeWithQueue(a.index, b.index);
	}

	public double getTravelingTimeWithQueue(int indexA, int indexB) {
		return travelingTimesWithQueue[indexA][indexB];
	}
	
	public void setTravelingTimesWithQueueMatrix(double matrix[][]) {
		travelingTimesWithQueue = matrix;
	}
	
	public void setTravelingTimeWithQueue(int indexA, int indexB, double value) {
		travelingTimesWithQueue[indexA][indexB] = value;
	}
	
	// traveling distance
	public double getTravelingDistance(Node a, Node b) {
		return getTravelingDistance(a.index, b.index);
	}

	public double getTravelingDistance(int indexA, int indexB) {
		return this.travelingDistances[indexA][indexB];
	}
	
	public void setTravelingDistancesMatrix(double matrix[][]) {
		travelingDistances = matrix;
	}
	
	public void setTravelingDistance(int indexA, int indexB, double value) {
		travelingDistances[indexA][indexB] = value;
	}
	//---------------------------------- Methods for Task/Commodity manipulate ---------------------------//
	public int commodityIndex(String commodityID) {
		int csize = this.commodities.size();
		for (int i = 0; i < csize; i++) {
			if (commodities.get(i).id.equals(commodityID)) {
				return i;
			}
		}
		
		return -1;
	}
	
	public Commodity getCommodityById(String commodityID) {
		int csize = this.commodities.size();
		for (int i = 0; i < csize; i++) {
			if (commodities.get(i).id.equals(commodityID)) {
				return commodities.get(i);
			}
		}
		
		return null;
	}
	
	/*
	 * This classify commodities in nw.commodities
	 * into mandatory and optional for each period
	 */
	public void classifyCommodities() {
		int commodityQuantity = commodities.size();
		for (int i = 0; i < commodityQuantity; i++) {
			classifyCommodity(commodities.get(i));
		}
	}
	
	public void classifyCommodity(Commodity c) {
		if (c.tasks.isEmpty()) return; // Commodity can contain zero tasks -_-b 
		int numberOfShifts = this.getNumberOfShifts();
		
		for (int i = 0; i < numberOfShifts; i++) {
			Route tmp = new Route(this, 1, i);
			tmp.taskSet.add(c.tasks.firstElement());
			int res = tmp.check();
			if (res > 0)
				c.completableShifts.add(i);
			
		}
		
		if (c.completableShifts.size() > 0) {
			c.latestPeriod = c.completableShifts.lastElement();
			c.completableShifts.remove(c.completableShifts.lastElement());
			
			mandatoryCommodities.get(c.latestPeriod).add(c);
			for (int i = 0; i < c.completableShifts.size(); i++) {
				optionalCommodities.get(c.completableShifts.get(i)).add(c);
			}
		}
	}
	
	//Returns the time need to transport this commodity (without grabbed task)
	public double getCommodityTimeConsumption(Commodity c) {
		return travelingTimes[c.src.index][c.dest.index] + c.src.loadTime + c.dest.unloadTime;
	}
	
	
	
	public Task getTaskByID(String ID, boolean takeOutTask) {
		for (int i = 0; i < this.taskSet.size(); i++) {
			if (taskSet.get(i).cmdt.id.equals(ID)) {
				if (takeOutTask) 
					return taskSet.remove(i);
				else
					return taskSet.get(i);
			}
		}
		return null;
	}
	
	//get all TA of this period, also removes the related one in nw.ta
	// optionalTaskTimeLimit is within how many hours should the optional tasks be. 
	public Vector<Task> getPeriodTaskSet(int periodIndex, boolean mandatory) {
		Vector<Task> periodTaskSet = new Vector<Task>();
		Vector<Commodity> commoditySet;
		if (mandatory) commoditySet = mandatoryCommodities.elementAt(periodIndex);
		else commoditySet = optionalCommodities.elementAt(periodIndex);
		
		for (int i = 0; i < commoditySet.size(); i++) {
			Commodity c = commoditySet.elementAt(i);
			for (int j = 0; j < taskSet.size(); j++) {
				Task t = taskSet.elementAt(j);
				if (t.cmdt.id.equals(c.id)) {
					periodTaskSet.add(t);
					taskSet.remove(j--);
				}
			}
		}
		return periodTaskSet;
	}
	
	public void returnPeriodTasks(Vector<Task> detachedTA) {
		taskSet.addAll(detachedTA);
		detachedTA.clear();
//		while (!detachedTA.isEmpty()) {
//			if (detachedTA.get(0).gT != null) {
//				taskSet.add(detachedTA.get(0).gT);
//				detachedTA.get(0).gT = null;
//			}
//			taskSet.add(detachedTA.get(0));
//			detachedTA.remove(0);
//		}
	}
	
	public void combineLightTask(Vector<Task> unassignedTa) {
		//int count = 0;
		for (int i = 0; i < unassignedTa.size(); i++) {
			Task ti = unassignedTa.elementAt(i);
			if (ti.size == 2 || ti.gT != null)
				continue;

			for (int j = i + 1; j < unassignedTa.size(); j++) {
				Task tj = unassignedTa.elementAt(j);
				if (tj.size == 2 || tj.gT != null)
					continue;
				
				if (ti.cmdt.id.equals(tj.cmdt.id)) {
					ti.gT = tj;
					unassignedTa.remove(j);
					//count++;
					break;
				}
			}
		}
		//System.out.println("combined: " + count);
	}
	
	public void uncombineLightTask(Vector<Task> unassignedTA) {
		int tsize = unassignedTA.size();
		Vector<Task> TAtemp = new Vector<Task>();
		for (int i = 0; i < tsize; i++) {
			Task t = unassignedTA.get(i);
			if (t.gT != null) {
				TAtemp.add(t.gT);
				t.gT = null;
			}
		}
		unassignedTA.addAll(TAtemp);
	}
	
	/*
	 * Given a time value, set all finished TA (and ta that is being finished)
	 * as finished = true
	 */
	public void setFinishedTask(Calendar ctime) {
		if (!checkAllRoutes()) {
			RoutingPlanner.errormsg("Error setting finished ta: not all routes currect!");
			System.exit(0);
		}
		
		//If the vehicle is on its way to load commodity (deadheading)
		// what shall we do??
		int numberOfShifts = this.getNumberOfShifts();
		for (int i = 0; i < numberOfShifts; i++) {
			int nroutes = routesOfShifts.get(i).size();
			for (int j = 0; j < nroutes; j++) {
				routesOfShifts.get(i).get(j).setFinishedTA(ctime);
			}
		}
	}
	
	public Vector<Task> removeUnfinishedTask(int periodIndex) {
		Vector<Task> removedTaskSet = new Vector<Task>();
		
		Vector<Route> currentPeriod = routesOfShifts.elementAt(periodIndex);
		int rsize = currentPeriod.size();
		for (int i = 0; i < rsize; i++) {
			Route currentRoute = currentPeriod.elementAt(i);
			for (int j = 0; j < currentRoute.taskSet.size(); j++) {
				Task t = currentRoute.taskSet.get(j);
				Task gT = t.gT;
				if (!t.finished) {
					currentRoute.taskSet.remove(j--);
					taskSet.add(t);
					if (gT != null) {
						t.gT = null;
						taskSet.add(gT);
					}
				}
			}
		}
		
		return removedTaskSet;
	}

	//---------------------------- Solution saving/ restore ---------------------------//
	public boolean restoreSolution(Solution s) {
		if (s == null) return false;
		System.out.println("Restoring solution");
		
		this.routesOfShifts = s.routesOfShifts;
		this.taskSet = s.taskSet;
		return true;
	}
	
	// -----------------------------Info outputting ------------------------//
	
	public String getCommodityInfo() {
		String s = new String();
		s += "id\tsource node\tdest node\tsmall\tlarge\n";
		for (int i = 0; i < commodities.size(); i++) {
			s += commodities.elementAt(i) + "\n";
		}
		return s;
	}
	
	public String outputRoutes() {
		String output = "";
		for (int i = 0; i < routesOfShifts.size(); i++) {
			output += "----============= Period" + i + " with " + routesOfShifts.get(i).size() + " routes =============----\n";
			Vector<Route> currentPeriod = routesOfShifts.elementAt(i);
			for (int j = 0; j < currentPeriod.size(); j++) { 
				output += currentPeriod.elementAt(j).toString() + "\n";
			}
		}
		return output;
	}
	
	public String getCommodityCount() {
		String output = "";
		if (this.routesOfShifts == null || this.taskSet == null) {
			return "No commodity data read.";
		}
		
		output += "Tasks in unrouted task set: ";
		int unroutedTaskCount = 0;
		for (int i = 0; i < taskSet.size(); i++) {
			if (taskSet.get(i).gT == null)
				unroutedTaskCount++;
			else
				unroutedTaskCount += 2;
		}
		output += unroutedTaskCount + "\n";
		
		int routedTasksCount = 0, numberOfShifts = this.getNumberOfShifts();
		for (int p = 0; p < numberOfShifts; p++) {
			for (int r = 0; r < routesOfShifts.get(p).size(); r++) {
				Vector<Task> ts = routesOfShifts.get(p).get(r).taskSet;
				for (int t = 0; t < ts.size(); t++) {
					if (ts.get(t).gT == null) {
						routedTasksCount += 1;
					} else {
						routedTasksCount += 2;
					}
				}
			}
		}
		output += "Tasks routed: ";
		output += routedTasksCount + "\n";
		return output;
	}
	
	// -------------------------  Route Session Management    ----------------------------//
	public boolean assignSession(Route r, XMLSession s) {
		return false;
	}
	
	public boolean startSession(Route r) {
		if (r.getSession() == null) {
			return false;
		}
		
		r.setStatus(status.ASSIGNED);
		return true;
	}
	
	public boolean suspendSession(Route r) {
		if (r.getSession() == null) {
			return false;
		}
		
		r.setStatus(status.SESSION_SUSPENDED);
		return true;
	}
	
	public boolean resumeSession(Route r) {
		if (r.getSession() == null) {
			return false;
		}
		
		r.setStatus(status.ASSIGNED);
		return true;
	}
	
	public boolean endSession(Route r) {
		if (r.getSession() == null) {
			return false;
		}
		
		r.setStatus(status.SESSION_ENDED);
		return true;
	}
	
	// -------------------------  Route checking / evaluating ----------------------------//
	
	/*
	 * result array:
	 * [empty, loaded]
	 */
	public String getSolutionObjectiveValues() {
		String res = "=============== Objective Values ===============\n";
		int psize = routesOfShifts.size();
		double total_e = 0, total_l = 0;
		if (!this.checkAllRoutes()) {
			System.out.println("Route infeasibility occured!");
		}
		
		res += "Shift\tLD\tED\tLDR\n"; 
		for (int i = 0; i < psize; i++) {
			res += i +  "\t";
			double e = 0, l = 0;
			
			Vector<Route> shift = routesOfShifts.elementAt(i);
			int rsize = shift.size();
			
			for (int j = 0; j < rsize; j++) {
				e+= shift.elementAt(j).emptyDistance;
				l+= shift.elementAt(j).loadedDistance;
				total_e += shift.elementAt(j).emptyDistance;
				total_l += shift.elementAt(j).loadedDistance;
				
			}
			res += l + "\t" + e + "\t";
			res += (l / (e + l)) + "\n"; 
			
		}
		
		res += "Avg\t" + total_l + "\t" + total_e + "\t" + (total_l / (total_l + total_e)) + "\n";
		return res;
	}
	
	public String getShiftObjectiveValues(int shiftIndex) {
		String out = "";
		Vector<Route> shift = this.routesOfShifts.get(shiftIndex);
		double e = 0, l = 0;
		int rsize = shift.size();
		
		for (int j = 0; j < rsize; j++) {
			e += shift.elementAt(j).emptyDistance;
			l += shift.elementAt(j).loadedDistance;
		}
		out += l + "\t" + e + "\t" + (l / (e + l)); 
		return out;
	}
	
	public boolean checkAllRoutes() {
		int psize = routesOfShifts.size();
		for (int i = 0; i < psize; i++) {
			Vector<Route> r = routesOfShifts.elementAt(i);
			int rsize = r.size();
			for (int j = 0; j < rsize; j++) {
				int checkResult = r.elementAt(j).check();
				if (checkResult < 1)
					return false;
			}
		}
		
		return true;
	}
		
	/*
	 * Updates: emptyDistance, loadedDistance
	 */
	public void updateObjectiveValues(Route r) {
		Vector<Task> taskSet = r.taskSet;
		r.emptyDistance = r.loadedDistance = 0;
		
		for (int i = 0; i < taskSet.size(); i++) {
			Task taskI = r.taskSet.elementAt(i);
			Commodity c = taskI.cmdt;
			int srcI = c.src.index, 
					destI = c.dest.index, 
					depotI = depot.index;
			
			//find and check grabbed commodity
			Commodity g = null;
			int gSrcI = 0, gDestI = 0;
			if (taskI.gT != null) {
				g = taskI.gT.cmdt;
				gSrcI = g.src.index;
				gDestI = g.dest.index;
				if (taskI.size == 2 || taskI.size == 2) {
					RoutingPlanner.errormsg("Checking Route: Error, overweight when grabbing additional ta.");
					System.exit(0);
				}
			}

			//dead heading to source node.
			if (i == 0) { //depot to src
				r.emptyDistance += travelingDistances[depotI][srcI];
				
			} else {//dest to src
				Commodity c2 = r.taskSet.elementAt(i - 1).cmdt;
				if (!c.src.equals(c2.dest)) {
					r.emptyDistance += travelingDistances[c2.dest.index][srcI];
				}
			}
			
			// load time of commodity c
			if (g == null) {
				//src -> dest
				r.loadedDistance += travelingDistances[srcI][destI];
			} else {
				//src -> gSrc
				r.loadedDistance += travelingDistances[srcI][gSrcI];
				
				//gSrc -> gDest
				r.loadedDistance += travelingDistances[gSrcI][gDestI];
			
				//gDest -> dest
				r.loadedDistance += travelingDistances[gDestI][destI];
			}
			
			// if last task, go back to depot
			if (i == r.taskSet.size() - 1) {  //dest to depot
				r.emptyDistance += travelingDistances[destI][depotI];
			}
		}
		
	}
}
