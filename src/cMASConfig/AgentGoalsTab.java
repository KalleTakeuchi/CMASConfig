package cMASConfig;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class AgentGoalsTab extends Tab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5162615552889299181L;
	Agent agent;
	JList<String> goalList;
	JTabbedPane tabbedPane;
	GoalGeneralTab generalTab;
	
	public AgentGoalsTab(Agent agent) {
		this.agent = agent;
		this.setLayout(new BorderLayout());
		createSideBar();
		tabbedPane = new JTabbedPane();
		this.add(tabbedPane, BorderLayout.CENTER);
	}
	
	private void createSideBar() {
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new MigLayout("fill"));
		myPanel.setBorder(new EmptyBorder(5,5,5,5));
		
		//Label
		myPanel.add(new JLabel("Goals"), "north, gapy 0px 5px");
		
		//List of goals
		String myGoals[] = new String[agent.Goals.size()];
		
		for (int i = 0; i < agent.Goals.size(); i++) {
			if (agent.Goals.get(i).Name == "") {
				myGoals[i] = agent.Goals.get(i).ID;
			}
			else {
				myGoals[i] = agent.Goals.get(i).Name;
			}
		}
		
		goalList = new JList<String>(myGoals);
		goalList.setPreferredSize(new Dimension(100, 0));
		goalList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		goalList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
							
				String selection = (String) ((JList<String>) e.getSource()).getSelectedValue();	
				Goal myGoal = null;
				
				/*
				 * The search algorithm below has a bug. If a goal has the same name as 
				 * another goal's ID it is not certain which goal will
				 * be found.
				 */
				for(Goal aGoal : agent.Goals) {
					if (aGoal.Name == selection || aGoal.ID == selection) {
						myGoal = aGoal;
						break;
					}
				}
				
				if (myGoal == null)
					return;
				
				updateTabs(myGoal);
								
			}
		});
		
		myPanel.add(goalList, "dock center, gapy 0px 5px, wrap");
		
		//South button panel
		JPanel southSubPanel = new JPanel();
		southSubPanel.setLayout(new FlowLayout());
		
		//Button to remove interface
		JButton btnRemoveGoal = new JButton("-");
		btnRemoveGoal.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeSelectedGoal();
				generalTab.updateContent();
			}
		});
		
		southSubPanel.add(btnRemoveGoal);
		
		//Button to add interface
		JButton btnNewGoal = new JButton("+");
		btnNewGoal.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addNewGoal();
				generalTab.updateContent();
			}
		});
		
		southSubPanel.add(btnNewGoal);
		
		myPanel.add(southSubPanel, "south, gapy 0px 5px");
		this.add(myPanel, BorderLayout.WEST);
		
	}
	
	protected void addNewGoal() {
		
		Goal myGoal = new Goal(DataManager.createUniqueID());
		
		agent.Goals.add(myGoal);
		
		TreeManager.addNode(myGoal.ID, "", NavigationTreeNode.NodeType.GOAL, Optional.of(agent.ID));
		
		updateGoalList();
		
	}

	protected void removeSelectedGoal() {
		String selection = goalList.getSelectedValue();
		
		if (selection == null || selection.equals(""))
			return;
		
		Integer res = null;
		
		for (Goal aGoal : agent.Goals) {
			if (aGoal.ID == selection) {
				res = JOptionPane.showConfirmDialog(this, 
						"Are you sure you want to delete the goal with ID " +
						selection + "?", 
						"Delete goal",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE);
			}
			else if (aGoal.Name == selection) {
				res = JOptionPane.showConfirmDialog(this, 
						"Are you sure you want to delete the goal with name " +
						selection + "?", 
						"Delete goal",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE);
			}
			
			if (res != null) {
				if (res.equals(JOptionPane.YES_OPTION)) {
					
					agent.Goals.remove(aGoal);
					TreeManager.removeNode(aGoal.ID);
					updateGoalList();
					removeTabs();
					
					tabbedPane.validate();
					tabbedPane.repaint();
					
					break;
				}
			}
		}
		
	}
	
	private void removeTabs() {
		if (tabbedPane.getTabCount() > 0) {
			tabbedPane.remove(tabbedPane.indexOfTab("General"));
			tabbedPane.remove(tabbedPane.indexOfTab("Parameters"));
		}
	}

	private void updateTabs(Goal selectedGoal) {

		generalTab = new GoalGeneralTab(selectedGoal, agent);
		generalTab.addGoalChangeListener(new GoalChangeListener() {
			
			@Override
			public void goalNameChanged(Goal goal) {
				updateGoalList();
			}
			
			@Override
			public void goalIDChanged(Goal goal) {
				updateGoalList();
				generalTab.updateContent();
			}
		});
		
		GoalParametersTab parametersTab = new GoalParametersTab(selectedGoal);
			
		removeTabs();
		tabbedPane.add(generalTab, "General");
		tabbedPane.add(parametersTab, "Parameters");
		}
			
	private void updateGoalList() {
			
			String myGoals[] = new String[agent.Goals.size()];
			
			for (int i = 0; i < agent.Goals.size(); i++) {
				if (agent.Goals.get(i).Name == "") {
					myGoals[i] = agent.Goals.get(i).ID;
				}
				else {
					myGoals[i] = agent.Goals.get(i).Name;
				}
			}
			
			goalList.setListData(myGoals);
			
		}
	
	@Override
	void updateContent() {
		// TODO Auto-generated method stub

	}
	

}
