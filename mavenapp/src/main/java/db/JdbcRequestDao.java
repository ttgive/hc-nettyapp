package db;


import java.text.SimpleDateFormat;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

import web.StatusCommand;

import domain.Request;
/**
 * Data Access Object for {@link Request}
 * @author harnyk
 *
 */
public class JdbcRequestDao {

	// JDBC driver name and database URL
	String JDBC_DRIVER;
	String DB_URL;

	// Database credentials
	String USER;
	String PASS;
	
	Connection conn = null;
	Statement stmt = null;
	
	public JdbcRequestDao() {
		Properties prop = new Properties();		 
    	try {
    		prop.load(new FileInputStream("db.properties"));
    		JDBC_DRIVER = prop.getProperty("driver");
    		DB_URL = prop.getProperty("db_url");
    		USER = prop.getProperty("user");
    		PASS = prop.getProperty("pass");
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
	}
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	
	public StatusCommand getStatusCommand(){
		StatusCommand stc = new StatusCommand();
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			Map<String, Integer> countMap = this.getCount(); 
			stc.setTotalCount(countMap.get("totalCount"));
			stc.setIpCount(countMap.get("ipCount"));
			stc.setIpRequests(this.getIpRequests());
			stc.setRedirectUrls(this.getRedirectUrls());
			stc.setLastRequests(this.getLastRequests(16));
			
			stmt.close();
			conn.close();	
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}// nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
		return stc;
		
	}
	
	private Map<String, Integer> getCount() throws SQLException{
		Map<String, Integer> countMap = new HashMap<String, Integer>();
		String sql = "SELECT COUNT(id) as count, COUNT(DISTINCT(src_ip)) as count_ip FROM requests";
		ResultSet rs = stmt.executeQuery(sql);
		
		rs.next();
		countMap.put("totalCount", rs.getInt("count"));
		countMap.put("ipCount", rs.getInt("count_ip"));
		rs.close();
		return countMap;
	}
	
	private List<Map<String, String>> getIpRequests() throws SQLException{
		List<Map<String, String>> ipRequests = new ArrayList<Map<String, String>>();
		
		String sql = "SELECT src_ip, COUNT(id) AS count, MAX(`when`) AS `when` FROM requests " +
				"GROUP BY src_ip";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			Map<String, String> row = new HashMap<String, String>();
			row.put("src_ip", rs.getString("src_ip"));
			row.put("count", rs.getString("count"));
			row.put("when", rs.getString("when"));
			
			ipRequests.add(row);
		}
		rs.close();		
		return ipRequests;	
	}
	
	private List<Map<String, String>> getRedirectUrls() throws SQLException{
		List<Map<String, String>> redirectUrls = new ArrayList<Map<String, String>>();
		
		String sql = "SELECT redirect_url, COUNT(id) AS count FROM requests " +
				"WHERE redirect_url != '' GROUP BY redirect_url";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			Map<String, String> row = new HashMap<String, String>();
			row.put("redirect_url", rs.getString("redirect_url"));
			row.put("count", rs.getString("count"));
			
			redirectUrls.add(row);
		}
		rs.close();		
		return redirectUrls;	
	}
	
	private List<Request> getLastRequests(Integer count) throws SQLException{
		List<Request> requests = new ArrayList<Request>();
		
		String sql = "SELECT src_ip, uri, timestamp, sent_bytes, received_bytes FROM requests " +
				"ORDER BY `when` DESC LIMIT " + count.toString();
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			Request request = new Request();
			request.setIp(rs.getString("src_ip"));
			request.setUri(rs.getString("uri"));
			request.setTimestamp(rs.getLong("timestamp"));
			request.setReceived_bytes(rs.getInt("received_bytes"));
			request.setSent_bytes(rs.getInt("sent_bytes"));
			
			requests.add(request);
		}
		rs.close();		
		return requests;	
	}
	
	public void saveRequest(Request request){
		Connection conn = null;
		Statement stmt = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();

			String sql = "INSERT INTO `requests` " +
					"(`src_ip`,`uri`, `when`, `redirect_url`, `received_bytes`, `sent_bytes`, `timestamp`) " +
					"VALUES ('"+ request.getIp()
					+"', '"+ request.getUri()
					+"', '"+ this.format.format(request.getWhen())
					+"', '"+ request.getRedirectUrl()
					+"', "+ request.getReceived_bytes()	
					+", "+ request.getSent_bytes()
					+", "+ request.getTimestamp()
					+")";

			stmt.executeUpdate(sql);

			stmt.close();
			conn.close();	
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}// nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
		
	}
	
public List<Request> getAllRequests() {
		
		List<Request> requests = new ArrayList<Request>();
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			
			String sql;
			sql = "SELECT * FROM requests";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Request request = new Request();
				request.setId(rs.getInt("id"));
				request.setIp(rs.getString("src_ip"));
				request.setUri(rs.getString("uri"));
				request.setWhen(this.format.parse(rs.getString("when")));
				request.setReceived_bytes(rs.getInt("received_bytes"));
				request.setSent_bytes(rs.getInt("sent_bytes"));
				request.setRedirectUrl(rs.getString("redirect_url"));
				
				requests.add(request);
			}
			rs.close();
			stmt.close();
			conn.close();	
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}// nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
		return requests;

	}
}

