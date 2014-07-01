package uk.ac.nottingham.ningboport.planner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

public class CommodityReader {
	private static Vector<String> commodityFile = new Vector<String>();
	public static void readFile(String path, Network nw) throws IOException {
		BufferedReader br;
		br = new BufferedReader(new FileReader(path));
		String lineStr;
		while((lineStr = br.readLine()) != null) {
			commodityFile.add(lineStr);
		}
		br.close();
		parseCommodity(nw);
	}
	
	/* Correct format is like:
	 * Id::from::to::import time::finish time::small::large
	 * 
	 */
	private static void parseCommodity(Network nw) {
		Calendar allPeriodsStartTime = nw.periodStartTimes[0]; // Earliest time
		Calendar allPeriodsDeadline = nw.periodEndTimes[nw.periodEndTimes.length - 1]; // Latest Time
		commodityFile.remove(0); //remove the heading
		for (int i = 0; i < commodityFile.size(); i++) {
			String ts[] = commodityFile.elementAt(i).split("\t");
			if (ts.length != 7) {
				RoutingPlanner.errormsg("Wrong commodity file!");
				System.exit(0);
			}
			
			Node from = Node.findByID(ts[1], nw);
			int fromI = nw.nodes.indexOf(from);
			Node to = Node.findByID(ts[2], nw);
			int toI = nw.nodes.indexOf(to);
			double fuel = nw.getTravelingDistance(fromI, toI) / 100;
			
			Calendar commodityAvailTime = RoutingPlanner.readCalString(ts[3]);
			
			if (commodityAvailTime.after(allPeriodsDeadline))
				continue;
			
			Calendar commodityDeadline = RoutingPlanner.readCalString(ts[4]);
			
			if (commodityDeadline.before(allPeriodsStartTime))
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
				t.cmdt = c;
				c.tasks.add(t);
				nw.taskSet.add(t);
			}
			
			
		} // for all lines
		nw.classifyCommodities();
	}

}
