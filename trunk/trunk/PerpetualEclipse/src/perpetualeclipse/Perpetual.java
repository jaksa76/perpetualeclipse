package perpetualeclipse;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.junit.ITestRunListener;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaLaunchDelegate;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.State;
import org.eclipse.pde.core.IModel;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.FeatureModelManager;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.TargetPlatform;
import org.eclipse.pde.internal.core.WorkspaceModelManager;
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

import report.BuildReport;
import report.TestCase;
import report.TestReport;


public class Perpetual {

	private final class TestListener implements ITestRunListener {
		private final TestReport report;

		TestCase currentTestCase;

		private TestListener(TestReport report) {
			this.report = report;
		}

		public void testStarted(String testId, String testName) {
			currentTestCase.started();
			String name = testName.substring(0, testName.indexOf('('));
			String classname = testName.substring(testName.indexOf('(') + 1, testName.lastIndexOf(')'));
			currentTestCase = new TestCase(name, classname);
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

	private static NullProgressMonitor nullProgressMonitor = new NullProgressMonitor();
	private static ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();

	public String head(String trace, int i) {
		if (trace.length() <= i) return trace;
		else return trace.substring(0, i) + "...";
	}

	public BuildReport compileProject(String projectName) throws CoreException {
		IProject project = getProject(projectName);
		BuildReport buildReport = new BuildReport(project.getName());

		System.out.println("building " + project.getName());
		project.build(IncrementalProjectBuilder.FULL_BUILD, null);

		IMarker[] markers = project.findMarkers(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, true, IResource.DEPTH_INFINITE);
		System.out.println("found " + markers.length + " problems");

		for (IMarker marker : markers) {
			if (((Integer)marker.getAttribute("severity")) > 1) {
				String errorMessage = (String) marker.getAttribute("message");
				buildReport.addError(errorMessage);
				System.out.println(errorMessage);
			}
		}

		return buildReport;
	}

	public void exportPlugin(final String name, final String destination) throws Exception {
		FeatureExportInfo info = new FeatureExportInfo() {{
			items = new Object[] {getPlugin(name)};
			destinationDirectory = destination;
			toDirectory = true;
			useJarFormat = true;
		}};

		runOperation(name, destination, new PluginExportOperation(info));
	}

	private void runOperation(final String name, final String destination, FeatureExportOperation operation) throws Exception {
		System.out.println("exporting " + name + " to " + destination + "...");
		operation.run(nullProgressMonitor);
		if (operation.hasErrors()) 
			throw new Exception("Errors during export of " 
				+ name + " see the log file in " + destination + "/logs.zip");
		System.out.println(" done.");
	}

	public void exportFeature(final String name, final String destination) throws Exception {
		FeatureExportInfo info = new FeatureExportInfo() {{
			items = new Object[] {getFeature(name)};
			destinationDirectory = destination;
			toDirectory = true;
			useJarFormat = true;
		}};

		runOperation(name, destination, new FeatureExportOperation(info));
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

		runOperation(name, destination, new ProductExportOperation(info, productModel.getProduct(), ""));
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
    
	public IProject[] getProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		System.out.println("projects: " + projects.length);
		for (IProject project : projects) {
			System.out.println(project.getName() + " - " + project.getLocation());
		}
		return projects;
	}

	public TestReport run(String target) throws CoreException, TargetNotFoundException {
		ILaunchConfiguration configuration = getLaunchConfiguration(target);

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
	
	public void updateProject(String projectName) throws SVNException, CoreException {
		IProject project = getProject(projectName);

		SVNClientManager clientManager = SVNClientManager.newInstance();
		SVNUpdateClient updateClient = clientManager.getUpdateClient();
		long revisionNumber = updateClient.doUpdate(project.getLocation().toFile(), SVNRevision.HEAD, true);
	}

	private ILaunchConfiguration getLaunchConfiguration(String name) throws CoreException, TargetNotFoundException {
		ILaunchConfiguration[] launchconfigurations = getLaunchconfigurations();
		for (ILaunchConfiguration configuration : launchconfigurations) {
			if (name.equals(configuration.getName())) return configuration;
		}
		throw new TargetNotFoundException("target " + name + " not found");
	}

	private ILaunchConfiguration[] getLaunchconfigurations() throws CoreException {
		ILaunchConfiguration[] configurations = launchManager.getLaunchConfigurations();
		System.out.println("configurations: " + configurations.length);
		return configurations;
	}

	private IProductModel getProduct(String path) throws Exception {
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));
		WorkspaceProductModel productModel = new WorkspaceProductModel(file, false);
		productModel.load();
		return productModel;
	}

	private IModel[] getPlugins() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		ArrayList plugins = new ArrayList();
		for (int i = 0; i < projects.length; i++) {
			if (!WorkspaceModelManager.isBinaryPluginProject(projects[i]) && WorkspaceModelManager.isPluginProject(projects[i])) {
				IModel model = PDECore.getDefault().getModelManager().findModel(projects[i]);
				if (model != null && isValidModel(model) && hasBuildProperties((IPluginModelBase)model)) {
					plugins.add(model);
				}
			}
		}

		return (IModel[]) plugins.toArray(new IModel[plugins.size()]);
	}

	private IModel getPlugin(String name) {
		return PDECore.getDefault().findPlugin(name).getModel();
	}

	private IModel getFeature(String name) {
		return PDECore.getDefault().findFeature(name).getModel();
	}

	private IProject getProject(String projectName) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		project.refreshLocal(IResource.DEPTH_INFINITE, null);
		return project;
	}

	private boolean hasBuildProperties(IPluginModelBase model) {
		File file = new File(model.getInstallLocation(),"build.properties"); //$NON-NLS-1$
		return file.exists();
	}

	protected boolean isValidModel(IModel model) {
		return model != null && model instanceof IPluginModelBase;
	}
}
