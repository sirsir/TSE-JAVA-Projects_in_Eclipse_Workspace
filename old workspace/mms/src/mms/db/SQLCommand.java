package mms.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mms.db.pool.JDCConnectionDriver;


public class SQLCommand {
	
	static {
        try{
        	new JDCConnectionDriver("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/mms?user=root&password=password&useUnicode=true&characterEncoding=UTF-8");
		}catch(Exception e){
			e.printStackTrace();
		}
    }
	
    public Connection getConnection() throws SQLException {
    	return DriverManager.getConnection("jdbc:jdc:jdcpool");
    }
    
    private String message;    
    
	public static final String INSERT = "insert into #!- (#!-) values(#!-)";
	public static final String UPDATE = "update #!- set #!-='#!-' where #!-=#!-";
	public static final String DELETE = "delete from #!- where #!-";
	public static final String LAST_ID = "select last_insert_id()";
    
    public boolean executeUpdate(String sql) {
    	List<String> values = new ArrayList<String>();
    	
    	return executeUpdate(sql, values);
    }
    
    public boolean executeUpdate(String sql, String value) {
    	List<String> values = new ArrayList<String>();
    	values.add(value);
    	
    	return executeUpdate(sql, values);
    }
    
    public String replacedSQL(String sql, List<String> values) {
    	int size = 0;
    	if(values != null)
    		size = values.size();
    	
    	for(int i=0; i<size; i++) {
    		if(values.get(i) == null)
    			sql = sql.replaceFirst("#!-", "");
    		else if("".equals(values.get(i)))
    			sql = sql.replaceFirst("#!-", "''");
    		else {
    			String value = values.get(i).replaceAll("'", "\\\\\\\\'");        		
    			sql = sql.replaceFirst("#!-", value);
    		}
    	}
    	sql = sql.replaceAll("\'null\'", "null");
    	sql = sql.replaceAll("\'\'\'\'", "null");
    	sql = sql.replaceAll("\'\'", "null");
    	return sql;
    }
    
    public boolean executeUpdate(String sql, List<String> values) {
    	boolean success = false;
    	sql = replacedSQL(sql, values);
    	//System.out.println(sql);
    	Connection con = null;
		Statement ps = null;
		try {
			con = getConnection();
		    ps = con.createStatement();
			
			if (ps.executeUpdate(sql) != 0)
			    success = true;
			else {
				if(sql.indexOf("update") == 0)
					message = "The record not found.";
				else message = "Cannot execute sql command";
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
			message = e.getMessage();
		} 
		finally {
		   try {
		      ps.close();
			  con.close();
		   } catch (Exception e) {
			   message = e.getMessage();
		   }
		}
		
		return success;
    }
    
    public String executeQuery(String sql, String value) {
    	sql = sql.replaceFirst("#!-", value);
    	//System.out.println(sql);
    	return executeQuery(sql);
    }
    
    public String executeQuery(String sql, List<String> values) {
    	int size = values.size();
    	
    	for(int i=0; i<size; i++) {
    		sql = sql.replaceFirst("#!-", values.get(i));
    	}
    	//System.out.println(sql);
    	return executeQuery(sql);
    }
    public String executeQuery(String sql) {
    	Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
           con=getConnection();
           ps=con.prepareStatement(sql);
           ps.executeQuery();
           
           rs = ps.getResultSet();
           
           String value = "";
           ResultSetMetaData rsmd = rs.getMetaData();
           int colCount = rsmd.getColumnCount();
           
           while(rs.next()) {
        	   for(int i=1; i<=colCount; i++) {
        		   value += rs.getString(i) + "\t"; 
               }
        	   
        	   value += "_n-";
           }
           
           if(value.length() > 2) {
	           value = value.replaceAll("\t_n-", "_n-");
	           value = value.substring(0, value.length()-3);
           }
           
           return value;
       }
       catch (SQLException e) {
    	   message = e.getMessage();
    	   e.printStackTrace();
       }
       finally {
          try {
             ps.close();
             con.close();
          }
          catch (Exception e) {
        	  message = e.getMessage();
          }
       }
       
       return null;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	} 
}
