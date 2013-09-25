package info.thezero.eclipse.pdt.restapi.uri;

import info.thezero.eclipse.pdt.restapi.Activator;
import info.thezero.eclipse.pdt.restapi.preferences.PreferenceConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.php.internal.core.PHPCorePlugin;

public class UriMap {
	private UriNode root;
	private static final UriMap instance = new UriMap();

	public static UriMap getDefault() {
		return instance;
	}

	public UriMap() {
		this.init();
	}

	@SuppressWarnings("restriction")
	public void init() {
		root = new UriNode("");

		String location = Activator.getDefault().getPreferenceStore()
				.getString(PreferenceConstants.P_URI_DEFINITION);

		if (!location.isEmpty()) {
			try {
				File uriFile = new File(location);
				SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
				UriFileParser handler = new UriFileParser(this);
				parser.parse(uriFile, handler);
			} catch (Exception E) {
				PHPCorePlugin.log(E);
			}
		}
		root.normalize();
	}

	void addUri(String apiUri) {
		// sanity check
		if (apiUri.isEmpty()) {
			return;
		}

		// remove trailing slash
		if (apiUri.charAt(0) == '/') {
			apiUri = apiUri.substring(1);
		}

		UriNode node = root;
		for (String part : apiUri.split("/")) {
			if (node.hasChild(part)) {
				node = node.getChild(part);
			} else {
				node = node.addChild(part);
			}
		}

		// last part of URL denotes leaf
		if (node != root) {
			node.setIsLeaf(true);
		}
	}

	public String[] suggest(String uri) {
		String[] parts = uri.split("/");
		int partsCount = parts.length;
		String lastPart = parts[partsCount - 1];

		if (uri.length() > 1 && uri.charAt(uri.length() -1) == '/') {
			// Java removes trailing delimiter from split
			// add a part to trigger child suggestions
			++partsCount;
			lastPart = "";
		}

		UriNode node = root;
		for (int i = 0; i < partsCount - 1; i++) {
			// normalization doesn't change keys in HashMap
			// so we're searching only for first part
			// ie. "users" => {name: "users/:username"}
			if (!node.hasChild(parts[i])) {
				// no matches in subpart
				return null;
			}
			node = node.getChild(parts[i]);
			// additionally skip all parts removed during normalization
			i += node.getSkip();
		}

		List<String> result = new ArrayList<String>();
		for (UriNode child : node.getChildren()) {
			if (child.getName().startsWith(lastPart)) {
				if (child.isLeaf()) {
					result.add(child.getName());
				} else {
					result.add(child.getName().concat("/"));
				}
			}
		}

		return result.toArray(new String[result.size()]);
	}

}
