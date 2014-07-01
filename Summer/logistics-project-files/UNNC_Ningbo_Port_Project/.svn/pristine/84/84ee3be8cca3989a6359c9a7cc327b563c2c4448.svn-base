package uk.ac.nottingham.ningboport.network.util;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import uk.ac.nottingham.ningboport.client.test.TaskUpdate;
import uk.ac.nottingham.ningboport.network.model.XMLDataRequest;
import uk.ac.nottingham.ningboport.network.model.XMLGps;
import uk.ac.nottingham.ningboport.network.model.XMLLogin;
import uk.ac.nottingham.ningboport.network.model.XMLLoginComm;
import uk.ac.nottingham.ningboport.network.model.XMLSession;
import uk.ac.nottingham.ningboport.network.model.XMLTask;
import uk.ac.nottingham.ningboport.network.model.XMLTask.Action;
import uk.ac.nottingham.ningboport.network.model.XMLTask.Status;

/**
 * Interpreter XML string to build objects
 * 
 * @author Jiaqi LI
 *
 */
public class XMLInterpreter {

	public static XMLLoginComm inteXmlLoginComm(String xmlData) {

		XMLLogin xmlLogin = new XMLLogin();
		XMLSession xmlSession = new XMLSession();

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new ByteArrayInputStream(xmlData.getBytes()));
			
			doc.getDocumentElement().normalize();

			Node loginNode = doc.getElementsByTagName("login").item(0);
			
			if (loginNode.getNodeType() == Node.ELEMENT_NODE) {

				xmlLogin.setUsername(((Element) loginNode)
						.getElementsByTagName("username").item(0)
						.getTextContent());
				xmlLogin.setPassword(((Element) loginNode)
						.getElementsByTagName("password").item(0)
						.getTextContent());
				xmlLogin.setVehicleID(((Element) loginNode)
						.getElementsByTagName("vehicleID").item(0)
						.getTextContent());

			}

			return new XMLLoginComm(xmlSession, xmlLogin);

		} catch (Exception e) {
			e.printStackTrace();
			return new XMLLoginComm(xmlSession, xmlLogin);
		}
	}

	public static XMLDataRequest inteXmlDataRequest(String xmlData) {
		System.out.println(xmlData);
		XMLSession xmlSession = new XMLSession();
		XMLGps xmlGps = new XMLGps();
		XMLTask xmlTask = null;
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new ByteArrayInputStream(xmlData.getBytes()));
			
			doc.getDocumentElement().normalize();

			Node sessionNode = doc.getElementsByTagName("session").item(0);
			Node gpsNode = doc.getElementsByTagName("gps").item(0);
			Node taskNode = doc.getElementsByTagName("task").item(0);

			if (sessionNode.getNodeType() == Node.ELEMENT_NODE) {
				xmlSession.setDriverName(((Element) sessionNode)
						.getElementsByTagName("driverName").item(0)
						.getTextContent());
				xmlSession.setStartTime(Long.parseLong(((Element) sessionNode)
						.getElementsByTagName("startDate").item(0)
						.getTextContent()));
				xmlSession.setExpireTime(Long.parseLong(((Element) sessionNode)
						.getElementsByTagName("expireDate").item(0)
						.getTextContent()));
				xmlSession.setVehicleID(((Element) sessionNode)
						.getElementsByTagName("vehicleID").item(0)
						.getTextContent());
			}
			
			if (taskNode!= null && taskNode.getNodeType() == Node.ELEMENT_NODE) {
				int sequenceNo = Integer.parseInt(((Element)taskNode).getElementsByTagName("sequenceID").item(0).getTextContent());
				XMLTask.Action action = stringToAction(((Element)taskNode).getElementsByTagName("action").item(0).getTextContent());
				xmlTask = new XMLTask(sequenceNo, action);
				
				TaskUpdate a = new TaskUpdate();
				a.taskAction(xmlSession,sequenceNo,action);
			}	
			if (gpsNode.getNodeType() == Node.ELEMENT_NODE) {
				xmlGps.setLatitude(Double.parseDouble(((Element) gpsNode)
						.getElementsByTagName("latitude").item(0)
						.getTextContent()));
				xmlGps.setLongitude(Double.parseDouble(((Element) gpsNode)
						.getElementsByTagName("longitude").item(0)
						.getTextContent()));
			}

			XMLDataRequest request = new XMLDataRequest(xmlSession, xmlGps);
			request.setTask(xmlTask);
			return request;

		} catch (Exception e) {
			System.out.println("error");
			XMLDataRequest request = new XMLDataRequest(xmlSession, xmlGps);
			return request;
		}
	}

	public static Action stringToAction(String a) {

		if (a.equals("start")) {
			return Action.start;
		} else if (a.equals("finish")) {
			return Action.finish;
		} else {
			return null;
		}

	}

	public static Status stringToStatus(String a) {

		if (a.equals("planned")) {
			return Status.planned;
		} else if (a.equals("started")) {
			return Status.started;
		} else if (a.equals("finished")) {
			return Status.finished;
		} else {
			return null;
		}
	}
}
