package perpetualeclipse.eclipse;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.pde.core.IModel;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.WorkspaceModelManager;

import perpetualeclipse.TargetNotFoundException;

public class Eclipse {
	private static ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
	private static PDECore pde = PDECore.getDefault();
	private static IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
	
	public static IModel getPlugin(String name) {
		return pde.findPlugin(name).getModel();
	}

	public static IModel getFeature(String name) {
		return pde.findFeature(name).getModel();
	}
	
	public static ILaunchConfiguration getLaunchConfiguration(String name) throws CoreException, TargetNotFoundException {
		ILaunchConfiguration[] launchconfigurations = getLaunchconfigurations();
		for (ILaunchConfiguration configuration : launchconfigurations) {
			if (name.equals(configuration.getName())) return configuration;
		}
		throw new TargetNotFoundException("target " + name + " not found");
	}

	public static ILaunchConfiguration[] getLaunchconfigurations() throws CoreException {
		ILaunchConfiguration[] configurations = launchManager.getLaunchConfigurations();
		System.out.println("configurations: " + configurations.length);
		return configurations;
	}
	
	public static IProject getProject(String projectName) throws CoreException {
		IProject project = workspace.getProject(projectName);
		project.refreshLocal(IResource.DEPTH_INFINITE, null);
		return project;
	}
	
	public static IModel[] getPlugins() {
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
	
	private static boolean hasBuildProperties(IPluginModelBase model) {
		File file = new File(model.getInstallLocation(),"build.properties"); //$NON-NLS-1$
		return file.exists();
	}

	private static boolean isValidModel(IModel model) {
		return model != null && model instanceof IPluginModelBase;
	}
}
