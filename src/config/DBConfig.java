package config;

import config.main.abstractLayer.Configurable;
import utils.DBHelper;

public class DBConfig implements Configurable{

	@Override
	public void run() throws Exception {
		DBHelper.CreateTable("exercise","id","int NOT NULL AUTO_INCREMENT","question","TEXT"
				,"answer","TEXT","category","VARCHAR(255)","PRIMARY KEY","(id)");
	}

}
