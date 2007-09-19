package perpetualeclipse.eclipse;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.State;
import org.eclipse.pde.core.IModel;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.FeatureModelManager;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.TargetPlatform;
import org.eclipse.pde.internal.core.WorkspaceModelManager;
import org.eclipse.pde.internal.core.ifeature.IFeatureModel;
import org.eclipse.pde.internal.core.iproduct.IProductFeature;
import org.eclipse.pde.internal.core.iproduct.IProductModel;
import org.eclipse.pde.internal.core.iproduct.IProductPlugin;
import org.eclipse.pde.internal.core.product.WorkspaceProductModel;

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
	
	public static IProject[] getProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		System.out.println("projects: " + projects.length);
		for (IProject project : projects) {
			System.out.println(project.getName() + " - " + project.getLocation());
		}
		return projects;
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
	
	
    public static IProductModel getProduct(String path) throws Exception {
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));
		WorkspaceProductModel productModel = new WorkspaceProductModel(file, false);
		productModel.load();
		return productModel;
	}

    public static IFeatureModel[] getProductFeatures(IProductModel productModel) {
        ArrayList list = new ArrayList();
        FeatureModelManager manager = PDECore.getDefault().getFeatureModelManager();
        IProductFeature[] features = productModel.getProduct().getFeatures();
        for (int i = 0; i < features.length; i++) {
            IFeatureModel model = manager.findFeatureModel(features[i].getId(), features[i].getVersion());
            if (model != null) list.add(model);
        }
        return (IFeatureModel[]) list.toArray(new IFeatureModel[list.size()]);
    }
    
    public static BundleDescription[] getProductPlugins(IProductModel productModel) {
        ArrayList list = new ArrayList();
        State state = TargetPlatform.getState();
        IProductPlugin[] plugins = productModel.getProduct().getPlugins();
        for (int i = 0; i < plugins.length; i++) {
            BundleDescription bundle = state.getBundle(plugins[i].getId(), null);
            if (bundle != null) list.add(bundle);
        }
        return (BundleDescription[]) list.toArray(new BundleDescription[list.size()]);
    }
}
