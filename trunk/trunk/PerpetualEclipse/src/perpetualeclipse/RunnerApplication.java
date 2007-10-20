package perpetualeclipse;

import java.util.Arrays;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import perpetualeclipse.tasks.CompileTask;
import perpetualeclipse.tasks.ExportFeatureTask;
import perpetualeclipse.tasks.ExportPluginTask;
import perpetualeclipse.tasks.ExportProductTask;
import perpetualeclipse.tasks.TestTask;
import perpetualeclipse.tasks.UpdateTask;

/**
 * This application is the entry point when running in server mode.
 * 
 * @author Jaksa
 */
public class RunnerApplication implements IPlatformRunnable {

	private void interpret(String[] args) throws Exception {
		System.out.println(Arrays.toString(args));

		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			if ("update".equals(arg)) new UpdateTask(args[++i]);
			else if ("run".equals(arg)) new TestTask(args[++i]).execute();
			else if ("compile".equals(arg)) new CompileTask(args[++i]).execute();                
			else if ("exportPlugin".equals(arg)) new ExportPluginTask(args[++i], args[++i]).execute();                
			else if ("exportFeature".equals(arg)) new ExportFeatureTask(args[++i], args[++i]).execute(); 
			else if ("exportProduct".equals(arg)) new ExportProductTask(args[++i], args[++i]).execute();
			else { System.out.println("could not recognize " + arg); }
		}
	}

	public Object run(Object argsObject) throws Exception {
		final String[] args = (String[]) argsObject;

		Display display = PlatformUI.createDisplay();
		try {
			new Thread() {
				public void run() {
					try {
						waitUntilWorkbenchStarted();
						// Scheduler.run();
						interpret(args);
					} catch (Exception e) { e.printStackTrace(); 
					} finally { closeWorkbench(); }
				}
			}.start();

			int code = PlatformUI.createAndRunWorkbench(display, new PerpetualWorkbenchAdvisor());

		} catch (Exception e) { e.printStackTrace(); 
		} finally { if (display != null) display.dispose(); }

		return EXIT_OK;
	}

	private class PerpetualWorkbenchAdvisor extends WorkbenchAdvisor {
		public String getInitialWindowPerspectiveId() { return null; }
		public boolean openWindows() { return true; } // Overriding this to prevent opening a window
	}

	private void waitUntilWorkbenchStarted() throws InterruptedException {
		while (!PlatformUI.isWorkbenchRunning()) Thread.sleep(1000);
	}

	private void closeWorkbench() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				PlatformUI.getWorkbench().close();
			}
		});
	}
}
