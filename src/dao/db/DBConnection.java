package dao.db;


import java.sql.*;


public class DBConnection {
	private static String url = "jdbc:mysql://localhost:3306/exobank?sessionVariables=sql_mode='NO_ENGINE_SUBSTITUTION'&jdbcCompliantTruncation=false";
	private static String username = "root";
	private static String password = "";
	private Connection conn;
	private static DBConnection instance;
	
	public Connection getConnection()
	{
		return this.conn;
	}
	
	private DBConnection() throws Exception
	{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.conn=DriverManager.getConnection(url,username,password);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("Cannot connect to DB");
		}  
	}
	
	public static DBConnection openSession()
	{
		try {
			if(instance==null)
				instance=new DBConnection();
			return instance;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void closeSession()
	{
		try {
			this.conn.close();
			instance=null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
