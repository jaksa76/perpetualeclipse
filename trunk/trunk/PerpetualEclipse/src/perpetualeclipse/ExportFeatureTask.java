package perpetualeclipse;

import org.eclipse.pde.internal.core.exports.FeatureExportInfo;
import org.eclipse.pde.internal.core.exports.FeatureExportOperation;

import perpetualeclipse.eclipse.Eclipse;
import perpetualeclipse.report.ExportReport;

public class ExportFeatureTask extends ExportTask {
	private final String name;
	private final String destination;

	public ExportFeatureTask(String name, String destination) {
		this.name = name;
		this.destination = destination;
	}
	
	public ExportReport execute() throws Exception {
		FeatureExportInfo info = new FeatureExportInfo() {{
			items = new Object[] {Eclipse.getFeature(name)};
			destinationDirectory = destination;
			toDirectory = true;
			useJarFormat = true;
		}};

		runExportOperation(name, destination, new FeatureExportOperation(info));
		return new ExportReport();
	}
}
