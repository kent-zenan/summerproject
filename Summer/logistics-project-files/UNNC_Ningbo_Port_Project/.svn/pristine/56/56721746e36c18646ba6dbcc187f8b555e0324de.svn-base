package GUI;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

public class MainFrame extends JFrame{
	private MenusBar mb = new MenusBar();
	private RoutePanel routePanel = new RoutePanel();

	public MainFrame() {
		// TODO Auto-generated constructor stub
		setJMenuBar(mb);
		add(new JScrollPane(new JScrollPane(routePanel)));
	}

	// create and shows GUI
	public static void createAndShowGUI(){
		MainFrame frame = new MainFrame();
		frame.setDefaultCloseOperation(3);
		frame.setTitle("GPS Map");
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true); 
	}

	public static void main(String[] args) {

		//Set the software UI look and feel and fonts
		try {  
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
			UIManager.put( "Button.font", new Font("Verdana",Font.PLAIN, 12) );
			UIManager.put( "Label.font", new Font("Verdana",Font.PLAIN, 12) );
			UIManager.put( "MenuItem.font", new Font("Verdana",Font.PLAIN, 12) );
			UIManager.put( "TabbedPane.font", new Font("Verdana",Font.PLAIN, 12) );
			UIManager.put( "Table.font", new Font("Verdana",Font.PLAIN, 11) );
		}  
		catch (Exception e) {  
			e.printStackTrace();  
		}  

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}
}
