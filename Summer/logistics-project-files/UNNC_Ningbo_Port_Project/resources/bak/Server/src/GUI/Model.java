package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import planner.Route;

public class Model {
	private Vector<Route> routes;
	private ArrayList<ActionListener> actionListenerList = new ArrayList<ActionListener>();


	public Model(Vector<Route> routes) {
		setRoutes(routes);
	}

	public Vector<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(Vector<Route> routes) {
		this.routes = routes;
		// Notify the listener for the change
		processEvent(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "routes"));
	}

	public void setActionListenerList
	(ArrayList<ActionListener> actionListenerList){
		this.actionListenerList = actionListenerList;

		// Notify the listener for the change on bounds
		processEvent(
				new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "actionList"));
	}

	/** Add an action event listener */
	public synchronized void addActionListener(ActionListener l) {
		if (actionListenerList == null)
			actionListenerList = new ArrayList<ActionListener>();

		actionListenerList.add(l);
	}

	/** Remove an action event listener */
	public synchronized void removeActionListener(ActionListener l) {
		if (actionListenerList != null && actionListenerList.contains(l))
			actionListenerList.remove(l);
	}

	/** Fire TickEvent */
	private void processEvent(ActionEvent e) {
		ArrayList list;

		synchronized (this) {
			if (actionListenerList == null) return;
			list = (ArrayList)actionListenerList.clone();
		}

		for (int i = 0; i < list.size(); i++) {
			ActionListener listener = (ActionListener)list.get(i);
			listener.actionPerformed(e);

		}
	}

}
