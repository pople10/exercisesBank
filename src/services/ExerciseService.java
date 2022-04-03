package services;

import java.sql.SQLException;
import java.util.List;

import beans.Exercise;
import dao.ExerciseDao;
import mapper.Mapper;
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
	
	public List<String> listAllCategories() throws SQLException
	{
		return this.exerciseDao.listAllCategories();
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
