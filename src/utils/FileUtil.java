package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtil {
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

}
