package cMASConfig;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;

import net.miginfocom.swing.MigLayout;

public class GoalParametersTab extends Tab {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5330942353460516139L;
	private Goal goal;
	private List<VariableChangeListener> variableChangeListeners;
	
	public GoalParametersTab(Goal goal) {
		
		this.goal = goal;
		
		this.variableChangeListeners = new ArrayList<VariableChangeListener>();
		
		this.setLayout(new MigLayout("", "[grow] [shrink]", ""));
		
		/*
		 * Button to create new parameter
		 */
		JButton btnNewVariable = new JButton("+");
		
		btnNewVariable.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				addNewVariable();
				
			}
		});
		
		this.add(btnNewVariable, "cell 1 0, wrap");
		
		for (Variable myVar : goal.Parameters) {
			VariableComponent varCom = createVariableComponent(myVar);
			this.add(varCom, "grow, span, wrap");
		}
			
	}
	
	/**
	 * Since Variable is an abstract class, a Value is instantiated.
	 */
	private void addNewVariable() {
		Variable newVar = new Value(DataManager.createUniqueID());
		
		this.goal.Parameters.add(newVar);
		VariableComponent varCom = createVariableComponent(newVar);
		this.add(varCom, "grow, span, wrap");
		this.revalidate();
		
		TreeManager.addNode(newVar.ID, newVar.Name, NavigationTreeNode.NodeType.VARIABLE, Optional.of(goal.ID));
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
	
	private void removeVariable(Variable variable) {
		
		if (!goal.Parameters.contains(variable))
			return;
		
		String varID = variable.ID;
		
		
		goal.Parameters.remove(variable);
		
		for (VariableChangeListener listener : variableChangeListeners)
			listener.variableRemoved(varID);
		
	}
	
	public void removeComponent(VariableComponent variableCmp) {
		this.remove(variableCmp);
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Find the old instance of the variable and replaces it with the new one.
	 * @param variable The new instance of the variable.
	 */
	protected void variableReinitializedHandler(Variable variable) {
		
		for (Variable aVariable : goal.Parameters) {
			if (aVariable.ID.equals(variable.ID)) {
				goal.Parameters.remove(aVariable);
				break;
			}
		}
		
		goal.Parameters.add(variable);
		
	}
	
	@Override
	void updateContent() {
		// TODO Auto-generated method stub

	}

}
