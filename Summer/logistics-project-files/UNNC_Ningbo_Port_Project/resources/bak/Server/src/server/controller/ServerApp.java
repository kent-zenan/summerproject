package server.controller;

import planner.*;

public class ServerApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RoutingPlanner a = new RoutingPlanner();
		a.main(args);
		
		ServerController server = new ServerController();
		server.start();
	}

}
