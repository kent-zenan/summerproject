package uk.ac.nottingham.ningboport.planner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

import uk.ac.nottingham.ningboport.planner.Algorithms.Insertion;
import uk.ac.nottingham.ningboport.planner.Algorithms.VNS;

public class RTSimulator {
	public Vector<Task> ta = new Vector<Task>();
	public Vector<Vector<Commodity>> mandatoryCommodities; // vector for each period
	public Vector<Vector<Commodity>> optionalCommodities;
	private Network nw;
	private Calendar eventTime;
	private Vector<Route> delayedRoute = new Vector<Route>();
	private Vector<Integer> delayedAmount = new Vector<Integer>();
	
	public void startRoutingSimulation() {
		System.out.println("----------------RT EVENT@-------------------!");
		nw.setFinishedTask(eventTime);
		int numberOfShifts = nw.getNumberOfShifts();
		for (int i = 0; i < numberOfShifts; i++) {
			nw.removeUnfinishedTask(i);
		}
		
		nw.taskSet.addAll(this.ta);
		for (int i = 0; i < mandatoryCommodities.size(); i++) {
			nw.mandatoryCommodities.get(i).addAll(mandatoryCommodities.get(i));
			nw.optionalCommodities.get(i).addAll(optionalCommodities.get(i));
		}
		
		for (int i = 0; i < delayedRoute.size(); i++) {
//			System.out.printf("Setting Route %d at period %d with delay %d\n",
//					delayedRoute.get(i).id, delayedRoute.get(i).period, delayedAmount.get(i));
			delayedRoute.get(i).setRouteDelay(delayedAmount.get(i));
		}
		
		
		Insertion ins2 = new Insertion(nw, true, false);
		ins2.run();
		VNS ts2 = new VNS(nw);
		ts2.run(25);
	}
	
	public void readFile(String path, Network network) throws IOException {
		this.nw = network;
		int numberOfShifts = nw.getNumberOfShifts();
		mandatoryCommodities = new Vector<Vector<Commodity>>();
		optionalCommodities = new Vector<Vector<Commodity>>();
		for (int i = 0; i < numberOfShifts; i++) {
			mandatoryCommodities.add(new Vector<Commodity>());
			optionalCommodities.add(new Vector<Commodity>());
		}
		BufferedReader br;
		br = new BufferedReader(new FileReader(path));
		Vector<String> rtFileSV = new Vector<String>();
		String lineStr;
		while((lineStr = br.readLine()) != null) {
			rtFileSV.add(lineStr);
		}
		br.close();
		parseRTFile(rtFileSV);
	}
	
	private void parseRTFile(Vector<String> rtFileSV) {
		boolean parsingDelays = false;
		Vector<String> delayLines = new Vector<String>();
		boolean parsingCommodities = false;
		Vector<String> commodityLines = new Vector<String>();
		for (int i = 0; i < rtFileSV.size(); i++) {
			String as[] = rtFileSV.get(i).split("\t| ");
			if (as[0].equals("#event_time")) {
				eventTime = RoutingPlanner.readCalString(rtFileSV.get(i+1));
			} else if (as[0].equals("#delays")) {
				parsingDelays = true;
				parsingCommodities = false;
			} else if (as[0].equals("#additional")) {
				parsingCommodities = true;
				parsingDelays = false;
			} else {
				if (parsingDelays)
					delayLines.add(rtFileSV.get(i));
				else if (parsingCommodities)
					commodityLines.add(rtFileSV.get(i));
				//else
				//	System.out.println("skipped: " + rtFileSV.get(i));
			}
		}
//		for (int i = 0; i < delayLines.size(); i++) {
//			System.out.println(delayLines.get(i));
//		}
//		
//		for (int i = 0; i < commodityLines.size(); i++) {
//			System.out.println(commodityLines.get(i));
//		}
		parseCommodity(commodityLines);
		parseRouteDelay(delayLines);
	}
	
	private int eventInPeriod(Calendar eventTime) {
		int numberOfShifts = nw.getNumberOfShifts();
		for (int i = 0; i < numberOfShifts; i++) {
			if (eventTime.after(nw.periodStartTimes[i])
					&& eventTime.before(nw.periodEndTimes[i])) {
				return i;
			}
		}
		return -1;
	}
	
