package perpetualeclipse;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

public class RunnerApplication implements IPlatformRunnable {
	
	private void cycle(String[] args) throws Exception {
		for (String arg : args) {
			System.out.println(arg);
		}

		Perpetual perpetual = new Perpetual();
		
         for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if ("run".equals(arg)) { perpetual.run(args[++i]);
            } else if ("compile".equals(arg)) { perpetual.compileProject(args[++i]);                
            } else if ("exportPlugin".equals(arg)) { perpetual.exportPlugin(args[++i], args[++i]);                
            } else if ("exportFeature".equals(arg)) { perpetual.exportFeature(args[++i], args[++i]); 
            } else if ("exportProduct".equals(arg)) { perpetual.exportProduct(args[++i], args[++i]);
            } else { System.out.println("could not recognize " + arg); }
        }

//		perpetual.updateProject("p1");
//		perpetual.exportPlugin("org.asdf.codesearch", "C:\\temp\\");
//		perpetual.compileProject("PerpetualEclipse");
//		perpetual.compileProject("p2");
//		perpetual.run("DummyTest");
//		perpetual.run("DummyPDETest");
	}
	
	public Object run(Object argsObject) throws Exception {
		final String[] args = (String[]) argsObject;
		
		Display display = PlatformUI.createDisplay();
		try {
			new Thread() {
				public void run() {
					try {
						waitUntilWorkbenchStarted();
						cycle(args);
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
