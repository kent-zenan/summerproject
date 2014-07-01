package ac.uk.nottingham.ningboport.controller;

import java.util.ArrayList;
import java.util.Date;

import ac.uk.nottingham.ningboport.model.AndroidTaskItem;
import ac.uk.nottingham.ningboport.model.XMLDataResponse;
import ac.uk.nottingham.ningboport.model.XMLLoginComm;
import ac.uk.nottingham.ningboport.model.XMLSession;
import ac.uk.nottingham.ningboport.model.XMLTask;
import ac.uk.nottingham.ningboport.model.XMLTask.Action;

public class LocalDataManager {

	private static LocalDataManager instance = null;
	private XMLSession session = new XMLSession();
	private ArrayList<XMLTask> tasks = new ArrayList<XMLTask>();

	private LocalDataManager() {
	}

	public static LocalDataManager getInstance() {
		synchronized (LocalDataManager.class) {
			if (instance == null) {
				instance = new LocalDataManager();
			}
			return instance;
		}
	}

	public void update(XMLLoginComm resp) {

		// Update session
		this.session = resp.getSession();
	}

	public void update(XMLDataResponse resp) {

		// Update tasks
		ArrayList<XMLTask> updateTasks = resp.getTasks();
		for (int index = 0; index < updateTasks.size(); index++) {

			XMLTask updateTask = updateTasks.get(index);
			int sn = updateTask.getSequenceNo();

			switch (updateTask.getAction()) {
			case update:
				if (updateTask.getDeclareID() != null)
					this.tasks.get(sn).setDeclareID(updateTask.getDeclareID());
				if (updateTask.getSrc() != null)
					this.tasks.get(sn).setSrc(updateTask.getSrc());
				if (updateTask.getDest() != null)
					this.tasks.get(sn).setDest(updateTask.getDest());
				if (updateTask.getAvailableTime() != 0)
					this.tasks.get(sn).setAvailableTime(
							updateTask.getAvailableTime());
				if (updateTask.getDeadline() != 0)
					this.tasks.get(sn).setDeadline(updateTask.getDeadline());
				if (updateTask.getSize() != 0)
					this.tasks.get(sn).setSize(updateTask.getSize());
				if (updateTask.getWeight() != 0)
					this.tasks.get(sn).setWeight(updateTask.getWeight());
				if (updateTask.getQuantity() != 0)
					this.tasks.get(sn).setQuantity(updateTask.getQuantity());
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
	}

	public ArrayList<XMLTask> getTasks() {
		return tasks;
	}

	public XMLSession getSession() {
		return session;
	}

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
