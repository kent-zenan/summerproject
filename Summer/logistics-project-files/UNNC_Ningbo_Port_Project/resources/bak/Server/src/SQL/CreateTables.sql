drop table IF EXISTS Box,Gps,Session,Driver,Vehicle;

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

CREATE TABLE Box (
	declareID VARCHAR(100),
	bigBox INT,
	smallBox INT,
	
	CONSTRAINT boxPK PRIMARY KEY (declareID)
	/*Maybe add a foreign key here */
);

CREATE TABLE Gps (
	gps_sessionID INT NOT NULL,
	longitude DOUBLE,
	latitude DOUBLE,
	
	CONSTRAINT gpsPK PRIMARY KEY (gps_sessionID),
	CONSTRAINT gps_sessionFK FOREIGN KEY(gps_sessionID) REFERENCES Session(sessionID)
);