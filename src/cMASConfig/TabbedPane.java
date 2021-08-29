package cMASConfig;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TabbedPane extends DnDTabbedPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3195978419356678502L;
	public static final int TAB_CLOSE_BUTTON_WIDTH = 17;
	public static final int TAB_CLOSE_BUTTON_HEIGHT = 17;
	public static final int TAB_CLOSE_BUTTON_FONT_SIZE = 8;
	public static final int TAB_LABEL_FONT_SIZE = 10;
	
	private HashMap<Object,String> tabTitles = new HashMap<Object, String>();
	/*I really wanted to avoid references to components and windows inside of components, but the chain of 
	 * events necessary to let the main window know that an agent or something else has been added,
	 * removed or otherwise changed is simply too tedious to program. So I call "callUpdeteContent"
	 * using this reference instead.
	 */
	private MainWindow mainWindow;
	
	public TabbedPane(MainWindow mainWindow) {
		super();
		this.mainWindow = mainWindow;
	}
	
	
	public AgentTab getTabOfAgent(Agent agent) {
		Object[] keys = tabTitles.keySet().toArray();
		
		for (Object key : keys) {
			if (key instanceof AgentTab) {
				AgentTab aTab = (AgentTab) key;
				if(aTab.getAgent().equals(agent))
					return aTab;
			}
		}
		
		return null;
	}
	
	public List<Tab> getTabs() {
		
		ArrayList<Tab> tabs = new ArrayList<Tab>();
		Object[] keys = tabTitles.keySet().toArray();
		
		for(Object key : keys) {
			if (key instanceof Tab) {
				tabs.add((Tab) key);
			}
		}
		
		return tabs;
		
	}
	
	public PlanTab getTabOfPlan(Plan plan) {
		Object[] keys = tabTitles.keySet().toArray();
		
		for (Object key : keys) {
			if (key instanceof PlanTab) {
				PlanTab aTab = (PlanTab) key;
				if(aTab.getPlan().equals(plan))
					return aTab;
			}
		}
		
		return null;
	}
	
	public boolean agentHasTab(Agent agent) {
		Object[] keys = tabTitles.keySet().toArray();
		
		for (Object key : keys) {
			if (key instanceof AgentGeneralTab) {
				AgentGeneralTab aTab = (AgentGeneralTab) key;
				
				if (aTab.getAgent().equals(agent)) {
					return true;
				}
			}
		}
		
		return false;
	}
	

	public void addNewTab(Tab tab, Integer index) {
		
		if (tab instanceof AgentTab) {
			setAgentTabListeners((AgentTab)tab);
		}
		else if (tab instanceof PlanTab) {
			setPlanTabListeners((PlanTab)tab);
		}
		
		//Add tab and get index
		String title;
		if (tabTitles.values().contains(tab.toString())) {
			Random rnd = new Random();
			title = Long.valueOf(rnd.nextLong()).toString();
			
			while (tabTitles.containsValue(title))
				title = Long.valueOf(rnd.nextLong()).toString();
			
		}
		else {
			title = tab.toString();
		}
		
		
		this.insertTab(title, null, tab, null, index);
		tabTitles.put(tab, title);
		
		//create tab component
		JPanel myComponent = new JPanel(new FlowLayout());
		myComponent.setOpaque(false);
		JLabel lblTitle = new JLabel(tab.toString());
		lblTitle.setFont(new Font(lblTitle.getFont().getFontName(), Font.PLAIN, TAB_LABEL_FONT_SIZE));
		lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		myComponent.add(lblTitle);// "cell 0 0");
		JButton btnClose = new JButton("X");
		btnClose.setFont(new Font(btnClose.getFont().getFontName(), Font.PLAIN, TAB_LABEL_FONT_SIZE));
		btnClose.setPreferredSize(new Dimension(TAB_CLOSE_BUTTON_WIDTH, TAB_CLOSE_BUTTON_WIDTH));
		
		btnClose.addActionListener(new ActionListener() {
			private Tab myTab = (Tab) tab;
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeTabCMAS(myTab);
			}
			
		});
		
		btnClose.setContentAreaFilled(false);
		btnClose.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		myComponent.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		myComponent.add(btnClose);
		this.setTabComponentAt(this.indexOfTab(title), myComponent);
		this.setSelectedIndex(this.indexOfTab(title));
	}
	
	public void addNewTab(Tab tab) {
		
		if (tab instanceof AgentTab) {
			setAgentTabListeners((AgentTab)tab);
		}
		else if (tab instanceof PlanTab) {
			setPlanTabListeners((PlanTab)tab);
		}
		else if (tab instanceof AgentChartTab) {
			setAgentChartTabListeners((AgentChartTab)tab);
		}
		
		//Add tab and get index
		String title;
		if (tabTitles.values().contains(tab.toString())) {
			Random rnd = new Random();
			title = Long.valueOf(rnd.nextLong()).toString();
			
			while (tabTitles.containsValue(title))
				title = Long.valueOf(rnd.nextLong()).toString();
			
		}
		else {
			title = tab.toString();
		}
		
		
		this.add(title, tab);
		tabTitles.put(tab, title);
		
		//create tab component
		JPanel myComponent = new JPanel(new FlowLayout());
		myComponent.setOpaque(false);
		JLabel lblTitle = new JLabel(tab.toString());
		lblTitle.setFont(new Font(lblTitle.getFont().getFontName(), Font.PLAIN, TAB_LABEL_FONT_SIZE));
		lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		myComponent.add(lblTitle);// "cell 0 0");
		JButton btnClose = new JButton("X");
		btnClose.setFont(new Font(btnClose.getFont().getFontName(), Font.PLAIN, TAB_LABEL_FONT_SIZE));
		btnClose.setPreferredSize(new Dimension(TAB_CLOSE_BUTTON_WIDTH, TAB_CLOSE_BUTTON_WIDTH));
		
		btnClose.addActionListener(new ActionListener() {
			private Tab myTab = (Tab) tab;
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeTabCMAS(myTab);
			}
			
		});
		
		btnClose.setContentAreaFilled(false);
		btnClose.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		myComponent.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		myComponent.add(btnClose);
		this.setTabComponentAt(this.indexOfTab(title), myComponent);
		this.setSelectedIndex(this.indexOfTab(title));
	}
	
	private void setAgentChartTabListeners(AgentChartTab tab) {
		
		tab.getAgentChart().addAgentChartListener(new AgentChartListener() {
			
			@Override
			public void agentWasSet(Agent newAgent) {
				
				System.out.println("TabbedPane: Event was handled.");
				
				if (newAgent.Name == null || newAgent.Name.equals("")) {
					updateTabText(tabTitles.get(tab),"Agent chart: " + newAgent.ID);	
				}
				else {
					updateTabText(tabTitles.get(tab),"Agent chart: " + newAgent.Name);	
				}
				

				
			}
		});
		
	}


	private void setPlanTabListeners(PlanTab tab) {
		
		PlanGeneralTab generalTab = tab.getGeneralTab();
		
		generalTab.addPlanChangeListener(new PlanChangeListener() {
			
			@Override
			public void planNameChanged(Plan plan) {
				
				if (plan == null)
					return;
				
				TreeManager.changeNode(plan.ID, 
						new NavigationTreeNode(
								plan.ID, 
								plan.Name, 
								NavigationTreeNode.NodeType.PLAN)
						);

				if (!plan.Name.equals("") && plan.Name != null) {
					updateTabText(tabTitles.get(getTabOfPlan(plan)),"Plan: " + plan.Name);	
				}
				else {
					updateTabText(tabTitles.get(getTabOfPlan(plan)),"Plan: " + plan.ID);
				}
				
			}
			
			@Override
			public void planIDChanged(String oldID, Plan plan) {

				TreeManager.changeNode(oldID, new NavigationTreeNode(plan.ID, plan.Name, 
						NavigationTreeNode.NodeType.PLAN));
				
				if (plan.Name == null || plan.Name.equals(""))
					updateTabText(tabTitles.get(getTabOfPlan(plan)), "Agent: " + plan.ID);
				
			}


			@Override
			public void executionChanged() {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	/**
	 * Removes a given tab
	 * @param tabTitle
	 * @param newText
	 */
	public void removeTabCMAS(Tab myTab) {
		remove(indexOfTab(tabTitles.get(myTab)));
		tabTitles.remove(myTab);
	}
	
	public void selectTab(Object tab) {
		this.setSelectedIndex(this.indexOfTab(tabTitles.get(tab)));
	}
	
	public void updateTabText(String tabTitle, String newText) {
		int tabIndex = this.indexOfTab(tabTitle);
		JPanel panel = (JPanel) this.getTabComponentAt(tabIndex);
		
		System.out.println("updateTabText: tab index was: " + String.valueOf(tabIndex)
		+ ", tabTitle was " + tabTitle + ", newText was " + newText);
		
		//tab component has one JLabel and one button. JLabel needs to be changed
		for (int i = 0; i < panel.getComponentCount(); i++) {
			Component component = panel.getComponent(i);
			
			if (component instanceof JLabel) {
				JLabel label = (JLabel) component;
				label.setText(newText);
				System.out.println("Label was found and changed");
				return;
			}
		}
	
	}
	
	public PlanTab createPlanTab(Plan myPlan) {
		
		PlanTab myPlanTab;
		myPlanTab = new PlanTab(myPlan);
		
		return myPlanTab;
	}
	
	private void setAgentTabListeners(AgentTab agentTab) {
		
		AgentGeneralTab generalTab = agentTab.getGeneralTab();
		generalTab.removeAgentChangeListeners();
		generalTab.addAgentChangeListener(new AgentChangeListener() {

			@Override
			public void agentIDChanged(String oldID, Agent agent) {
				
				if (agent.Name == null || agent.Name.equals(""))
					updateTabText(tabTitles.get(getTabOfAgent(agent)), "Agent: " + agent.ID);
				
				mainWindow.callUpdateContent();
			}

			@Override
			public void agentRemoved(String agentID) {
				mainWindow.callUpdateContent();
				
			}

			@Override
			public void agentNameChanged(Agent agent) {
				
				if (agent == null)
					return;

				if (!agent.Name.equals("") && agent.Name != null) {
					updateTabText(tabTitles.get(getTabOfAgent(agent)),"Agent: " + agent.Name);	
				}
				else {
					updateTabText(tabTitles.get(getTabOfAgent(agent)),"Agent: " + agent.ID);
				}
				
				mainWindow.callUpdateContent();
				
			}
			
		});
		
	}
	
	public AgentChartTab createAgentChartTab() {
		return new AgentChartTab();
	}
	
	public AgentChartTab createAgentChartTab(Agent agent) {
		return new AgentChartTab(agent);
	}
	
	public AgentTab createAgentTab(Agent myAgent) {
		
		AgentTab myAgentTab;
		myAgentTab = new AgentTab(myAgent);
		
		return myAgentTab;
	}

}
