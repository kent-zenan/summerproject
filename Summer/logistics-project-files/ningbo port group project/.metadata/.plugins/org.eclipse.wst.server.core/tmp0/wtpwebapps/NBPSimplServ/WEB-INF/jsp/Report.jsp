<%@page import="uk.ac.nottingham.ningboport.client.test.GPSManager"%>
<%@page import="uk.ac.nottingham.ningboport.client.test.GPSTest"%>
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
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script>
  $(function() {
    $( "#datefrom" ).datepicker();
    $( "#dateto" ).datepicker();
    
    $( "#task_datefrom" ).datepicker();
    $( "#task_dateto" ).datepicker();
  });
</script>
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
		<button id="generate">Generate Routes</button>
		<script type="text/javascript">
				    $(function() {
					    $("#generate").click(function(){
					    	generate_send();		    	
					    });
				    });
	    
					var generate_createXmlHttpRequest = (function() {
					    var factories = [
					        function() { return new XMLHttpRequest(); },
					        function() { return new ActiveXObject("Msxml2.XMLHTTP.6.0"); },
					        function() { return new ActiveXObject("Msxml2.XMLHTTP.3.0"); },
					        function() { return new ActiveXObject("Microsoft.XMLHTTP"); }
					    ];

					    for (var i = 0, len = factories.length; i < len; ++i) {
					        try {
					            if ( factories[i]() ) return factories[i];
					        } catch (e) {}
					    }
					})();
					
					function generate_send()
					{

						var xmlhttp = generate_createXmlHttpRequest();
						
						xmlhttp.onreadystatechange=function()
						  {
						  if (xmlhttp.readyState==4 && xmlhttp.status==200)
						    {
							   alert(xmlhttp.responseText);
							   
						    }
						  }
						xmlhttp.open("GET","changeSer",true);
						xmlhttp.send();
					}
		</script>
		<div class="full_width">
			<div id="tabs">
				<ul>
					<li><a href="#tabs-3">GPS_Realtime</a></li>
					<li><a href="#tabs-1">Data</a></li>
					<li><a href="#tabs-2">GPS_Search</a></li>
					<li><a href="#tabs-4">Task_Route</a></li>
					<li><a href="#tabs-5">Modify_Route</a></li>
				</ul>
				<div id="tabs-1">
					<%=sb.toString()%>
				</div>
				
				
				<div id="tabs-3">			
					<div id="gps_tracking">
					<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.2&key=b98efe6b74317f1a2739cd3b03b3511e">
					</script> 					
					<script type="text/javascript">
					var createXmlHttpRequest = (function() {
					    var factories = [
					        function() { return new XMLHttpRequest(); },
					        function() { return new ActiveXObject("Msxml2.XMLHTTP.6.0"); },
					        function() { return new ActiveXObject("Msxml2.XMLHTTP.3.0"); },
					        function() { return new ActiveXObject("Microsoft.XMLHTTP"); }
					    ];

					    for (var i = 0, len = factories.length; i < len; ++i) {
					        try {
					            if ( factories[i]() ) return factories[i];
					        } catch (e) {}
					    }
					})();
					
					function loadXMLDoc()
					{

						var xmlhttp = createXmlHttpRequest();
						
						xmlhttp.onreadystatechange=function()
						  {
						  if (xmlhttp.readyState==4 && xmlhttp.status==200)
						    {
							   gps_tracking_initial(xmlhttp.responseXML);
							   
						    }
						  }
						xmlhttp.open("GET","GPS",true);
						xmlhttp.send();
					}
					
					function getRandomColor(){
						  return  '#' +
						    (function(color){
						    return (color +=  '0123456789abcdef'[Math.floor(Math.random()*16)])
						      && (color.length == 6) ?  color : arguments.callee(color);
						  })('');
					}

						//var update = 0;
						var p1 = new AMap.LngLat(121, 29);
						var mapObj1 = new AMap.Map("gps_tracking",{center:p1});
						loadXMLDoc();
						 
					function gps_tracking_initial(xml){
							//update ++;
							var xmlDoc = xml.documentElement;
							
							var gpsList = xmlDoc.getElementsByTagName("gps");
							var length = gpsList.length;
							var i;
							var m = new Array(length);
							var n = new Array(length);
							var info = new Array(length);
							var marker = new Array(length);
							for (i = 0; i < length; i ++) {
								n = gpsList[i];
								var sessionID = n.getElementsByTagName("sessionID")[0].childNodes[0].nodeValue;
								var longitude = n.getElementsByTagName("longitude")[0].childNodes[0].nodeValue;
								var latitude = n.getElementsByTagName("latitude")[0].childNodes[0].nodeValue;
								var driverName = n.getElementsByTagName("driverName")[0].childNodes[0].nodeValue;
								var vehicleID = n.getElementsByTagName("vehicleID")[0].childNodes[0].nodeValue;
								//var startTime = n.getElementsByTagName("startTime")[0].childNodes[0].nodeValue;
								//var expireTime = n.getElementsByTagName("expireTime")[0].childNodes[0].nodeValue;
								var Time = n.getElementsByTagName("Time")[0].childNodes[0].nodeValue;

								var info1 = "Session" + sessionID + " DriverName: " + driverName + " vehicleID: " + vehicleID;
								var info2 = "Longitude: " + longitude + " Latitude: " + latitude;
								var info3 = Time;


							    m[i] = document.createElement("div");  
								
							    m[i].className =  "session" + sessionID;  
							    n[i] = document.createElement("div");  
							    n[i].innerHTML = "s" + sessionID; 
							    m[i].appendChild(n[i]); 

							    eval('var marker' + i + ' = new AMap.Marker({map:mapObj1, ' +
							    	'position: new AMap.LngLat(longitude, latitude),offset: new AMap.Pixel(-10,-34),' +  
									'content:m[i]});');
								

							    eval('marker[i] = marker' + i); 
							    
							    info[i] = [];
								info[i].push(info1); 
								info[i].push(info2);  
								info[i].push(info3);   

								eval('var inforWindow' + i + ' = new AMap.InfoWindow({  ' +                
								       'offset:new AMap.Pixel(45,-34),' +                  
								       'content:info[i] .join("<br/>")});');    

								eval('AMap.event.addListener( marker' + i + ',"click",function(e){' +   
									'inforWindow' + i + '.open(mapObj1,marker' + i + '.getPosition());})');  
								
								
								$("div.session" + sessionID).css("height","40px");
								$("div.session" + sessionID).css("width","20px");
								$("div.session" + sessionID + " div").css("background-color",getRandomColor());
								$("div.session" + sessionID).css("background","url(http://webapi.amap.com/images/0.png)  left top no-repeat");

								
							}
							mapObj1.setFitView(marker);
					}

						setInterval("loadXMLDoc()","10000");

					  </script>  	
					</div>					
				</div>
				
				<div id="tabs-2">
				
					<label>driverName</label>
					<input id="driverName">
					<label>vehicleID</label>
					<input id="vehicleID">
					<p>
						<label>Date from</label> <input type="text" id="datefrom" /> 
						<label>To</label> <input type="text" id="dateto" />
					</p>
					<p>
						<button id="search">show</button>
					</p> 
					<div id="gps_search"></div>

					<script type="text/javascript">

					var createXmlHttpRequest = (function() {
					    var factories = [
					        function() { return new XMLHttpRequest(); },
					        function() { return new ActiveXObject("Msxml2.XMLHTTP.6.0"); },
					        function() { return new ActiveXObject("Msxml2.XMLHTTP.3.0"); },
					        function() { return new ActiveXObject("Microsoft.XMLHTTP"); }
					    ];

					    for (var i = 0, len = factories.length; i < len; ++i) {
					        try {
					            if ( factories[i]() ) return factories[i];
					        } catch (e) {}
					    }
					})();
				    
					
					var xmlHttpRequest = createXmlHttpRequest();
					
				    function sendRequestPost(url,param){  
				    	 
				        xmlHttpRequest.open("POST",url,true);  
				        xmlHttpRequest.setRequestHeader("Content-Type","application/x-www-form-urlencoded");  
				        xmlHttpRequest.onreadystatechange = processResponse;  
				        xmlHttpRequest.send(param);  
				    }  
				
				    function processResponse(){  
				        if(xmlHttpRequest.readyState == 4){  
				            if(xmlHttpRequest.status == 200){  
				            	gps_routes(xmlHttpRequest.responseXML);  
				            }else{  
				                window.alert("request page not normal");  
				            }  
				        }  
				    }  
				 
				    function search(){  
				        var driverName =  $("#driverName").val();  
				        var vehicleID = $("#vehicleID").val();  
				  		var startDate = $("#datefrom").val();
				  		var endDate = $("#dateto").val();
				        var url = "GPS";  
				        var param = "driverName="+ driverName +"&vehicleID="+vehicleID
				       		 +"&startDate=" +startDate +"&endDate="+ endDate;  
				        
				        sendRequestPost(url,param);  
				       
				    }  

				    $(function() {
					    $("#search").click(function(){
					    	search();		    	
					    });
				    });
				    
					  <!-- show the gps routes -->

						function getRandomColor(){
						  return  '#' +
						    (function(color){
						    return (color +=  '0123456789abcdef'[Math.floor(Math.random()*16)])
						      && (color.length == 6) ?  color : arguments.callee(color);
						  })('');
						}
						
						var p2 =new AMap.LngLat(121, 29);
						var mapObj2 =new AMap.Map("gps_search",{center:p2});
						
						function gps_routes(xml){  
							var xmlDoc = xml.documentElement;
							mapObj2.clearMap();
							var gpsList = xmlDoc.getElementsByTagName("gps");
							var length = gpsList.length;
							var sessions =new Array();
							var i;
							
							for (i = 0; i < length; i ++) {
								var l = sessions.length;
								var k;
								var n = gpsList[i];
								var sessionID = n.getElementsByTagName("sessionID")[0].childNodes[0].nodeValue;
								var ifexist = false;
								for(k = 0; k < l; k++){
									if(sessions[k] == sessionID){
										ifexist = true;
										break;
									}
								}
								if (!ifexist){
									sessions.push(sessionID);
								}
							}
							
							var session_length = sessions.length;
							
							var m = new Array(session_length);
							var n = new Array(session_length);
							var info = new Array(length);
							var arr = new Array(session_length);
							var marker = new Array(length);
							
							for (i = 0; i < session_length; i ++) {
								arr[i] = new Array();
							}
							
							for (i = 0; i < length; i ++) {
								var k;
								n = gpsList[i];
								var sessionID = n.getElementsByTagName("sessionID")[0].childNodes[0].nodeValue;
								var sessions_id = 0;
								for(k = 0; k < session_length; k++){
									if(sessions[k] == sessionID){
										sessions_id = k;
									}
								}
								var longitude = n.getElementsByTagName("longitude")[0].childNodes[0].nodeValue;
								var latitude = n.getElementsByTagName("latitude")[0].childNodes[0].nodeValue;
								var driverName = n.getElementsByTagName("driverName")[0].childNodes[0].nodeValue;
								var vehicleID = n.getElementsByTagName("vehicleID")[0].childNodes[0].nodeValue;
								//var startTime = n.getElementsByTagName("startTime")[0].childNodes[0].nodeValue;
								//var expireTime = n.getElementsByTagName("expireTime")[0].childNodes[0].nodeValue;
								var Time = n.getElementsByTagName("Time")[0].childNodes[0].nodeValue;
								
								arr[sessions_id].push(new AMap.LngLat(longitude ,latitude)); 
								
								var info1 = "Session" + sessionID + " DriverName: " + driverName + " vehicleID: " + vehicleID;
								var info2 = "Longitude: " + longitude + " Latitude: " + latitude;
								var info3 = Time;


							    m[sessions_id] = document.createElement("div");  
								
							    m[sessions_id].className =  "session" + sessionID;  
							    n[sessions_id] = document.createElement("div");  
							    n[sessions_id].innerHTML = "s" + sessionID; 
							    m[sessions_id].appendChild(n[sessions_id]); 

							    eval('var marker' + i + ' = new AMap.Marker({map:mapObj2, ' +
								    	'position: new AMap.LngLat(longitude, latitude),offset: new AMap.Pixel(-10,-34),' +  
										'content:m[sessions_id]});');
							    
							    eval('marker[i] = marker' + i); 
							    
							    info[i] = [];
								info[i].push(info1); 
								info[i].push(info2);  
								info[i].push(info3);   

								eval('var inforWindow' + i + ' = new AMap.InfoWindow({  ' +                
								       'offset:new AMap.Pixel(45,-34),' +                  
								       'content:info[i] .join("<br/>")});');    

								eval('AMap.event.addListener( marker' + i + ',"click",function(e){' +   
									'inforWindow' + i + '.open(mapObj2,marker' + i + '.getPosition());})'); 				
								
							}
							
							var polyline = new Array(session_length);
							
							for (i = 0; i < session_length; i++) {
								var id = sessions[i];

							$("div.session" + id).css("height","40px");
							$("div.session" + id).css("width","20px");
							$("div.session" + id + " div").css("background-color",getRandomColor());
							$("div.session" + id).css("background","url(http://webapi.amap.com/images/0.png)  left top no-repeat");
								polyline[i]  = new AMap.Polyline({                    
										map:mapObj2,                  
									    path:arr[i],                    
									    strokeColor:getRandomColor(),                    
									    strokeOpacity:0.4,                    
									    strokeWeight:3,                    
									    strokeStyle:"dashed",                    
									    strokeDasharray:[10,5]                    
								}); 
							}
							mapObj2.setFitView(marker);
						}
						search();
					</script>  
				</div>
				
				<div id="tabs-4">
				
					<label>driverName</label>
					<input id="task_driverName">
					<label>vehicleID</label>
					<input id="task_vehicleID">
					<p>
						<label>Date from</label> <input type="text" id="task_datefrom" /> 
						<label>To</label> <input type="text" id="task_dateto" />
					</p>
					<p>
						<button id="task_button">show</button>
					</p> 
					<div id="task_search"></div>

					<script type="text/javascript">

					var createXmlHttpRequest1 = (function() {
					    var factories = [
					        function() { return new XMLHttpRequest(); },
					        function() { return new ActiveXObject("Msxml2.XMLHTTP.6.0"); },
					        function() { return new ActiveXObject("Msxml2.XMLHTTP.3.0"); },
					        function() { return new ActiveXObject("Microsoft.XMLHTTP"); }
					    ];

					    for (var i = 0, len = factories.length; i < len; ++i) {
					        try {
					            if ( factories[i]() ) return factories[i];
					        } catch (e) {}
					    }
					})();
				    
					
					var xmlHttpRequest1 = createXmlHttpRequest1();
					
				    function sendRequestPost1(url,param){  
				        xmlHttpRequest1.open("POST",url,true);  
				        xmlHttpRequest1.setRequestHeader("Content-Type","application/x-www-form-urlencoded");  
				        xmlHttpRequest1.onreadystatechange = processResponse1;  
				        xmlHttpRequest1.send(param);  
				    }  
				
				    function processResponse1(){  
				        if(xmlHttpRequest1.readyState == 4){  
				            if(xmlHttpRequest1.status == 200){ 
				            	task_routes(xmlHttpRequest1.responseXML);  
				            }else{  
				                window.alert("request page not normal");  
				            }  
				        }  
				    }  
				 
				    function search1(){  
				        var driverName =  $("#task_driverName").val();  
				        var vehicleID = $("#task_vehicleID").val();  
				  		var startDate = $("#task_datefrom").val();
				  		var endDate = $("#task_dateto").val();
				        var url = "Task";  
				        var param = "driverName="+ driverName +"&vehicleID="+vehicleID
				       		 +"&startDate=" +startDate +"&endDate="+ endDate;  
				        sendRequestPost1(url,param);  
				    }  

				    $(function() {
					    $("#task_button").click(function(){
					    	search1();		    	
					    });
				    });
				    
					  <!-- show the task routes -->

						function getRandomColor(){
						  return  '#' +
						    (function(color){
						    return (color +=  '0123456789abcdef'[Math.floor(Math.random()*16)])
						      && (color.length == 6) ?  color : arguments.callee(color);
						  })('');
						}
						
						var p3 =new AMap.LngLat(121, 29);
						var mapObj3 =new AMap.Map("task_search",{center:p3});
						
						function task_routes(xml){  
							var xmlDoc = xml.documentElement;
							mapObj3.clearMap();
							var taskList = xmlDoc.getElementsByTagName("task");
							var length = taskList.length;
							var sessions =new Array();
							var i;
							
							for (i = 0; i < length; i ++) {
								var l = sessions.length;
								var k;
								var n = taskList[i];
								var sessionID = n.getElementsByTagName("sessionID")[0].childNodes[0].nodeValue;
								var ifexist = false;
								for(k = 0; k < l; k++){
									if(sessions[k] == sessionID){
										ifexist = true;
										break;
									}
								}
								if (!ifexist){
									sessions.push(sessionID);
								}
							}
							
							var session_length = sessions.length;
							
							var m = new Array(session_length*2);
							var n = new Array(session_length*2);
							var info = new Array(length*2);
							var arr = new Array(session_length);
							var marker = new Array(length*2);
							
							for (i = 0; i < session_length; i ++) {
								arr[i] = new Array();
							}
							
							for (i = 0; i < length; i ++) {
								var k;
								n = taskList[i];
								var sessionID = n.getElementsByTagName("sessionID")[0].childNodes[0].nodeValue;
								var sessions_id = 0;
								for(k = 0; k < session_length; k++){
									if(sessions[k] == sessionID){
										sessions_id = k;
									}
								}
								
								var des_index = length+i;
								
								var driverName = n.getElementsByTagName("driverName")[0].childNodes[0].nodeValue;
								var vehicleID = n.getElementsByTagName("vehicleID")[0].childNodes[0].nodeValue;
								var shiftTime = n.getElementsByTagName("shiftTime")[0].childNodes[0].nodeValue;	
								var routeID = n.getElementsByTagName("routeID")[0].childNodes[0].nodeValue;		
								var sequenceNo = n.getElementsByTagName("sequenceNo")[0].childNodes[0].nodeValue;
								var small = n.getElementsByTagName("small")[0].childNodes[0].nodeValue;
								var large = n.getElementsByTagName("large")[0].childNodes[0].nodeValue;
								var commodityID = n.getElementsByTagName("commodityID")[0].childNodes[0].nodeValue;
								var source = n.getElementsByTagName("source")[0].childNodes[0].nodeValue;
								var destination = n.getElementsByTagName("destination")[0].childNodes[0].nodeValue;
								var src_longitude = n.getElementsByTagName("srcLongitude")[0].childNodes[0].nodeValue;
								var src_latitude = n.getElementsByTagName("srcLatitude")[0].childNodes[0].nodeValue;
								var des_longitude = n.getElementsByTagName("desLongitude")[0].childNodes[0].nodeValue;
								var des_latitude = n.getElementsByTagName("desLatitude")[0].childNodes[0].nodeValue;
								
								
								
								var nums =new Array();
								arr[sessions_id].push(new AMap.LngLat(src_longitude ,src_latitude));
								arr[sessions_id].push(new AMap.LngLat(des_longitude ,des_latitude));

								var info1 = "Session" + sessionID + " DriverName: " + driverName + " vehicleID: " + vehicleID;
								var info2 = "ShiftTime: " + shiftTime + " sequenceNo: " + sequenceNo;
								var info3 = "small: " + small + " large: " + large;
								var info4 = "commodityID: " + commodityID + " src: " + source ;
								var info5 = "commodityID: " + commodityID + " des: " + destination;

							    m[sessions_id] = document.createElement("div");  
							    m[sessions_id].className =  "session" + sessionID;  
							    n[sessions_id] = document.createElement("div");  
							    n[sessions_id].innerHTML = "s" + sessionID; 
							    m[sessions_id].appendChild(n[sessions_id]); 
							    
							    m[sessions_id+session_length] = document.createElement("div");  
							    m[sessions_id+session_length].className =  "session" + sessionID;  
							    n[sessions_id+session_length] = document.createElement("div");  
							    n[sessions_id+session_length].innerHTML = "s" + sessionID; 
							    m[sessions_id+session_length].appendChild(n[sessions_id+session_length]); 

							    eval('var marker' + i + ' = new AMap.Marker({map:mapObj3, ' +
								    	'position: new AMap.LngLat(src_longitude, src_latitude),offset: new AMap.Pixel(-10,-34),' +  
										'content:m[sessions_id]});');
							    
							    eval('marker[i] = marker' + i); 
							    
							    info[i] = [];
								info[i].push(info1); 
								info[i].push(info2);  
								info[i].push(info3);   
								info[i].push(info4);
								
								eval('var inforWindow' + i + ' = new AMap.InfoWindow({  ' +                
								       'offset:new AMap.Pixel(45,-34),' +                  
								       'content:info[i] .join("<br/>")});');    

								eval('AMap.event.addListener( marker' + i + ',"click",function(e){' +   
									'inforWindow' + i + '.open(mapObj3,marker' + i + '.getPosition());})'); 

							    eval('var marker' + des_index + ' = new AMap.Marker({map:mapObj3, ' +
								    	'position: new AMap.LngLat(des_longitude, des_latitude),offset: new AMap.Pixel(-10,-34),' +  
										'content:m[sessions_id+session_length]});');
							    
							    eval('marker[length+i] = marker' + des_index); 
								
							    info[des_index] = [];
								info[des_index].push(info1); 
								info[des_index].push(info2);  
								info[des_index].push(info3);   
								info[des_index].push(info5);

								eval('var inforWindow' + des_index + ' = new AMap.InfoWindow({  ' +                
									       'offset:new AMap.Pixel(45,-34),' +                  
									       'content:info[length+i] .join("<br/>")});');    

								eval('AMap.event.addListener( marker' + des_index + ',"click",function(e){' +   
									'inforWindow' + des_index + '.open(mapObj3,marker' + des_index + '.getPosition());})'); 	
							}
							

							for (var i = 0; i < session_length; i++) {
								var id = sessions[i];
								
								var route = new AMap.RouteSearch();
								
								route.getNaviPath(arr[i],function(data){
									var nums = new Array();
									var lists = data.list;
									for(var e = 0; e < lists.length; e++){
										var s = lists[e].coor;
										var a = s.split(";");
										for(var k = 0; k < a.length; k++){
											var ll = a[k];
											var lo = ll.split(",");
											nums.push(new AMap.LngLat(lo[0] ,lo[1]));
										}
									}
									var polyline = new AMap.Polyline({                    
										map:mapObj3,                  
									    path:nums,                    
									    strokeColor:getRandomColor(),                    
									    strokeOpacity:0.8,                    
									    strokeWeight:3,                    
									    strokeStyle:"dashed",                    
									    strokeDasharray:[10*Math.random(),5*Math.random()],     
									});
								});
		
								$("div.session" + id).css("height","40px");
								$("div.session" + id).css("width","20px");
								$("div.session" + id + " div").css("background-color",getRandomColor());
								$("div.session" + id).css("background","url(http://webapi.amap.com/images/0.png)  left top no-repeat");

						}
						mapObj3.setFitView(marker);
					}
						
					search1();
					</script>  					
				</div>
				<div id="tabs-5">
					<iframe src="change" style="width: 100%; height: 100%;">
						<p>Your browser does not support iframes.</p>
					</iframe>
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