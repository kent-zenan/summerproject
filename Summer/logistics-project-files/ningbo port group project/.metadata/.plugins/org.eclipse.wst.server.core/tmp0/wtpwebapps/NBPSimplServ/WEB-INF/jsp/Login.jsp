<%@page import="uk.ac.nottingham.ningboport.client.test.AuthLog"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Auth Log</title>
<style type="text/css" title="currentStyle">
@import "http://112.124.50.122/NBPSimplServ/demo_page.css";

@import "http://112.124.50.122/NBPSimplServ/demo_table_jui.css";

@import "http://112.124.50.122/NBPSimplServ/jquery-ui-1.8.4.custom.css";
</style>
<script type="text/javascript" language="javascript"
	src="http://112.124.50.122/NBPSimplServ/jquery.js"></script>
<script type="text/javascript" language="javascript"
	src="http://112.124.50.122/NBPSimplServ/jquery.dataTables.js"></script>
<script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
		$('#example').dataTable({
			"sScrollY" : 248,
			"bJQueryUI" : true,
			"sPaginationType" : "full_numbers"
		});
	});
</script>
</head>
<body id="dt_example">
	<%
		@SuppressWarnings("unchecked")
		ArrayList<AuthLog> logs = (ArrayList<AuthLog>) request
				.getAttribute("log");
		StringBuilder sb = new StringBuilder();
		sb.append("<table cellpadding='0' cellspacing='0' border='0' class='display' id='example' width='100%'>");
		//Head of Table
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th>Driver</th>");
		sb.append("<th>Password</th>");
		sb.append("<th>Vehicle ID</th>");
		sb.append("<th>Keywords</th>");
		sb.append("<th>Date</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		for (int i = 0; i < logs.size(); i++) {
			//Body of Table
			sb.append("<tr class='"
					+ (logs.get(i).getSuccess() ? "gradeA" : "gradeX")
					+ "'>");
			sb.append("<td>" + logs.get(i).getDriver() + "</td>");
			sb.append("<td>******</td>");
			sb.append("<td>" + logs.get(i).getId() + "</td>");
			sb.append("<td>Audit "
					+ (logs.get(i).getSuccess() ? "Success" : "Failure")
					+ "</td>");
			sb.append("<td>" + logs.get(i).getTime().toGMTString()
					+ "</td>");
			sb.append("</tr>");
		}
		//Foot of Table
		sb.append("<tfoot>");
		sb.append("<tr>");
		sb.append("<th>Driver</th>");
		sb.append("<th>Password</th>");
		sb.append("<th>Vehicle ID</th>");
		sb.append("<th>Sucess</th>");
		sb.append("<th>Date</th>");
		sb.append("</tr>");
		sb.append("</tfoot>");
		sb.append("</table>");
	%>
	<div id="container">
		<div class="full_width big">Drivers Authentication Log</div>
		Click <a href="http://112.124.50.122:8080/NBPSimplServ/Report">here</a> to see the GPS log,
		Click <a href="http://112.124.50.122:8080/NBPSimplServ/">here</a> to go to the home page.<br />
		<h1>How it works?</h1>
		<p>
		GET &lt;this address&gt; HTTP/1.1 displays the log of authentication of the system,<br/>
		POST &lt;this address&gt; HTTP/1.1 handles the login request.
		</p>
		<h1>Data</h1>
		<div class="full_width">
			<%=sb.toString()%>
		</div>
		<h1></h1>
		
		<p>
			Table view by <a href="https://datatables.net/">DataTables</a>, 
			Theme by <a href="http://jqueryui.com/">jQuery-UI</a>.<br/>
			System time: <%=request.getAttribute("date")%>, 
			you're from: <%=request.getAttribute("addr")%>
		</p>
	</div>
</body>
</html>