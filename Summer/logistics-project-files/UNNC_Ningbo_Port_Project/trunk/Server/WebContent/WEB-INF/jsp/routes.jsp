<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.nottingham.ningboport.planner.Route"%>
<%@ page import="uk.ac.nottingham.ningboport.planner.RoutingPlanner"%>
<%@ page import="uk.ac.nottingham.ningboport.planner.Network"%>
<%@page import="uk.ac.nottingham.ningboport.planner.Task"%>
<%@page import="uk.ac.nottingham.ningboport.client.test.TaskUpdate"%>


<%
	TaskUpdate t = new TaskUpdate();
	Vector<Route> routes = t.getRoutes();
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<%
	session.setAttribute("routes", routes);

	Route route;
	Vector<Task> taskSet;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'route.jsp' starting page</title>
<!-- <link rel="stylesheet" href="http://112.124.50.122/NBPSimplServ/flora.all.css" type="text/css" -->
<!-- 	media="screen" title="Flora (Default)" /> -->
<link rel="stylesheet" href="http://112.124.50.122/NBPSimplServ/style.css" type="text/css" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="http://112.124.50.122/NBPSimplServ/jquery-1.2.4b.js"></script>

<script type="text/javascript" src="http://112.124.50.122/NBPSimplServ/ui.core.js"></script>
<script type="text/javascript" src="http://112.124.50.122/NBPSimplServ/ui.sortable.js"></script>
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
.hover {
	background: #000;
}

.unassigned {
	width: 15px;
	height: 15px;
	background: #ffff00;
	-moz-border-radius: 50px;
	-webkit-border-radius: 50px;
	border-radius: 50px;
	margin-top: 20px;
	margin-left: 10px;
	margin-bottom: 50px;
	float: left;
}

.assigned {
	width: 15px;
	height: 15px;
	background: #00ff00;
	-moz-border-radius: 50px;
	-webkit-border-radius: 50px;
	border-radius: 50px;
	margin-top: 20px;
	margin-left: 10px;
	margin-bottom: 50px;
	float: left;
}

.session_ended {
	width: 15px;
	height: 15px;
	background: #7e7e7e;
	-moz-border-radius: 50px;
	-webkit-border-radius: 50px;
	border-radius: 50px;
	margin-top: 20px;
	margin-left: 10px;
	margin-bottom: 50px;
	float: left;
}


.session_suspend {
	width: 15px;
	height: 15px;
	background: #ffff00;
	-moz-border-radius: 50px;
	-webkit-border-radius: 50px;
	border-radius: 50px;
	margin-top: 20px;
	margin-left: 10px;
	margin-bottom: 50px;
	float: left;
}
</style>
<script language="javascript">

$(window).bind('load', function() {
	//$("#sort0").sortable({ placeholder: "hover", revert: true });
	prepare();
	for ( var i = 0; i <<%=routes.size()%>; i++) {
		$("#sort" + i).sortable({
			placeholder : "hover",
			connectWith : getOthers(i,<%=routes.size()%>),
			revert : true,
			receive : function(e, ui) {

				//console.log("received ", ui.item, " from ", ui.sender);
			},
			update : function(event, ui) {
	
				var xmlHttp=ajaxFunction();

				xmlHttp.open("post","change?timeStamp="+new Date().getTime(),true);
				xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
				xmlHttp.send("order="+$(this).sortable('toArray').toString());	
			}	
		});
	};
});

	function prepare() {
		<%for (int i = 0; i < routes.size(); i++) {
				route = routes.get(i);
				taskSet = route.taskSet;
				String status1 = "";
				
				switch(route.getStatus()){
				case UNASSIGNED:
					status1 = "unassigned";
					break;
				case ASSIGNED:
					status1 = "assigned";
					break;
				case SESSION_SUSPENDED:
					status1 = "session_suspend";
					break;
					
				case SESSION_ENDED:
					status1 = "session_ended";
					break;				
				}
				%>
			var ulElement = document.createElement("ul");
			ulElement.setAttribute("id", "sort" + <%=i%>);
			ulElement.setAttribute("name", "sort" + <%=i%>);
			ulElement.setAttribute(
							"style",
							"list-style-position: inside; height: 80px; cursor: hand; cursor: pointer;list-style: none;");
			var liElement = document.createElement("li");
			liElement.setAttribute(
					"style",
					"float: left; width: 300px; height:75px; border: 1px solid #bbb;  background: #ff0c47; color:white;");
			var point = document.createElement("a");

			point.setAttribute("class", "<%=status1%>");
			point.setAttribute("id", "route" + <%=i%>);
			point.setAttribute("href", "javascript:changeState(\""+ point.getAttribute("id")+"\")");
			liElement.appendChild(point);
			ulElement.appendChild(liElement);
			liElement.setAttribute("id", <%=i+1%>	);
			
			<%for (int j = 0; j < taskSet.size(); j++) {%>
				liElement = document.createElement("li");
				<%Task task = taskSet.get(j);%>
				liElement
						.setAttribute(
								"style",
								"float: left; width: 300px; height:75px; border: 1px solid #bbb;  background: #ff0c47; color:white;");
				
					liElement.appendChild(document.createTextNode("<%=task.toDisplayString1()%>"));
					liElement.appendChild(document.createElement("br"));
					liElement.appendChild(document.createTextNode("<%=task.toDisplayString2()%>"));
					liElement.appendChild(document.createElement("br"));
					liElement.appendChild(document.createTextNode("<%=task.toDisplayString3()%>"));
					liElement.appendChild(document.createElement("br"));
					liElement.appendChild(document.createTextNode("<%=task.toDisplayString4()%>"));
					liElement.appendChild(document.createElement("br"));
					liElement.appendChild(document.createTextNode("<%=task.toDisplayString5()%>"));
					liElement.setAttribute("id", <%=task.taskID%>	);
					ulElement.appendChild(liElement);
	
<%}%>
	document.getElementById("sort").appendChild(ulElement);
<%}%>
	}

	function ajaxFunction() {
		var xmlHttp;
		try { // Firefox, Opera 8.0+, Safari
			xmlHttp = new XMLHttpRequest();
		} catch (e) {
			try {// Internet Explorer
				xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) {
				try {
					xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
				} catch (e) {
				}
			}
		}
		return xmlHttp;
	}

	function getOthers(number, amount) {
		var arr = new Array(amount - 1);
		for ( var i = 0; i < amount; i++) {
			if (i != number) {
				arr[i] = "#sort" + i;
			}
		}
		;
		return arr;
	}

	function changeState(elementID, routeID) {
		var route = document.getElementById(elementID);
		var status = route.getAttribute("class");
		
		if ( status == "session_suspend") {
			route.setAttribute("class", "assigned");

		} else if (status == "assigned"){
			route.setAttribute("class", "session_suspend");
		}
		var xmlHttp = ajaxFunction();

		xmlHttp.open("post", "changeSer?timeStamp="
				+ new Date().getTime(), true);
		xmlHttp.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded");
		xmlHttp.send("status=" + route.getAttribute("class") + "&" + "id="
				+ route.parentNode.getAttribute("id"));
		
		
	}
</script>
</head>

<body style="width: 10000px;">
	<form action="change" method="post" accept-charset="utf-8">
		<div class="playground" id="sort"></div>
		<br>
		<p>
			<input type="button" value="确定" style="margin: 0 0 0 840px;"
				onclick="submit()" />

		</p>
	</form>
</body>
</html>
