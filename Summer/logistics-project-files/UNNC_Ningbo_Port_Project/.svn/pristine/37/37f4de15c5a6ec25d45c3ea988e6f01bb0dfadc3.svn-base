package uk.ac.nottingham.ningboport.network.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.ac.nottingham.ningboport.client.test.LoginTest;
import uk.ac.nottingham.ningboport.network.model.XMLLoginComm;
import uk.ac.nottingham.ningboport.network.util.XMLBuilder;
import uk.ac.nottingham.ningboport.network.util.XMLInterpreter;
import uk.ac.nottingham.ningboport.server.datamgr.Verification;

/**
 * Servlet implementation class Login
 * 
 * @author Jiaqi LI
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setAttribute("addr", request.getRemoteAddr() + ":" + request.getRemotePort());
		request.setAttribute("log", LoginTest.getAllLogin());
		request.setAttribute("date", (new Date().toString()));
		request.getRequestDispatcher("WEB-INF/jsp/Login.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		BufferedReader reader = request.getReader();
		String line ="";
		StringBuilder sb = new StringBuilder();
        while(line != null){
        	line = reader.readLine();
        	if(line == null) break;
            sb.append(line);
        }
        
        XMLLoginComm xmlLoginComm = XMLInterpreter.inteXmlLoginComm(sb.toString());
        
        response.setContentType("text/xml");
        PrintWriter pw = response.getWriter();
        pw.print(XMLBuilder.buildXmlLoginComm(Verification.validateLogin(xmlLoginComm)));
	}

}
