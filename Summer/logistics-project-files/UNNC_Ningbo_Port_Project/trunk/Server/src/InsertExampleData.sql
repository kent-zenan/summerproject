INSERT INTO Driver VALUES("Alex", "politics");
INSERT INTO Driver VALUES("Turing", "computers");
INSERT INTO Driver VALUES("Jane", "smartypants");

INSERT INTO Vehicle VALUES ("9A773");
INSERT INTO Vehicle VALUES ("8S583");
INSERT INTO Vehicle VALUES ("11223");

INSERT INTO Session(session_driverName, session_vehicleID, startTime, expireTime) values ("Alex", "9A773", "1" , "2");

INSERT INTO Gps(gps_sessionID, longitude, latitude, gps_time ) VALUES (1, "123.22", "22.44" , "3");