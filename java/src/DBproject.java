/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */

public class DBproject{
	//reference to physical database connection
	private Connection _connection = null;
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	public DBproject(String dbname, String dbport, String user, String passwd) throws SQLException {
		System.out.print("Connecting to database...");
		try{
			// constructs the connection URL
			String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
			System.out.println ("Connection URL: " + url + "\n");
			
			// obtain a physical connection
	        this._connection = DriverManager.getConnection(url, user, passwd);
	        System.out.println("Done");
		}catch(Exception e){
			System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
	        System.out.println("Make sure you started postgres on this machine");
	        System.exit(-1);
		}
	}
	
	/**
	 * Method to execute an update SQL statement.  Update SQL instructions
	 * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
	 * 
	 * @param sql the input SQL string
	 * @throws java.sql.SQLException when update failed
	 * */
	public void executeUpdate (String sql) throws SQLException { 
		// creates a statement object
		Statement stmt = this._connection.createStatement ();

		// issues the update instruction
		stmt.executeUpdate (sql);

		// close the instruction
	    stmt.close ();
	}//end executeUpdate

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and outputs the results to
	 * standard out.
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQueryAndPrintResult (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		/*
		 *  obtains the metadata object for the returned result set.  The metadata
		 *  contains row and column info.
		 */
		ResultSetMetaData rsmd = rs.getMetaData ();
		int numCol = rsmd.getColumnCount ();
		int rowCount = 0;
		
		//iterates through the result set and output them to standard out.
		boolean outputHeader = true;
		while (rs.next()){
			if(outputHeader){
				for(int i = 1; i <= numCol; i++){
					System.out.print(rsmd.getColumnName(i) + "\t");
			    }
			    System.out.println();
			    outputHeader = false;
			}
			for (int i=1; i<=numCol; ++i)
				System.out.print (rs.getString (i) + "\t");
			System.out.println ();
			++rowCount;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the results as
	 * a list of records. Each record in turn is a list of attribute values
	 * 
	 * @param query the input query string
	 * @return the query result as a list of records
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public List<List<String>> executeQueryAndReturnResult (String query) throws SQLException { 
		//creates a statement object 
		Statement stmt = this._connection.createStatement (); 
		
		//issues the query instruction 
		ResultSet rs = stmt.executeQuery (query); 
	 
		/*
		 * obtains the metadata object for the returned result set.  The metadata 
		 * contains row and column info. 
		*/ 
		ResultSetMetaData rsmd = rs.getMetaData (); 
		int numCol = rsmd.getColumnCount (); 
		int rowCount = 0; 
	 
		//iterates through the result set and saves the data returned by the query. 
		boolean outputHeader = false;
		List<List<String>> result  = new ArrayList<List<String>>(); 
		while (rs.next()){
			List<String> record = new ArrayList<String>(); 
			for (int i=1; i<=numCol; ++i) 
				record.add(rs.getString (i)); 
			result.add(record); 
		}//end while 
		stmt.close (); 
		return result; 
	}//end executeQueryAndReturnResult
	
	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT).  This
	 * method issues the query to the DBMS and returns the number of results
	 * 
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public int executeQuery (String query) throws SQLException {
		//creates a statement object
		Statement stmt = this._connection.createStatement ();

		//issues the query instruction
		ResultSet rs = stmt.executeQuery (query);

		int rowCount = 0;

		//iterates through the result set and count nuber of results.
		if(rs.next()){
			rowCount++;
		}//end while
		stmt.close ();
		return rowCount;
	}
	
	/**
	 * Method to fetch the last value from sequence. This
	 * method issues the query to the DBMS and returns the current 
	 * value of sequence used for autogenerated keys
	 * 
	 * @param sequence name of the DB sequence
	 * @return current value of a sequence
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	
	public int getCurrSeqVal(String sequence) throws SQLException {
		Statement stmt = this._connection.createStatement ();
		
		ResultSet rs = stmt.executeQuery (String.format("Select currval('%s')", sequence));
		if (rs.next()) return rs.getInt(1);
		return -1;
	}

	/**
	 * Method to close the physical connection if it is open.
	 */
	public void cleanup(){
		try{
			if (this._connection != null){
				this._connection.close ();
			}//end if
		}catch (SQLException e){
	         // ignored.
		}//end try
	}//end cleanup

	/**
	 * The main execution method
	 * 
	 * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
	 */
	public static void main (String[] args) {
		if (args.length != 3) {
			System.err.println (
				"Usage: " + "java [-classpath <classpath>] " + DBproject.class.getName () +
		            " <dbname> <port> <user>");
			return;
		}//end if
		
		DBproject esql = null;
		
		try{
			System.out.println("(1)");
			
			try {
				Class.forName("org.postgresql.Driver");
			}catch(Exception e){

				System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
				e.printStackTrace();
				return;
			}
			
			System.out.println("(2)");
			String dbname = args[0];
			String dbport = args[1];
			String user = args[2];
			
			esql = new DBproject (dbname, dbport, user, "");
			
			boolean keepon = true;
			while(keepon){
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add Plane");
				System.out.println("2. Add Pilot");
				System.out.println("3. Add Flight");
				System.out.println("4. Add Technician");
				System.out.println("5. Book Flight");
				System.out.println("6. List number of available seats for a given flight.");
				System.out.println("7. List total number of repairs per plane in descending order");
				System.out.println("8. List total number of repairs per year in ascending order");
				System.out.println("9. Find total number of passengers with a given status");
				System.out.println("10. < EXIT");
				
				switch (readChoice()){
					case 1: AddPlane(esql); break;
					case 2: AddPilot(esql); break;
					case 3: AddFlight(esql); break;
					case 4: AddTechnician(esql); break;
					case 5: BookFlight(esql); break;
					case 6: ListNumberOfAvailableSeats(esql); break;
					case 7: ListsTotalNumberOfRepairsPerPlane(esql); break;
					case 8: ListTotalNumberOfRepairsPerYear(esql); break;
					case 9: FindPassengersCountWithStatus(esql); break;
					case 10: keepon = false; break;
				}
			}
		}catch(Exception e){
			System.err.println (e.getMessage ());
		}finally{
			try{
				if(esql != null) {
					System.out.print("Disconnecting from database...");
					esql.cleanup ();
					System.out.println("Done\n\nBye !");
				}//end if				
			}catch(Exception e){
				// ignored.
			}
		}
	}

	public static int readChoice() {
		int input;
		// returns only if a correct value is given.
		do {
			System.out.print("Please make your choice: ");
			try { // read the integer, parse it and break.
				input = Integer.parseInt(in.readLine());
				break;
			}catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}//end try
		}while (true);
		return input;
	}//end readChoice

