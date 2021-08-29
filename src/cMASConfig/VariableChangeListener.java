package cMASConfig;

public interface VariableChangeListener {
	
	void variableIDChanged(String oldID, Variable variable);
	void variableNameChanged(Variable variable);
	void variableRemoved(String variableID);
	void variableRemoveRequest(VariableComponent variableCmp);
	void variableAdded(Variable variable, Agent agent);
	void variableReinitialized(Variable variable);
}
