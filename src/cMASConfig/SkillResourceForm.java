package cMASConfig;

import java.util.List;

public class SkillResourceForm extends ResourceForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4151375878577204223L;
	private Skill skill;
	
	public SkillResourceForm(Skill skill) {
		super();
		this.skill = skill;
		
		//Skills
		createNewSkill();
		
		//Variables		
		createNewVariable();

		createControls();
	}

	public Skill getSkill() {
		return skill;
	}

	@Override
	protected List<Skill> getAvailableSkills() {
		
		
		/*
		 * Get parent agent
		 */
		Agent agent = DataManager.getAgentWithSkill(skill.ID);
		
		/*
		 * Get resource interfaces with the same name as those on this agent
		 */
		List<Interface> interfaces = DataManager.getResourceInterfacesWithNames(agent.Interfaces);
		
		/*
		 * Now we get all skills on all interfaces that share a name with those in the
		 * list we just created.
		 */
		List<Skill> skillsList = DataManager.getSkillsOnResourceInterfaces(interfaces);
		
		return skillsList;
		
	}

	@Override
	protected List<Variable> getAvailableVariables() {
		
		/*
		 * Get parent agent
		 */
		Agent agent = DataManager.getAgentWithSkill(skill.ID);
		
		
		/*
		 * Get resource interfaces with the same name as those on this agent
		 */
		List<Interface> interfaces = DataManager.getResourceInterfacesWithNames(agent.Interfaces);
		
		/*
		 * Now we get all skills on all interfaces that share a name with those in the
		 * list we just created.
		 */
		List<Variable> variablesList = DataManager.getVariablesOnResourceInterfaces(interfaces);
		
		return variablesList;
	}
	
}
