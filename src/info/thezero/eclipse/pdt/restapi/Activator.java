package info.thezero.eclipse.pdt.restapi;

import info.thezero.eclipse.pdt.restapi.preferences.PreferenceConstants;
import info.thezero.eclipse.pdt.restapi.uri.UriMapCollection;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "info.thezero.eclipse.pdt.restapi"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		this.getPreferenceStore()
			.addPropertyChangeListener(new IPropertyChangeListener() {
				// change listener to API URI location file

				@Override
				public void propertyChange(PropertyChangeEvent event) {
					if (event.getProperty() == PreferenceConstants.P_URI_DEFINITION) {
						UriMapCollection.getDefault().init();
					} else if (event.getProperty() == PreferenceConstants.P_COLLAPSE_LIMIT) {
						try {
							int newValue = (Integer) event.getNewValue();
							UriMapCollection.getDefault().setMaxSuggestions(newValue);
						} catch (ClassCastException E) {
							// do nothing, keep the old value
						}
					}
				}
			});
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
