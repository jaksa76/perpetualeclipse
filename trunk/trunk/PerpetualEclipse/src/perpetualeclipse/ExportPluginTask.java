package perpetualeclipse;

import org.eclipse.pde.internal.core.exports.FeatureExportInfo;
import org.eclipse.pde.internal.core.exports.PluginExportOperation;

import perpetualeclipse.eclipse.Eclipse;
import perpetualeclipse.report.ExportReport;

public class ExportPluginTask extends ExportTask {
	private final String name;
	private final String destination;

	public ExportPluginTask(String name, String destination) {
		this.name = name;
		this.destination = destination;
	}
	
	public ExportReport execute() throws Exception {
		FeatureExportInfo info = new FeatureExportInfo() {{
			items = new Object[] {Eclipse.getPlugin(name)};
			destinationDirectory = destination;
			toDirectory = true;
			useJarFormat = true;
		}};

		runExportOperation(name, destination, new PluginExportOperation(info));
		return new ExportReport();
	}
}
