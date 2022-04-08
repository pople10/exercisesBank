package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JFileChooser;

import gui.messages.Dialogs;

public class LatexUtil {
	private static String path = "template/pdflatex.txt";
	
	public static void generateLatex(File file1) throws Exception
	{
		if(conditionToSetBin())
		{
			throw new Exception("Vous avez pas configurer votre PDFLATEX BIN");
		}
		String filename=file1.getName();
		String path = file1.getAbsolutePath().replace("\\"+filename, "");
		try {
			String command=LatexUtil.getPathForLatexBinary();
			command+="pdflatex.exe"+" "+filename;
			System.out.print(command);
			ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c",command);
	        pb.directory(new File(path));
	        try {
	            Process p = pb.start();
	            StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream(), true);
	            StreamPrinter fluxErreur = new StreamPrinter(p.getErrorStream(), true);
	            new Thread(fluxSortie).start();
	            new Thread(fluxErreur).start();
	            p.waitFor();
	        } catch (IOException | InterruptedException ex) {
	            ex.printStackTrace();
	        	throw ex;
	        }
		}
		catch(Exception e)
		{
        	throw e;
		}
	}
	
	public static void resetConfiguration()
	{
		File file = new File(path);
		if(file.exists())
			file.delete();
	}
	
	public static String getPathForLatexBinary()
	{
		try {
			return Files.readString(Path.of(path));
		} catch (IOException e) {
			return null;
		}
	}
	
	public static void setPathForLatexBinary() throws Exception
	{
		if(!conditionToSetBin())
		{
			return;
		}
		JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
        f.showSaveDialog(null);
        System.out.println(f.getCurrentDirectory());
        if(f.getSelectedFile()==null)
        	throw new Exception("Rien séléctionné");
        String saveTo = f.getSelectedFile().getAbsolutePath()+File.separator;
        File file = FileUtil.writeAndMakeFile(path, saveTo);
        if(file==null)
        {
        	throw new Exception("On n'a pas pu configuré votre pdflatex bin");
        }
	}
	
	public static boolean conditionToSetBin()
	{
		String check=getPathForLatexBinary();
		if(check!=null&&!check.trim().isEmpty())
		{
			return false;
		}
		return true;
	}
}
