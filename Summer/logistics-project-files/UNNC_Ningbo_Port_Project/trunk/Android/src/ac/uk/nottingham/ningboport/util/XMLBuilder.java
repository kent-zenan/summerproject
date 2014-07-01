package ac.uk.nottingham.ningboport.util;

import ac.uk.nottingham.ningboport.model.XMLDataRequest;
import ac.uk.nottingham.ningboport.model.XMLGps;
import ac.uk.nottingham.ningboport.model.XMLLogin;
import ac.uk.nottingham.ningboport.model.XMLLoginComm;
import ac.uk.nottingham.ningboport.model.XMLSession;
import ac.uk.nottingham.ningboport.model.XMLTask;

/**
 * XML Builder
 * 
 * @author Haoyue ZHU, Jiaqi LI
 * 
 */
public class XMLBuilder {

	/**
	 * Build a XML string of login request.
	 * 
	 * @param xmlLoginComm
	 *            XMLLoginComm object
	 * @return a string of XML login request
	 */
	public static String buildLoginRequest(XMLLoginComm xmlLoginComm) {
		XMLSession s = xmlLoginComm.getSession();
		XMLLogin l = xmlLoginComm.getLogin();

		// Session
		String driverName = checkNull(s.getDriverName());
		String startDate = checkNull(s.getStartTime());
		String expireDate = checkNull(s.getExpireTime());
		String vehicleID = checkNull(s.getVehicleID());

		// Login
		String username = checkNull(l.getUsername());
		String password = checkNull(l.getPassword());
		String loginVehicleID = checkNull(l.getVehicleID());

		String xml = "<nbp>" + "<session>" + "<driverName>" + driverName
				+ "</driverName>" + "<startDate>" + startDate + "</startDate>"
				+ "<expireDate>" + expireDate + "</expireDate>" + "<vehicleID>"
				+ vehicleID + "</vehicleID>" + "</session>" + "<login>"
				+ "<username>" + username + "</username>" + "<password>"
				+ password + "</password>" + "<vehicleID>" + loginVehicleID
				+ "</vehicleID>" + "</login>" + "</nbp>";

		return xml;
	}

	/**
	 * Build a XML string of update request.
	 * 
	 * @param xmlDataRequest
	 *            XMLDataRequest object
	 * @return a string of XML update request
	 */
	public static String buildDataRequest(XMLDataRequest xmlDataRequest) {
		XMLSession s = xmlDataRequest.getSession();
		XMLGps g = xmlDataRequest.getGps();

		// Session
		String driverName = s.getDriverName();
		String startDate = Long.toString(s.getStartTime());
		String expireDate = Long.toString(s.getExpireTime());
		String vehicleID = s.getVehicleID();

		// GPS
		String longitude = Double.toString(g.getLongitude());
		String latitude = Double.toString(g.getLatitude());

		StringBuilder sb = new StringBuilder();

		sb.append("<nbp>" + "<session>" + "<driverName>" + driverName
				+ "</driverName>" + "<startDate>" + startDate + "</startDate>"
				+ "<expireDate>" + expireDate + "</expireDate>" + "<vehicleID>"
				+ vehicleID + "</vehicleID>" + "</session>" + "<gps>"
				+ "<longitude>" + longitude + "</longitude>" + "<latitude>"
				+ latitude + "</latitude>" + "</gps>");

		if (xmlDataRequest.getTask() != null) {
			sb.append("<task>" + "<sequenceID>"
					+ xmlDataRequest.getTask().getSequenceNo()
					+ "</sequenceID>" + "<action>"
					+ actionToString(xmlDataRequest.getTask().getAction())
					+ "</action>" + "</task>");
		}
		sb.append("</nbp>");

		return sb.toString();

	}

	private static String checkNull(String a) {
		if (a == null) {
			return "";
		}else{
			return a;
		}
	}

	private static String checkNull(long a) {
		if (a == 0) {
			return "";
		}else{
			return Long.toString(a);
		}
	}

	private static String actionToString(XMLTask.Action action) {

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
}
