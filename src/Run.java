import config.main.Configuration;
import gui.PanelGUI;

public class Run {
	public static void main(String[] args) throws Exception
	{
		runConfiguration();
		new PanelGUI();
	}
	
	public static void runConfiguration()
	{
		try {
			Configuration.getInstance().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
