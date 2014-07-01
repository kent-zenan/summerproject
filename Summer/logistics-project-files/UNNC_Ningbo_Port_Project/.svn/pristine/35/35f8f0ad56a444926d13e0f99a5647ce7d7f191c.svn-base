package ac.uk.nottingham.ningboport.controller;

import java.util.ArrayList;
import java.util.Date;
import ac.uk.nottingham.ningboport.model.AndroidTaskItem;
import ac.uk.nottingham.ningboport.model.XMLDataResponse;
import ac.uk.nottingham.ningboport.model.XMLLoginComm;
import ac.uk.nottingham.ningboport.model.XMLSession;
import ac.uk.nottingham.ningboport.model.XMLTask;
import ac.uk.nottingham.ningboport.model.XMLTask.Action;

/**
 * Provides method to update session and task list, and also provides methods to
 * return session and tasks objects. There will be only one instance of
 * LocalDataManager in the application.
 * 
 * @author Jiaqi LI
 * 
 */
public class LocalDataManager {

	private static LocalDataManager instance = null;
	private XMLSession session = new XMLSession();
	private ArrayList<XMLTask> tasks = new ArrayList<XMLTask>();

	/**
	 * Deliberate set to private to force singleton.
	 */
	private LocalDataManager() {
	}

	/**
	 * If no instance exists, than a new LocalDataManager is created and
	 * returned, otherwise the existed one is returned.
	 * 
	 * @return the instance of DataManager
	 */
	public static LocalDataManager getInstance() {
		synchronized (LocalDataManager.class) {
			if (instance == null) {
				instance = new LocalDataManager();
			}
			return instance;
		}
	}

	/**
	 * Update session
	 * 
	 * @param resp
	 *            the response of login request as an XMLLoginComm object.
	 *            XMLLoginComm ensures the response is valid, hence no further
	 *            validation needed here.
	 */
	public void update(XMLLoginComm resp) {

		this.session = resp.getSession();
	}

	/**
	 * Update tasks. Do all possible actions - add, update and delete. It is
	 * server's response to provide fully detailed ordered instructions on how
	 * to update task list, invalid sequence will be ignored.
	 * 
	 * @param resp
	 *            the response of update request as an XMLDataResponse object.
	 *            XMLDataResponse ensures the response is valid, hence no
	 *            further validation needed here.
	 */
	public void update(XMLDataResponse resp) {

		try {
			// Update tasks
			ArrayList<XMLTask> updateTasks = resp.getTasks();
			for (int index = 0; index < updateTasks.size(); index++) {

				XMLTask updateTask = updateTasks.get(index);
				int sn = updateTask.getSequenceNo();

				switch (updateTask.getAction()) {
				case update:
					if (updateTask.getDeclareID() != null)
						this.tasks.get(sn).setDeclareID(
								updateTask.getDeclareID());
					if (updateTask.getSrc() != null)
						this.tasks.get(sn).setSrc(updateTask.getSrc());
					if (updateTask.getDest() != null)
						this.tasks.get(sn).setDest(updateTask.getDest());
					if (updateTask.getAvailableTime() != 0)
						this.tasks.get(sn).setAvailableTime(
								updateTask.getAvailableTime());
					if (updateTask.getDeadline() != 0)
						this.tasks.get(sn)
								.setDeadline(updateTask.getDeadline());
					if (updateTask.getSize() != 0)
						this.tasks.get(sn).setSize(updateTask.getSize());
					if (updateTask.getWeight() != 0)
						this.tasks.get(sn).setWeight(updateTask.getWeight());
					if (updateTask.getQuantity() != 0)
						this.tasks.get(sn)
								.setQuantity(updateTask.getQuantity());
					if (updateTask.getPlannedStartTime() != 0)
						this.tasks.get(sn).setPlannedStartTime(
								updateTask.getPlannedStartTime());
					if (updateTask.getPlannedLoadTime() != 0)
						this.tasks.get(sn).setPlannedLoadTime(
								updateTask.getPlannedLoadTime());
					if (updateTask.getPlannedTravelTime() != 0)
						this.tasks.get(sn).setPlannedTravelTime(
								updateTask.getPlannedTravelTime());
					if (updateTask.getPlannedUnloadTime() != 0)
						this.tasks.get(sn).setPlannedUnloadTime(
								updateTask.getPlannedUnloadTime());
					if (updateTask.getPlannedFinishTime() != 0)
						this.tasks.get(sn).setPlannedFinishTime(
								updateTask.getPlannedFinishTime());
					if (updateTask.getActualStartTime() != 0)
						this.tasks.get(sn).setActualStartTime(
								updateTask.getActualStartTime());
					if (updateTask.getActualLoadTime() != 0)
						this.tasks.get(sn).setActualLoadTime(
								updateTask.getActualLoadTime());
					if (updateTask.getActualTravelTime() != 0)
						this.tasks.get(sn).setActualTravelTime(
								updateTask.getActualTravelTime());
					if (updateTask.getActualUnloadTime() != 0)
						this.tasks.get(sn).setActualUnloadTime(
								updateTask.getActualUnloadTime());
					if (updateTask.getActualFinishTime() != 0)
						this.tasks.get(sn).setActualFinishTime(
								updateTask.getActualFinishTime());
					if (updateTask.getStatus() != null)
						this.tasks.get(sn).setStatus(updateTask.getStatus());
					break;
				case add:
					updateTask.setAction(Action.none);
					this.tasks.add(sn, updateTask);
					break;

				case delete:
					this.tasks.remove(sn);
					break;

				case none:
				default:
					break;
				}

			}
		} catch (Exception e) {
		}
	}

	/**
	 * Get array of tasks.
	 * 
	 * @return the ArrayList of XMLTask objects.
	 */
	public ArrayList<XMLTask> getTasks() {
		return tasks;
	}

	/**
	 * Get session
	 * 
	 * @return session as a XMLSession object.
	 */
	public XMLSession getSession() {
		return session;
	}

	/**
	 * Build and return an ArrayList of AndroidTaskItem objects. This is for UI
	 * display purposes.
	 * 
	 * @return an ArrayList of AndroidTaskItem representation of tasks.
	 */
	public ArrayList<AndroidTaskItem> getAndroidTasks() {
		ArrayList<AndroidTaskItem> tasks = new ArrayList<AndroidTaskItem>();
		for (int index = 0; index < this.tasks.size(); index++) {
			XMLTask xmlTask = this.tasks.get(index);
			tasks.add(new AndroidTaskItem(xmlTask.getSequenceNo(), xmlTask
					.getSrc(), xmlTask.getDest(), new Date(xmlTask
					.getPlannedFinishTime())));
		}
		return tasks;
	}
}
