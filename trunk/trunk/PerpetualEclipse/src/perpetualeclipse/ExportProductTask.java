package perpetualeclipse;

import org.eclipse.pde.internal.core.exports.FeatureExportInfo;
import org.eclipse.pde.internal.core.exports.ProductExportOperation;
import org.eclipse.pde.internal.core.iproduct.IProductModel;

import perpetualeclipse.eclipse.Eclipse;
import perpetualeclipse.report.ExportReport;

public class ExportProductTask extends ExportTask {
	private final String path;
	private final String destination;

	public ExportProductTask(String path, String destination) {
		this.path = path;
		this.destination = destination;
	}
	
	public ExportReport execute() throws Exception {
		final IProductModel productModel = Eclipse.getProduct(path);
		final String name = productModel.getProduct().getName();

		FeatureExportInfo info = new FeatureExportInfo() {{
			items = productModel.getProduct().useFeatures() ? Eclipse.getProductFeatures(productModel) : Eclipse.getProductPlugins(productModel);
			destinationDirectory = destination;
			toDirectory = false;
			zipFileName = name + ".zip";
		}};

		runExportOperation(name, destination, new ProductExportOperation(info, productModel.getProduct(), ""));
		return new ExportReport();
	}

}
