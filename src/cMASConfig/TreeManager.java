/**
 * 
 */
package cMASConfig;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import cMASConfig.NavigationTreeNode.NodeType;

/**
 * Contains static methods for managing and creating the JTree. This class also houses the JTree as a static variable
 * @author kalle 
 */
public class TreeManager {

	public static final String AGENTS_NODE_STRING = "Agents";
	public static final String INTERFACES_NODE_STRING = "Interfaces";
	public static final String SKILLS_NODE_STRING = "Skills";
	public static final String VARIABLES_NODE_STRING = "Variables";
	public static final String PLANS_NODE_STRING = "Plans";
	public static final String INTERFACE_VARIABLES_NODE_STRING = "Variables";
	public static final String GOALS_NODE_STRING = "Goals";
	
	private static JTree navigationTree;
	

	/*public static void setNavigationTree(JTree tree) {
		navigationTree = tree;
	}*/
	
	public static JTree getNavigationTree() {
		
		if (navigationTree == null)
			CreateNavigationTree(createTreeStructure(DataManager.getAgents(), DataManager.getPlans()));
		
		return navigationTree;
	}

	/**
	 * 
	 * @param ID The entity ID
	 * @param name Entity name, if there is one. Can be left as an empty string.
	 * @param type NodeType
	 * @param parentID Is only needed if a node deeper in the hierarchy than Agent is added. This will often be an agent-ID, but in the case of interface variables it can also be an interface ID
	 */
	static void addNode(String ID, String name, NavigationTreeNode.NodeType type, Optional<String> parentID) {
		
		NavigationTreeNode root = (NavigationTreeNode) navigationTree.getModel().getRoot();
		NavigationTreeNode node;
		DefaultTreeModel model = (DefaultTreeModel) navigationTree.getModel();
		int index;

		if (type.equals(NavigationTreeNode.NodeType.AGENT)) {
			// Find "Agents" node;
			node = getChild(root, AGENTS_NODE_STRING);
			if (node == null) {
				node = new NavigationTreeNode(AGENTS_NODE_STRING,
						"",
						NavigationTreeNode.NodeType.ORGANIZATION);
				node.add(new NavigationTreeNode(ID, name, type));
				index = root.getChildCount();
				
				if(index > 0)
					index -= 1;
				
				model.insertNodeInto(node, root, root.getChildCount());
			}
			else {
			
				index = node.getChildCount();
				
				if(index > 0)
					index -= 1;
				
				model.insertNodeInto(
						new NavigationTreeNode(ID, name, type),
						node, 
						index);
			}
		}
		else if (type.equals(NavigationTreeNode.NodeType.VARIABLE)) {
			if (!parentID.isPresent())
				return;
			
			//Find agent node
			node = getChild(root, parentID.get());
			
			if (node == null)
				return;
			
			NavigationTreeNode variablesNode = getChild(node, VARIABLES_NODE_STRING);
			
			if (variablesNode == null) {
				variablesNode = new NavigationTreeNode(VARIABLES_NODE_STRING, "", NodeType.ORGANIZATION);
				variablesNode.add(new NavigationTreeNode(ID, name, type));
				
				index = node.getChildCount();
				
				if (index > 0)
					index -= 1;
				
				model.insertNodeInto(variablesNode, node, 0);
			}
			else {
				
				index = variablesNode.getChildCount();
				
				if (index > 0)
					index -= 1;
				
				model.insertNodeInto(
						new NavigationTreeNode(ID, name, type), 
						variablesNode, 
						index);
			}
				
		}
		else if (type.equals(NavigationTreeNode.NodeType.INTERFACE)) {
			//Get the interface node
			node = getChild(root, parentID.get());
			
			if (node == null)
				return;
			
			NavigationTreeNode interfacesNode = getChild(node, INTERFACES_NODE_STRING);
			
			if (interfacesNode == null) {
				interfacesNode = new NavigationTreeNode(INTERFACES_NODE_STRING, "", NavigationTreeNode.NodeType.ORGANIZATION);
				interfacesNode.add(new NavigationTreeNode(ID, name, type));
				
				index = node.getChildCount();
				
				if (index > 0)
					index -= 1;
				
				model.insertNodeInto(interfacesNode, node, 0);
			}
			else {
				
				index = interfacesNode.getChildCount();
				
				if (index > 0)
					index -= 1;
				
				model.insertNodeInto(new NavigationTreeNode(ID, name, type), 
						interfacesNode, 
						index);
				
			}
		}
		else if (type.equals(NavigationTreeNode.NodeType.SKILL)) {
			
			node = getChild(root, parentID.get());
			
			if (node == null)
				return;
			
			//Get the Skills node
			NavigationTreeNode skillsNode = getChild(node, SKILLS_NODE_STRING);
			
			if (skillsNode == null) {
				skillsNode = new NavigationTreeNode(SKILLS_NODE_STRING, "", NavigationTreeNode.NodeType.ORGANIZATION);
				skillsNode.add(new NavigationTreeNode(ID, name, type));
				
				index = node.getChildCount();
				
				if (index > 0)
					index -= 1;
				
				model.insertNodeInto(skillsNode, node, 0);
			}
			else {
				
				index = skillsNode.getChildCount();
				
				if (index > 0)
					index -= 1;
				
				model.insertNodeInto(new NavigationTreeNode(ID, name, type), 
						skillsNode, 
						index);
				
			}
		}
		else if (type.equals(NavigationTreeNode.NodeType.INTERFACE_VARIABLE)) {
			
			node = getChild(root, parentID.get());
			
			if (node == null)
				return;
			
			//Get the variables node
			NavigationTreeNode variablesNode = getChild(node, INTERFACE_VARIABLES_NODE_STRING);
			
			if (variablesNode == null) {
				variablesNode = new NavigationTreeNode(INTERFACE_VARIABLES_NODE_STRING, "", NavigationTreeNode.NodeType.ORGANIZATION);
				variablesNode.add(new NavigationTreeNode(ID, name, type));
				
				index = node.getChildCount();
				
				if (index > 0)
					index -= 1;
				
				model.insertNodeInto(variablesNode, node, 0);
			}
			else {
				
				index = variablesNode.getChildCount();
				
				if (index > 0)
					index -= 1;
				
				model.insertNodeInto(new NavigationTreeNode(ID, name, type), 
						variablesNode, 
						index);
				
			}
			
		}
		else if (type.equals(NavigationTreeNode.NodeType.GOAL)) {
			node = getChild(root, parentID.get());
			
			if (node == null)
				return;
			
			//Get the goals node
			NavigationTreeNode goalsNode = getChild(node, GOALS_NODE_STRING);
			
			if (goalsNode == null) {
				goalsNode = new NavigationTreeNode(GOALS_NODE_STRING, "", NavigationTreeNode.NodeType.ORGANIZATION);
				goalsNode.add(new NavigationTreeNode(ID, name, type));
				
				index = node.getChildCount();
				
				if (index > 0)
					index -= 1;
				
				model.insertNodeInto(goalsNode, node, 0);
			}
			else {
				
				index = goalsNode.getChildCount();
				
				if (index > 0)
					index -= 1;
				
				model.insertNodeInto(new NavigationTreeNode(ID, name, type), 
						goalsNode, 
						index);
				
			}
		}
		else if (type.equals(NavigationTreeNode.NodeType.PLAN) ) {
			node = getChild(root, PLANS_NODE_STRING);
			if (node == null) {
				node = new NavigationTreeNode(PLANS_NODE_STRING,
						"",
						NavigationTreeNode.NodeType.ORGANIZATION);
				node.add(new NavigationTreeNode(ID, name, type));
				model.insertNodeInto(node, root, root.getChildCount());
			}
			else {
				model.insertNodeInto(
						new NavigationTreeNode(ID, name, type),
						node, 
						node.getChildCount());
			}
		}
	}

