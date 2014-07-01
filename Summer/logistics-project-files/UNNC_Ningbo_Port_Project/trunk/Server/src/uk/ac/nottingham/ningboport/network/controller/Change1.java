package uk.ac.nottingham.ningboport.network.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.nottingham.ningboport.client.test.TaskUpdate;

/**
 * Servlet implementation class Change1
 */
@WebServlet("/Change1")
public class Change1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Change1() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int taskID = Integer.parseInt(request.getParameter("taskID"));
		int newRouteID = Integer.parseInt(request.getParameter("routeID"));
		int newSequenceNo = Integer.parseInt(request.getParameter("sequenceNo"));
		
		TaskUpdate taskUpdate = new TaskUpdate();
		taskUpdate.updatePhoneTask(taskID,newRouteID,newSequenceNo);
		
		//TODO: Change the updateDBSTask method
       // PrintWriter printWriter = response.getWriter();
        //printWriter.write("taskID: " + taskID + "routeID:" + routeID + "sequenceNo" + sequenceNo);
        //printWriter.flush();
        //printWriter.close();
       
	}

}
