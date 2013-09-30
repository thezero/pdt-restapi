package info.thezero.eclipse.pdt.restapi.uri;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UriNode {
	private boolean leaf;
	private String name;
	private Map<String, UriNode> children = new HashMap<String, UriNode>();
	private int skip = 0;
	private int childrenCount = 0;

	public UriNode(String name) {
		this.name = name;
	}

	public void setIsLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public boolean isLeaf() {
		return this.leaf;
	}

	public String getName() {
		return this.name;
	}

	public UriNode addChild(String name) {
		UriNode child = new UriNode(name);
		this.children.put(name, child);
		return child;
	}

	public boolean hasChild(String name) {
		return this.children.containsKey(name);
	}

	public UriNode getChild(String name) {
		return this.children.get(name);
	}

	public Collection<UriNode> getChildren() {
		return this.children.values();
	}

	public void normalize() {
		for (UriNode child : this.children.values()) {
			child.normalize();
			this.childrenCount += child.getChildrenCount();
		}

		if (this.isLeaf()) {
			this.childrenCount++;
		}
	}

	public int getSkip() {
		return this.skip;
	}

	public int getChildrenCount() {
		return this.childrenCount;
	}

	public Collection<String> collapse() {
		return this.collapse("");
	}

	private Collection<String> collapse(String prefix) {
		List<String> childrenNames = new ArrayList<String>();

		// leaf is presented without trailing slash
		prefix = prefix.concat(this.name);
		if (this.isLeaf()) {
			childrenNames.add(prefix);
		}

		// all children need a slash
		prefix = prefix.concat("/");
		for (UriNode child : this.children.values()) {
			childrenNames.addAll(child.collapse(prefix));
		}
		return childrenNames;
	}
}
