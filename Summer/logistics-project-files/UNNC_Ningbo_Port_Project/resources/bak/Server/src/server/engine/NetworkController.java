package server.engine;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Callable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import server.model.HttpRequestModel;
import server.model.XMLDataRequest;
import server.model.XMLDataResponse;
import server.model.XMLGps;
import server.model.XMLLogin;
import server.model.XMLLoginComm;
import server.model.XMLSession;
import server.source.UpdateManager;
import server.source.VehicleManager;
/**
 * THIS FILE iS UNDER PROCESSING.
 * 
 * Network controller
 * 
 * This is submitted as part of G52APR course work, 2012. Modified for Ningbo
 * Port Project, 2013.
 * 
 * @author Jiaqi LI
 */
public class NetworkController implements Callable<Void> {

	private final Socket socket;
	private HttpRequestModel request;

	public NetworkController(Socket socket) {
		this.socket = socket;
	}

	@Override
	public Void call() throws Exception {
		// System.out.print("Request Accepted.\n");
		InputStream requestStream = new BufferedInputStream(
				socket.getInputStream());
		OutputStream responseStream = new BufferedOutputStream(
				socket.getOutputStream());
		BufferedReader requestReader = new BufferedReader(
				new InputStreamReader(requestStream));

		try {
			// process request - request line.
			RequestInterpreter requestInterpreter = new RequestInterpreter(
					requestReader.readLine());

			// process request - headers
			String header = new String();
			while ((header = requestReader.readLine()) != null) {
				if (!requestInterpreter.processHeaders(header)) {
					break;
				}
			}

			// process request - body
			char[] body = new char[128];
			while (requestInterpreter.getBodyLength() < requestInterpreter
					.getContentLenght()) {
				if (requestReader.read(body, 0, 128) == -1)
					break;
				requestInterpreter.processBody(body);
			}
			request = requestInterpreter.getRequestModel();

			System.out.println(request.toString());
			// Process XML
			/*
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(request.toString());
			
			if(doc.getDocumentElement().getTagName().compareTo("nbp") != 0){
				String driverName = ((Element)doc.getElementsByTagName("session").item(0)).getAttribute("driverName");
				String startDate = ((Element)doc.getElementsByTagName("session").item(0)).getAttribute("startDate");
				String expireDate = ((Element)doc.getElementsByTagName("session").item(0)).getAttribute("expireDate");
				String sessionVehicleID = ((Element)doc.getElementsByTagName("session").item(0)).getAttribute("vehicleID");
				
				// Login
				if(driverName == null){
					String username = ((Element)doc.getElementsByTagName("login").item(0)).getAttribute("username");
					String password = ((Element)doc.getElementsByTagName("login").item(0)).getAttribute("password");
					String loginVehicleID = ((Element)doc.getElementsByTagName("login").item(0)).getAttribute("vehicleID");
					XMLSession session = new XMLSession(null, null, 0, 0); // Empty session
					XMLLogin login = new XMLLogin(username, password, loginVehicleID);
					XMLLoginComm loginRequest = new XMLLoginComm(session, login);
					XMLLoginComm loginResponse = VehicleManager.validateLogin(loginRequest);
					// TODO: send login response back to terminak;
				}else{ // Update
					double longitude = Double.valueOf(((Element)doc.getElementsByTagName("gps").item(0)).getAttribute("longitude"));
					double latitude = Double.valueOf(((Element)doc.getElementsByTagName("gps").item(0)).getAttribute("latitude"));
					XMLGps gps = new XMLGps(longitude, latitude);
					XMLSession session = new XMLSession(driverName, sessionVehicleID, Long.valueOf(startDate), Long.valueOf(expireDate));
					XMLDataRequest xmlRequest = new XMLDataRequest(session, gps);
					XMLDataResponse xmlResponse = UpdateManager.update(xmlRequest, null);
				}
				
			}else{
				//TODO: invalid XML
			}
			*/
			// response
			if (request != null
					&& requestInterpreter.getRequestModel().getRequestLine()
							.getMethod().compareTo("POST") == 0) {
				responseStream.write("HTTP/1.0 200 OK\r\n".getBytes());
				SimpleDateFormat dateFormatGmt = new SimpleDateFormat(
						"EEE, dd MMM yyyy HH:mm:ss", Locale.US);
				dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
				responseStream.write(("Date: "
						+ dateFormatGmt.format(new Date()) + " GMT\r\n")
						.getBytes());
				responseStream.write(("Last-Modified: "
						+ dateFormatGmt.format(new Date()) + " GMT\r\n")
						.getBytes());

				// TODO: Replace content with XML.
				responseStream.write("Content-Length: 17\r\n\r\n".getBytes());
				responseStream.write("XML will be here.".getBytes());
			} else {
				responseStream.write("HTTP/1.0 400 Bad Request\r\n".getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error occured when processing the request.\n");
		} finally {
			responseStream.close();
		}
		return null;
	}

}
