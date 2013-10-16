package info.thezero.eclipse.pdt.restapi.uri;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UriMap {
	private UriNode root;
	private Set<String> triggers = new HashSet<String>();
	private boolean anyTrigger = false;

	public UriMap() {
		this.startInit();
	}

	public void startInit() {
		root = new UriNode("");
	}

	public void finishInit() {
		root.normalize();
	}

	void addTrigger(String trigger) {
		if (trigger.equals("*")) {
			this.anyTrigger = true;
		} else {
			this.triggers.add(trigger);
		}
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

	public List<UriNode> suggest(String uri) {
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

		List<UriNode> suggestions = new ArrayList<UriNode>();
		for (UriNode child : node.getChildren()) {
			if (child.getName().startsWith(lastPart)) {
				suggestions.add(child);
			}
		}

		return suggestions;
	}

	public boolean suggestsFor(String suggestionTrigger) {
		if (this.anyTrigger) {
			return true;
		}

		for (String trigger : triggers) {
			if (trigger.equalsIgnoreCase(suggestionTrigger)) {
				return true;
			}
		}

		return false;
	}

}
