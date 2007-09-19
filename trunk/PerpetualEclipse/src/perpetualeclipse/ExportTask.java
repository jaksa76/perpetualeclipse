package perpetualeclipse;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.pde.internal.core.exports.FeatureExportOperation;

import perpetualeclipse.report.Report;

public abstract class ExportTask implements Task {
	private static NullProgressMonitor nullProgressMonitor = new NullProgressMonitor();
	
	protected void runExportOperation(final String name, final String destination, FeatureExportOperation operation) throws Exception {
		System.out.println("exporting " + name + " to " + destination + "...");
		operation.run(nullProgressMonitor);
		if (operation.hasErrors()) 
			throw new Exception("Errors during export of " 
					+ name + " see the log file in " + destination + "/logs.zip");
		System.out.println(" done.");
	}

	public abstract Report execute() throws Exception;	
}
