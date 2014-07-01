package planner.statistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collections;
import java.util.Vector;

import planner.Commodity;
import planner.Network;
import planner.Node;
import planner.Route;
import planner.Task;


class FlowVector {
	private Node node1, node2;
	private Vector<FlowVector> flowVectors;
	public int flow;
	
	public FlowVector(Task a, Vector<FlowVector> v) {
		node1 = a.cmdt.src;
		node2 = a.cmdt.dest;
		this.flowVectors = v;
		if (a.gT == null) flow = 1;
		else flow = 2;
	}
	
	public FlowVector(Node n1, Node n2, Vector<FlowVector> v) {
		node1 = n1;
		node2 = n2;
		flow = 1;
		this.flowVectors = v;
	}
	
	private FlowVector get(Node n1, Node n2) {
		int l = flowVectors.size();
		for (int i = 0; i < l; i++) {
			FlowVector fv = flowVectors.get(i);
			if (fv.node1 == n1 && fv.node2 == n2) {
				return fv;
			}
		}
		
		return null;
	}
	
	/*
	 * returns: whether this flowVector still exists in the flowVectors
	 * 0 => all exists
	 * 1 => itself get removed
	 * 2 => v get removed
	 * 3 => all get removed
	 */
	public int add(FlowVector v) {
		int c = 0;
		FlowVector a = null, b = null;
		if (v.node1 == this.node1 && v.node2 == this.node2) {
			c = 1;
			
		} else if (v.node1 == this.node2 && v.node2 == this.node1) {
			c = 2;
			
		} else if (v.node1 == this.node2) {
			a = this;
			b = v;
			c = 3;
			
		} else if (v.node2 == this.node1) {
			a = v;
			b = this;
			c = 3;	
		}
		
		switch (c) {
		case 1:
			this.flow += v.flow;
			flowVectors.remove(v);
			return 2;
			
		case 2:
			if (this.flow >= v.flow) {
				this.flow -= v.flow;
				flowVectors.remove(v);
				return 2;
			} else {
				v.flow -= this.flow;
				flowVectors.remove(this);
				return 1;
			}
			
			
		case 3: // a.node1 is start, b.node2 is the end
			FlowVector fv = this.get(a.node1, b.node2);
			if (fv == null) {
				flowVectors.add(new FlowVector(a.node1, b.node2, flowVectors));
				fv = flowVectors.lastElement();
			}
				
			if (a.flow > b.flow) {
				fv.flow += b.flow;
				a.flow -= b.flow;
				flowVectors.remove(b);
				if (b == this) return 1;
				else return 2;
				
			} else if (b.flow > a.flow) {
				fv.flow += a.flow;
				b.flow -= a.flow;
				flowVectors.remove(a);
				if (a == this) return 1;
				else return 2;
				
			} else {
				fv.flow += a.flow;
				flowVectors.remove(a);
				flowVectors.remove(b);
				return 3;
			}
			
		default:
			return 0;
		}
		
		
	} //end of add
	
	public String toString() {
		return "[" + node1.id + " " + node2.id + "]\t" + flow;  
	}
}

public class StatisticsAnalyzer {

	private Network nw;
	public StatisticsAnalyzer(Network nw) {
		this.nw = nw;
	}
	
	public String getArcUsage() {
		Vector<Node> nodes = nw.nodes;
		Vector<Task> taskSet = nw.taskSet;
		
		int nsize = nodes.size();
		int usageMatrix[][] = new int[nsize][nsize];
		for (int i = 0; i < taskSet.size(); i++) {
			Commodity c = taskSet.get(i).cmdt;
			usageMatrix[c.src.index][c.dest.index]++;
		}
		String out = "-----: arc usage :----\n";
		out += "from\n";
		
		for (int i = 0; i < nsize; i++) {
			out += nodes.get(i) + "\t";
			for (int j = 0; j < nsize; j++) {
				out += usageMatrix[i][j] + "\t";
			}
			out += "\n";
		}
		return out;
	}
	
