package cMASConfig;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class TabbedPaneManager extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8398172295532873166L;
	private List<TabbedPane> tabbedPanes = new ArrayList<TabbedPane>();
	private List<JSplitPane> splitPanes = new ArrayList<JSplitPane>();
	
	public TabbedPaneManager(TabbedPane firstTabbedPane) {
		this.setLayout(new BorderLayout());
		tabbedPanes.add(firstTabbedPane);
		this.add(firstTabbedPane, BorderLayout.CENTER);
	}
	
	/**
	 * Returns all tabs
	 * @return
	 */
	public List<Tab> getTabs() {
		
		ArrayList<Tab> tabs = new ArrayList<Tab>();
		
		for (TabbedPane pane : tabbedPanes) {
			for (Tab tab : pane.getTabs()) {
				tabs.add(tab);
			}
		}
		
		return tabs;
	}
	
	/**
	 * Splits the view horizontally once
	 * @param newTabbedPane
	 */
	public void splitView(TabbedPane newTabbedPane) {
		
		JSplitPane newSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPanes.add(newSplitPane);
		
		if (splitPanes.size() == 1) {
			newSplitPane = splitPanes.get(0);
			this.remove(tabbedPanes.get(0));
			newSplitPane.setLeftComponent(tabbedPanes.get(0));
			newSplitPane.setRightComponent(newTabbedPane);
			this.add(newSplitPane, BorderLayout.CENTER);
		}
		else {
			JSplitPane lowestNestedSplitPane;
			lowestNestedSplitPane = splitPanes.get(splitPanes.size() - 2);
			newSplitPane.setLeftComponent(tabbedPanes.get(tabbedPanes.size()-1));
			newSplitPane.setRightComponent(newTabbedPane);
			lowestNestedSplitPane.setRightComponent(newSplitPane);
		}
		
		tabbedPanes.add(newTabbedPane);
		this.revalidate();
		this.repaint();
		
	}
	
	/**
	 * Undoes the latest view split.
	 */
	public void unsplitView() {
		
		if (splitPanes.size() == 0)
			return;
		
		TabbedPane lastTabbedPane = tabbedPanes.get(tabbedPanes.size() - 1);
		TabbedPane secondToLastTabbedPane = tabbedPanes.get(tabbedPanes.size() - 2);
		JSplitPane lastSplitPane = splitPanes.get(splitPanes.size() - 1);
		
		for (Tab tab : lastTabbedPane.getTabs())
			secondToLastTabbedPane.addNewTab(tab);
		
		if(splitPanes.size() == 1) {
			this.removeAll();
			this.add(secondToLastTabbedPane);
			splitPanes.remove(lastSplitPane);
			tabbedPanes.remove(lastTabbedPane);
		}
		
		this.validate();
		this.repaint();
		
	}
	
	/**
	 * Adds a tab to the last tabbedPane
	 * @param tab
	 */
	public void addTab(Tab tab) {
		TabbedPane lastTabbedPane = tabbedPanes.get(tabbedPanes.size() - 1);
		lastTabbedPane.addNewTab(tab);
	}
	
	public void deleteAgentTab(Agent agent) {
		//TODO: Only works with last pane.
		TabbedPane lastTabbedPane = tabbedPanes.get(tabbedPanes.size() - 1);
		AgentTab myTab = lastTabbedPane.getTabOfAgent(agent);
		
		if (myTab == null)
			return;
		
		lastTabbedPane.removeTabCMAS(myTab);
	}
	
	
	/**
	 * searches through all tabbed panes in the manager
	 */
	public AgentTab getTabOfAgent(Agent agent) {
		
		AgentTab myAgentTab;
		
		for (TabbedPane myTabbedPane : tabbedPanes) {
			myAgentTab = myTabbedPane.getTabOfAgent(agent);
			if (myAgentTab != null) {
				return myAgentTab;
			}
		}
		
		return null;
	}
	
	/**
	 * searches through all tabbed panes in the manager
	 */
	public PlanTab getTabOfPlan(Plan plan) {
		
		PlanTab myPlanTab;
		
		for (TabbedPane myTabbedPane : tabbedPanes) {
			myPlanTab = myTabbedPane.getTabOfPlan(plan);
			if (myPlanTab != null) {
				return myPlanTab;
			}
		}
		
		return null;
	}
	
	/**
	 * Creates a new agent tabs, adds it to tabbedPane and returns
	 * a reference to the tab
	 * @param myAgent
	 * @return Reference to the added tab
	 */
	public AgentTab createAgentTab(Agent myAgent) {
		
		if (myAgent == null)
			return null;
				
		AgentTab agentTab = getTabOfAgent(myAgent);
		
		if (agentTab != null) {
			return agentTab;
		}
		
		TabbedPane myTabbedPane = tabbedPanes.get(tabbedPanes.size() - 1);
		agentTab = myTabbedPane.createAgentTab(myAgent);
		
		if (agentTab==null)
			return null;

		myTabbedPane.addNewTab(agentTab);
		
		return agentTab;
	}
	
	public AgentChartTab createAgentChartTab () {
		TabbedPane myTabbedPane = tabbedPanes.get(tabbedPanes.size() - 1);
		AgentChartTab myAgentTabChart = myTabbedPane.createAgentChartTab();
		
		if (myAgentTabChart == null)
			return null;
		
		myTabbedPane.addNewTab(myAgentTabChart);
		
		return myAgentTabChart;
	}
	
	public AgentChartTab createAgentChartTab (Agent agent) {
		TabbedPane myTabbedPane = tabbedPanes.get(tabbedPanes.size() - 1);
		AgentChartTab myAgentTabChart = myTabbedPane.createAgentChartTab(agent);
		
		if (myAgentTabChart == null)
			return null;
		
		myTabbedPane.addNewTab(myAgentTabChart);
		
		return myAgentTabChart;
	}
	
	public PlanTab createPlanTab(Plan plan) {

		if (plan == null)
			return null;
				
		PlanTab planTab = getTabOfPlan(plan);
		
		if (planTab != null)
			return planTab;
		
		TabbedPane myTabbedPane = tabbedPanes.get(tabbedPanes.size() - 1);
		planTab = myTabbedPane.createPlanTab(plan);
		
		if (planTab==null)
			return null;

		myTabbedPane.addNewTab(planTab);
		
		return planTab;
	}
	
	/*
	 * Returns true if the given agent already has a 
	 * tab open
	 */
	public boolean agentHasTab(Agent agent) {
	
		for (TabbedPane myTabbedPane : tabbedPanes) {
			if (myTabbedPane.agentHasTab(agent))
			 return true;
		}
		
		return false;
	}
	
}