	public static void AddPlane(DBproject esql) {//1
		int id;
		String make;
		String model;
		int age;
		int seats;
		System.out.println("hello");
		
		do{
			System.out.print("Please input Plane ID Number: ");
			try{
				id = Integer.parseInt(in.readLine());
				break;
			}
			catch(Exception e){
				System.out.println("Invalid input. Please enter a valid ID.");
				continue;
			}
			
		}while(true);
		
		do{
			System.out.print("Please input Plane Make: ");
			try{
				make = in.readLine();
				if(make.length() <= 0 || make.length() > 32){
					throw new RuntimeException("Plane Make must be 32 characters or less");
				}
				break;
			}
			catch(Exception e){
				System.out.println(e.getMessage());
				continue;
			}
			
		}while(true);
		
		do{
			System.out.print("Please input Plane Model: ");
			try{
				model = in.readLine();
				if(model.length() <= 0 || model.length() > 64){
					throw new RuntimeException("Plane Model must be 64 characters or less");
				}
				break;
			}
			catch(Exception e){
				System.out.println(e.getMessage());
				continue;
			}
			
		}while(true);
		
		do{
			System.out.print("Please input Plane Age: ");
			try{
				age = Integer.parseInt(in.readLine());
				if(age < 0){
					throw new RuntimeException("Age can not be negative.");
				}
				break;
			}
			catch(Exception e){
				System.out.println(e.getMessage());
				continue;
			}
			
		}while(true);
		
		do{
			System.out.print("Please input number of Plane Seats: ");
			try{
				seats = Integer.parseInt(in.readLine());
				if(seats < 0 || seats >= 500){
					throw new RuntimeException("Number of seats can not be negative.");
				}
				break;
			}
			catch(Exception e){
				System.out.println(e.getMessage());
				continue;
			}
			
		}while(true);
		try{
			String query = "INSERT INTO Plane (id, make, model, age, seats)\nVALUES(" + "\'" + id + "\',\'" + make + "\',\'"+ model + "\',\'" + age + "\',\'" + seats + "\');";
			//String query = "INSERT INTO Plane (id, make, model, age, seats)\nVALUES(" + "\'" + "69" + "\',\'" + "Honda" + "\',\'"+ "plane" + "\',\'" + "22" + "\',\'" + "200" + "\');";
			
			esql.executeQueryAndPrintResult(query);

		}
		catch(Exception e){
			System.err.println(e.getMessage());
		}
		
		/*INSERT INTO PLANE (id,make.model,age,seats);
		 * VALUES(id, make, model, age, seats);*/
		 
	}

