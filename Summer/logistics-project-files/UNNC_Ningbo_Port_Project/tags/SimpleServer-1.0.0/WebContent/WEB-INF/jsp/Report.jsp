<%@page import="uk.ac.nottingham.ningboport.client.test.GPSManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Random"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>GPS Log</title>
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
<!-- Tab -->
<link rel="stylesheet"
	href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script>
	$(function() {
		$("#tabs").tabs();
	});
</script>
</head>
<body id="dt_example">
	<%
		@SuppressWarnings("unchecked")
		ArrayList<GPSManager> logs = (ArrayList<GPSManager>) request
				.getAttribute("log");
		StringBuilder sb = new StringBuilder();
		sb.append("<table cellpadding='0' cellspacing='0' border='0' class='display' id='example' width='100%'>");
		//Head of Table
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th>Driver</th>");
		sb.append("<th>Vehicle ID</th>");
		sb.append("<th>Longitude</th>");
		sb.append("<th>Latitude</th>");
		sb.append("<th>Date</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		for (int i = 0; i < logs.size(); i++) {
			//Body of Table
			sb.append("<tr>");
			sb.append("<td>" + logs.get(i).getDriverName() + "</td>");
			sb.append("<td>" + logs.get(i).getVehicleID() + "</td>");
			sb.append("<td>" + logs.get(i).getLongitude() + "</td>");
			sb.append("<td>" + logs.get(i).getLatitude() + "</td>");
			sb.append("<td>"
					+ (new Date(logs.get(i).getTime())).toGMTString()
					+ "</td>");
			sb.append("</tr>");
		}
		//Foot of Table
		sb.append("<tfoot>");
		sb.append("<tr>");
		sb.append("<th>Driver</th>");
		sb.append("<th>Vehicle ID</th>");
		sb.append("<th>Longitude</th>");
		sb.append("<th>Latitude</th>");
		sb.append("<th>Date</th>");
		sb.append("</tr>");
		sb.append("</tfoot>");
		sb.append("</table>");
	%>
	<div id="container1">
		<div class="full_width big">GPS Log</div>
		Click <a href="http://112.124.50.122:8080/NBPSimplServ/Login">here</a>
		to see the Authentication log, Click <a
			href="http://112.124.50.122:8080/NBPSimplServ/">here</a> to go to the
		home page.<br/>
		<h1>How it works?</h1>
		<p>
			GET &lt;this address&gt; HTTP/1.1 displays the log of GPS location of
			the system,<br /> POST &lt;this address&gt; HTTP/1.1 handles the
			update request of Android terminals.
		</p>
		<h1>Data</h1>
		<div class="full_width">
			<div id="tabs">
				<ul>
					<li><a href="#tabs-1">Data</a></li>
					<li><a href="#tabs-2">Map</a></li>
				</ul>
				<div id="tabs-1">
					<%=sb.toString()%>
				</div>
				<div id="tabs-2">
					<div id="container">
					
					  <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.2&key=b98efe6b74317f1a2739cd3b03b3511e">
					  </script> 
					  <script type="text/javascript">  
						function getRandomColor(){
						  return  '#' +
						    (function(color){
						    return (color +=  '0123456789abcdef'[Math.floor(Math.random()*16)])
						      && (color.length == 6) ?  color : arguments.callee(color);
						  })('');
						}
						
						function initialize(){  
							var position=new AMap.LngLat(<%=logs.get(0).getLongitude()%>, <%=logs.get(0).getLatitude()%>);
							var mapObj=new AMap.Map("container",{center:position});
							
							<%
							ArrayList<Integer> sessions = new ArrayList<Integer>();
							for (int i = 0; i < logs.size(); i++) {
								int id = logs.get(i).getSessionID();
							%>		 
								<%
								boolean exist = false; 
								for (int k = 0; k < sessions.size(); k++ ){
									if(sessions.get(k) == id) {
										exist = true;
										break;
									}
								}
								if (!exist){
									sessions.add(id);
									%>
									var arr<%=id%> = new Array();

 
								<%} %>
							
							<%} %>	

							<%
								for (int i = 0; i < logs.size(); i++) {
									double longitude = logs.get(i).getLongitude();
									double latitude  = logs.get(i).getLatitude();
									int sessionID = logs.get(i).getSessionID();
									String info1 = "'Session" + sessionID + " DriverName: " + logs.get(i).getDriverName() + " vehicleID: " + logs.get(i).getVehicleID() + "'";
									String info2 = "'Longitude: " + Double.toString(longitude) + " Latitude: " + Double.toString(latitude) + "'";
									String info3 = "'" + new Date(logs.get(i).getTime()).toGMTString() + "'";
							%>
									arr<%=sessionID%>.push(new AMap.LngLat(<%=longitude%> ,<%=latitude%>));  
									
								    var m<%=sessionID%> = document.createElement("div");  
									
								    m<%=sessionID%>.className = <%= "'session" + sessionID + "'"%>;  
								    var n<%=sessionID%> = document.createElement("div");  
								    n<%=sessionID%>.innerHTML = <%= "'s" + sessionID + "'"%>;  
								    m<%=sessionID%>.appendChild(n<%=sessionID%>); 
									
									
									var marker<%=i%> = new AMap.Marker({                  
										map:mapObj,                  
										position: new AMap.LngLat(<%=longitude%>, <%=latitude%>),                  
										offset: new AMap.Pixel(-10,-34), 
										content:m<%=sessionID%>
									});	
									
									var info<%=i%> = []; 
									info<%=i%>.push(<%= info1%>); 
									info<%=i%>.push(<%= info2%>);  
									info<%=i%>.push(<%= info3%>);   
 						              
									var inforWindow<%=i%> = new AMap.InfoWindow({                  
									       offset:new AMap.Pixel(45,-34),                  
									       content:info<%=i%>.join("<br/>")                  
									});                  
									AMap.event.addListener(marker<%=i%>,"click",function(e){                  
									       inforWindow<%=i%>.open(mapObj,marker<%=i%>.getPosition());                  
									});                  
							
							<% } %>

							
							<%for (int i = 0; i < sessions.size(); i++) {
								int id = sessions.get(i);

							%>	
							$(<%="'div.session" + id + "'"%>).css("height","40px");
							$(<%="'div.session" + id + "'"%>).css("width","20px");
							$(<%="'div.session" + id + " div'"%>).css("background-color",getRandomColor());
							$(<%="'div.session" + id + "'"%>).css("background","url(http://webapi.amap.com/images/0.png)  left top no-repeat");
								var polyline<%=id%>  = new AMap.Polyline({                    
										map:mapObj,                  
									    path:arr<%=id%>,                    
									    strokeColor:getRandomColor(),                    
									    strokeOpacity:0.4,                    
									    strokeWeight:3,                    
									    strokeStyle:"dashed",                    
									    strokeDasharray:[10,5]                    
								}); 
								
                   

								mapObj.setFitView();
						<%}%>  
						}
						
						initialize();
					  </script>  
					
					</div>
				
				</div>

			</div>
			<h1></h1>

			<p>
				Table view by <a href="https://datatables.net/">DataTables</a>,
				Theme by <a href="http://jqueryui.com/">jQuery-UI</a>.<br /> System
				time:
				<%=request.getAttribute("date")%>, you're from:
				<%=request.getAttribute("addr")%>
			</p>
		</div>
	</div>
</body>
</html>