	/**Checks if the the node has a child with a NavigationTreeNode with the given
	/* ID. Returns the child or null
	 * 
	 * @param node
	 * @param treeNodeID
	 * @return
	 */
	static NavigationTreeNode getChild(NavigationTreeNode node, String treeNodeID) {
		if (node.getChildCount() <= 0)
			return null;

		for (@SuppressWarnings("rawtypes")
		Enumeration e = node.children(); e.hasMoreElements();) {
			NavigationTreeNode child = (NavigationTreeNode) e.nextElement();
			
			if (child.getID() == treeNodeID)
				return child;

			if (child.getChildCount() > 0) {
				NavigationTreeNode grandChild = getChild(child, treeNodeID);
				if (grandChild != null) {
					return grandChild;
				}
			}
		}

		return null;
	}

	static NavigationTreeNode createInterfacesNode(Agent agent) {

		NavigationTreeNode interfacesNode = new NavigationTreeNode(INTERFACES_NODE_STRING, 
				"", 
				NavigationTreeNode.NodeType.ORGANIZATION);

		for (int j = 0; j < agent.Interfaces.size(); j++) {

			NavigationTreeNode interfaceNode = new NavigationTreeNode(
					agent.Interfaces.get(j).ID, 
					agent.Interfaces.get(j).Name, 
					NavigationTreeNode.NodeType.INTERFACE);
			
			interfacesNode.add(interfaceNode);

			// Add skills
			if (agent.Interfaces.get(j).Skills != null) {
				NavigationTreeNode skillsNode = createSkillsNode(agent.Interfaces.get(j).Skills);

				if (skillsNode.getChildCount() > 0)
					interfaceNode.add(skillsNode);
			}

			// Add variables
			if (agent.Interfaces.get(j).Variables != null) {
				NavigationTreeNode variablesNode = createVariablesNode(agent.Interfaces.get(j));
				
				if (variablesNode.getChildCount() > 0)
					interfaceNode.add(variablesNode);
			}

		}

		return interfacesNode;
	}

