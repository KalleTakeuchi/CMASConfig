package cMASConfig;

import java.util.List;

public class PlanResourceForm extends ResourceForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4861700591120593867L;
	private Plan plan;

	
	public PlanResourceForm(Plan plan) {
		super();
		this.plan = plan;
		
		//Skills
		createNewSkill();
		
		//Variables		
		createNewVariable();

		createControls();
	}

	protected List<Skill> getAvailableSkills() {

		/*
		 * See function documentation.
		 */
		List<Interface> interfacesOnAllAgents = DataManager.getInterfacesAccessibleFromGoalPlan(plan.Name);
		
		/*
		 * Now we get all skills on all interfaces that share a name with those in the
		 * list we just created.
		 */
		List<Skill> skillsList = DataManager.getSkillsOnResourceInterfaces(interfacesOnAllAgents);
		
		return skillsList;
		
	}
	
	public Plan getPlan() {
		return plan;
	}
	
	protected List<Variable> getAvailableVariables() {
		
		/*
		 * See function documentation
		 */
		List<Interface> interfacesOnAllAgents = DataManager.getInterfacesAccessibleFromGoalPlan(plan.Name);
		
		/*
		 * Now we get all skills on all interfaces that share a name with those in the
		 * list we just created.
		 */
		List<Variable> variablesList = DataManager.getVariablesOnResourceInterfaces(interfacesOnAllAgents);
		
		return variablesList;
	}

	public PlanResourceForm get(int i) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
