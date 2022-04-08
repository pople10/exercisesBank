package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
	public static String dir="template/tmp";
	
	public static File makeFile(String filename,String content)
	{
		(new File(dir)).mkdirs();
		File file = new File(dir+File.separator+filename);
		try {
			if(!file.createNewFile())
				return null;
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return file;
	}
	
	public static String getContentFromPath(String path)
	{
		File file = new File(path);
		if(!file.exists())
			return null;
		String out="";
		String st;
    	try {
    		BufferedReader br= new BufferedReader(new FileReader(file));
			while ((st = br.readLine()) != null)
				out+=st+"\n";
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    	return out;
	}
	
	public static void copyFileUsingStreamWithDelete(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	        source.delete();
	    }
	}

}