	public static void AddPilot(DBproject esql) {//2
		int id;
		String fullname;
		String nationality;
		
		/*INSERT INT PILOT(id, fullname, nationality);
		 * VALUES(id, fullname, nationality)*/
		 
		 
	}

	public static void AddFlight(DBproject esql) {//3
		// Given a pilot, plane and flight, adds a flight in the DB
		int fnum;
		int cost;
		int num_sold;
		int num_stops;
		String actual_departure_date;
		String actual_arrival_date;
		String arrival_airport;
		String departure_airport;
		
		/*INSERT INTO FLIGHT(fnum, cost, num_sold, num_stop, actual_departure_date, 
		 * actual_arrival_date, arrival_airport, departure_airport);
		 * VALUES(fnum, cost, num_sold, num_stop, actual_departure_date, 
		 * actual_arrival_date, arrival_airport, departure_airport);*/
	}

	public static void AddTechnician(DBproject esql) {//4
		int id;
		String full_name;
		
		/*INSERT INTO Technician(id, full_name)
		 * VALUES(id, full_name*/
		
	}

	public static void BookFlight(DBproject esql) {//5
		// Given a customer and a flight that he/she wants to book, add a reservation to the DB
		int rnum;
		int cid;
		int fid;
		String date;
		String status;
		
		do{
			System.out.print("Please input Customer ID: ");
			try{
				cid = Integer.parseInt(in.readLine());
				break;
			}
			catch(Exception e){
				System.out.println("Invalid input. Please input a valid Customer ID");
				continue;
			}
		}while(true);
		
		do{
			System.out.print("Please input Flight ID: ");
			try{
				fid = Integer.parseInt(in.readLine());
				
				String ret = "SELECT fi.fiid\nFROM FlightInfo fi\n WHERE fi.fiid =" + Integer.toString(fid)+';';
				int x = esql.executeQueryAndPrintResult(ret);

				if(x == 0){
					throw new RuntimeException("Flight does not exist.");

				}
				break;
			}
			catch(Exception e){
				System.err.println("Flight does not exist.");
				continue;

			}
			
		}while(true);
		String input;
		do{
			try{
				String check = "SELECT R.rnum\nFROM Reservation r\n WHERE r.cid =" + Integer.toString(cid) + "AND r.fid =" + Integer.toString(fid)+';';
				int a = esql.executeQueryAndPrintResult(check);
				if( a == 1){
					try{
						System.out.println("Reservation found!");
						String checkStatus = "SELECT R.rnum\nFROM Reservation r\n WHERE r.cid =" + Integer.toString(cid) + "AND r.fid =" + Integer.toString(fid)+ "AND r.status = \'R\';";
						int b = esql.executeQueryAndPrintResult(checkStatus);
						if(b == 1){
							do{
								try{
								System.out.println("Your reservation is  currently reserved. Would you like to confirm? (Y/N)");
								input = in.readLine();
								if(input.equals("y") || input.equals("Y")){
									do{
										try{
											System.out.println("hey");
											String updateRes = "UPDATE RESERVATION\nSET status = 'C'\n WHERE cid =" + Integer.toString(cid) + "AND fid =" + Integer.toString(fid)+';';
											esql.executeUpdate(updateRes);
											System.out.println("Your reservation is now confirmed!");
											break;
										}
										catch(Exception e){
											System.out.println("Invalid input");
											continue;
										}
									}while(true);
								}
								else{
									System.out.println("Reservation not confirmed.");
								}
								break;
							}
							catch(Exception e){
								System.out.println("Your input is invalid.");
								continue;
							}
							}while(true);	
						}
					else{
						System.out.println("Your reservation is already confirmed!");
					}
					}
					catch(Exception e){
						System.out.println("Reservation not reserved");
						break;
					}
				}
				else{
					System.out.println("Reservation does not exist.");
					
				}
				break;

			}
			catch(Exception e){
							System.err.println("Error");
							continue;
			}
		}while(true);
		
		do{
			try{
				String checkSeats = "SELECT COUNT(*) \nFROM FlightInfo fi, Flight f, Plane p \n WHERE fi.flight_id ="+ Integer.toString(fid)+"AND fi.plane_id = p.id AND f.num_sold < p.seats;";
				int s = esql.executeQueryAndPrintResult(checkSeats);
				if(s == 0){
					do{
						System.out.println("Input new reservation number. (Must be greater than 9999)");
						try{
							rnum = Integer.parseInt(in.readLine());
							if(rnum > 9999){
								status = 'R';
								String query = "INSERT INTO Reservation (rnum, cid, fid, status)\nVALUES(" + "\'" + rnum + "\',\'" + cid + "\',\'"+ fid + "\',\'" + status + "\');";
								esql.executeQueryAndPrintResult(query);
							}
							else{
								throw new RuntimeException("Reservation number not large enough");
							}

							break;
						}
						catch(Exception e){
							System.out.println(e.getMessage());
							continue;
						}
					}while(true)
				}	
				break;
			}
			catch(Exception e){
				System.out.println("Invalid");
				continue;
			}
			
				
			
		}while(true);
		
		
		/*do{
			System.out.print("Please input date: ");
			try{
				date = in.readLine();
				
				String ret = "SELECT f.actual_departure_date\nFROM Flight f\nWHERE f.actual_departure_date =" + date+';';
				int x = esql.executeQueryAndPrintResult(ret);
				
				System.out.println("x: "+ x);
				if(x == 0){
					throw new RuntimeException("Flight does not exist.");
				}	
				break;
			}
			catch(Exception e){
				System.out.println(e.getMessage());
				continue;
			}
		}while(true);*/
		
		//INSERT INTO RESERVATION (rnum, cid, fid, status)
		//VALUES (rnum, cid, fid, status)
		
		//Use fid to get plane_id, check num_sold < plane.seats
		//ask for flight data & time, then compare with fid and put w/c/r
	}

