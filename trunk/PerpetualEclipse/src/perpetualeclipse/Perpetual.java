package perpetualeclipse;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.State;
import org.eclipse.pde.internal.core.FeatureModelManager;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.TargetPlatform;
import org.eclipse.pde.internal.core.exports.FeatureExportInfo;
import org.eclipse.pde.internal.core.exports.FeatureExportOperation;
import org.eclipse.pde.internal.core.exports.PluginExportOperation;
import org.eclipse.pde.internal.core.exports.ProductExportOperation;
import org.eclipse.pde.internal.core.ifeature.IFeatureModel;
import org.eclipse.pde.internal.core.iproduct.IProductFeature;
import org.eclipse.pde.internal.core.iproduct.IProductModel;
import org.eclipse.pde.internal.core.iproduct.IProductPlugin;
import org.eclipse.pde.internal.core.product.WorkspaceProductModel;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

import perpetualeclipse.eclipse.Eclipse;


public class Perpetual {
	private static NullProgressMonitor nullProgressMonitor = new NullProgressMonitor();
	private static ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();

	public void exportFeature(final String name, final String destination) throws Exception {
		FeatureExportInfo info = new FeatureExportInfo() {{
			items = new Object[] {Eclipse.getFeature(name)};
			destinationDirectory = destination;
			toDirectory = true;
			useJarFormat = true;
		}};

		runExportOperation(name, destination, new FeatureExportOperation(info));
	}

	public void exportPlugin(final String name, final String destination) throws Exception {
		FeatureExportInfo info = new FeatureExportInfo() {{
			items = new Object[] {Eclipse.getPlugin(name)};
			destinationDirectory = destination;
			toDirectory = true;
			useJarFormat = true;
		}};

		runExportOperation(name, destination, new PluginExportOperation(info));
	}

	public void exportProduct(final String path, final String destination) throws Exception {
		final IProductModel productModel = getProduct(path);
		final String name = productModel.getProduct().getName();

		FeatureExportInfo info = new FeatureExportInfo() {{
			items = productModel.getProduct().useFeatures() ? getProductFeatures(productModel) : getProductPlugins(productModel);
			destinationDirectory = destination;
			toDirectory = false;
			zipFileName = name + ".zip";
		}};

		runExportOperation(name, destination, new ProductExportOperation(info, productModel.getProduct(), ""));
	}

	public IProject[] getProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		System.out.println("projects: " + projects.length);
		for (IProject project : projects) {
			System.out.println(project.getName() + " - " + project.getLocation());
		}
		return projects;
	}

	public void updateProject(String projectName) throws SVNException, CoreException {
		IProject project = Eclipse.getProject(projectName);

		SVNClientManager clientManager = SVNClientManager.newInstance();
		SVNUpdateClient updateClient = clientManager.getUpdateClient();
		long revisionNumber = updateClient.doUpdate(project.getLocation().toFile(), SVNRevision.HEAD, true);
	}
	
    private IProductModel getProduct(String path) throws Exception {
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));
		WorkspaceProductModel productModel = new WorkspaceProductModel(file, false);
		productModel.load();
		return productModel;
	}

    private IFeatureModel[] getProductFeatures(IProductModel productModel) {
        ArrayList list = new ArrayList();
        FeatureModelManager manager = PDECore.getDefault().getFeatureModelManager();
        IProductFeature[] features = productModel.getProduct().getFeatures();
        for (int i = 0; i < features.length; i++) {
            IFeatureModel model = manager.findFeatureModel(features[i].getId(), features[i].getVersion());
            if (model != null) list.add(model);
        }
        return (IFeatureModel[]) list.toArray(new IFeatureModel[list.size()]);
    }
    
	private BundleDescription[] getProductPlugins(IProductModel productModel) {
        ArrayList list = new ArrayList();
        State state = TargetPlatform.getState();
        IProductPlugin[] plugins = productModel.getProduct().getPlugins();
        for (int i = 0; i < plugins.length; i++) {
            BundleDescription bundle = state.getBundle(plugins[i].getId(), null);
            if (bundle != null) list.add(bundle);
        }
        return (BundleDescription[]) list.toArray(new BundleDescription[list.size()]);
    }

	private String head(String trace, int i) {
		if (trace.length() <= i) return trace;
		else return trace.substring(0, i) + "...";
	}

	private void runExportOperation(final String name, final String destination, FeatureExportOperation operation) throws Exception {
		System.out.println("exporting " + name + " to " + destination + "...");
		operation.run(nullProgressMonitor);
		if (operation.hasErrors()) 
			throw new Exception("Errors during export of " 
				+ name + " see the log file in " + destination + "/logs.zip");
		System.out.println(" done.");
	}
}
