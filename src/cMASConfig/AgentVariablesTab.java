package cMASConfig;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;

public class AgentVariablesTab extends Tab {
	
	private ArrayList<VariableChangeListener> variableChangeListeners;
	private Agent agent;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3183922832529528905L;

	public void removeVariableChangeListeners() {
		variableChangeListeners = new ArrayList<VariableChangeListener>();
	}
	
	public AgentVariablesTab(Agent agent) {
		
		this.agent = agent;
		
		this.variableChangeListeners = new ArrayList<VariableChangeListener>();
		
		this.setLayout(new MigLayout("", "[grow] [shrink]", ""));
		
		/*
		 * Button to create new variable
		 */
		JButton btnNewVariable = new JButton("+");
		
		btnNewVariable.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				addNewVariable();
				
			}
		});
		
		this.add(btnNewVariable, "cell 1 0, wrap");
		
		for (Variable myVar : agent.Variables) {
			VariableComponent varCom = createVariableComponent(myVar);
			this.add(varCom, "grow, span, wrap");
		}
			
	}
	
	private VariableComponent createVariableComponent(Variable variable) {
		VariableComponent varCom = new VariableComponent(variable);
		varCom.addVariableChangeListener(new VariableChangeListener() {
			
			@Override
			public void variableRemoved(String variableID) {
				
			}
			
			@Override
			public void variableNameChanged(Variable variable) {

				
			}
			
			@Override
			public void variableIDChanged(String oldID, Variable variable) {

				for (VariableChangeListener listener : variableChangeListeners) {
					listener.variableIDChanged(oldID, variable);
				}
				
			}

			@Override
			public void variableAdded(Variable variable, Agent agent) {
				
			}

			@Override
			public void variableRemoveRequest(VariableComponent variableCmp) {
				
				removeVariable(variableCmp.getVariable());
				removeComponent(variableCmp);
				
				TreeManager.removeNode(variableCmp.getVariable().ID);
				
				for (VariableChangeListener listener : variableChangeListeners) {
					listener.variableRemoved(variableCmp.getVariable().ID);
				}
				
			}

			@Override
			public void variableReinitialized(Variable variable) {
				
				variableReinitializedHandler(variable);
				
			}

		});
		
		return varCom;
	}
	
	/**
	 * Find the old instance of the variable and replaces it with the new one.
	 * @param variable The new instance of the variable.
	 */
	protected void variableReinitializedHandler(Variable variable) {
		
		for (Variable aVariable : agent.Variables) {
			if (aVariable.ID.equals(variable.ID)) {
				agent.Variables.remove(aVariable);
				break;
			}
		}
		
		agent.Variables.add(variable);
		
	}

	public void removeComponent(VariableComponent variableCmp) {
		this.remove(variableCmp);
		this.revalidate();
		this.repaint();
	}
	
	private void removeVariable(Variable variable) {
		
		if (!agent.Variables.contains(variable))
			return;
		
		String varID = variable.ID;
		
		
		agent.Variables.remove(variable);
		
		for (VariableChangeListener listener : variableChangeListeners)
			listener.variableRemoved(varID);
		
	}
	
	/**
	 * Since Variable is an abstract class, a Value is instantiated.
	 */
	private void addNewVariable() {
		Variable newVar = new Value(DataManager.createUniqueID());
		
		this.agent.Variables.add(newVar);
		VariableComponent varCom = createVariableComponent(newVar);
		this.add(varCom, "grow, span, wrap");
		this.revalidate();
		
		TreeManager.addNode(newVar.ID, newVar.Name, NavigationTreeNode.NodeType.VARIABLE, Optional.of(agent.ID));
		
		for (VariableChangeListener listener : variableChangeListeners) {
			listener.variableAdded(newVar, agent);
		}
	}
	
	@Override
	public String getName() {
		return "Variables";
	}
	
	public void addVariableChangeListener(VariableChangeListener listener) {
		if (listener == null)
			return;
		variableChangeListeners.add(listener);
	}

	@Override
	void updateContent() {
		// TODO Auto-generated method stub
		
	}
}
