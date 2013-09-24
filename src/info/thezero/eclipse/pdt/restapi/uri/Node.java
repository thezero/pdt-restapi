package info.thezero.eclipse.pdt.restapi.uri;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Node {
	private boolean leaf;
	private String name;
	private Map<String, Node> children = new HashMap<String, Node>();
	private int skip = 0;

	public Node(String name) {
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

	public Node addChild(String name) {
		Node child = new Node(name);
		this.children.put(name, child);
		return child;
	}

	public boolean hasChild(String name) {
		return this.children.containsKey(name);
	}

	public Node getChild(String name) {
		return this.children.get(name);
	}

	public Collection<Node> getChildren() {
		return this.children.values();
	}

	public void normalize() {
		if (this.children.size() == 1 && !this.leaf) {
			Node child    = (Node) this.children.values().toArray()[0];
			this.name     = String.format("%s/%s", this.name, child.name);
			this.children = child.children;
			this.leaf     = child.leaf;
			this.skip++;
			this.normalize();
		} else {
			for (Node child : this.children.values()) {
				child.normalize();
			}
		}
	}

	public int getSkip() {
		return this.skip;
	}
}
