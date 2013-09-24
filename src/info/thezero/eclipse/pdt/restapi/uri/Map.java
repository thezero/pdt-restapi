package info.thezero.eclipse.pdt.restapi.uri;

import java.util.ArrayList;
import java.util.List;

public class Map {
	private Node root;

	public Map() {
		root = new Node("");
		this.init();
	}

	private void init() {
		for (String apiUri : getUris()) {
			// sanity check
			if (apiUri.isEmpty()) {
				continue;
			}

			// remove trailing slash
			if (apiUri.charAt(0) == '/') {
				apiUri = apiUri.substring(1);
			}

			Node node = root;
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
	}

	private String[] getUris() {
		return new String[] {
			"/users/:username",
			"/users/:username/profile",
			"/users/:username/account",
			"/login/sso"
		};
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

		Node node = root;
		for (int i = 0; i < partsCount - 1; i++) {
			if (!node.hasChild(parts[i])) {
				// no matches in subpart
				return null;
			}
			node = node.getChild(parts[i]);
		}

		List<String> result = new ArrayList<String>();
		for (Node child : node.getChildren()) {
			if (child.getName().startsWith(lastPart)) {
				result.add(child.getName());
			}
		}

		return result.toArray(new String[result.size()]);
	}

}
