package uk.ac.nottingham.ningboport.planner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import uk.ac.nottingham.ningboport.planner.Network;
import uk.ac.nottingham.ningboport.planner.NetworkReader;
import uk.ac.nottingham.ningboport.planner.Algorithms.Insertion;
import uk.ac.nottingham.ningboport.planner.Algorithms.VNS;

public class RoutingPlanner {

	private static String output = "";
	private static String networkFileName = null, commodityFileName = null, solutionFileName = null,
			startDate = null, periodLength = null, numOfPeriods = null;//, rtFileName = null;
	/**
	 * @param args
	 * Network_file commodity_file starting_time duration_of_one_periods number_of_periods
	 * 
	 * example: "-n network -sol realLifeData -s 20121201080000 -pl 12 -np 60"
	 */
	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].charAt(0) == '-') {
				String argType = args[i].substring(1);
				if (argType.equals("n")) {
					networkFileName = args[++i];
					
				} else if (argType.equals("c")) {
					commodityFileName = args[++i];
					
				} else if (argType.equals("s")) {
					startDate = args[++i];
					
				} else if (argType.equals("pl")) {
					periodLength = args[++i];
					
				} else if (argType.equals("np")) {
					numOfPeriods = args[++i];
					
				} /*else if (argType.equals("r")) {
					rtFileName = args[++i];
					
				}*/ else if (argType.equals("sol")) {
					solutionFileName = args[++i];
					
				}else {
					errormsg("Bad parameter!");
					return;
				}
			}
			
		}
		
		Network nw = new Network();
		Calendar stime = null;
		
		if (solutionFileName == null) {
			if (networkFileName == null || startDate == null || 
					commodityFileName == null ||
					periodLength == null || numOfPeriods == null) {
				errormsg("Missing Parameter");
				return;
			}
			
			//RTSimulator rts = new RTSimulator();
			try {
				stime = readCalString(startDate);
				nw.createPeriodBoundaries(stime, Integer.parseInt(periodLength), Integer.parseInt(numOfPeriods));
				NetworkReader.readFile(nw, networkFileName);
				CommodityReader.readFile(commodityFileName, nw);
				//if (rtFileName != null)
				//	rts.readFile(rtFileName, nw);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			nw.combineLightTask(nw.taskSet);
		} else { // comparing with existing solution.
			stime = readCalString(startDate);
			nw.createPeriodBoundaries(stime, Integer.parseInt(periodLength), Integer.parseInt(numOfPeriods));
			try {
				NetworkReader.readFile(nw, networkFileName);
				RealWorldSolutionReader rwsr = new RealWorldSolutionReader(nw);
				rwsr.readSolution(solutionFileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		
//		for (int j = 0; j < nw.mandatoryCommodities.size(); j++) {
//			Vector<Commodity> c = nw.mandatoryCommodities.get(j);
//			int csize = c.size();
//			int ctsize = 0;
//			for (int i = 0; i < csize; i++) {
//				ctsize += c.get(i).small + c.get(i).large;
//			}
//			System.out.println((double)ctsize/csize);
//		}
		
		
		Insertion ins = new Insertion(nw, false, false);
		ins.run();
		
		VNS ts = new VNS(nw);
		ts.run(120);
		
		RoutingPlanner.addToOutput("------ Algorithm Solution ------\n");
		//RoutingPlanner.addToOutput(nw.getNodeThroughputStatistics());
		RoutingPlanner.addToOutput(nw.outputRoutes());
		RoutingPlanner.addToOutput(nw.getSolutionObjectiveValues());
		//RoutingPlanner.addToOutput(nw.getSolutionFlowVectorStatistics());
		RoutingPlanner.addToOutput("------ End of Algorithm Solution ------\n\n\n");
		
//		if (args.length == 6)
//			rts.startRoutingSimulation();

		writeOutput();
/*		double gas = res[0] * nw.emptyVehicleFuelCost + res[1] * nw.loadedVehilceFuelCost;
		System.out.println("remain: " + nw.ta.size() + 
				" rate " + res[0]/(res[0]+res[1]) +
				"(" + res[0] + "," + res[1] + ")" +
				" Gas: " + gas);*/
	}
	
	public static void addToOutput(String s) {
		if (s == null) return;
		
		output += s;
	}
	
	public static void writeOutput() {
		try {
			PrintWriter pw = new PrintWriter(new File("result_" + startDate + "_" + numOfPeriods + ".txt"));
			pw.println(output);
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println(output);
	}
	
	public static Calendar readCalString(String s) {
		if (s == null || s.length() != 14) {
			//System.out.println("warning: bad date format: " + s);
			return null;
		}
		Calendar c = new GregorianCalendar();
		c.set(Integer.parseInt(s.substring(0, 4)),
				Integer.parseInt(s.substring(4, 6)) - 1,
				Integer.parseInt(s.substring(6, 8)),
				Integer.parseInt(s.substring(8, 10)),
				Integer.parseInt(s.substring(10, 12)),
				Integer.parseInt(s.substring(12, 14)));
		return c;
	}
	
	public static void errormsg(String s) {
		System.out.println(s);
	}

	public static Network getExampleData() {
		String[] args = {"-n","network","-sol","realLifeData","-s","20121201080000","-pl","12","-np","60"};
		for (int i = 0; i < args.length; i++) {
			if (args[i].charAt(0) == '-') {
				String argType = args[i].substring(1);
				if (argType.equals("n")) {
					networkFileName = args[++i];
					
				} else if (argType.equals("c")) {
					commodityFileName = args[++i];
					
				} else if (argType.equals("s")) {
					startDate = args[++i];
					
				} else if (argType.equals("pl")) {
					periodLength = args[++i];
					
				} else if (argType.equals("np")) {
					numOfPeriods = args[++i];
					
				} /*else if (argType.equals("r")) {
					rtFileName = args[++i];
					
				}*/ else if (argType.equals("sol")) {
					solutionFileName = args[++i];
					
				}else {
					errormsg("Bad parameter!");
					return null;
				}
			}
			
		}
		
		Network nw = new Network();
		Calendar stime = null;
		
		if (solutionFileName == null) {
			if (networkFileName == null || startDate == null || 
					commodityFileName == null ||
					periodLength == null || numOfPeriods == null) {
				errormsg("Missing Parameter");
				return null;
			}
			
			//RTSimulator rts = new RTSimulator();
			try {
				stime = readCalString(startDate);
				nw.createPeriodBoundaries(stime, Integer.parseInt(periodLength), Integer.parseInt(numOfPeriods));
				NetworkReader.readFile(nw, networkFileName);
				CommodityReader.readFile(commodityFileName, nw);
				//if (rtFileName != null)
				//	rts.readFile(rtFileName, nw);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			nw.combineLightTask(nw.taskSet);
		} else { // comparing with existing solution.
			stime = readCalString(startDate);
			nw.createPeriodBoundaries(stime, Integer.parseInt(periodLength), Integer.parseInt(numOfPeriods));
			try {
				NetworkReader.readFile(nw, networkFileName);
				RealWorldSolutionReader rwsr = new RealWorldSolutionReader(nw);
				rwsr.readSolution(solutionFileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
		
		Insertion ins = new Insertion(nw, false, false);
		ins.run();
		
		return nw;
	}
}
