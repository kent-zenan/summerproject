package uk.ac.nottingham.ningboport.network.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.nottingham.ningboport.client.test.TaskUpdate;
import uk.ac.nottingham.ningboport.planner.RoutingPlanner;

/**
 * Servlet implementation class changeSer
 */
@WebServlet("/changeSer")
public class ChangeSer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeSer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		RoutingPlanner rp = new RoutingPlanner();
		String args[] = {"-n","network","-db","-dbUsername","nbp","-dbPassword","cretofnbp","-s","20120207080000","-pl","12","-np","2","-maximumShakeTime","17","-tabuEnabled","false","-shakeBase","5"};
		File a = new File("network");
        PrintWriter printWriter = response.getWriter();
        printWriter.write(a.getAbsolutePath());
        printWriter.flush();
        printWriter.close();
		rp.main(args);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String status = request.getParameter("status");
		int routeID = Integer.parseInt(request.getParameter("id"));
		TaskUpdate taskUpdate = new TaskUpdate();
		
		if (status.equals("assigned")) {
			taskUpdate.updateStatus(routeID, 1);
			System.out.print(routeID);
		}else if (status.equals("session_suspend")) {
			taskUpdate.updateStatus(routeID, 2);
		}
		

	}

}
