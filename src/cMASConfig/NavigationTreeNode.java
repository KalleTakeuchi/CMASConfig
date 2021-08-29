package cMASConfig;

import javax.swing.tree.DefaultMutableTreeNode;

public class NavigationTreeNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8006177165858885729L;
	private String entityID;
	private NodeType typeValue;
	private String entityName;
	
	public enum NodeType {
		AGENT,
		SKILL,
		INTERFACE,
		VARIABLE,
		INTERFACE_VARIABLE,
		GOAL,
		PLAN,
		ORGANIZATION;
	}
	
	public NavigationTreeNode(String ID, String name, NodeType type) {
		super();
		entityID = ID;
		this.entityName = name;
		typeValue = type;
	}
	
	public NavigationTreeNode(String text) {
		super(text);
		this.entityID = text;
	}
	
	public void copyParametersFrom(NavigationTreeNode node) {
		this.entityName = node.getName();
		this.entityID = node.getID();
		this.typeValue = node.getNodeType();
		this.setUserObject(node.getUserObject());
	}
	
	@Override
	public String toString() {
		if (getUserObject() instanceof String) {
			if (getUserObject().toString() != "" && getUserObject() != null)
				return getUserObject().toString();
		}
		else if(entityName == null || entityName == "") {
			return entityID;
		}
		
		return entityName;
	}
	
	public NodeType getNodeType() {
		return typeValue;
	}
	
	public String getName() {
		return entityName;
	}
	
	public String getID() {
		return entityID;
	}
	
	public boolean hasName() {
		if (entityName == null || entityName == "")
			return false;
		return true;
	}
}
