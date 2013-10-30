package info.thezero.eclipse.pdt.restapi.uri;

import info.thezero.eclipse.pdt.restapi.Activator;
import info.thezero.eclipse.pdt.restapi.preferences.PreferenceConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.php.internal.core.PHPCorePlugin;

@SuppressWarnings("restriction")
public class UriMapCollection {

	private static final UriMapCollection instance = new UriMapCollection();
	private int maxSuggestions = 10;
	private List<UriMap> maps = new ArrayList<UriMap>();

	public static UriMapCollection getDefault() {
		return instance;
	}
	public UriMapCollection() {
		// TODO Auto-generated constructor stub
		this.init();
	}

	public void init() {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		this.setMaxSuggestions(preferenceStore.getInt(PreferenceConstants.P_COLLAPSE_LIMIT));
		String location = preferenceStore.getString(PreferenceConstants.P_URI_DEFINITION);

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
	}

	public UriMap addMap() {
		UriMap map = new UriMap();
		this.maps.add(map);
		return map;
	}

	public Collection<UriSuggestion> suggest(String trigger, String uri) {
		String[] parts = uri.split("/");
		int partsCount = parts.length;

		if (uri.length() > 1 && uri.charAt(uri.length() -1) == '/') {
			// Java removes trailing delimiter from split
			// add a part to trigger child suggestions
			++partsCount;
		}

		List<UriNode> suggestions = new ArrayList<UriNode>();
		for (UriMap map : this.maps) {
			if (trigger.isEmpty() || map.suggestsFor(trigger)) {
				suggestions.addAll(map.suggest(uri));
			}
		}
		
		int totalSuggestions = 0;
		for (UriNode node : suggestions) {
			totalSuggestions += node.getChildrenCount();
		}

		// for presentation purposes, prefix URI with starting slash
		String showUri = "";
		for (int i = 0; i < partsCount - 1; i++) {
			showUri = showUri.concat(parts[i]).concat("/");
		}

		List<UriSuggestion> result = new ArrayList<UriSuggestion>();
		boolean useAll = (totalSuggestions <= this.getMaxSuggestions());
		for (UriNode child : suggestions) {
			if (useAll) {
				// collapse adds leafs automatically
				for (String childUri : child.collapse()) {
					result.add(new UriSuggestion(showUri, childUri));
				}
			} else if (child.isLeaf()) {
				String childUri = child.getName();
				result.add(new UriSuggestion(showUri, childUri));
			} else if (!useAll) {
				String childUri = child.getName().concat("/");
				result.add(new UriSuggestion(showUri, childUri));
			}
		}

		return result;
	}
	public int getMaxSuggestions() {
		return this.maxSuggestions;
	}
	public void setMaxSuggestions(int maxSuggestions) {
		this.maxSuggestions = maxSuggestions;
	}

}
