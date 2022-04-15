package services;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JFileChooser;

import beans.Exercise;
import dao.ExerciseDao;
import gui.messages.PushNotification;
import mapper.Mapper;
import utils.Callback;
import utils.FileUtil;
import utils.StreamPrinter;
import utils.GenericUtil;
import utils.LatexUtil;

public class ExerciseService {
	private Mapper mapper = Mapper.getInstance();
	private ExerciseDao exerciseDao = ExerciseDao.getDao();
	
	private static String postfixCorr = "-corrigé";
	
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
	
	public void generatePDF(List<Exercise> list,Callback function,String title) throws Exception
	{
		String content = FileUtil.getContentFromPath("template/latex/examTemplate.txt");
		if(content==null)
		{
			throw new Exception("Erreur se produit");
		}
		Calendar today = Calendar.getInstance();
		String date="";
		try {
			 date= ""+today.get(Calendar.DAY_OF_MONTH)+" "+
					 (new SimpleDateFormat("MMM")).format(today.getTime())
					 +" "+today.get(Calendar.YEAR);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		content=content.replace("**date**", date);
		content=content.replace("**title**", title);
		String qsts = "";
		String both = "";
		String content2 = new String(content);
		for(Exercise exo : list)
		{
			qsts+="\t\\item "+exo.getQuestion().replace("\n", "\\linebreak ")+"\n";
			both+="\t\\item "+exo.getQuestion().replace("\n", "\\linebreak ");
			both+="\\linebreak \\textbf{Reponse:} \\linebreak "+exo.getAnswer().replace("\n", "\\linebreak ")+"\n";
		}
		content=content.replace("**qsts**", qsts);
		content2=content2.replace("**qsts**", both);
		String now = ""+(new Date()).getTime();
		File file1 = FileUtil.makeFile(now+".tex", content);
		File file2 = FileUtil.makeFile(now+postfixCorr+".tex", content2);
		String filename=file1.getName();
		String path = file1.getAbsolutePath().replace("\\"+filename, "");
		try {
			PushNotification.pushNotificationInfo("On a commancé la conversion du latex à PDF, Veuillez attendre un peu du temp","Service commancé");
			LatexUtil.generateLatex(file1);
			LatexUtil.generateLatex(file2);
		}
		catch(Exception e)
		{
			file1.delete();
			file2.delete();
			throw e;
		}
		file1.delete();
		file2.delete();
		File pdf1 = new File(path+File.separator+now+".pdf");
		File pdf2 = new File(path+File.separator+now+postfixCorr+".pdf");
		if(pdf1.exists())
		{
			JFileChooser f = new JFileChooser();
	        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
	        f.showSaveDialog(null);
	        System.out.println(f.getCurrentDirectory());
	        String saveTo = f.getSelectedFile().getAbsolutePath();
	        File toSaveFile = new File(saveTo+File.separator+"examen-"+now+".pdf");
	        toSaveFile.createNewFile();
	        FileUtil.copyFileUsingStreamWithDelete(pdf1, toSaveFile);
	        File toSaveFileCorr = new File(saveTo+File.separator+"examen-"+now+postfixCorr+".pdf");
	        toSaveFileCorr.createNewFile();
	        FileUtil.copyFileUsingStreamWithDelete(pdf2, toSaveFileCorr);
	        FileUtil.openFile(toSaveFile);
	        FileUtil.openFile(toSaveFileCorr);
	        function.call();
		}
		
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
