package config.main;

import java.util.ArrayList;
import java.util.List;

import config.DBConfig;
import config.main.abstractLayer.Configurable;

public class Configuration implements Configurable {
	private static List<Class<? extends Configurable>> configs = new ArrayList<Class<? extends Configurable>>();
	static {
		configs.add(DBConfig.class);
	}

	@Override
	public void run() throws Exception  {
		for(Class c : configs)
		{
			((Configurable) c.getDeclaredConstructor().newInstance()).run();
		}
	}
	
	private static volatile Configuration instance;
	
	public static Configuration getInstance() {
		Configuration localRef = instance;
        if (localRef == null) {
        	instance = localRef = new Configuration();
        }
        return localRef;
    }

}
