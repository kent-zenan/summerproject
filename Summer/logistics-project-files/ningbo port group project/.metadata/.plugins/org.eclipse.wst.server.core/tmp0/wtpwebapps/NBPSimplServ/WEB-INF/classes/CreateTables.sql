drop table IF EXISTS Route,Login,Gps,Session,Driver,Vehicle,Ports,Commodity,Task;

CREATE TABLE Driver (
	driverName VARCHAR(100) NOT NULL,
	password VARCHAR(100) NOT NULL,
	
	CONSTRAINT driverPK PRIMARY KEY (driverName)
);

CREATE TABLE Vehicle (
	vehicleID VARCHAR(100) NOT NULL,
	
	CONSTRAINT vehiclePK PRIMARY KEY (vehicleID)
);

CREATE TABLE Session (
	sessionID INT AUTO_INCREMENT,
	session_driverName VARCHAR(100),
	session_vehicleID VARCHAR(100),
	startTime VARCHAR(25), 
	expireTime VARCHAR(25),
    
	CONSTRAINT sessionPK PRIMARY KEY (sessionID),
	CONSTRAINT session_driverFK FOREIGN KEY(session_driverName) REFERENCES Driver(driverName),
    CONSTRAINT session_vehicleFK FOREIGN KEY(session_vehicleID) REFERENCES Vehicle(vehicleID),
    CONSTRAINT session_unique UNIQUE(session_driverName,session_vehicleID,startTime,expireTime)
);

CREATE TABLE Login (
	loginID INT NOT NULL AUTO_INCREMENT,
	login_driverName VARCHAR(100),
	login_vehicleID VARCHAR(100),
	login_password VARCHAR(100),
	login_success BOOLEAN,
	login_time VARCHAR(25),
	
	CONSTRAINT loginPK PRIMARY KEY (loginID)
);


CREATE TABLE Gps (
	gpsID INT NOT NULL AUTO_INCREMENT,
	gps_sessionID INT NOT NULL,
	longitude DOUBLE,
	latitude DOUBLE,
	gps_time VARCHAR(25),
	
	CONSTRAINT gpsPK PRIMARY KEY (gpsID),
	CONSTRAINT gps_sessionFK FOREIGN KEY(gps_sessionID) REFERENCES Session(sessionID)
);



CREATE TABLE Ports (
	name VARCHAR(20) NOT NULL,
	gps_l DOUBLE NOT NULL,
	gps_a DOUBLE NOT NULL
);

insert into Ports values ("BLCT", 121.517, 28.33);
insert into Ports values ("BLCT2", 120.2367, 28.3355);
insert into Ports values ("BLCT3", 121.2367, 28.5544);
insert into Ports values ("BLCTYD", 121.1122, 29.9833);
insert into Ports values ("BLCTZS", 121.34211, 29.9822);
insert into Ports values ("BLCTE", 121.8832, 30.58);
insert into Ports values ("BLCTMS", 120.9982, 28.4433);
insert into Ports values ("ZHCT", 122.2345, 28.9834);
insert into Ports values ("B2SCT", 121.2345, 29.8888);

CREATE TABLE Commodity (
	id VARCHAR(50) NOT NULL REFERENCES Ports.name,
	source VARCHAR(20) NOT NULL,
	destination VARCHAR(20) NOT NULL,
	available_t DATETIME NOT NULL,
	deadline DATETIME NOT NULL,
	small INT NOT NULL,
	large INT NOT NULL,
	finished_small INT NOT NULL,
	finished_large INT NOT NULL
);

LOAD DATA INFILE "/var/lib/mysql/commodity_sorted" into table Commodity;

CREATE TABLE Route (
    id INT AUTO_INCREMENT PRIMARY KEY,
	sessionID INT REFERENCES Session.sessionID,
    shift_normal_start_time DATETIME NOT NULL,
    route_num INT NOT NULL,
	status INT NOT NULL DEFAULT 0,
    CONSTRAINT shift_route UNIQUE (shift_normal_start_time, route_num)
);

CREATE TABLE Task (
    id INT AUTO_INCREMENT PRIMARY KEY,
	commodity_id VARCHAR(50) NOT NULL REFERENCES Commodity.id,
	route_id INT REFERENCES Route.id,
	sequence_num INT NOT NULL,
    small INT NOT NULL,
    large INT NOT NULL,
    planned_start DATETIME,
    planned_finish DATETIME,
	actual_start DATETIME,
	actual_finish DATETIME,
	status INT NOT NULL DEFAULT 0
);

insert into Driver(driverName,password) values ("admin","admin");
insert into Driver(driverName,password) values ("a","a");
insert into Driver(driverName,password) values ("b","b");
insert into Driver(driverName,password) values ("c","c");
insert into Driver(driverName,password) values ("d","d");
insert into Driver(driverName,password) values ("e","e");
insert into Driver(driverName,password) values ("f","f");
insert into Vehicle values ("11223");
insert into Vehicle values ("00000");


