package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.db.DBConnection;
import dao.generic.GenericDao;

public class ExerciseDao extends GenericDao{
	
	private static volatile ExerciseDao instance;

	private ExerciseDao() {
		super("exercise");
	}
	
	public ResultSet findByCategorie(String categorie) throws SQLException
	{
		return this.findByColumn(categorie, "matiere");
	}

	public static ExerciseDao getDao() {
		ExerciseDao localRef = instance;
        if (localRef == null) {
        	instance = localRef = new ExerciseDao();
        }
        return localRef;
    }
	
	public List<String> listAllCategories() throws SQLException
	 {
		 List<String> list = new ArrayList<String>();
		 try {
			 String sql = "SELECT DISTINCT matiere FROM "+DB_NAME+" WHERE 1";
			 Statement statement = DBConnection.openSession().getConnection().createStatement();
			 ResultSet set = statement.executeQuery(sql);
			 if(set!=null)
				 while(set.next())
				 {
					 list.add(set.getString("matiere"));
				 }
		 }
		 catch(SQLException sqlExp)
		 {
			 throw sqlExp;
		 }
		 catch(Exception e)
		 {
			 
		 }
		 finally {
			 return list;
		 }
	 }
}
