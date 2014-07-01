drop table IF EXISTS Login,Box,Gps,Session,Driver,Vehicle;

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


CREATE TABLE Box (
	declareID VARCHAR(100),
	bigBox INT,
	smallBox INT,
	
	CONSTRAINT boxPK PRIMARY KEY (declareID)
	/*Maybe add a foreign key here */
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