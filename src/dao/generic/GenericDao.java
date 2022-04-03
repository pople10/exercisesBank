package dao.generic;


import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.db.DBConnection;
import utils.GenericUtil;

public abstract class GenericDao {
	 protected String DB_NAME;
	 
	 public GenericDao(String DB_NAME)
	 {
		 this.DB_NAME=DB_NAME;
	 }
	 
	 public boolean create(Map<String,String> args) throws SQLException
	 {
		 try {
			 String sql = "INSERT INTO "+DB_NAME+" ("+GenericUtil.generateArgs(args)+") VALUES ("+GenericUtil.generateQuestionMarks(args)+")";
			 PreparedStatement statement = DBConnection.openSession().getConnection().prepareStatement(sql);
			 statement = GenericUtil.injectData(args, statement);
			 return ((statement.executeUpdate()==0)?false:true);
		 }
		 catch(SQLException sqlExp)
		 {
			 throw sqlExp;
		 }
		 catch(Exception e)
		 {
			 return false;
		 }
		 finally {
			 DBConnection.openSession().closeSession();
		 }
	 }
	 
	 public boolean update(Map<String,String> args,Long id) throws SQLException
	 {
		 try {
			 String sql = "UPDATE "+DB_NAME+" SET "+GenericUtil.generateArgsForUpdate(args)+" WHERE id=?";
			 PreparedStatement statement = DBConnection.openSession().getConnection().prepareStatement(sql);
			 statement = GenericUtil.injectData(args, statement,id);
			 return ((statement.executeUpdate()==0)?false:true);
		 }
		 catch(SQLException sqlExp)
		 {
			 throw sqlExp;
		 }
		 catch(Exception e)
		 {
			 return false;
		 }
		 finally {
			 DBConnection.openSession().closeSession();
		 }
	 }
	 
	 public boolean delete(Long id) throws SQLException
	 {
		 try {
			 String sql = "DELETE FROM "+DB_NAME+" WHERE id=?";
			 PreparedStatement statement = DBConnection.openSession().getConnection().prepareStatement(sql);
			 statement.setLong(1,id);
			 return ((statement.executeUpdate()==0)?false:true);
		 }
		 catch(SQLException sqlExp)
		 {
			 throw sqlExp;
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
			 return false;
		 }
		 finally {
			 DBConnection.openSession().closeSession();
		 }
	 }
	 
	 public ResultSet findById(Long id) throws SQLException
	 {
		 try {
			 String sql = "SELECT * FROM "+DB_NAME+" WHERE id=?";
			 PreparedStatement statement = DBConnection.openSession().getConnection().prepareStatement(sql);
			 statement.setLong(1,id);
			 return statement.executeQuery();
		 }
		 catch(SQLException sqlExp)
		 {
			 throw sqlExp;
		 }
		 catch(Exception e)
		 {
			 return null;
		 }
		 finally {
			 //DBConnection.openSession().closeSession();
		 }
	 }
	 
	 protected ResultSet findByColumn(Object value,String columnName) throws SQLException
	 {
		 try {
			 String sql = "SELECT * FROM "+DB_NAME+" WHERE "+columnName+"=?";
			 PreparedStatement statement = DBConnection.openSession().getConnection().prepareStatement(sql);
			 statement.setString(1, value.toString());;
			 return statement.executeQuery();
		 }
		 catch(SQLException sqlExp)
		 {
			 throw sqlExp;
		 }
		 catch(Exception e)
		 {
			 return null;
		 }
		 finally {
			 //DBConnection.openSession().closeSession();
		 }
	 }
	 
	 public ResultSet findAll() throws SQLException
	 {
		 try {
			 String sql = "SELECT * FROM "+DB_NAME+" WHERE 1";
			 Statement statement = DBConnection.openSession().getConnection().createStatement();
			 return statement.executeQuery(sql);
		 }
		 catch(SQLException sqlExp)
		 {
			 throw sqlExp;
		 }
		 catch(Exception e)
		 {
			 return null;
		 }
		 finally {
			 //DBConnection.openSession().closeSession();
		 }
	 }

}