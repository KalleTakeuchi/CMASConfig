/**
 * This tab itself is divided into subtabs
 * for Interfaces, Goals, General settings and so on.
 * Thus it contains a tabbed pane of its own
 */
package cMASConfig;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * @author kalle
 *
 */
public class AgentTab extends Tab {
	
	public static final String GENERAL_TAB_TITLE = "General";
	public static final String VARIABLES_TAB_TITLE = "Variables";
	public static final String INTERFACES_TAB_TITLE = "Interfaces";
	public static final String GOALS_TAB_TITLE = "Goals";
	
	private AgentGeneralTab generalTab;
	private AgentVariablesTab variablesTab;
	private AgentInterfacesTab interfacesTab;
	private AgentGoalsTab goalsTab;
	
	public AgentGeneralTab getGeneralTab() {
		return generalTab;
	}
	
	public AgentVariablesTab getVariablesTab() {
		return variablesTab;
	}
	
	public AgentInterfacesTab getInterfacesTab() {
		return interfacesTab;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7888861227000750252L;
	private JTabbedPane tabbedPane;
	private Agent agent;
	
	public AgentTab(Agent agent) {
	
		this.agent = agent;
		this.generalTab = new AgentGeneralTab(agent);
		this.interfacesTab = new AgentInterfacesTab(agent);
		this.variablesTab = new AgentVariablesTab(agent);
		this.goalsTab = new AgentGoalsTab(agent);
		
		this.setLayout(new BorderLayout());
		tabbedPane = new JTabbedPane();
		this.add(tabbedPane, BorderLayout.CENTER);
		tabbedPane.add(new JScrollPane(generalTab), GENERAL_TAB_TITLE);
		tabbedPane.add(new JScrollPane(variablesTab), VARIABLES_TAB_TITLE);
		tabbedPane.add(new JScrollPane(interfacesTab), INTERFACES_TAB_TITLE);
		tabbedPane.add(new JScrollPane(goalsTab), GOALS_TAB_TITLE);
		
	}
	
	public void selectGeneralTab() {
		tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(GENERAL_TAB_TITLE));
	}
	
	public void selectVariablesTab() {
		tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(VARIABLES_TAB_TITLE));
	}
	
	public void selectInterfacesTab() {
		tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(INTERFACES_TAB_TITLE));
	}
	
	public void selectGoalsTab() {
		tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(GOALS_TAB_TITLE));
	}
	
	public Agent getAgent() {
		return agent;
	}
	
	@Override
	public String toString() {
		
		if (agent.Name != null) {
			if (!agent.Name.equals(""))
				return "Agent: " + agent.Name;
		}
		
		return "Agent: " + agent.ID;
	}

	@Override
	void updateContent() {
		generalTab.updateContent();
		variablesTab.updateContent();
		interfacesTab.updateContent();
	}
}
