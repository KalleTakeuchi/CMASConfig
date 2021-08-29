package cMASConfig;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class AgentGeneralTab extends Tab {
	
	private Agent agent;
	private List<AgentChangeListener> agentChangeListeners;

	/**
	 * 
	 */;
	private static final long serialVersionUID = -4991501843679557817L;
	
	public void removeAgentChangeListeners() {
		agentChangeListeners = new ArrayList<AgentChangeListener>();
	}
	
	private void createIDComponents() {
		
		this.add(new JLabel("Entity ID: "), "align right");
		JTextField txtEntID = new JTextField();
		txtEntID.setText(agent.ID);
		txtEntID.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txt = (JTextField) e.getSource();
				updateAgentID(txt.getText());
				
			}
			
		});
		
		this.add(txtEntID, "grow, wrap");
		
	}
	
	private void createDescriptionComponents() {
		
		this.add(new JLabel("Description: "), "align right");
		JTextField txtDescription = new JTextField();
		
		if (agent.Description == null)
			agent.Description = "";
		
		txtDescription.setText(agent.Description);
		txtDescription.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txt = (JTextField) e.getSource();
				updateAgentDescription(txt.getText());
				
			}
			
		});
		
		this.add(txtDescription, "grow, wrap");
	}
	
	private void createNameComponents() {
		JTextField txtName = new JTextField();
		txtName.setText(agent.Name);
		txtName.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txt = (JTextField) e.getSource();
				updateAgentName(txt.getText());
			}
			
		});
		
		this.add(new JLabel("Name: "), "align right");
		txtName.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				updateAgentName(txtName.getText());
				
			}
			
		});
		
		this.add(txtName, "grow, wrap");
	}
	
	private void createBaseTypeComponents() {
		String agentBaseTypes[] = new String[BaseType.values().length];
		int counter = 0;
		
		for (BaseType type : BaseType.values()) {
			agentBaseTypes[counter] = type.toString();
			counter++;
		}
		
		JComboBox<String> cmbAgentBaseType = new JComboBox<String>(agentBaseTypes);
		cmbAgentBaseType.setEditable(true);
		cmbAgentBaseType.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> source = (JComboBox<String>) e.getSource();
				String selection = source.getSelectedItem().toString();
				
				if (selection == null)
					return;
				
				agent.AgentBaseType = BaseType.valueOf(selection);
				
			}
			
		});
		
		this.add(new JLabel("Agent base type: "), "align right");
		this.add(cmbAgentBaseType, "grow, wrap");
		cmbAgentBaseType.setSelectedItem(agent.AgentBaseType);
	}
	
	private void createSpecificTypeComponents() {
		
		String[] agentSpecificTypes = new String[AgentSpecificType.values().length];
		
		int counter = 0;
		
		for (AgentSpecificType type : AgentSpecificType.values()) {
			agentSpecificTypes[counter] = type.toString();
			counter++;
		}
		
		/*
		if (!AgentSpecificType.belongsToEnum(agent.AgentSpecificType)) {
			ArrayList<String> specificTypes = new ArrayList<String>(Arrays.asList(agentSpecificTypes));
			specificTypes.add(agent.AgentSpecificType);
			agentSpecificTypes = new String[specificTypes.size()];
			
			for (int i = 0; i < specificTypes.size(); i++)
				agentSpecificTypes[i] = specificTypes.get(i);
			
			//Empty string does not require a warning
			if(!agent.AgentSpecificType.equals("")) {
				JOptionPane.showMessageDialog(new JPanel(),
						"The agent with ID " + agent.ID + " has an unrecognised specific type " + agent.AgentSpecificType + 
						". Consider adding this to the AgentSpecificType enumeration in the source code.",
						"Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		}*/
		
		JComboBox<String> cmbAgentSpecificType = new JComboBox<String>(agentSpecificTypes);
		cmbAgentSpecificType.setEditable(true);
		cmbAgentSpecificType.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> source = (JComboBox<String>) e.getSource();
				String selection = (String) source.getSelectedItem();
				
				if (selection == null)
					return;
				
				agent.AgentSpecificType = selection;
				
			}
			
		});
		
		
		this.add(new JLabel("Agent specific type: "), "align right");
		this.add(cmbAgentSpecificType, "grow, wrap");
		cmbAgentSpecificType.setSelectedItem(agent.AgentSpecificType);
	}

	
	public AgentGeneralTab(Agent agent) {
		
		super();
		
		agentChangeListeners = new ArrayList<AgentChangeListener>();
		this.agent = agent;
		this.setLayout(new MigLayout("fillx",
				"[shrink] [grow]",
				""));
		
		createIDComponents();
		createNameComponents();
		createDescriptionComponents();
		createBaseTypeComponents();
		createSpecificTypeComponents();
		
		
	}

	public void addAgentChangeListener(AgentChangeListener listener) {
		agentChangeListeners.add(listener);
	}
	
	
	private void updateAgentID(String ID) {
		if (!agent.ID.equals(ID)) {
			
			String oldID = agent.ID;
			agent.ID = ID;
			
			TreeManager.changeNode(oldID, new NavigationTreeNode(agent.ID, agent.Name, 
					NavigationTreeNode.NodeType.AGENT));

			for (AgentChangeListener lstnr : agentChangeListeners) {
				lstnr.agentIDChanged(oldID, agent);
			}
			
		}
	}
	
	private void updateAgentDescription(String description) {
		if (!agent.Description.equals(description)) {
			
			agent.Description = description;

		}
	}
	
	private void updateAgentName(String name) {
		if (!agent.Name.equals(name)) {
			
			agent.Name = name;
			
			TreeManager.changeNode(agent.ID, 
					new NavigationTreeNode(
							agent.ID, 
							agent.Name, 
							NavigationTreeNode.NodeType.AGENT)
					);
			
			for (AgentChangeListener lstnr : agentChangeListeners)
				lstnr.agentNameChanged(agent);
		}
	}
	
	public String getTitle() {
		return agent.ID;
	}
	
	public Agent getAgent() {
		return agent;
	}
	
	@Override
	public String getName() {
		return "General";
	}

	@Override
	void updateContent() {
		// TODO Auto-generated method stub
		
	}
}
