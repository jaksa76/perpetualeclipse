package perpetualeclipse;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import perpetualeclipse.webinterface.DummyXMLContentProvider;
import perpetualeclipse.webinterface.FlexSecurityConfigurationContentProvider;
import perpetualeclipse.webinterface.SimpleReportContentProvider;
import perpetualeclipse.webinterface.WebInterface;

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
		initWebInterface();		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		this.webInterface.stop();
		webInterface = null;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static PerpetualPlugin getDefault() {
		return plugin;
	}

	public WebInterface getWebInterface() {
		return webInterface;
	}

	private void initWebInterface() throws Exception {
		webInterface = new WebInterface(8080);
		webInterface.addContentProvider("/", new SimpleReportContentProvider());
		webInterface.addContentProvider("/dummy", new DummyXMLContentProvider());
		webInterface.addContentProvider("/crossdomain.xml", new FlexSecurityConfigurationContentProvider());
	}

}
