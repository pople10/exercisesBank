package gui.messages;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

public class PushNotification {
	public static void pushNotificationInfo(String msg,String title)
	{
		pushNotification(msg,title,MessageType.INFO);
	}
	
	public static void pushNotificationWarning(String msg,String title)
	{
		pushNotification(msg,title,MessageType.WARNING);
	}
	
	public static void pushNotificationError(String msg,String title)
	{
		pushNotification(msg,title,MessageType.ERROR);
	}
	
	public static void pushNotificationNone(String msg,String title)
	{
		pushNotification(msg,title,MessageType.NONE);
	}
	/******************* Generic ************************/
	private static void pushNotification(String msg,String title,MessageType type)
	{
		SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("images/logo.png");
        TrayIcon trayIcon = new TrayIcon(image, "Exo Bank");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("Notification from Exo bank");
        try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
        trayIcon.displayMessage(title,msg, type);
	}
}
