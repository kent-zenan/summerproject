package uk.ac.nottingham.ningboport.planner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Vector;

import uk.ac.nottingham.ningboport.planner.statistics.StatisticsAnalyzer;

public class RealWorldSolutionReader {
	private Network nw;
	private Vector<String[]> solutionFile = new Vector<String[]>();
	// format: VehicleId  driverName source dest size quantity stime etime
	//         0          1          2      3    4    5        6     7
	// id availTime deadline shipDockTime type
	// 8  9         10       11           12
	
	public RealWorldSolutionReader(Network nw) {
		this.nw = nw;
	}
	
	public boolean readSolution(String path) throws IOException {
		solutionFile.clear();
		
		BufferedReader br;
		br = new BufferedReader(new FileReader(path));
		String lineStr;
		while((lineStr = br.readLine()) != null) {
			String[] line = lineStr.split("\t");
			if (line.length == 13)
				solutionFile.add(line);
			else
				System.out.println("Error in line: " + lineStr);
		}
		solutionFile.remove(0);
		parseCommodities();
		
		System.out.println(nw.commodities.size() + " commodity parsed from existing solution.");
		br.close();
		useParsedRoutesAsTaskSet(parseVehicleRoutes());

		StatisticsAnalyzer sa = new StatisticsAnalyzer(nw);
		RoutingPlanner.addToOutput("------ Real world Solution ------\n");
		//RoutingPlanner.addToOutput(nw.getNodeThroughputDeadlineStatistics(Task.sortingMethod.DEADLINE));
		RoutingPlanner.addToOutput(sa.getNodeThroughputStatistics());
		RoutingPlanner.addToOutput(sa.getSolutionFlowVectorStatistics());
		RoutingPlanner.addToOutput(nw.outputRoutes());
		RoutingPlanner.addToOutput(nw.getSolutionObjectiveValues());
		//RoutingPlanner.addToOutput(nw.getRouteAppearanceRate());
		
		for (int i = 0; i < nw.routesOfShifts.size(); i++) {
			Vector<Route> rs = nw.routesOfShifts.get(i);
			for (int j = 0; j < rs.size(); j++) {
				rs.get(j).taskSet.clear();
			}
		}
		
		nw.classifyCommodities();
		RoutingPlanner.addToOutput(sa.getTaskCountStatistics());
		RoutingPlanner.addToOutput(sa.getArcUsage());
		RoutingPlanner.addToOutput("------ End of Real world Solution ------\n\n\n\n\n");
		return true;
	}
	
	private void parseCommodities() {
		int flen = solutionFile.size();
		for (int i = 0; i < flen; i++) {
			String[] line = solutionFile.get(i);
			if (line.length != 13)
				continue;
			//create task
			Task t = new Task();
			Commodity c = nw.getCommodityById(line[8]);
			
			Node src = nw.getNodeById(line[2]),
					dest = nw.getNodeById(line[3]);
			
			//actual start time 
			t.actualStartT  = RoutingPlanner.readCalString(line[6]);
			
			if (src == null || dest == null || t.actualStartT == null) {
				continue;
			}
			
			//if commodity not exist
			if (c == null) {
				Calendar avail = RoutingPlanner.readCalString(line[9]);
				if (avail == null) {
					avail = (Calendar) nw.periodStartTimes[0].clone();
				}
				
				Calendar dead = RoutingPlanner.readCalString(line[11]);
				
				if (dead == null)
					dead = RoutingPlanner.readCalString(line[10]);
				
				if (dead == null)
					dead = (Calendar) nw.periodEndTimes[nw.getNumberOfShifts() - 1].clone();
				
				c = new Commodity(line[8], avail, dead, 
						src, dest, 0, 0);
				nw.commodities.add(c);
			}
			t.cmdt = c;
			c.tasks.add(t);
			
			//Finish Time
			t.actualFinishT = RoutingPlanner.readCalString(line[7]);
			if(t.actualFinishT == null) {
				System.out.println("Warning: no actual finish time available, I will estimate.");
				t.actualFinishT = (Calendar) t.actualStartT.clone();
				t.actualFinishT.add(Calendar.MINUTE, (int) nw.getCommodityTimeConsumption(c));
			}
			
			if (Integer.parseInt(line[4]) == 40) {//large container
				t.size = 2;
				c.large++;
			} else {
				t.size = 1;
				c.small++;
			}
			
			if (Integer.parseInt(line[5]) == 2) { // quantity
				Task t2 = t.clone();
				t.gT = t2;
				c.tasks.add(t2);
				c.small++;
			}
			
			// Driver name and Vehicle
			t.vehicleID = line[0];
			t.driver = line[1];
			
			nw.taskSet.add(t);
		}
	}
	
