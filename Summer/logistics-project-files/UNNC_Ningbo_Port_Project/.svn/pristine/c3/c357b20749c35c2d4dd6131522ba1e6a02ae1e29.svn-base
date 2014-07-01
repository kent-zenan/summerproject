package uk.ac.nottingham.ningboport.network.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.nottingham.ningboport.client.test.GPSTest;

/**
 * Servlet implementation class GPS
 */
@WebServlet("/GPS")
public class GPS extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GPS() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		

		//request.setAttribute("gps", GPSTest.getLastGPS());
		GPSTest gps = new GPSTest();
        String xmlFile = gps.getLastGPS();
        response.setHeader("Cache-Control ", "no-cache");
        response.setContentType("text/xml;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(xmlFile);
        printWriter.flush();
        printWriter.close();
        
		//request.getRequestDispatcher("WEB-INF/jsp/GPS.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
        String search_driverName = request.getParameter("driverName");
        String search_vehicleID = request.getParameter("vehicleID");
        long startDate = 0;
        long endDate = 0;
        if(request.getParameter("startDate") != null){
        	startDate = StringToDate(request.getParameter("startDate"));
        }
        if(request.getParameter("startDate") != null){
        	endDate =  StringToDate(request.getParameter("endDate"));
        }
        GPSTest gpsTest = new GPSTest(search_driverName, search_vehicleID, startDate, endDate);
        
        String GpsXml = gpsTest.getSearchedGPS();
        response.setHeader("Cache-Control ", "no-cache");
        response.setContentType("text/xml;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(GpsXml);
        printWriter.flush();
        printWriter.close();

        
	}
	
	private static long StringToDate(String timeString){

		if (timeString == "") {
			return 0;
		}
		else{
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date date = null;
			
			try {
				date = dateFormat.parse(timeString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			return date.getTime();
		}
	}
	

}
