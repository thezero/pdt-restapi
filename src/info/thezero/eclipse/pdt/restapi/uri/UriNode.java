package info.thezero.eclipse.pdt.restapi.uri;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UriNode {
	private boolean leaf;
	private String name;
	private Map<String, UriNode> children = new HashMap<String, UriNode>();
	private int skip = 0;

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
		if (this.children.size() == 1 && !this.leaf) {
			UriNode child    = (UriNode) this.children.values().toArray()[0];
			this.name     = String.format("%s/%s", this.name, child.name);
			this.children = child.children;
			this.leaf     = child.leaf;
			this.skip++;
			this.normalize();
		} else {
			for (UriNode child : this.children.values()) {
				child.normalize();
			}
		}
	}

	public int getSkip() {
		return this.skip;
	}
}
