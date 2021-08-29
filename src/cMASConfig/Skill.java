package cMASConfig;

import java.util.ArrayList;
import java.util.List;

public class Skill extends Entity {
	public String Execution;
	public Integer Cost;
	
	public Skill(String id) {
		InitializeEntity(id, "", "", EntityType.SKILL);
		Execution = "";
	}
	
	/**
	 * Returns the types and names of variables that are inputs to the process plan.
	 * Example: STRING aString;
	 * @return
	 */
	public List<String> getInputs() {
		
		List<String> listToReturn = new ArrayList<String>();
		
		if (Execution == null)
			return listToReturn;
		
		ExecutionParser parser = new ExecutionParser();
		
		List<String> variables = parser.getUsedVariables(Execution);

		//Get parent agent
		Agent myAgent = DataManager.getAgentWithSkill(this.ID);
		
		/*
		 * Find interface variables matching the names found.
		 * Variables must be writable
		 */
		for(String variableName : variables) {
			for(Interface intrfce : myAgent.Interfaces) {
				for (Variable var : intrfce.Variables) {
					if (var.Name.equals(variableName) && !var.ReadOnly) {
						listToReturn.add(var.TypeOfVariable.toString() + " " + var.Name);
					}
				}
			}
		}
		return listToReturn;
	}
	
	/**
	 * returns the variables that are inputs to the process plan
	 * @return
	 */
	public List<Variable> getInputVariables() {
		List<Variable> listToReturn = new ArrayList<Variable>();
		
		if (Execution == null)
			return listToReturn;
		
		ExecutionParser parser = new ExecutionParser();
		
		List<String> variables = parser.getUsedVariables(Execution);

		//Get parent agent
		Agent myAgent = DataManager.getAgentWithSkill(this.ID);
		
		/*
		 * Find interface variables matching the names found.
		 * Variables must be writable
		 */
		for(String variableName : variables) {
			for(Interface intrfce : myAgent.Interfaces) {
				for (Variable var : intrfce.Variables) {
					if (var.Name.equals(variableName) && !var.ReadOnly) {
						listToReturn.add(var);
					}
				}
			}
		}
		return listToReturn;
	}
	
	@Override
	public String toString() {
		
		String stringToReturn = Name + "(";
		List<String> inputs = getInputs();
		
		for (int i = 0; i < inputs.size(); i++) {
			if (i >0)
				stringToReturn = stringToReturn + ", ";
			
			stringToReturn = stringToReturn + inputs.get(i);
		}
		
		return stringToReturn + ")";
	}
}
	
