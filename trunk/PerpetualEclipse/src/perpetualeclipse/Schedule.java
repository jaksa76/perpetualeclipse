package perpetualeclipse;

import perpetualeclipse.report.BuildReport;

/**
 * A Schedule determines when a {@link Build} will be executed. It is based an interval. 
 * 
 * @author Jaksa
 */
public class Schedule {
	private Build build;
	private long lastTimeRun = 0;
	private History history = History.getInstance();
	private final long interval;
	
	public Schedule(long interval) {
		this.interval = interval;
	}
	
	public void run() {
		BuildReport report = build.execute();
		history.addReport(report);
		lastTimeRun = System.currentTimeMillis();
	}
	
	public void waitFor() {
		long timeToWait = getNextRun() - System.currentTimeMillis();
		try { Thread.sleep(timeToWait); } catch (InterruptedException e) { }
	}

	private long getNextRun() {
		return lastTimeRun + interval;
	}
}
