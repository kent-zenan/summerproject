package GUI;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.sun.org.apache.bcel.internal.generic.NEW;

import planner.Route;
import planner.Route.status;
import planner.RoutingPlanner;
import planner.Task;



public class RoutePanel extends JDesktopPane implements ActionListener{
	private Model model;
	private JPopupMenu popMenus = new JPopupMenu();
	private JMenuItem jmiGPSInformation = new JMenuItem("GPS information");
	private JMenuItem jmiStatus = new JMenuItem("Suspend session / Resume session");
	private Vector<Route> routes;
	private int selectedRouteId, mouseX2, mouseY2;
	private Vector<Task> taskSet;
	private Vector<Route> routesExample = RoutingPlanner.getExampleData().routesOfShifts.get(0);;
	private Dimension area = new Dimension(0,0);
	private int maxWidth = 0;

	/** The window count. */
	int windowCount = 0;


	public RoutePanel() {
		// TODO Auto-generated constructor stub

		popMenus.add(jmiGPSInformation);
		popMenus.add(jmiStatus);

		jmiGPSInformation.addActionListener(new ButtonActionListener());
		jmiStatus.addActionListener(new ButtonActionListener());
		setPreferredSize(new Dimension(700, 600));
		setMinimumSize(new Dimension(350, 100));
		setBackground(new Color(Integer.valueOf("006699", 16)));

				for (int i = 0; i < routesExample.get(0).taskSet.size(); i++)
					System.out.println(routesExample.get(0).taskSet.get(i).toDisplayString());
		
				for (int i = 0; i < routesExample.get(1).taskSet.size(); i++)
					System.out.println(routesExample.get(1).taskSet.get(i).toDisplayString());

		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				//				xDiff = x - e.getX();
				//				yDiff = y - e.getY();
				showPopup(e);
			}

			public void mouseReleased(MouseEvent e){
				mouseX2 = e.getX();
				mouseY2 = e.getY();

				if (mouseX2 > 10 && mouseX2 < 210 && mouseY2 % 80 > 10)
					showPopup(e);
			}
		});
	}

	// get the model
	public Model getModel(){
		return model;
	}

	// set the model
	public void setModel(Model model){
		this.model = model;

		if(model != null)
			model.addActionListener(this);

		repaint();
	}

	private void showPopup(MouseEvent evt){
		if(evt.isPopupTrigger())
			popMenus.show(evt.getComponent(), evt.getX(), evt.getY());
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (model == null) return;
		repaint();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//		if (model == null) {
		//			return;
		//		}
		//		routes = model.getRoutes();
		routes = routesExample;
		for(int i = 0; i < routes.size(); i++)
			drawRoute(g, i, routes.get(i));

		area.height = routes.size() * 80;
		area.width = maxWidth;
		setPreferredSize(area);
	}

	public void drawRoute(Graphics g, int routeId, Route route) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(3));
		FontMetrics fm = g2.getFontMetrics();
		int width = 0;
		int lengthe = 0;
		int stringWidth = 6;
		int stringAHeight = 5;	
		int y = 10 + routeId * 80;
		int x = 10;
		String text;
		Route.status status = route.getStatus();

		g2.setPaint(Color.BLACK);
		g2.fillRect(x, y, 200, 70);
		g2.setPaint(Color.WHITE);
		//g2.drawString(route.getSession().getDriverName(), x + 10, y);
		//g2.drawString(route.getSession().getVehicleID(), x + 10, y);
		drawStatus(g2, x + 160, y + 30, status);
		x += 210;

		taskSet = route.taskSet;
		for(int i = 0; i < taskSet.size(); i++){

			//			for (String line : (text = taskSet.get(i).toDisplayString()).split("\n")){
			//				if (stringWidth < fm.stringWidth(line))
			//					stringWidth = fm.stringWidth(line) + 6;
			//				stringAHeight += fm.getHeight();
			//			}

			Rectangle r = new Rectangle(x , y ,g2.getFontMetrics().stringWidth("(Thu Nov 29 15:22:46 CST 2012 - Tue Dec 04 20:00:00 CST 2012)") + 10, g2.getFontMetrics().getHeight() * 5);
			g2.setPaint(Color.BLACK);

			g2.fill(r);

			g2.draw(r);
			g2.setPaint(Color.WHITE);

			for (String line : taskSet.get(i).toDisplayString().split("\n"))
				g2.drawString(line, x + 3, y += g2.getFontMetrics().getHeight());

			x += g2.getFontMetrics().stringWidth("(Thu Nov 29 15:22:46 CST 2012 - Tue Dec 04 20:00:00 CST 2012)") + 20;
			y -= g2.getFontMetrics().getHeight() * 4;
		}

		if (x > maxWidth)
			maxWidth = x;

		g2.drawLine(220, 10 + routeId * 80 - 5, x, y - 5);

	}

	public void setStatus(int routeId, Route.status status){
		routes.get(routeId).setStatus(status);
		repaint();
	}

	public void drawStatus(Graphics2D g2, int x, int y, status status){
		switch (status) {
		case UNASSIGNED:
			g2.setPaint(Color.YELLOW);
			break;
		case ASSIGNED:
			g2.setPaint(Color.GREEN);
			break;
		case SESSION_SUSPENDED:
			g2.setPaint(Color.GRAY);
			break;
		case SESSION_ENDED:
			g2.setPaint(Color.RED);
			break;

		default:
			break;
		}

		g2.fillOval(x, y, 10, 10);
	}

	// Popup menu item's listener
	class ButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JMenuItem jmi = (JMenuItem)e.getSource();
			Route route;

			if (jmi == jmiGPSInformation) {
				createInternalFrame(null, 1, 200, 200);
			} else {
				if (routes == null)
					return;
				selectedRouteId = mouseY2 / 80;
				route = routes.get(selectedRouteId);
				if (route.getStatus() == status.ASSIGNED ) {
					setStatus(selectedRouteId, status.SESSION_SUSPENDED);
				} else if(route.getStatus() == status.SESSION_SUSPENDED) {
					setStatus(selectedRouteId,status.ASSIGNED);
				}

				System.out.print(route.getStatus());
			}
		}

	}

	/**
	 * Create an internal frame and add a scrollable imageicon to it.
	 *
	 * @param icon the icon
	 * @param layer the layer
	 * @param width the width
	 * @param height the height
	 * @return the j internal frame
	 */
	public JInternalFrame createInternalFrame(Icon icon, Integer layer, int width, int height) {
		JInternalFrame jif = new JInternalFrame("GPS data");

		// set properties
		jif.setClosable(true);
		jif.setResizable(true);

		jif.setBounds(20*(windowCount%10), 20*(windowCount%10), width, height);

		windowCount++;

		add(jif, layer);  

		// Set this internal frame to be selected

		try {
			jif.setSelected(true);
		} catch (java.beans.PropertyVetoException e2) {
		}

		jif.show();

		return jif;
	}

}
