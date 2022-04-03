package gui.messages;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Dialogs {
	public static void showErrorMessage(String msg)
	{
		JOptionPane.showMessageDialog(null, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void showSuccessMessage(String msg)
	{
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("images/done.png"));
		} catch (IOException e) {
			showErrorMessage("Loading image is getting an error");
		}
		Image image = img.getScaledInstance((int)30, (int)30, Image.SCALE_SMOOTH);
		ImageIcon icon = new ImageIcon(image);
		JOptionPane.showMessageDialog(null, msg, "Success", JOptionPane.INFORMATION_MESSAGE, 
				icon);
	}
	
	public static void showWarningMessage(String msg)
	{
		JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE);
	}

}
