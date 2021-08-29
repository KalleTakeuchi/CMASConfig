package cMASConfig;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

public class AgentInterfacesTab extends Tab {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3391131858277420298L;
	
	Agent agent;
	List<InterfaceChangeListener> interfaceChangeListeners;
	JList<String> interfaceList;
	JTabbedPane tabbedPane;
	
	public AgentInterfacesTab(Agent agent) {
		this.agent = agent;
		this.setLayout(new BorderLayout());
		interfaceChangeListeners = new ArrayList<InterfaceChangeListener>();
		createSideBar();
		tabbedPane = new JTabbedPane();
		this.add(tabbedPane, BorderLayout.CENTER);
	}
	
	public void removeInterfaceChangeListeners() {
		interfaceChangeListeners = new ArrayList<InterfaceChangeListener>();
	}
	
	private void createSideBar() {
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new MigLayout("fill"));
		myPanel.setBorder(new EmptyBorder(5,5,5,5));
		
		//Label
		myPanel.add(new JLabel("Interfaces"), "north, gapy 0px 5px");
		
		//List of interfaces
		String myInterfaces[] = new String[agent.Interfaces.size()];
		
		for (int i = 0; i < agent.Interfaces.size(); i++) {
			if (agent.Interfaces.get(i).Name == "") {
				myInterfaces[i] = agent.Interfaces.get(i).ID;
			}
			else {
				myInterfaces[i] = agent.Interfaces.get(i).Name;
			}
		}
		
		interfaceList = new JList<String>(myInterfaces);
		interfaceList.setPreferredSize(new Dimension(100, 0));
		interfaceList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		interfaceList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
							
				String selection = (String) ((JList<String>) e.getSource()).getSelectedValue();	
				Interface myInterface = null;
				
				/*
				 * The search algorithm below has a bug. If an interface has the same name as 
				 * another interface's ID it is not certain which interface will
				 * be found.
				 */
				for(Interface anInterface : agent.Interfaces) {
					if (anInterface.Name == selection || anInterface.ID == selection) {
						myInterface = anInterface;
						break;
					}
				}
				
				if (myInterface == null)
					return;
				
				updateTabs(myInterface);
				
			}
		});
		
		myPanel.add(interfaceList, "dock center, gapy 0px 5px, wrap");
		
		//South button panel
		JPanel southSubPanel = new JPanel();
		southSubPanel.setLayout(new FlowLayout());
		
		//Button to remove interface
		JButton btnRemoveInterface = new JButton("-");
		btnRemoveInterface.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeSelectedInterface();				
			}
		});
		
		southSubPanel.add(btnRemoveInterface);
		
		//Button to add interface
		JButton btnNewInterface = new JButton("+");
		btnNewInterface.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addNewInterface();				
			}
		});
		
		southSubPanel.add(btnNewInterface);
		
		myPanel.add(southSubPanel, "south, gapy 0px 5px");
		this.add(myPanel, BorderLayout.WEST);
		
	}

	private void removeSelectedInterface() {
		String selection = interfaceList.getSelectedValue();
		
		if (selection == null || selection.equals(""))
			return;
		
		Integer res = null;
		
		for (Interface intrfce : agent.Interfaces) {
			if (intrfce.ID == selection) {
				res = JOptionPane.showConfirmDialog(this, 
						"Are you sure you want to delete the interface with ID " +
						selection + "?", 
						"Delete interface",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE);
			}
			else if (intrfce.Name == selection) {
				res = JOptionPane.showConfirmDialog(this, 
						"Are you sure you want to delete the interface with name " +
						selection + "?", 
						"Delete interface",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE);
			}
			
			if (res != null) {
				if (res.equals(JOptionPane.YES_OPTION)) {
					
					agent.Interfaces.remove(intrfce);
					TreeManager.removeNode(intrfce.ID);
					updateInterfaceList();
					removeTabs();
					
					tabbedPane.validate();
					tabbedPane.repaint();
					
					for(InterfaceChangeListener listener : interfaceChangeListeners) {
						listener.interfaceRemoved(agent.ID, intrfce);
					}
					
					break;
				}
			}
		}
		
	}
	
	private void removeTabs() {
		if (tabbedPane.getTabCount() > 0) {
			tabbedPane.remove(tabbedPane.indexOfTab("General"));
			tabbedPane.remove(tabbedPane.indexOfTab("Variables"));
			tabbedPane.remove(tabbedPane.indexOfTab("Skills"));
		}
	}

	private void addNewInterface() {
		
		Interface myInterface = new Interface(DataManager.createUniqueID());
		
		agent.Interfaces.add(myInterface);
		
		TreeManager.addNode(myInterface.ID, "", NavigationTreeNode.NodeType.INTERFACE, Optional.of(agent.ID));
		
		updateInterfaceList();
		
	}

	private void updateTabs(Interface selectedInterface) {

		InterfaceGeneralTab generalTab = new InterfaceGeneralTab(selectedInterface, agent);
		generalTab.addInterfaceChangeListener(new InterfaceChangeListener() {
			
			@Override
			public void interfaceRemoved(String agentID, Interface intrfceId) {
				updateInterfaceList();
								
				for(InterfaceChangeListener listener : interfaceChangeListeners) {
					listener.interfaceRemoved(agentID, intrfceId);
				}
				
			}
			
			@Override
			public void interfaceNameChanged(String agentID, Interface intrfce) {
				updateInterfaceList();
				TreeManager.changeNode(intrfce.ID, 
						new NavigationTreeNode(intrfce.ID, 
								intrfce.Name, 
								NavigationTreeNode.NodeType.INTERFACE));
				
				for(InterfaceChangeListener listener : interfaceChangeListeners) {
					listener.interfaceNameChanged(agentID, intrfce);
				}
				
			}
			
			@Override
			public void interfaceIDChanged(String oldID, String agentID, Interface intrfce) {
				updateInterfaceList();
				TreeManager.changeNode(oldID, 
						new NavigationTreeNode(intrfce.ID, 
								intrfce.Name, 
								NavigationTreeNode.NodeType.INTERFACE));
				
				for(InterfaceChangeListener listener : interfaceChangeListeners) {
					listener.interfaceIDChanged(oldID, agentID, intrfce);
				}
			}
		});
		InterfaceVariablesTab variablesTab = new InterfaceVariablesTab(selectedInterface);
		InterfaceSkillsTab skillsTab = new InterfaceSkillsTab(selectedInterface);
		
		removeTabs();
		tabbedPane.add(generalTab, "General");
		tabbedPane.add(variablesTab, "Variables");
		tabbedPane.add(skillsTab, "Skills");
	}
	
	private void updateInterfaceList() {
		
		String myInterfaces[] = new String[agent.Interfaces.size()];
		
		for (int i = 0; i < agent.Interfaces.size(); i++) {
			if (agent.Interfaces.get(i).Name == "") {
				myInterfaces[i] = agent.Interfaces.get(i).ID;
			}
			else {
				myInterfaces[i] = agent.Interfaces.get(i).Name;
			}
		}
		
		interfaceList.setListData(myInterfaces);
		
	}
	
	public void addInterfaceChangeListener(InterfaceChangeListener interfaceChangeListener) {
		interfaceChangeListeners.add(interfaceChangeListener);
	}


	@Override
	void updateContent() {
		
		updateInterfaceList();
	}

}
