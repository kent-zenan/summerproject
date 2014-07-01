package uk.ac.nottingham.ningboport.planner;

public class Node {
	public int index; //index in the vector
	public String id;
	public int loadTime, unloadTime;
	public Node(String id, int loadTime, int unloadTime) {
		this.id = id;
		this.loadTime = loadTime;
		this.unloadTime = unloadTime;
	}
	
	public static Node findByID(String id, Network nw) {
		for (int i = 0; i < nw.nodes.size(); i++) {
			if (nw.nodes.elementAt(i).id.equals(id))
				return nw.nodes.elementAt(i);
		}
		RoutingPlanner.errormsg(id + " not found in the network. check your commodity file");
		System.exit(0);
		return null;
	}
	
	public String toString() {
		return id;
	}
}
