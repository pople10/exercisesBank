package utils;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.db.DBConnection;

public class DBHelper {
	private static Logger logger = Logger.getGlobal();
	
	public static void CreateTable(String... args) throws Exception
	{
		if(args.length<3)
			throw new Exception("Too few arguments");
		if(args.length%2!=1)
			throw new Exception("Invalid arguments [tableName,colName1,type1,colName2,type2...)");
		try {
			 logger.log(Level.INFO, "Creating table "+args[0]);
			 String sql = "CREATE TABLE "+args[0] +"("+GenericUtil.generateForTableCreation(args)+") ENGINE=InnoDB;";
			 Statement statement = DBConnection.openSession().getConnection().createStatement();
			 statement.execute(sql);
			 logger.log(Level.INFO, "Table "+args[0]+" has been created");
		 }
		 catch(SQLException sqlExp)
		 {
			 logger.log(Level.WARNING, "Table "+args[0]+" couldn't be created because : "+sqlExp.getMessage());
		 }
		 catch(Exception e)
		 {
			 logger.log(Level.WARNING, "Table "+args[0]+" couldn't be created because : "+e.getMessage());
			 return;
		 }
		 finally {
			 //DBConnection.openSession().closeSession();
		 }
	}
}
