package perpetualeclipse;

import java.util.ArrayList;
import java.util.List;

import perpetualeclipse.report.BuildFailureReport;
import perpetualeclipse.report.BuildReport;

public class Build implements Task {
	private List<UpdateTask> updateTasks = new ArrayList<UpdateTask>();
	private List<CompileTask> compileTasks = new ArrayList<CompileTask>();
	private List<TestTask> testTasks = new ArrayList<TestTask>();
	private List<ExportTask> exportTasks = new ArrayList<ExportTask>();

	public synchronized void addProject(String project) {
		updateTasks.add(new UpdateTask(project));
		compileTasks.add(new CompileTask(project));
	}

	public synchronized void addTest(String configuration) {
		testTasks.add(new TestTask(configuration));
	}

	public synchronized void addExport(ExportTask export) {
		// TODO add export
	}

	public synchronized BuildReport execute() {
		// synchronize on class as well so there will be only one build running in the JVM
		synchronized (Build.class) {
			BuildReport report = new BuildReport();
			try {
				process(updateTasks, report);
				process(compileTasks, report);
				process(testTasks, report);
				process(exportTasks, report);
			} catch (Exception e) {
				report.addReport(new BuildFailureReport(e));
			}
			return report;
		}
	}
	
	private void process(List<? extends Task> targets, BuildReport report) throws Exception {
		for (Task target : targets) {
			report.addReport(target.execute());
		}
	}
}