	private Vector<Route> parseVehicleRoutes() {
		int numberOfShifts = nw.getNumberOfShifts();
		@SuppressWarnings("unchecked")
		Vector<Task> nwTaskSet = (Vector<Task>) nw.taskSet.clone();
		int routeID = 0;
		Vector<Route> routesParsed = new Vector<Route>();
		while (!nwTaskSet.isEmpty()) {
			routeID++;
			String currentVehicleID = nwTaskSet.get(0).vehicleID;
			Vector<Task> vehicleTaskSet = new Vector<Task>();
			//get all tasks for the same vehicle
			for (int i = 0; i < nwTaskSet.size(); i++) {
				if (nwTaskSet.get(i).vehicleID.equals(currentVehicleID)) {
					vehicleTaskSet.add(nwTaskSet.remove(i--));
				}
			}
			
			//System.out.println(solutionFileLine[0] + " has " + taskSet.size() + " tasks.");
			if (vehicleTaskSet.size() == 0) // Fix 1: array out of bound
				continue;

			//sort taskSet using ACTUAL_STARTING_TIME
			Task.sortBy = Task.sortingMethod.ACTUAL_STARTING_TIME;
			Collections.sort(vehicleTaskSet);
			
			// Assigning 
			//String driverName = routeTaskSet.get(0).driver; // Bug 1: Array out of bound
			int periodNumber = 0;
			
			Calendar shiftStart = nw.periodStartTimes[periodNumber];
			Calendar shiftEnd = nw.periodEndTimes[periodNumber];
			Route r = new Route(nw, routeID, periodNumber);
			routesParsed.add(r);
			
			while (!vehicleTaskSet.isEmpty()) {
				Task currentTask = vehicleTaskSet.get(0);
				if (currentTask.actualFinishT.before(shiftStart)) {
					vehicleTaskSet.remove(0);
					continue;
					
				} else if (currentTask.actualFinishT.after(shiftEnd)) {
					periodNumber++;
					if (periodNumber >= numberOfShifts) {
						break;
					}
					shiftStart = nw.periodStartTimes[periodNumber];
					shiftEnd = nw.periodEndTimes[periodNumber];
					
					r = new Route(nw, routeID, periodNumber);
					routesParsed.add(r);
				}
				
				r.taskSet.add(vehicleTaskSet.remove(0));
			}
		
		}
		 
		for (int i= 0; i < numberOfShifts; i++) {
			Vector<Route> rs = nw.routesOfShifts.get(i);
			rs.clear();
			for (int j = 0; j < routesParsed.size(); j++) {
				if (routesParsed.get(j).period == i) {
					rs.add(routesParsed.get(j));
				}
			}
		}
		
		System.out.println("Got " + routesParsed.size() + " routes from the real world file.");
		return routesParsed;
	}
	
	public void useParsedRoutesAsTaskSet(Vector<Route> routesParsed) {
		nw.taskSet.clear();
		for (int i = 0; i < routesParsed.size(); i++) {
			nw.taskSet.addAll(routesParsed.get(i).taskSet);
		}
		System.out.println("Number of tasks: " + nw.taskSet.size());
	}
	
}