	private void parseRouteDelay(Vector<String> delayLines) {
		int eventPeriodIndex = eventInPeriod(eventTime);
		//System.out.println("Event in period " + eventPeriodIndex);
		if (eventPeriodIndex < 0)
			return;
		Vector<Route> eventPeriod = nw.routesOfShifts.get(eventPeriodIndex);
		for (int i = 0; i < delayLines.size(); i++) {
			String s[] = delayLines.get(i).split("\t| ");
			if (s.length != 2) {
				RoutingPlanner.errormsg("Error in Route delay definition: \n\t" + delayLines.get(i));
				continue;
			}
			
			delayedRoute.add(eventPeriod.get(new Integer(s[0])));
			delayedAmount.add(new Integer(s[1]));
		}
	}
	
	private void parseCommodity(Vector<String> cdata) {
		Calendar allPeriodsStartTime = nw.periodStartTimes[0]; // Earliest time
		Calendar allPeriodsDeadline = nw.periodEndTimes[nw.periodEndTimes.length - 1]; // Latest Time
		cdata.remove(0);
		for (int i = 0; i < cdata.size(); i++) {
			String ts[] = cdata.elementAt(i).split("\t");
				
			if (ts.length != 7) {
				RoutingPlanner.errormsg("Wrong RTdata file! " + ts.length);
				System.exit(0);
			}
			
			Node from = Node.findByID(ts[1], nw);
			int fromI = nw.nodes.indexOf(from);
			Node to = Node.findByID(ts[2], nw);
			int toI = nw.nodes.indexOf(to);
			double fuel = nw.getTravelingDistance(fromI, toI) / 100;
			double time = nw.getTravelingTimeWithQueue(fromI, toI);
			
			Calendar commodityAvailTime = RoutingPlanner.readCalString(ts[3]);
			
			if (commodityAvailTime.before(allPeriodsStartTime))
				commodityAvailTime = (Calendar) allPeriodsStartTime.clone();
			
			if (commodityAvailTime.after(allPeriodsDeadline))
				continue;
			
			Calendar commodityDeadline = RoutingPlanner.readCalString(ts[4]);
			
			if (commodityDeadline.before(allPeriodsStartTime))
				continue;
			
			// emergency time
			// this is to check whether the task is possible to complete
			// if we do it at first. If not, the task is not considered.
			Calendar latestStartTime = (Calendar) commodityDeadline.clone(); 
			latestStartTime.add(Calendar.MINUTE,
					-(int) (time + nw.getTravelingTime(nw.depot.index, fromI)));
			
			if (commodityAvailTime.after(latestStartTime)) {
				RoutingPlanner.errormsg("Error: commodity is available after emergency time (no time to finish)! "
						+ "Delete this entry!" + i);
				System.out.println(cdata.elementAt(i));
				continue;
			}
			
			if (latestStartTime.compareTo(allPeriodsStartTime) < 0)// || latestStartTime.compareTo(allPeriodsDeadline) > 0)
				continue;
	
			Calendar earliestFinishTime = (Calendar) commodityAvailTime.clone();
			earliestFinishTime.add(Calendar.MINUTE, 
					(int) time + (int) nw.getTravelingTime(nw.depot.index, fromI));
			
			//if (earliestFinishTime.after(timeUpperBound) || earliestFinishTime.after(deadline))
			//	continue;
			// The first one is not removed, cuz we will add it anyway (Regardless of time window violation)
			if (earliestFinishTime.after(commodityDeadline))
					continue;
			
			Commodity c = new Commodity(ts[0], 
					commodityAvailTime, commodityDeadline, 
					from, to, Integer.parseInt(ts[5]), Integer.parseInt(ts[6]));
			nw.commodities.add(c);
			int total = c.small + c.large;
			for (int j = 0; j < total; j++) {
				Task t = new Task();
				if (j >= c.small) t.size = 2;
				else t.size = 1;
				t.fuel = fuel;
				//t.time = time;
				t.cmdt = c;
				c.tasks.add(t);
				this.ta.add(t);
			}
			
			Calendar currentPeriodS, currentPeriodE;
			int numberOfShifts = nw.getNumberOfShifts();
			for (int j = 0; j < numberOfShifts; j++) {
				currentPeriodS = nw.periodStartTimes[j];
				currentPeriodE = nw.periodEndTimes[j];
				if (earliestFinishTime.before(currentPeriodE)) {
					if (latestStartTime.before(currentPeriodE) && latestStartTime.after(currentPeriodS)) {
						c.latestPeriod = j; 
						this.mandatoryCommodities.elementAt(j).add(c);
					} else if (latestStartTime.after(currentPeriodE)) {
						c.completableShifts.add(new Integer(j));
						this.optionalCommodities.elementAt(j).add(c);
					}
					// else is expired work
				} 
				// else is work that is not available
			}
		}

	}
	
}
