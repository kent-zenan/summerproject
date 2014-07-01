package uk.ac.nottingham.ningboport.network.util;

import java.util.ArrayList;

import uk.ac.nottingham.ningboport.network.model.XMLDataResponse;
import uk.ac.nottingham.ningboport.network.model.XMLLogin;
import uk.ac.nottingham.ningboport.network.model.XMLLoginComm;
import uk.ac.nottingham.ningboport.network.model.XMLSession;
import uk.ac.nottingham.ningboport.network.model.XMLTask;
import uk.ac.nottingham.ningboport.network.model.XMLTask.Action;
import uk.ac.nottingham.ningboport.network.model.XMLTask.Status;

/**
 * Build XML string from objects
 * @author Jiaqi LI
 *
 */
public class XMLBuilder {

	public static String buildXmlLoginComm(XMLLoginComm xmlLoginComm){
		XMLSession s = xmlLoginComm.getSession();
		XMLLogin l = xmlLoginComm.getLogin();
		
		//Session
		String driverName = checkNull(s.getDriverName());
		String startDate = checkNull(s.getStartTime());
		String expireDate =  checkNull(s.getExpireTime());
		String vehicleID = checkNull(s.getVehicleID());
		
		//Login
		String username = checkNull(l.getUsername());
		String password = checkNull(l.getPassword());
		String loginVehicleID = checkNull(l.getVehicleID());
		
		String xml =   "<nbp>" +
							"<session>" +
								"<driverName>" + driverName + "</driverName>" + 
								"<startDate>" + startDate + "</startDate>" +
								"<expireDate>" + expireDate + "</expireDate>" +
								"<vehicleID>" + vehicleID + "</vehicleID>" + 
							"</session>" +
							"<login>" + 
								"<username>" + username +"</username>" +
								"<password>" + password +"</password>" +
								"<vehicleID>" + loginVehicleID +"</vehicleID>" +
							"</login>" +
						"</nbp>";		
		
		return xml;
	}
	
	public static String buildXmlDat(XMLDataResponse xmlDataResponse){
		
		XMLSession s = xmlDataResponse.getSession();
		ArrayList<XMLTask> tasks = xmlDataResponse.getTasks();
		StringBuilder sb = new StringBuilder();
		
		//Session
		String driverName = checkNull(s.getDriverName());
		String startDate = checkNull(s.getStartTime());
		String expireDate =  checkNull(s.getExpireTime());
		String vehicleID = checkNull(s.getVehicleID());
		
		//Session
		sb.append("<nbp>");
		sb.append("<session>");
		sb.append("<driverName>" + driverName + "</driverName>");
		sb.append("<startDate>" + startDate + "</startDate>");
		sb.append("<expireDate>" + expireDate + "</expireDate>");
		sb.append("<vehicleID>" + vehicleID + "</vehicleID>");
		sb.append("</session>");
				 
		for(int index = 0; index < tasks.size(); index++){
			
			XMLTask task = tasks.get(index);
			sb.append("<task>");
			sb.append("<sequenceID>" + task.getSequenceNo() +"</sequenceID>");
			sb.append("<action>" + actionToString(task.getAction()) + "</action>");
			if(task.getDeclareID() != null)
				sb.append("<declarID>" + task.getDeclareID() + "</declarID>");
			if(task.getSrc() != null)
				sb.append("<src>" + task.getSrc() + "</src>");
			if(task.getDest() != null)
				sb.append("<dest>" + task.getDest() + "</dest>");
			if(task.getStatus() != null)
				sb.append("<status>" + statusToString(task.getStatus())+ "</status>");
			
			if(task.getAvailableTime() != 0)
				sb.append("<availableT>" + task.getAvailableTime() +"</availableT>");
			if(task.getDeadline() != 0)
				sb.append("<deadline>" + task.getDeadline() +"</deadline>");
			if(task.getSize() != 0)
				sb.append("<size>" + task.getSize() +"</size>");
			if(task.getWeight() != 0)
				sb.append("<weight>" + task.getWeight() +"</weight>");
			if(task.getQuantity() != 0)
				sb.append("<quantity>" + task.getQuantity() +"</quantity>");
			
			if(task.getPlannedStartTime() != 0)
				sb.append("<plannedStartTime>" + task.getPlannedStartTime() +"</plannedStartTime>");
			if(task.getPlannedFinishTime() != 0)
				sb.append("<plannedFinishTime>" + task.getPlannedFinishTime() +"</plannedFinishTime>");
			if(task.getPlannedLoadTime() != 0)
				sb.append("<plannedLoadTime>" + task.getPlannedLoadTime() +"</plannedLoadTime>");
			if(task.getPlannedTravelTime() != 0)
				sb.append("<plannedTravelTime>" + task.getPlannedTravelTime() +"</plannedTravelTime>");
			if(task.getPlannedUnloadTime() != 0)
				sb.append("<plannedUnloadTime>" + task.getPlannedUnloadTime() +"</plannedUnloadTime>");
			if(task.getActualStartTime() != 0)
				sb.append("<actualStartTime>" + task.getActualStartTime() +"</actualStartTime>");
			if(task.getActualLoadTime() != 0)
				sb.append("<actualLoadTime>" + task.getActualLoadTime() +"</actualLoadTime>");
			if(task.getActualTravelTime() != 0)
				sb.append("<actualTravelTime>" + task.getActualTravelTime() +"</actualTravelTime>");
			if(task.getActualUnloadTime() != 0)
				sb.append("<actualUnloadTime>" + task.getActualUnloadTime() +"</actualUnloadTime>");
			if(task.getActualFinishTime() != 0)
				sb.append("<actualFinishTime>" + task.getActualFinishTime() +"</actualFinishTime>");
			sb.append("</task>");
		}
		
		sb.append("</nbp>");
	
		return sb.toString();		

	}
	
	private static String checkNull(String a){
		if ((a == null)) {
			return "";
		}else
			return a;
	}
	
	private static String checkNull(long a){
		if ((a == 0)) {
			return "";
		}else
			return Long.toString(a);
	}
	
	private static String actionToString(Action action){
		
		switch (action) {
		case add:
			return "add";
		case update:
			return "update";
		case delete:
			return "delete";
		case start:
			return "start";
		case finish:
			return "finish";
		default:
			return "none";
		}
	}
	
	private static String statusToString(Status status){
		
		switch (status) {
		case planned:
			return "planned";
		case started:
			return "started";
		case finished:
			return "finished";
		default:
			return "";
		}
	}
}
