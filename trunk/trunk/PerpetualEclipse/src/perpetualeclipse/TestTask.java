package perpetualeclipse;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.junit.ITestRunListener;
import org.eclipse.jdt.junit.JUnitCore;

import perpetualeclipse.eclipse.Eclipse;
import perpetualeclipse.report.TestResult;
import perpetualeclipse.report.TestReport;

public class TestTask implements Task {
	private final String target;

	public TestTask(String target) {
		this.target = target;
	}

	public TestReport execute() throws CoreException, TargetNotFoundException {
		ILaunchConfiguration configuration = Eclipse.getLaunchConfiguration(target);
	
		final TestReport testReport = new TestReport(configuration.getName());
		ITestRunListener listener = new TestListener(testReport);
		
		JUnitCore.addTestRunListener(listener);
		
		try {
			ILaunch launch = configuration.launch(ILaunchManager.RUN_MODE, null);
			while (!launch.isTerminated()) Thread.sleep(1000);
		} catch (Exception e) { 
			e.printStackTrace(); 
		} finally { JUnitCore.removeTestRunListener(listener); }
				
		return testReport;
	}
	
	private class TestListener implements ITestRunListener {
		private final TestReport report;

		TestResult currentTestCase;

		private TestListener(TestReport report) {
			this.report = report;
		}

		public void testStarted(String testId, String testName) {
			currentTestCase.started();
			String name = testName.substring(0, testName.indexOf('('));
			String classname = testName.substring(testName.indexOf('(') + 1, testName.lastIndexOf(')'));
			currentTestCase = new TestResult(name, classname);
		}

		public void testFailed(int status, String testId, String testName, String trace) {
						currentTestCase.failed(trace);
		//				if (status == STATUS_FAILURE) ;
		//				else if (status == STATUS_ERROR) ;
					}

		public void testEnded(String testId, String testName) {
			currentTestCase.finished();
			report.addTestCase(currentTestCase);
		}

		public void testReran(String testId, String testClass, String testName, int status, String trace) {}
		public void testRunEnded(long elapsedTime) {}
		public void testRunStarted(int testCount) {}
		public void testRunStopped(long elapsedTime) {}
		public void testRunTerminated() {}
	}

}
