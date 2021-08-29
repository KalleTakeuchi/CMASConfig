/**
 * 
 */
package cMASConfig;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author kalle
 * Launches the application
 */
public class Launcher {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Try to set system look and feel. If it fails, use cross platform L&F
	    try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e1) {
				System.out.print("Error: Could not set Java Swing a look and feel.\n");
				e1.printStackTrace();
				return;
			}
		}
	    
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				MainWindow main = new MainWindow();
				main.show();
			}
			
		});
		
		

	}

}
