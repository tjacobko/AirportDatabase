/*Customer*/
CREATE INDEX customer_id on Customer USING BTREE(id);
CREATE INDEX customer_fname on Customer USING BTREE(fname);
CREATE INDEX customer_lname on Customer USING BTREE(lname);
CREATE INDEX customer_gtype on Customer USING BTREE(gtype);
CREATE INDEX customer_dob on Customer USING BTREE(dob);
CREATE INDEX customer_address on Customer USING BTREE(address);
CREATE INDEX customer_phone on Customer USING BTREE(phone);
CREATE INDEX customer_zipcode on Customer USING BTREE(zipcode);

/*Pilot*/
CREATE INDEX pilot_id on Pilot USING BTREE(id);
CREATE INDEX pilot_fullname on Pilot USING BTREE(fullname);
CREATE INDEX pilot_nationality on Pilot USING BTREE(nationality);

/*Flight*/
CREATE INDEX flight_fnum on Flight USING BTREE(fnum);
CREATE INDEX flight_cost on Flight USING BTREE(cost);
CREATE INDEX flight_num_sold on Flight USING BTREE(num_sold);
CREATE INDEX flight_num_stops on Flight USING BTREE(num_stops);
CREATE INDEX flight_actual_departure_date on Flight USING BTREE(actual_departure_date);
CREATE INDEX flight_actual_arrival_date on Flight USING BTREE(actual_arrival_date);
CREATE INDEX flight_arrival_airport on Flight USING BTREE(arrival_airport);
CREATE INDEX flight_departure_airport on Flight USING BTREE(departure_airport);

/*Plane*/
CREATE INDEX plane_id on Plane USING BTREE(id);
CREATE INDEX plane_make on Plane USING BTREE(make);
CREATE INDEX plane_model on Plane USING BTREE(model);
CREATE INDEX plane_age on Plane USING BTREE(age);
CREATE INDEX plane_seats on Plane USING BTREE(seats);

/*Technician*/
CREATE INDEX tech_id on Technician USING BTREE(id);
CREATE INDEX tech_full_name on Technician USING BTREE(full_name);

/*Reservation*/
CREATE INDEX reservation_rnum on Reservation USING BTREE(rnum);
CREATE INDEX reservation_cid on Reservation USING BTREE(cid);
CREATE INDEX reservation_fid on Reservation USING BTREE(fid);
CREATE INDEX reservation_status on Reservation USING BTREE(status);

/*FlightInfo*/
CREATE INDEX FlightInfo_fiid on FlightInfo USING BTREE(fiid);
CREATE INDEX FlightInfo_flight_id on FlightInfo USING BTREE(flight_id);
CREATE INDEX FlightInfo_pilot_id on FlightInfo USING BTREE(pilot_id);
CREATE INDEX FlightInfo_plane_id on FlightInfo USING BTREE(plane_id);

/*Repairs*/
CREATE INDEX repair_rid on Repairs USING BTREE(rid);
CREATE INDEX repair_repair_date on Repairs USING BTREE(repair_date);
CREATE INDEX repair_repair_code on Repairs USING BTREE(repair_code);
CREATE INDEX repair_pilot_id on Repairs USING BTREE(pilot_id);
CREATE INDEX repair_plane_id on Repairs USING BTREE(plane_id);
CREATE INDEX repair_tech_id on Repairs USING BTREE(technician_id);

/*Schedule*/
CREATE INDEX schedule_id on Schedule USING BTREE(id);
CREATE INDEX schedule_flightNum on Schedule USING BTREE(flightNum);
CREATE INDEX schedule_departure_time on Schedule USING BTREE(departure_time);
CREATE INDEX schedule_arrival_time on Schedule USING BTREE(arrival_time);


