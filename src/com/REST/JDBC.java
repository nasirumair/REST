package com.REST;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JDBC {
	Connection conn;
	Statement stmt;

	public JDBC() {
		conn = null;
		stmt = null;
	}

	public HashMap<Integer, User> getData(String DB_URL,String USER, String PASS){
		HashMap<Integer, User> hm = new HashMap<Integer,User>();
		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			//STEP 4: Execute a query
			System.out.println("Creating statement...GETTING DATA");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT id, fName, lName FROM userTable";
			ResultSet rs = stmt.executeQuery(sql);

			//STEP 5: Extract data from result set
			while(rs.next()){
				//Retrieve by column name
				int id  = rs.getInt("id");
				String first = rs.getString("fName");
				String last = rs.getString("lName");
				hm.put(id, new User(id, first, last));
				//Display values
				System.out.print("ID: " + id);
				System.out.println(", Name: " + hm.get(id).getName() + " " + hm.get(id).getProfession());
			}
			//STEP 6: Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}
		return hm;

	}
	
	public List<Text> getTextData(String DB_URL,String USER, String PASS, String userName){
		List<Text> list = new ArrayList<Text>();
		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			//STEP 4: Execute a query
			System.out.println("Creating statement...GETTING TEXT DATA");
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * from textTable where userName = '"+userName+"'";
			ResultSet rs = stmt.executeQuery(sql);

			//STEP 5: Extract data from result set
			while(rs.next()){
				//Retrieve by column name
				String textEntry = rs.getString("textEntry");
				String time = rs.getString("time");
				String date = rs.getString("date");
				String reply = ( rs.getString("reply") == null ) ? "" : rs.getString("reply");
				String lat = rs.getString("latitude");
				String lng = rs.getString("longitude");
				String temp = rs.getString("temp");
				list.add(new Text(userName, textEntry, time, date, lat,lng,temp, reply));
			}
			System.out.println("Retrieved "+list.size()+" text entries from the DB");
			//STEP 6: Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}
		return list;

	}
	
	public void postData(String DB_URL,String USER, String PASS, List<User> userList){
		HashMap<Integer, User> hm = new HashMap<Integer,User>();
		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			//STEP 4: Execute a query
			System.out.println("Creating statement...POSTING DATA");
			stmt = conn.createStatement();
			String sql;
			sql = "DELETE FROM userTable;";
			stmt.executeUpdate(sql);
			for(User u: userList){
				sql = "INSERT INTO userTable (id, fName, lName) VALUES ("+u.getId()+",'"+u.getName()+"','"+u.getProfession()+"');";
				stmt.executeUpdate(sql);
			}

			//STEP 5: Extract data from result set
			System.out.println(userList.size()+" data records inserted into DB");
			//STEP 6: Clean-up environment
			stmt.close();
			conn.close();
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}

		
	}
	
	public void postText(String DB_URL,String USER, String PASS, List<Text> textList){
		/*for(Text text: textList){
			System.out.println(text.getUserName());
			System.out.println(text.getText());
			System.out.println(text.getTime());
			System.out.println(text.getDate());
			System.out.println(text.getReply());
			System.out.println(text.getLat());
			System.out.println(text.getLng());
			System.out.println(text.getTemp());
			System.out.println("-------");
		}*/
		try{
			//STEP 2: Register JDBC driver
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			//STEP 3: Open a connection
			System.out.println("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);

			//STEP 4: Execute a query
			System.out.println("Creating statement...POSTING TEXT DATA");
			stmt = conn.createStatement();
			String sql;
			
			sql = "DELETE FROM textTable where userName = '"+textList.get(0).getUserName()+"';";
			stmt.executeUpdate(sql);
			for(Text text: textList){
				
				PreparedStatement update = conn.prepareStatement( "INSERT INTO textTable (userName, textEntry, time, date, reply, latitude, longitude, temp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			    update.setString(1,text.getUserName());
			    update.setString(2, text.getText());
			    update.setString(3,text.getTime());
			    update.setString(4, text.getDate());
			    update.setString(5,text.getReply());
			    update.setString(6, text.getLat());
			    update.setString(7,text.getLng());
			    update.setString(8, text.getTemp());
				update.executeUpdate();
			}
			
			//STEP 5: Extract data from result set
			System.out.println("text data records inserted into DB");
			//STEP 6: Clean-up environment
			stmt.close();
			conn.close();
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}

		
	}
}
