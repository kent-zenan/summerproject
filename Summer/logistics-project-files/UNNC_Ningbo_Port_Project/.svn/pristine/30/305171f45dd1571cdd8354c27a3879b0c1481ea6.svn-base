<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>NBPSimplServ Home</title>
<style type="text/css" title="currentStyle">
@import "http://112.124.50.122/NBPSimplServ/demo_page.css";

@import "http://112.124.50.122/NBPSimplServ/demo_table_jui.css";

@import "http://112.124.50.122/NBPSimplServ/jquery-ui-1.8.4.custom.css";
</style>
</head>
<body id="dt_example">
	<div id="container">
		<div class="full_width big">Simple Server of Ningbo Port Project</div>
		This site is aim at testing Android app and Android-Server
		communication.

		<h1>Site Map</h1>
		<ul>
			<li><a href="http://112.124.50.122:8080/NBPSimplServ/Login">Authentication
				Log</a></li>
			<li><a href="http://112.124.50.122:8080/NBPSimplServ/Report">GPS Log</a></li>
			<li><a href="http://112.124.50.122/NBPSimplServ/NingboPort.apk">Download
				Android App</a></li>
		</ul>
		<h1>How to test?</h1>
		<p>
			1. Download the <a
				href="http://112.124.50.122/NBPSimplServ/NingboPort.apk">App</a> to
			your Android device, and install it. NB, make sure the
			"settings->security->unknown source" is selected.
		</p>
		<p>2. Turn on your GPS before run the app, and then use the
			following information to login.
		<ul>
			<li><strong>Username: admin</strong></li>
			<li><strong>Password: admin</strong></li>
			<li><strong>Vehicle ID: 11223</strong></li>
		</ul>
		</p>
		<p>3. The app will update once right after you logged in, and it
			will update every 30 seconds. Each update will send a GPS location to
			server and server will response with instructions of how to update
			task list on the app.</p>
		<p>4. For testing purpose, server replies a task with action
			"add" each time, so you can see a task will be added to your app
			every 30 seconds.</p>
		<p>5. Long-press any task will pop up a menu, from which you can choose to start the task, 
		finish the task or go to the map. 
		</p>
		<p>
			5. Click <a href="http://112.124.50.122:8080/NBPSimplServ/Report">here</a>
			to see the log of submitted GPS locations, click <a
				href="http://112.124.50.122:8080/NBPSimplServ/Login">here</a> to see
			login request.
		</p>
		<h1>known issues</h1>
		<ul>
			<li>Fixed @ 31/07: <strike>[important @ 29/07] Default location and auto-location on the map.</strike></li>
			<li>Fixed @ 31/07: <strike>[critical @ 29/07] Stable issue: crash when return from the map.</strike></li>
			<li>Fixed @ 31/07: <strike>[important @ 31/07] Map location auto reset</strike></li>
			<li>No known issues in queue.</li>
		</ul>
		<h1>Implementation log</h1>
		<ul>
			<li>Implemented @ 31/07: <strike>[planned @ 29/07] Start of a task</strike></li>
			<li>Implemented @ 31/07: <strike>[planned @ 29/07] Finish of a task</strike></li>
			<li>No new feature in queue, waiting for integration.</li>
		</ul>
		<h1></h1>
		<p>
			Theme by <a href="http://jqueryui.com/">jQuery-UI</a>.<br /> System
			time:
			<%=request.getAttribute("date")%>, you're from:
			<%=request.getAttribute("addr")%>
		</p>
	</div>
</body>
</html>