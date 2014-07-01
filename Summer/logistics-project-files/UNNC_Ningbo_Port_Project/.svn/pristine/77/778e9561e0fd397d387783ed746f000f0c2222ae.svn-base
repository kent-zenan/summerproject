package server.controller;

import server.engine.*;
import server.utils.ServerVariables;

/**
 * Server controller
 * @author Jiaqi LI
 * @version 0.1.0
 */
public class ServerController {

	private Engine engine;
	
	public void start(){
		
		// TODO: if log enabled, do something here!
		engine = new Engine(ServerVariables.DEFAULT_HOST, ServerVariables.DEFAULT_PORT);
	}
	
	public void stop(){
		
		ServerVariables.shutdown = true;
		System.out.println("Waiting for all threads to exit...");
		
		if (!engine.shutdown(10)) {
			engine.shutdownNow();
		}
		System.exit(0);
	}
}
