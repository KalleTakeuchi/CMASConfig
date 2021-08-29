package cMASConfig;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Optional;

import javax.swing.JButton;

import net.miginfocom.swing.MigLayout;

/**
 * Contains interface variables. Is very similar to the agent variables tab.
 * @author kalle
 *
 */
public class InterfaceVariablesTab extends Tab {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7332128749718603593L;
	
	private ArrayList<VariableChangeListener> variableChangeListeners;
	private Interface intrfce;
	
	public InterfaceVariablesTab(Interface intrfce) {
		
		this.intrfce = intrfce;
		
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
		
		for (Variable myVar : intrfce.Variables) {
			VariableComponent varCom = createVariableComponent(myVar);
			this.add(varCom, "grow, span, wrap");
		}
			
	}
	
	/**
	 * Since Variable is an abstract class, a Value is instantiated.
	 */
	private void addNewVariable() {
		Variable newVar = new Value(DataManager.createUniqueID());
		
		this.intrfce.Variables.add(newVar);
		VariableComponent varCom = createVariableComponent(newVar);
		this.add(varCom, "grow, span, wrap");
		this.revalidate();
		
		TreeManager.addNode(newVar.ID, newVar.Name, NavigationTreeNode.NodeType.VARIABLE, Optional.of(intrfce.ID));
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
		
		if (!intrfce.Variables.contains(variable))
			return;
		
		String varID = variable.ID;
		
		
		intrfce.Variables.remove(variable);
		
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
		
		for (Variable aVariable : intrfce.Variables) {
			if (aVariable.ID.equals(variable.ID)) {
				intrfce.Variables.remove(aVariable);
				break;
			}
		}
		
		intrfce.Variables.add(variable);
		
	}

	@Override
	void updateContent() {
		// TODO Auto-generated method stub
		
	}

}
