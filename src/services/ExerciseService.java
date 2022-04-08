package services;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import beans.Exercise;
import dao.ExerciseDao;
import mapper.Mapper;
import utils.Callback;
import utils.FileUtil;
import utils.GenericUtil;

public class ExerciseService {
	private Mapper mapper = Mapper.getInstance();
	private ExerciseDao exerciseDao = ExerciseDao.getDao();
	
	public boolean createExercise(Exercise exo) throws SQLException
	{
		return this.exerciseDao.create(mapper.toMap(exo));
	}
	
	public boolean updateExercise(Exercise exo) throws SQLException
	{
		return this.exerciseDao.update(mapper.toMap(exo),exo.getId());
	}
	
	public boolean deleteExercise(Long id) throws SQLException
	{
		return this.exerciseDao.delete(id);
	}
	
	public Exercise findExerciseById(Long id) throws SQLException
	{
		Exercise exo = (Exercise) mapper.mapper(this.exerciseDao.findById(id),new Exercise());
		if(exo.getId()==null||exo.getId()==0l)
			throw new SQLException("Exercise not found");
		return exo;
	}
	
	public List<Exercise> findAllExercises() throws SQLException
	{
		return (List<Exercise>) mapper.mapperList(this.exerciseDao.findAll(),new Exercise());
	}
	
	public List<Exercise> findAllExercises(int limit) throws SQLException
	{
		List<Exercise> list =  (List<Exercise>) mapper.mapperList(this.exerciseDao.findAll(),new Exercise());
		if(list.size()<=limit)
			return list;
		return list.subList(0, limit);
	}
	
	public List<Exercise> findAllExercisesByCategory(String category) throws SQLException
	{
		return (List<Exercise>) mapper.mapperList(this.exerciseDao.findByCategorie(category),new Exercise());
	}
	
	public List<Exercise> findAllExercisesByCategory(String category,int limit) throws SQLException
	{
		List<Exercise> list = (List<Exercise>) mapper.mapperList(this.exerciseDao.findByCategorie(category),new Exercise());
		if(list.size()<=limit)
			return list;
		return list.subList(0, limit);
	}
	
	public Object[][] getForDatatable() throws SQLException
	{
		return GenericUtil.generateDataTableFromExercises(this.findAllExercises());
	}
	
	public Object[][] getForDatatable(List<Exercise> list) throws SQLException
	{
		return GenericUtil.generateDataTableFromExercises(list);
	}
	
	public List<String> listAllCategories() throws SQLException
	{
		return this.exerciseDao.listAllCategories();
	}
	
	public void generatePDF(List<Exercise> list,Callback function) throws Exception
	{
		String content = FileUtil.getContentFromPath("template/latex/examTemplate.txt");
		if(content==null)
		{
			throw new Exception("Erreur se produit");
		}
		Calendar today = Calendar.getInstance();
		String date="";
		try {
			 date= ""+today.get(Calendar.DAY_OF_MONTH)+" "+today.get(Calendar.MONTH);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		content=content.replace("**date**", date);
		content=content.replace("**title**", "Title");
		String qsts = "";
		String both = "";
		String content2 = new String(content);
		for(Exercise exo : list)
		{
			both+="\t\\item "+exo.getQuestion().replace("\n", "\\linebreak ");
			both+="\\linebreak \\textbf{Reponse:} \\linebreak "+exo.getAnswer().replace("\n", "\\linebreak ")+"\n";
		}
		content=content.replace("**qsts**", qsts);
		content2=content2.replace("**qsts**", both);
		System.out.println(content);
		System.out.println(content2);
	}
	
	/**************************** Singeleton ***************************/

	private static volatile ExerciseService instance;
	
	public static ExerciseService getInstance() {
		ExerciseService localRef = instance;
        if (localRef == null) {
        	instance = localRef = new ExerciseService();
        }
        return localRef;
    }
}
