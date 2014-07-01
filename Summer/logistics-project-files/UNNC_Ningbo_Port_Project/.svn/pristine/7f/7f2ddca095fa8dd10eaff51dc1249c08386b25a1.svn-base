package ac.uk.nottingham.ningboport.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ac.uk.nottingham.ningboport.model.XMLDataResponse;
import ac.uk.nottingham.ningboport.model.XMLLogin;
import ac.uk.nottingham.ningboport.model.XMLLoginComm;
import ac.uk.nottingham.ningboport.model.XMLSession;
import ac.uk.nottingham.ningboport.model.XMLTask;
import ac.uk.nottingham.ningboport.model.XMLTask.Action;
import ac.uk.nottingham.ningboport.model.XMLTask.Status;

/**
 * XML interpreter 
 * @author Haoyue ZHU, Jiaqi LI
 *
 */
public class XMLInterpreter {

	/**
	 * Build a XMLLoginComm from XML string
	 * @param xmlLoginComm XML string of login information
	 * @return XMLLoginComm object
	 */
	public static XMLLoginComm inteXmlLoginComm(String xmlLoginComm){
        Document doc = getXMLFromString(xmlLoginComm);
        if (doc.getDocumentElement() == null ) {
        	try {
				throw new Exception("the doc is null");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        doc.getDocumentElement().normalize();
       
        NodeList session = doc.getElementsByTagName("session");
        Node n1 = session.item(0);
        
        XMLSession s = null;
        XMLLogin l = null;
        
		if (n1.getNodeType() == Node.ELEMENT_NODE) {
			 
			Element eElement = (Element) n1;
 
			String driverName = eElement.getElementsByTagName("driverName").item(0).getTextContent();
			long startDate = 0;
			long expireDate = 0;
			try{
				startDate = Long.parseLong(eElement.getElementsByTagName("startDate").item(0).getTextContent());
				expireDate = Long.parseLong(eElement.getElementsByTagName("expireDate").item(0).getTextContent());
			}catch(Exception e){}
			
			String vehicleID = eElement.getElementsByTagName("vehicleID").item(0).getTextContent();
			
			s = new XMLSession(driverName, vehicleID, startDate, expireDate);
		}
		
        NodeList login = doc.getElementsByTagName("login");
        Node n2 = login.item(0);
        
		if (n2.getNodeType() == Node.ELEMENT_NODE) {
			 
			Element eElement = (Element) n2;
 
			String username = eElement.getElementsByTagName("username").item(0).getTextContent();
			String password = eElement.getElementsByTagName("password").item(0).getTextContent();
			String vehicleID = eElement.getElementsByTagName("vehicleID").item(0).getTextContent();
			
			l = new XMLLogin(username, password, vehicleID);
		}		
		
		XMLLoginComm r = new XMLLoginComm(s,l);

		return r;
	}
	
	/**
	 * Build a XMLDataResponse from XML string
	 * @param xmlDataResponse XML string of update information
	 * @return XMLDataResponse object
	 */
	public static XMLDataResponse inteXmlDataResponse(String xmlDataResponse) {
		 Document doc = getXMLFromString(xmlDataResponse);
	     doc.getDocumentElement().normalize();
	     
	     NodeList session = doc.getElementsByTagName("session");
	     Node n1 = session.item(0);
	        
	     XMLSession s = null;
	     ArrayList<XMLTask> tasks = new ArrayList<XMLTask>();
	        
	     if (n1.getNodeType() == Node.ELEMENT_NODE) {
				 
			Element eElement = (Element) n1;
	 
			String driverName = eElement.getElementsByTagName("driverName").item(0).getTextContent();
			long startDate = 0;
			long expireDate = 0;
			try{
				startDate = Long.parseLong(eElement.getElementsByTagName("startDate").item(0).getTextContent());
				expireDate = Long.parseLong(eElement.getElementsByTagName("expireDate").item(0).getTextContent());
			}catch(Exception e){}
			
			String vehicleID = eElement.getElementsByTagName("vehicleID").item(0).getTextContent();
				
			s = new XMLSession(driverName, vehicleID, startDate, expireDate);
		}
			
	    NodeList taskList = doc.getElementsByTagName("task");
	    int length = taskList.getLength();
	    for (int temp = 0; temp < length; temp++) {
	    	Node n2 = taskList.item(temp);
	     
			if (n2.getNodeType() == Node.ELEMENT_NODE) {
				 
				Element eElement = (Element) n2;
	 
				int sequenceNo = Integer.parseInt(eElement.getElementsByTagName("sequenceID").item(0).getTextContent());
				Action action = stringToAction(eElement.getElementsByTagName("action").item(0).getTextContent());
				XMLTask t = new XMLTask(sequenceNo, action);
				
				//System.out.println(sequenceNo);
				//System.out.println(action);
				
				if (eElement.getElementsByTagName("declarID").item(0) != null) {
					String declareID = eElement.getElementsByTagName("declarID").item(0).getTextContent();
					t.setDeclareID(declareID);
				}
				else if (action == Action.update) {
					t.setDeclareID(null);
				}
				
				if (eElement.getElementsByTagName("src").item(0) != null) {
					String src = eElement.getElementsByTagName("src").item(0).getTextContent();
					t.setSrc(src);
				}
				else if (action == Action.update) {
					t.setSrc(null);
				}
				
				if (eElement.getElementsByTagName("dest").item(0) != null) {
					String dest = eElement.getElementsByTagName("dest").item(0).getTextContent(); 
					t.setDest(dest);
				}
				else if (action == Action.update) {
					t.setDest(null);
				}
				
				if (eElement.getElementsByTagName("availableT").item(0) != null) {
					long availableT = Long.parseLong(eElement.getElementsByTagName("availableT").item(0).getTextContent());
					t.setAvailableTime(availableT);
				}
				else if (action == Action.update) {
					t.setAvailableTime(0);
				}
				
				if (eElement.getElementsByTagName("deadline").item(0) != null) {
					long deadline = Long.parseLong(eElement.getElementsByTagName("deadline").item(0).getTextContent());
					t.setDeadline(deadline);
				}	
				else if (action == Action.update) {
					t.setDeadline(0);
				}
				
				if (eElement.getElementsByTagName("size").item(0) != null) {
					int size = Integer.parseInt(eElement.getElementsByTagName("size").item(0).getTextContent());
					t.setSize(size);
				}
				else if (action == Action.update) {
					t.setSize(0);
				}
				
				if (eElement.getElementsByTagName("weight").item(0) != null) {
					int weight = Integer.parseInt(eElement.getElementsByTagName("weight").item(0).getTextContent());
					t.setWeight(weight);
				}
				else if (action == Action.update) {
					t.setWeight(0);
				}
				
				if (eElement.getElementsByTagName("quantity").item(0) != null) {
					int quantity = Integer.parseInt(eElement.getElementsByTagName("quantity").item(0).getTextContent());
					t.setQuantity(quantity);
				}
				else if (action == Action.update) {
					t.setQuantity(0);
				}
				
				if (eElement.getElementsByTagName("plannedStartTime").item(0) != null) {
					long plannedStartTime = Long.parseLong(eElement.getElementsByTagName("plannedStartTime").item(0).getTextContent());
					t.setPlannedStartTime(plannedStartTime);
				}
				else if (action == Action.update) {
					t.setPlannedStartTime(0);
				}
				
				if (eElement.getElementsByTagName("plannedLoadTime").item(0) != null) {
					long plannedLoadTime =  Long.parseLong(eElement.getElementsByTagName("plannedLoadTime").item(0).getTextContent());
					t.setPlannedLoadTime(plannedLoadTime);
				}
				else if (action == Action.update) {
					t.setPlannedLoadTime(0);
				}
				
				if (eElement.getElementsByTagName("plannedTravelTime").item(0) != null) {
					long plannedTravelTime = Long.parseLong(eElement.getElementsByTagName("plannedTravelTime").item(0).getTextContent());
					t.setPlannedTravelTime(plannedTravelTime);
				}
				else if (action == Action.update) {
					t.setPlannedTravelTime(0);
				}
				
				if (eElement.getElementsByTagName("plannedUnloadTime").item(0) != null) {
					long plannedUnloadTime = Long.parseLong(eElement.getElementsByTagName("plannedUnloadTime").item(0).getTextContent());
					t.setPlannedUnloadTime(plannedUnloadTime);
				}
				else if (action == Action.update) {
					t.setPlannedUnloadTime(0);
				}
				
				if (eElement.getElementsByTagName("plannedFinishTime").item(0) != null) {
					long plannedFinishTime = Long.parseLong(eElement.getElementsByTagName("plannedFinishTime").item(0).getTextContent());
					t.setPlannedFinishTime(plannedFinishTime);
				}
				else if (action == Action.update) {
					t.setPlannedFinishTime(0);
				}
				
				if (eElement.getElementsByTagName("actualStartTime").item(0) != null) {
					long actualStartTime = Long.parseLong(eElement.getElementsByTagName("actualStartTime").item(0).getTextContent());
					t.setActualStartTime(actualStartTime);
				}
				else if (action == Action.update) {
					t.setActualStartTime(0);
				}
				
				if (eElement.getElementsByTagName("actualLoadTime").item(0) != null) {
					long actualLoadTime = Long.parseLong(eElement.getElementsByTagName("actualLoadTime").item(0).getTextContent());
					t.setActualLoadTime(actualLoadTime);
				}
				else if (action == Action.update) {
					t.setActualLoadTime(0);
				}
				
				if (eElement.getElementsByTagName("actualTravelTime").item(0) != null) {
					long actualTravelTime = Long.parseLong(eElement.getElementsByTagName("actualTravelTime").item(0).getTextContent());
					t.setActualTravelTime(actualTravelTime);
				}
				else if (action == Action.update) {
					t.setActualTravelTime(0);
				}
				
				if (eElement.getElementsByTagName("actualUnloadTime").item(0) != null) {
					long actualUnloadTime = Long.parseLong(eElement.getElementsByTagName("actualUnloadTime").item(0).getTextContent());
					t.setActualUnloadTime(actualUnloadTime);
				}
				else if (action == Action.update) {
					t.setActualUnloadTime(0);
				}
				
				if (eElement.getElementsByTagName("actualFinishTime").item(0) != null) {
					long actualFinishTime = Long.parseLong(eElement.getElementsByTagName("actualFinishTime").item(0).getTextContent());
					t.setActualFinishTime(actualFinishTime);
				}
				else if (action == Action.update) {
					t.setActualFinishTime(0);
				}
				
				if (eElement.getElementsByTagName("status").item(0) != null) {
					Status status = stringToStatus(eElement.getElementsByTagName("status").item(0).getTextContent());
					t.setStatus(status);
				}
				else if (action == Action.update) {
					t.setStatus(null);
				}
			
				tasks.add(t);
			}		    
	    }
	    
		XMLDataResponse r= new XMLDataResponse(s);
		r.setTasks(tasks);
		
		return r;
	}
	
	/**
	 * This method is for parsing string to document
	 * @param xml XML in string
	 * @return a XML Document object
	 */
    public static Document getXMLFromString(String xml) {
        Document doc = null;
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder dBuilder = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			doc = dBuilder.parse(new InputSource(new StringReader(xml)));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return doc;
    }
    
    /**
     * Convert a strong to action
     * @param a action in string
     * @return an corresponding Action
     */
    public static Action stringToAction(String a) {
    	if (a.equals("add")) {
    		return Action.add;
    	}
    	
    	if (a.equals("delete")) {
    		return Action.delete;
    	}
    	
    	if (a.equals("update")) {
    		return Action.update;
    	}
    	
    	else
    		return null;
    	
    }
    
    /**
     * Convert a string to status
     * @param a status in string 
     * @return a corresponding status
     */
    public static Status stringToStatus(String a) {
    	if (a.equals("planned")) {
    		return Status.planned;
    	}
    	
    	if (a.equals("started")) {
    		return Status.started;
    	}
    	
    	if (a.equals("finished")) {
    		return Status.finished;
    	}
    	
    	else
    		return null;
    	
    }
}
