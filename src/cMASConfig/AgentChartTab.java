package cMASConfig;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AgentChartTab extends Tab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3523302452315461226L;
	JComboBox<String> cmbAgents = new JComboBox<String>();
	AgentChartCanvas agentChart = new AgentChartCanvas();
	HashMap<String, Agent> agentMap = new HashMap<String, Agent>();
	
	public AgentChartTab() {
		initialize();
	}
	
	public AgentChartTab(Agent rootAgent) {
		initialize();
		
		if (rootAgent.Name == "") {
			if (cmbContainsItem(rootAgent.ID))
				cmbAgents.setSelectedItem(rootAgent.ID);
		}
		else {
			if (cmbContainsItem(rootAgent.Name))
				cmbAgents.setSelectedItem(rootAgent.Name);
		}
	}
	
	public AgentChartCanvas getAgentChart() {
		return agentChart;
	}
	
	private boolean cmbContainsItem(String item) {

		
		for (int i = 0; i < cmbAgents.getItemCount(); i++) {
			if (item == cmbAgents.getItemAt(i))
				return true;
		}
		
		return false;
		
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		JPanel tabTop = new JPanel();
		JPanel cmbAndLbl = new JPanel();
		/*JButton btnRefresh = new JButton("Refresh agent list");
		btnRefresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				populateAgentsList();								
			}
			
		});*/
		
		tabTop.setLayout(new BorderLayout());
		cmbAndLbl.setLayout(new FlowLayout());
		cmbAndLbl.add(new JLabel("Agent list"));
		cmbAndLbl.add(cmbAgents);
		tabTop.add(cmbAndLbl, BorderLayout.WEST);
		//tabTop.add(btnRefresh, BorderLayout.EAST);
		this.add(tabTop, BorderLayout.NORTH);
		this.add(new JScrollPane(agentChart), BorderLayout.CENTER);
		
		cmbAgents.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				setChartAgent((String)arg0.getItem());
			}
			
		});
		
		populateAgentsList();
	}
	
	
	/**
	 * The chart is shown with the agent that has the specified ID as the root.
	 * @param id
	 */
	private void setChartAgent(String id) {
		Agent myAgent = agentMap.get(id);
		//System.out.println("SetChartAgent: agent id is " + id);
		
		if (myAgent == null)
			return;
		
		//System.out.println("Agent was not null.");
		
		agentChart.resetVerticalOffset();
				
		agentChart.setAgent(myAgent, true);
		
	}
	
	private void populateAgentsList() {
		
		Enumeration<Agent> agents = DataManager.getAgents();
		
		cmbAgents.removeAllItems();
		
		while (agents.hasMoreElements()) {
			
			Agent anAgent = agents.nextElement();
			
			if (anAgent.Name == "") {
				agentMap.put(anAgent.ID, anAgent);
				cmbAgents.addItem(anAgent.ID);
			}
			else {
				agentMap.put(anAgent.Name, anAgent);
				cmbAgents.addItem(anAgent.Name);
			}
			
		}
		
	}

	@Override
	public String toString() {
		
		if (agentChart.getAgent() == null)
			return "Agent chart:";
		
		if (!agentChart.getAgent().Name.equals("")) {
			return "Agent chart: " + agentChart.getAgent().Name;
		}
		else {
			return "Agent chart: " + agentChart.getAgent().ID;
		}

	}

	@Override
	void updateContent() {
		
		//Save current selection so as not to change selected agent on the user.
		String ID;
		try {
			ID = agentChart.getAgent().ID;
		}
		catch (NullPointerException ex) {
			/*This exception may occur if no agents had been created when
			 * the chart was created.
			 */
			populateAgentsList();
			return;
		}
		
		
		agentChart.populateBoxList();
		populateAgentsList();
		
		//See if agent was deleted
		Agent myAgent = DataManager.getAgent(ID);
		
		if(myAgent != null) {
			
			if (myAgent.Name != null) {
				if (myAgent.Name != "") {
					cmbAgents.setSelectedItem(myAgent.Name);
					return;
				}
			}
			
			cmbAgents.setSelectedItem(myAgent.ID);
			
		}
		
	}
	
	
}