	static NavigationTreeNode createSkillsNode(List<Skill> skills) {

		NavigationTreeNode skillsNode = new NavigationTreeNode(SKILLS_NODE_STRING,
				"",
				NavigationTreeNode.NodeType.ORGANIZATION);

		for (int k = 0; k < skills.size(); k++) {
			skillsNode.add(new NavigationTreeNode(skills.get(k).ID, skills.get(k).Name, NavigationTreeNode.NodeType.SKILL));
		}

		return skillsNode;
	}

	/**
	 * Creates variables node for interface variables
	 * @param intrfce
	 * @return
	 */
	static NavigationTreeNode createVariablesNode(Interface intrfce) {

		NavigationTreeNode variablesNode = new NavigationTreeNode(VARIABLES_NODE_STRING, 
				"",
				NavigationTreeNode.NodeType.ORGANIZATION);

		// Add variables
		for (int j = 0; j < intrfce.Variables.size(); j++) {
			variablesNode.add(new NavigationTreeNode(intrfce.Variables.get(j).ID, intrfce.Variables.get(j).Name, 
					NavigationTreeNode.NodeType.VARIABLE));
		}

		return variablesNode;
	}
	
	/**
	 * Creates variables node for agent variables
	 * @param agent
	 * @return
	 */
	static NavigationTreeNode createVariablesNode(Agent agent) {

		NavigationTreeNode variablesNode = new NavigationTreeNode(VARIABLES_NODE_STRING, 
				"",
				NavigationTreeNode.NodeType.ORGANIZATION);

		// Add variables
		for (int j = 0; j < agent.Variables.size(); j++) {
			variablesNode.add(new NavigationTreeNode(agent.Variables.get(j).ID, agent.Variables.get(j).Name, 
					NavigationTreeNode.NodeType.VARIABLE));
		}

		return variablesNode;
	}

	static NavigationTreeNode createAgentsNode(Enumeration<Agent> agents) {

		NavigationTreeNode agentsNode = new NavigationTreeNode(AGENTS_NODE_STRING, 
				"", 
				NavigationTreeNode.NodeType.ORGANIZATION);
		
		NavigationTreeNode agentNode;
		NavigationTreeNode interfacesNode;
		NavigationTreeNode variablesNode;
		NavigationTreeNode goalsNode;

		for (Agent myAgent; agents.hasMoreElements();) {
			myAgent = agents.nextElement();
			agentNode = new NavigationTreeNode(myAgent.ID, myAgent.Name, NavigationTreeNode.NodeType.AGENT);
			agentsNode.add(agentNode);

			// Add interfaces
			if (myAgent.Interfaces != null) {
				interfacesNode = createInterfacesNode(myAgent);

				if (interfacesNode.getChildCount() > 0)
					agentNode.add(interfacesNode);
			}
			//Add variables
			if (myAgent.Variables != null) {
				variablesNode = createVariablesNode(myAgent);

				if (variablesNode.getChildCount() > 0)
					agentNode.add(variablesNode);
			}
			//Add goals
			if (myAgent.Goals != null) {
				goalsNode = createGoalsNode(myAgent);
				
				if (goalsNode.getChildCount() > 0)
					agentNode.add(goalsNode);
			}

		}

		return agentsNode;
	}
	