	public String getTaskCountStatistics() {
		int numberOfShifts = nw.getNumberOfShifts();
		Vector<Vector<Commodity>> mandatoryCommodities = nw.mandatoryCommodities;
		Vector<Vector<Commodity>> optionalCommodities = nw.optionalCommodities;
		
		String out = "----: Task count for each period :----\n";
		out += "\tmandatory\toptional\n";
		for (int i = 0; i < numberOfShifts; i++) {
			out += (i+1);
			
			int count = 0;
			Vector<Commodity> commoditySet = mandatoryCommodities.get(i);
			int size = commoditySet.size();
			for (int j = 0; j < size; j++) {
				count += commoditySet.get(j).tasks.size();
			}
			out += "\t" + count;
			
			count = 0;
			commoditySet = optionalCommodities.get(i);
			size = commoditySet.size();
			for (int j = 0; j < size; j++) {
				count += commoditySet.get(j).tasks.size();
			}
			out += "\t" + count + "\n";
		}
		return out;
	}
	

	/*
	 * This method summarizes the throughput of ports: for each port, how many tasks goes out for each period
	 * 
	 * This method uses the tasks set in the task set sorted by deadline to get the statistics.
	 */
	public String getNodeThroughputDeadlineStatistics(Task.sortingMethod method) {
		Vector<Node> nodes = nw.nodes;
		Vector<Task> taskSet = nw.taskSet;
		int numberOfShifts = nw.getNumberOfShifts();
		
		String out1 = new String(),
				out2 = new String(),
				out3 = new String(),
				out4 = new String();
		
		Vector<Task> ts = new Vector<Task>();
		ts.addAll(taskSet);
		Task.sortBy = method;
		Collections.sort(ts);
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			
			for (int j = 0; j < numberOfShifts; j++) {
				int inTask = 0, outTask = 0, inContainer = 0, outContainer = 0;
				Calendar pStart = nw.periodStartTimes[j],
						pEnd = nw.periodEndTimes[j];
				for (int k = 0; k < ts.size(); k++) {
					Task t = ts.get(k);
					Commodity c = t.cmdt;
					if (method == Task.sortingMethod.DEADLINE) {
						if (!c.deadline.before(pEnd) || !c.deadline.after(pStart)) {
							continue; // we already sorted this task set. it is safe
						}
					} else if (method == Task.sortingMethod.AVAIL_TIME) {
						if (!c.availTime.before(pEnd) || !c.availTime.after(pStart)) {
							continue;
						}
					}
					
					if (c.src == n) {
						outTask++;
						outContainer++;
						if (t.gT != null)
							outContainer++;
						
					} else if (c.dest == n) {
						inTask++;
						inContainer++;
						
						if (t.gT != null)
							inContainer++;
					}
				}
				
				out1 += inTask + "\t";
				out2 += outTask + "\t";
				out3 += inContainer + "\t";
				out4 += outContainer + "\t";
			}
			
			out1 += "\n";
			out2 += "\n";
			out3 += "\n";
			out4 += "\n";
		}
		return "Node Throughput(Deadline)\n" + out1 + "\n\n" + out2 + "\n\n" + out3 + "\n\n" + out4 + "\n\n";
	}


	/*
	 * This method summarizes the throughput of ports: for each port, how many tasks goes out for each period
	 * 
	 * This method uses the tasks set in the routes to get the statistics.
	 */
	public String getNodeThroughputStatistics() {
		Vector<Node> nodes = nw.nodes;
		int numberOfShifts = nw.getNumberOfShifts();
		
		Vector<Vector<Integer>> statistics = new Vector<Vector<Integer>>();
		int nodeQuantity = nodes.size();
		for (int i = 0; i < numberOfShifts; i++) {
			Vector<Route> rs = nw.routesOfShifts.get(i);
			
			Vector<Integer> periodStatistics = new Vector<Integer>();
			statistics.add(periodStatistics);
			
			// for each node, get its throughput
			for (int j = 0; j < nodeQuantity; j++) {
				Node n = nodes.get(j);
				int inTask = 0, outTask = 0, inContainer = 0, outContainer = 0;
				for (int k = 0; k < rs.size(); k++) {
					Vector<Task> ts = rs.get(k).taskSet;
					for (int l = 0; l < ts.size(); l++) {
						Task t = ts.get(l);
						
						if (t.cmdt.src == n) {
							outTask++;
							outContainer++;
							if (t.gT != null)
								outContainer++;
							
						} else if (t.cmdt.dest == n) {
							inTask++;
							inContainer++;
							
							if (t.gT != null)
								inContainer++;
						}
					}
				}
				
				//put the finding into periodStatics
				periodStatistics.add(inTask);
				periodStatistics.add(outTask);
				periodStatistics.add(inContainer);
				periodStatistics.add(outContainer);
			}
		}
		
		String out1 = "---: Task in :---\n",
				out2 = "---: Task out :---\n",
				out3 = "---: Container in :---\n",
				out4 = "---: Container out :---\n";
		for (int nodeI = 0; nodeI < nodeQuantity; nodeI++) {
			for (int periodI = 0; periodI < numberOfShifts; periodI++) {
				Vector<Integer> periodStatistics = statistics.get(periodI);
				out1 += periodStatistics.get(nodeI * 4) + "\t";
				out2 += periodStatistics.get(nodeI * 4 + 1) + "\t";
				out3 += periodStatistics.get(nodeI * 4 + 2) + "\t";
				out4 += periodStatistics.get(nodeI * 4 + 3) + "\t";
			}
			out1 += "\n";
			out2 += "\n";
			out3 += "\n";
			out4 += "\n";
		}
		
		return out1 + "\n\n" + out2 + "\n\n" + out3 + "\n\n" + out4 + "\n\n";
	}
	
	
	public String getSolutionFlowVectorStatistics() {
		String out = "-----: FlowVector(solution) :----\n";
		int numberOfShifts = nw.getNumberOfShifts();
		for (int i = 0 ; i < numberOfShifts; i++) {
			out += getShiftFlowVectorStatistics(i);
		}
		
		out += "-----: end of FlowVector(solution) :----\n";
		return out;
	}
	
	public String getShiftFlowVectorStatistics(int shift_index) {
		String out = "|----------- " + shift_index + " -----------|\n";
		Vector<FlowVector> fvv = new Vector<FlowVector>();
		// get all flowVectors
		Vector<Route> rs = nw.routesOfShifts.get(shift_index);
		int rs_size = rs.size();
		for (int i = 0; i < rs_size ; i++) {
			Vector<Task> r = rs.get(i).taskSet;
			int r_size = r.size();
			for (int j = 0; j < r_size; j++) {
				fvv.add(new FlowVector(r.get(j), fvv));
			}
		}
		
		if (fvv.size() == 0) {
			out += "Empty\n\n";
			return out;
		}
		
		// combine vector
		int fvv_index = 0;
		FlowVector f1 = null;
		while (fvv_index < fvv.size()) {
			f1 = fvv.get(fvv_index);
			
			FlowVector f2 = null;
			int fvv_index2 = fvv_index + 1;
			int r = 0;
			while (fvv_index2 < fvv.size()) {
				f2 = fvv.get(fvv_index2);
				
				r = f1.add(f2);
				if (r == 1 || r == 3) {
					fvv_index--;
					break;
					
				} else if (r == 0) {
					fvv_index2++;
					
				}
			}
			fvv_index++;
		}
		
		// calculate total flow imbalance
		int total_imb = 0; 
		for (int i = 0; i < fvv.size(); i++) {
			FlowVector fv = fvv.get(i);
			out += fv + "\n";
			total_imb += fv.flow;
		}
		
		out += "total:\t" + total_imb + "\n\n";
		return out;
	}
	
	public String getRouteAppearanceRate() {
		int numberOfShifts = nw.getNumberOfShifts();
		
		Vector<Route> allRoutes = new Vector<Route>();
		for (int i = 0; i < numberOfShifts; i++)
			allRoutes.addAll(nw.routesOfShifts.get(i));
		
		int times[] = new int[allRoutes.size()];
		Route route[] = new Route[allRoutes.size()];
		int c = 0;
		
		while (!allRoutes.isEmpty()) {
			route[c] = allRoutes.remove(0);
			for (int i = 0; i < allRoutes.size(); i++) {
				if (allRoutes.get(i).geographicallyIdenticalTo(route[c])) {
					times[c]++;
					allRoutes.remove(i);
					i--;
				}
			}
			c++;
		}
		
		String res = "========Route appearance rate========\n";
		for (int i = 0; i < c; i++) {
			res += "Route: " + route[i].outputPortSequence();
			res += "\t" + (times[i] + 1) + "\n";
		}
		res += "----------------------\n";
		return res;
	}
	
	public void outputStatistics(String filename) {
		try {
			PrintWriter pw = new PrintWriter(new File(filename));
			pw.println(getNodeThroughputStatistics());
			pw.println(getSolutionFlowVectorStatistics());
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