	public static void ListNumberOfAvailableSeats(DBproject esql) {//6
		// For flight number and date, find the number of availalbe seats (i.e. total plane capacity minus booked seats )
		int fnum;
		int a_d_date;
		

		/*
		* SELECT(
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
			WHERE f.fnum = '50')as seatsAvailable;*/
		
	}

	public static void ListsTotalNumberOfRepairsPerPlane(DBproject esql) {//7
		// Count number of repairs per planes and list them in descending order
		/*SELECT COUNT(*) as totalRepairs, p.id
FROM Repairs r, Plane p
WHERE r.plane_id = p.id
GROUP BY p.id
ORDER BY totalRepairs DESC;*/
		String query;
		try{
			query = "SELECT COUNT(*) as \"totalRepairs\", p.id\nFROM repairs r, Plane p WHERE p.id=r.plane_id\nGROUP BY p.id ORDER BY \"totalRepairs\" DESC;";
			esql.executeQueryAndPrintResult(query);

		}
		catch(Exception e){
			System.err.println(e.getMessage());
		}
	}

	public static void ListTotalNumberOfRepairsPerYear(DBproject esql) {//8
		// Count repairs per year and list them in ascending order
		/*
		 * SELECT EXTRACT(YEAR FROM r.repair_date) AS year, COUNT(*) as totalRepiars
			FROM Repairs r, Plane p
			GROUP by year;*/
		String query;
		
		try
		{
			query = "SELECT EXTRACT (Year FROM r.repair_date) as \"Year\", count(r.rid) as \"totalRepairs\"\nFROM repairs r\nGROUP BY \"Year\"\nORDER BY \"totalRepairs\" ASC;";
			esql.executeQueryAndPrintResult(query);
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
	
	public static void FindPassengersCountWithStatus(DBproject esql) {//9
		// Find how many passengers there are with a status (i.e. W,C,R) and list that number.
		
		/*
		 * SELECT COUNT(*) as totalPassengers, r.status
			FROM Reservation r, Customer c
			WHERE r.cid = c.id AND r.status='W'
			GROUP BY r.status;*/
	}
}
