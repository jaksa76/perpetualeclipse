package perpetualeclipse;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import perpetual.eclipse.webinterface.WebInterface;

/**
 * The activator class controls the plug-in life cycle
 */
public class PerpetualPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "PerpetualEclipse";

	// The shared instance
	private static PerpetualPlugin plugin;

	private WebInterface webInterface;
	
	/**
	 * The constructor
	 */
	public PerpetualPlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		this.webInterface = new WebInterface(8080);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		this.webInterface.stop();
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static PerpetualPlugin getDefault() {
		return plugin;
	}

}
