package uk.ac.nottingham.ningboport.client.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import uk.ac.nottingham.ningboport.network.model.XMLSession;
import uk.ac.nottingham.ningboport.network.model.XMLTask;
import uk.ac.nottingham.ningboport.network.model.XMLTask.Action;
import uk.ac.nottingham.ningboport.planner.Commodity;
import uk.ac.nottingham.ningboport.planner.Node;
import uk.ac.nottingham.ningboport.planner.Route;
import uk.ac.nottingham.ningboport.planner.Route.status;
import uk.ac.nottingham.ningboport.planner.Task;
import uk.ac.nottingham.ningboport.server.datamgr.SingleUpdateManager;
import uk.ac.nottingham.ningboport.server.datamgr.UpdateManager;
import uk.ac.nottingham.ningboport.server.db.DBManager;
import uk.ac.nottingham.ningboport.server.var.Global;

/**
 * contains method that deal with task updating
 * Now this file is not used
 * @author Haoyue Zhu
 */

public class TaskUpdate {
	
//	public static void main(String args[]) {
//		
//		TaskUpdate a = new TaskUpdate();
//		
//		XMLSession s = new XMLSession("admin","11223",
//				Long.parseLong("1376417027193"),Long.parseLong("1376460227193"));
//		
//		a.taskAction(s, 1, Action.finish);
//		a.updateID(1, 2 , 2);
//		a.updateID(7, 1 , 0);
//		a.assignRoutes(1);
//		System.out.println(a.getRoutes().get(0).currentStatus);
//		Vector<Route> b = a.getRoutes();
//		System.out.println(b.get(0).id);
//		System.out.println(b.get(0).taskSet.get(0).taskID);
//		System.out.println(b.get(0).taskSet.get(1).taskID);
//		System.out.println(b.get(0).taskSet.get(2).taskID);
//		System.out.println(b.get(0).taskSet.get(3).taskID);
//		
//	}

	
	/** 
	 * When user finish/start a task, save the time into database */
	//TODO: change the box amount of the commodity
	public void taskAction(XMLSession s, int sequenceNo,Action action ) {
		
		DBManager m = new DBManager();
		String driverName = s.getDriverName();
		String startTime = Long.toString(s.getStartTime());
		String expireTime = Long.toString(s.getExpireTime());
		String vehicleID = s.getVehicleID();
		ResultSet rs = m.getRs("SELECT t.id FROM Session s, Route r, Task t"
				+ " WHERE session_driverName = '" + driverName 
				+ "' AND s.startTime = " + startTime + " AND s.expireTime = " + expireTime 
				+ " AND session_vehicleID = '" + vehicleID + "' AND s.sessionID = r.sessionID"
				+ " AND r.id = t.route_id AND t.sequence_num = " + sequenceNo );
		try {
			if(rs.next()) {

				int task_id = rs.getInt("id");
				String currentTime = getCurrentTime();
				if (action.equals(Action.start)){
					m.updb("UPDATE Task SET actual_start = '" + currentTime 
							+ "' WHERE id = " + task_id );
				}
				
				if (action.equals(Action.finish)){
					m.updb("UPDATE Task SET actual_finish = '" + currentTime 
							+ "' WHERE id = " + task_id );
				}
				
				//TODO:Send to Phone About changing action
				
				
				ArrayList<UpdateManager> ml = Global.getUpdateTasks();
				int l = ml.size();	
				int index = -1;
				for (int i = 0; i <l ; i++){
					if(ml.get(i).getSession().equals(s)) {
						index = i;
						break;
					}
				}
				UpdateManager um = new UpdateManager(s);
				ArrayList<SingleUpdateManager> sl = new ArrayList<SingleUpdateManager>();
				if (index != -1){
					um = ml.get(index);
					sl = um.getTasks();
				}
				
				Task t = new Task();
				SingleUpdateManager a = new SingleUpdateManager(action, t);
				a.setSequenceNo(sequenceNo);
				sl.add(a);
				um.setTask(sl);
				//SAVE THE UPDATE TASK INTO GLOBAL VARIABLE
				if(index != -1){
					ml.set(index, um);
					Global.setUpdateTasks(ml);
				}
				else {

					Global.addUpdateTask(um);
				}				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getCurrentTime(){
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			return dateFormat.format(date);
	}
	
	/**
	 * When User Login and generate a new session, assign route to this session*/
	public void assignRoutes(int sessionID) {
		DBManager m = new DBManager();
		m.updb("UPDATE Route Set sessionID = " + sessionID + " WHERE sessionID IS NULL LIMIT 1;");
		getTasks(sessionID);
	}
	
	/**
	 * get All the task by sessionID
	 * used in case of first-login and re-login
	 * @param sessionID
	 */
	public void getTasks(int sessionID) {
		DBManager m = new DBManager();
		ResultSet rs = m.getRs("SELECT * FROM Session WHERE sessionID = " + sessionID );
		try {
			if( rs.next() ){
				String driverName = rs.getString("session_driverName");
				String vehicleID = rs.getString("session_vehicleID");
				long startTime = Long.parseLong(rs.getString("startTime"));
				long expireTime = Long.parseLong(rs.getString("expireTime"));
				XMLSession s = new XMLSession(driverName, vehicleID, startTime, expireTime);
				ArrayList<UpdateManager> ml = Global.getUpdateTasks();
				int l = ml.size();	
				int index = -1;
				for (int i = 0; i <l ; i++){
					if(ml.get(i).getSession().equals(s)) {
						index = i;
						break;
					}
				}
				
				UpdateManager um = new UpdateManager(s);
				ArrayList<SingleUpdateManager> sl = new ArrayList<SingleUpdateManager>();
				if (index != -1){
					um = ml.get(index);
					//Clear the tasks of this session
					//sl = um.getTasks();
				}

				ResultSet rss = m.getRs("SELECT commodity_id,sequence_num FROM Route r, Commodity c, Task t WHERE r.sessionID = " + sessionID + 
						" AND route_id = r.id AND c.id = t.commodity_id ORDER BY sequence_num ASC");
				while( rss.next() ){
					String commodityID = rss.getString("commodity_id");
					int sequenceNo = rss.getInt("sequence_num");
					ResultSet rsss = m.getRs("SELECT * FROM Commodity WHERE id = '" +  commodityID + "'");
					if(rsss.next()){
						String source = rsss.getString("source");
						String destination = rsss.getString("destination");
						String available_t = rsss.getString("available_t");
						String deadline = rsss.getString("deadline");
						int small = rsss.getInt("small");
						int large = rsss.getInt("large");
						
						Task t = new Task();
						t.driver = s.getDriverName();
						Node src = new Node(source, 0, 0);
						Node dest = new Node(destination, 0, 0);
						Commodity c = new Commodity(commodityID, StringToCalendar(available_t), StringToCalendar(deadline), src, dest, small, large);
						t.cmdt = c;
						SingleUpdateManager a = new SingleUpdateManager(Action.add, t);
						a.setSequenceNo(sequenceNo);
						
						sl.add(a);
						um.setTask(sl);
						//SAVE THE UPDATE TASK INTO GLOBAL VARIABLE
						if(index != -1){
							ml.set(index, um);
							Global.setUpdateTasks(ml);
						}
						else {

							Global.addUpdateTask(um);
						}
						//System.out.println(Global.getUpdateTasks().size());
					}
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.destory();

		
	}
	
	/**
	 * update the new route and sequenceNo of task
	 * send to phone
	 * @param taskId
	 * @param newRouteID
	 * @param newSequenceId
	 */
	public void updatePhoneTask(int taskId,int newRouteID,int newSequenceId) {
		DBManager m = new DBManager();
		ResultSet rs = m.getRs("SELECT s.*,t.* FROM Session s, Task t, Route r WHERE t.id = " 
				+ taskId + " AND t.route_id = r.id AND s.sessionID = r.sessionID");
		
		try {
			while(rs.next()){
				String driverName = rs.getString("session_driverName");
				String vehicleID = rs.getString("session_vehicleID");
				long startTime = Long.parseLong(rs.getString("startTime"));
				long expireTime = Long.parseLong(rs.getString("expireTime"));
				int sequenceNo = rs.getInt("sequence_num");
				//save the action into updateTasks
				//remove task first
				XMLSession s = new XMLSession(driverName, vehicleID, startTime, expireTime);
				ArrayList<UpdateManager> ml = Global.getUpdateTasks();
				int l = ml.size();	
				int index = -1;
				for (int i = 0; i <l ; i++){
					if(ml.get(i).getSession().equals(s)) {
						index = i;
						break;
					}
				}
				
				UpdateManager um = new UpdateManager(s);
				ArrayList<SingleUpdateManager> sl = new ArrayList<SingleUpdateManager>();
				if (index != -1){
					um = ml.get(index);
					sl = um.getTasks();
				}
				
				Task t = new Task();
				SingleUpdateManager a = new SingleUpdateManager(Action.delete, t);
				a.setSequenceNo(sequenceNo);
				sl.add(a);
				um.setTask(sl);
				if(index != -1){
					ml.set(index, um);
					Global.setUpdateTasks(ml);
				}
				else {
					Global.addUpdateTask(um);
				}
				
				//then add new task
				ResultSet rss = m.getRs("SELECT s.* FROM Route r, Session s WHERE r.sessionID = s.sessionID" + 
						" AND r.id = " + newRouteID);
				while( rss.next() ){
					String driverName1 = rss.getString("session_driverName");
					String vehicleID1 = rss.getString("session_vehicleID");
					long startTime1 = Long.parseLong(rss.getString("startTime"));
					long expireTime1 = Long.parseLong(rss.getString("expireTime"));
					
					ml = Global.getUpdateTasks();
					l = ml.size();
					int index1 = -1;
					XMLSession s1 = new XMLSession(driverName1, vehicleID1, startTime1, expireTime1);
					for (int i = 0; i <l ; i++){
						if(ml.get(i).getSession().equals(s1)) {
							index1 = i;
							break;
						}
					}
					
					UpdateManager um1 = new UpdateManager(s1);
					ArrayList<SingleUpdateManager> sl1 = new ArrayList<SingleUpdateManager>();
					if (index1 != -1){
						um1 = ml.get(index1);
						sl1 = um1.getTasks();
					}
					
					String commodityID = rs.getString("commodity_id");
					ResultSet rsss = m.getRs("SELECT * FROM Commodity WHERE id = '" +  commodityID + "'");
					if(rsss.next()){
						String source = rsss.getString("source");
						String destination = rsss.getString("destination");
						String available_t = rsss.getString("available_t");
						String deadline = rsss.getString("deadline");
						int small = rsss.getInt("small");
						int large = rsss.getInt("large");
						
						Task t1 = new Task();
						Node src = new Node(source, 0, 0);
						Node dest = new Node(destination, 0, 0);
						Commodity c = new Commodity(commodityID, StringToCalendar(available_t), StringToCalendar(deadline), src, dest, small, large);
						t1.cmdt = c;
						SingleUpdateManager b = new SingleUpdateManager(Action.add, t1);
						b.setSequenceNo(newSequenceId);
						sl1.add(b);
						um1.setTask(sl1);
						
						if(index1 != -1){
							ml.set(index1, um1);
							Global.setUpdateTasks(ml);
						}
						else{
							Global.addUpdateTask(um1);
						}

					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.updateDBSTask(taskId,newRouteID,newSequenceId);
		m.destory();
	}
	
	/**
	 * update the new route and sequenceNo of task
	 * save into database
	 * @param taskId
	 * @param newRouteID
	 * @param newSequenceId
	 */
	public void updateDBSTask(int taskId,int newRouteID,int newSequenceId) {
		DBManager m = new DBManager();
		// update into database
		ResultSet rs = m.getRs("SELECT route_id,sequence_num FROM Task t, Route r "
				+ "WHERE t.id = " + taskId + " AND t.route_id = r.id");
		try {
			if(rs.next()){
				int oriRouteID = rs.getInt("route_id");
				int oriSequenceNo= rs.getInt("sequence_num");
				m.updb("UPDATE Task , Route r SET sequence_num = sequence_num + 1 "
						+ "WHERE sequence_num >=" + newSequenceId 
						+ " AND route_id = r.id AND r.id = " + newRouteID);
				
				m = new DBManager();
				m.updb("UPDATE Task SET route_id = " + newRouteID + ", sequence_num = " + newSequenceId 
						+ " WHERE id = " + taskId);
				
				m = new DBManager();
				m.updb("UPDATE Task , Route r SET sequence_num = sequence_num - 1 "
						+ "WHERE sequence_num >" + oriSequenceNo 
						+ " AND route_id = r.id AND r.id = " +  oriRouteID);				
						
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * update the status of routes
	 * @param routeID
	 * @param status
	 */
	public void updateStatus(int routeID, int status) {
		DBManager m = new DBManager();
		m.updb("UPDATE Route SET status=" + status +
				" WHERE id = " + routeID);
		//status, 0:UNASSIGNED, 1:ASSIGNED£¬2£ºSESSION_SUSPENDED£¬ 3£ºSESSION_ENDED
		//TODO:When Updating, send message to Phone 
	}
	
	/**
	 * get all the routes that has been assigned from the database
	 * save into Vector<Route>
	 * to be used by Change.java controller
	 * @return
	 */
	public Vector<Route> getRoutes(){
		DBManager m = new DBManager();
		ResultSet rs = m.getRs("SELECT r.id AS route_id,s.sessionID,session_driverName,"
				+ "session_vehicleID,startTime,expireTime,status,shift_normal_start_time "
				+ "FROM Route r,Session s WHERE s.sessionID = r.sessionID");
		Vector<Route> routes = new Vector<Route>();
		try {
			while(rs.next()){
				int route_id = rs.getInt("route_id");
				int sessionID = rs.getInt("sessionID");
				String driverName = rs.getString("session_driverName");
				String vehicleID = rs.getString("session_vehicleID");
				long startTime = Long.parseLong(rs.getString("startTime"));
				long expireTime = Long.parseLong(rs.getString("expireTime"));
				int status = rs.getInt("status");//status, 0:UNASSIGNED, 1:ASSIGNED£¬2£ºSESSION_SUSPENDED£¬ 3£ºSESSION_ENDED
				
				Calendar shift_normal_start_time = StringToCalendar(rs.getString("shift_normal_start_time"));
				//TODO:What is Network and period
				Route route = new Route(null, route_id, 0);

				XMLSession session = new XMLSession(driverName,vehicleID,startTime,expireTime);
				route.setSession(session);
				route.setStatus(intToStatus(status));
;
				//TODO:HOW to use the shift_normal_start_time?
				Vector<Task> taskSet = new Vector<Task>();
				ResultSet rss = m.getRs("SELECT t.id AS task_id, t.commodity_id,sequence_num,"
						+ "t.small AS task_small,t.large AS task_large,planned_start,planned_finish,"
						+ "actual_start,actual_finish,source,destination,available_t,deadline,"
						+ "c.small AS com_small, c.large AS com_large,finished_small,finished_large "
						+ "FROM Route r, Task t, Commodity c "
						+ "WHERE r.id = t.route_id AND t.commodity_id = c.id AND r.id =" + route_id + " "
						+ "ORDER BY sequence_num ASC");
				while(rss.next()){
					int task_id = rss.getInt("task_id");
					String commodity_id = rss.getString("commodity_id");
					//int sequenceNo = rss.getInt("sequence_num");
					int task_small = rss.getInt("task_small");
					int task_large = rss.getInt("task_large");
					Calendar planned_start = StringToCalendar(rss.getString("planned_start"));
					Calendar planned_finish = StringToCalendar(rss.getString("planned_finish"));
					Calendar actual_start = StringToCalendar(rss.getString("actual_start"));
					Calendar actual_finish = StringToCalendar(rss.getString("actual_finish"));
					String source = rss.getString("source");
					String destination = rss.getString("destination");
					Calendar available_t = StringToCalendar(rss.getString("available_t"));
					Calendar deadline = StringToCalendar(rss.getString("deadline"));
					int com_small = rss.getInt("com_small");
					int com_large = rss.getInt("com_large");	
					int finished_small = rss.getInt("com_small");
					int finished_large = rss.getInt("com_large");	
					
					Task t = new Task();
					//TODO:WHAT IS loadTime and unloadTime
					Node src = new Node(source, 0, 0);
					Node des = new Node(destination, 0, 0);
					
					Commodity c = new Commodity(commodity_id,available_t,deadline,src,des,com_small,com_large);
					t.cmdt = c;
					t.taskID = task_id;
					//TODO:Not sure if its right..
					if (task_small > 0){
						t.size = 1;
					}
					else {
						t.size = 2;
					}
					t.startT = planned_start;
					t.finishT = planned_finish;
					t.actualStartT = actual_start;
					t.actualFinishT = actual_finish;
					t.driver = driverName;
					t.vehicleID = vehicleID;
					taskSet.add(t);
				}
					route.taskSet = taskSet;
					routes.add(route);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return routes;
		
	}
	
	private static Calendar StringToCalendar(String timeString){
		
		if (timeString == "" || timeString == null) {
			return null;
		}
		else{
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = null;
			Calendar c = Calendar.getInstance();
			try {
				date = dateFormat.parse(timeString);
				
				c.setTime(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return c;
		}
	}
	
	private static status intToStatus(int i){
		if(i == 0){
			return status.UNASSIGNED;
		}
		if(i == 1){
			return status.ASSIGNED;
		}	
		if(i == 2){
			return status.SESSION_SUSPENDED;
		}	
		if(i == 3){
			return status.SESSION_ENDED;
		}	
		return status.UNASSIGNED;
	}
}
