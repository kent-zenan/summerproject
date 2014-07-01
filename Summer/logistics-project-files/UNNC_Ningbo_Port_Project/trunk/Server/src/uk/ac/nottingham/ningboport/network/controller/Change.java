package uk.ac.nottingham.ningboport.network.controller;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.nottingham.ningboport.client.test.TaskUpdate;
import uk.ac.nottingham.ningboport.planner.Route;

/**
 * Servlet implementation class change
 */
@WebServlet("/change")
public class Change extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Change() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("WEB-INF/jsp/routes.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		Vector<Route> routes = (Vector<Route>) request.getSession().getAttribute("routes");
		Vector<Route> newRoute = routes;
		
		response.setContentType("text/html");
		String order = request.getParameter("order");
		String[] subStrs = order.split(",");
		
		int routeId = Integer.parseInt(subStrs[0]);
		TaskUpdate taskUpdate = new TaskUpdate();
		
		if(subStrs.length == 1)
			return;
		
//		for (int i = 1; i < subStrs.length; i++) {
//			taskUpdate.updateDBSTask(Integer.parseInt(subStrs[i]), routeId, i - 1);
//		}
		

//		
//		for (int i = 1; i < strs.length; i++){
//			upDateID(Integer.parseInt(strs[i]),routnumber , i);
//		
//		}	    
	}

}
