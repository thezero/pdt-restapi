package info.thezero.eclipse.pdt.restapi.uri;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Node {
	private boolean leaf;
	private String name;
	private Map<String, Node> children = new HashMap<String, Node>();

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
}
