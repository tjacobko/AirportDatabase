SELECT(
SELECT p.seats 
FROM Plane p
WHERE p.id = (
SELECT fi.plane_id
FROM FlightInfo fi
WHERE fi.flight_id = '50'))
-
(
SELECT f.num_sold
FROM flight f
WHERE f.fnum = '50')as seatsAvailable;
/*


SELECT fi.plane_id
FROM FlightInfo fi
WHERE fi.flight_id = '50';*/

SELECT COUNT(*) as totalRepairs, p.id
FROM Repairs r, Plane p
WHERE r.plane_id = p.id
GROUP BY p.id
ORDER BY totalRepairs DESC;

SELECT EXTRACT(YEAR FROM r.repair_date) AS year, COUNT(*) as totalRepiars
FROM Repairs r, Plane p
GROUP by year;

SELECT COUNT(*) as totalPassengers, r.status
FROM Reservation r, Customer c
WHERE r.cid = c.id AND r.status='W'
GROUP BY r.status;
