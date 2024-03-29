package perpetualeclipse;

/**
 * Executes builds when they are scheduled.
 * 
 * @author Jaksa
 */
public class Scheduler {
	private boolean finished = false;
	
	public void run() {
		while(!finished) {
			Schedule schedule = pickNextShedule();
			schedule.waitFor();
			schedule.run();
		}
	}

	private Schedule pickNextShedule() {
		// TODO Auto-generated method stub
		return null;
	}
}
