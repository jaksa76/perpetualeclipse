package perpetualeclipse;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.junit.ITestRunListener;
import org.eclipse.jdt.junit.JUnitCore;
import org.eclipse.pde.core.IModel;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.WorkspaceModelManager;
import org.eclipse.pde.internal.core.exports.FeatureExportInfo;
import org.eclipse.pde.internal.ui.PDELabelProvider;
import org.eclipse.pde.internal.ui.build.PluginExportJob;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;

import perpetualeclipse.Report.BuildReport;
import perpetualeclipse.Report.TestReport;

public class Perpetual {
	
	private final class TestListener implements ITestRunListener {
		public TestReport testReport;
		private boolean currentTestFailed = false;

		public void testEnded(String testId, String testName) {
			System.out.println("test ended " + testName);
			if (!currentTestFailed) testReport.addTestSuccess(testName);
			currentTestFailed = false;
		}

		public void testFailed(int status, String testId, String testName, String trace) {
			System.out.println("test failed " + testName);
			testReport.addTestFailure(testName, head(trace, 50));
			currentTestFailed = true;
		}

		public void testReran(String testId, String testClass, String testName, int status, String trace) {
			System.out.println("test reran: " + testName);
		}

		public void testRunEnded(long elapsedTime) {
			System.out.println("test run ended after " + elapsedTime);
		}

		public void testRunStarted(int testCount) {
			System.out.println("running " + testCount + " tests");
		}

		public void testRunStopped(long elapsedTime) {
			System.out.println("test run stopped after " + elapsedTime);
		}

		public void testRunTerminated() {
			System.out.println("test run terminated");			
		}

		public void testStarted(String testId, String testName) {
			System.out.println("test started: " + testName);			
		}
	}

	private class WaitForJob extends JobChangeAdapter {
		boolean done = false;
		
		public WaitForJob(Job job) { 
			job.addJobChangeListener(this);
			job.schedule();
			while (!done) {
				try { Thread.sleep(1000); } catch (InterruptedException e) {}
			}
		}

		public void done(IJobChangeEvent event) { 
			this.done = true; 
		}
	}

	ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
	Report report = new Report();
	private TestListener testListener;
	
	public Perpetual() {
		testListener = new TestListener();
		JUnitCore.addTestRunListener(testListener);
	}
	
	public String head(String trace, int i) {
		if (trace.length() <= i) return trace;
		else return trace.substring(0, i) + "...";
	}

	public void compileProject(String projectName) {
		try {
			IProject project = getProject(projectName);
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
			System.out.println("building " + project.getName());
			project.build(IncrementalProjectBuilder.FULL_BUILD, null);
			BuildReport buildReport = report.createBuildReport(project.getName());
			IMarker[] markers = project.findMarkers(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, true, IResource.DEPTH_INFINITE);
			System.out.println("found " + markers.length + " problems");
			for (IMarker marker : markers) {
				if (((Integer)marker.getAttribute("severity")) > 1) {
					String errorMessage = (String) marker.getAttribute("message");
					buildReport.addError(errorMessage);
					System.out.println(errorMessage);
				}
//				for (Object key: marker.getAttributes().keySet()) {
//					System.out.println(key + ": " + marker.getAttributes().get(key));
//				}
			}
		} catch (CoreException e) { e.printStackTrace(); }
	}

	@SuppressWarnings("restriction")
	public void exportPlugin(final String pluginName, final String destination) throws TargetNotFoundException {
		FeatureExportInfo info = new FeatureExportInfo() {{
			toDirectory = true;
			useJarFormat = true;
			exportSource = false;
			destinationDirectory = destination;
			zipFileName = null;
			items = new Object[] {getPlugin(pluginName)};
			signingInfo = null;
		}};
		
		PluginExportJob job = new PluginExportJob(info);
		new WaitForJob(job);
	}
	
	public IProject[] getProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		System.out.println("projects: " + projects.length);
		for (IProject project : projects) {
			System.out.println(project.getName() + " - " + project.getLocation());
		}
		return projects;
	}
	
	public void run(String target) throws CoreException, TargetNotFoundException {
		ILaunchConfiguration configuration = getLaunchConfiguration(target);
		run(configuration);
	}
	
	public void updateProject(String projectName) throws SVNException {
		IProject project = getProject(projectName);
		
		SVNClientManager clientManager = SVNClientManager.newInstance();
		SVNUpdateClient updateClient = clientManager.getUpdateClient();
		long revisionNumber = updateClient.doUpdate(project.getLocation().toFile(), SVNRevision.HEAD, true);
		
//		String[] path = new String[] {project.getLocation().toString()};
//		IRepositoryLocation repositoryLocation = SVNRemoteStorage.instance().getRepositoryLocation(project);
//		ISVNClientWrapper svnClient = repositoryLocation.acquireSVNProxy();
//		svnClient.update(path, Revision.HEAD, true, true, new SVNNullProgressMonitor());
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
	
	private IModel getPlugin(String name) throws TargetNotFoundException {
		IModel[] plugins = getPlugins();
		PDELabelProvider labelProvider = new PDELabelProvider();
		for (IModel plugin : plugins) {
			String currentPluginName = labelProvider.getText(plugin);
			System.out.println(currentPluginName);
			if (currentPluginName.contains(name)) {
				return plugin;
			}
		}
		throw new TargetNotFoundException(name + " not found");
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
	
	private IProject getProject(String projectName) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		return project;
	}
	
	private boolean hasBuildProperties(IPluginModelBase model) {
		File file = new File(model.getInstallLocation(),"build.properties"); //$NON-NLS-1$
		return file.exists();
	}

	private void run(ILaunchConfiguration configuration) {
		try {
			testListener.testReport = report.createTestReport(configuration.getName());
			ILaunch launch = configuration.launch(ILaunchManager.RUN_MODE, null);
			while (!launch.isTerminated()) Thread.sleep(1000);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	protected boolean isValidModel(IModel model) {
		return model != null && model instanceof IPluginModelBase;
	}
}