	private static NavigationTreeNode createGoalsNode(Agent agent) {
		NavigationTreeNode goalsNode = new NavigationTreeNode(GOALS_NODE_STRING,
				"",
				NavigationTreeNode.NodeType.ORGANIZATION);
		
		//Add goals
		for (int j = 0; j < agent.Goals.size(); j++) {
			goalsNode.add(new NavigationTreeNode(agent.Goals.get(j).ID, agent.Goals.get(j).Name,
					NavigationTreeNode.NodeType.GOAL));
		}
		
		return goalsNode;
	}

	static NavigationTreeNode createPlansNode(Enumeration<Plan> plans) {
		
		NavigationTreeNode plansNode = new NavigationTreeNode(
				PLANS_NODE_STRING,
				"",
				NavigationTreeNode.NodeType.ORGANIZATION);

		NavigationTreeNode planNode;
		
		for (Plan myPlan; plans.hasMoreElements();) {
			myPlan = plans.nextElement();
			planNode = new NavigationTreeNode(
					myPlan.ID,
					myPlan.Name,
					NavigationTreeNode.NodeType.PLAN);
			plansNode.add(planNode);
		}
		
		return plansNode;
	}
	
	/**
	 * Creates the root of the navigation tree and all 
	 * branches.
	 * @param agents
	 * @param plans
	 * @return
	 */
	static NavigationTreeNode createTreeStructure(Enumeration<Agent> agents, Enumeration<Plan> plans) {

		NavigationTreeNode root = new NavigationTreeNode("Configuration", 
				"", 
				NavigationTreeNode.NodeType.ORGANIZATION);
				
		NavigationTreeNode agentsNode = createAgentsNode(agents);
		if (agentsNode.getChildCount() > 0)
			root.add(agentsNode);
		
		NavigationTreeNode plansNode = createPlansNode(plans);
		if (plansNode.getChildCount() > 0)
			root.add(plansNode);

		return root;

	}

	static void removeNode(String treeNodeID) {
		DefaultTreeModel model = (DefaultTreeModel) navigationTree.getModel();
		NavigationTreeNode root = (NavigationTreeNode) model.getRoot();
		
		if (root == null)
			return;
		
		NavigationTreeNode node = getChild(root, treeNodeID);
		
		if (node == null)
			return;
		
		NavigationTreeNode parentNode = (NavigationTreeNode) node.getParent();
		
		if (parentNode.getChildCount() == 1) {
			model.removeNodeFromParent(parentNode);
		}
		else {
			model.removeNodeFromParent(node);
		}
		
	}
	
	static void changeNode(String treeNodeID, NavigationTreeNode newNode) {
		DefaultTreeModel model = (DefaultTreeModel) navigationTree.getModel();

		NavigationTreeNode root = (NavigationTreeNode) model.getRoot();

		NavigationTreeNode r = getChild(root, treeNodeID);

		if (r == null)
			return;

		r.copyParametersFrom(newNode);
		model.nodeChanged(r);
	}

	static void StyleTree() {
		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		renderer.setLeafIcon(null);
		renderer.setClosedIcon(null);
		renderer.setOpenIcon(null);
		String fontName = navigationTree.getFont().getFontName();
		Map<TextAttribute, Integer> fontAttributes = new HashMap<TextAttribute, Integer>();
		fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		renderer.setFont(new Font(fontName, Font.PLAIN, 14).deriveFont(fontAttributes));
		renderer.setTextNonSelectionColor(Color.BLUE);
		renderer.setTextSelectionColor(Color.BLUE);
		navigationTree.setShowsRootHandles(true);
		//navigationTree.setRootVisible(false);
		// navigationTree.putClientProperty("JTree.lineStyle", "None");

		navigationTree.setCellRenderer(renderer);
	}

	static void CreateNavigationTree(NavigationTreeNode root) {
		navigationTree = new JTree(root);

		StyleTree();
	}
	
	static void setDragEnabled(boolean b) {
		if (navigationTree == null)
			return;
		
		navigationTree.setDragEnabled(b);
	}
	
	static void setTransferHandler(TransferHandler th) {
		if (navigationTree == null)
			return;
		
		navigationTree.setTransferHandler(th);
	}
	
	static void addTreeSelectionListener(TreeSelectionListener listener) {
		if (navigationTree == null)
			return;
		
		navigationTree.addTreeSelectionListener(listener);
	}
	
	static void addMouseListener(MouseListener listener) {
		if (navigationTree == null)
			return;
		
		navigationTree.addMouseListener(listener);
	}
	
	static void setTreeModel(TreeModel model) {
		navigationTree.setModel(model);
	}
}
