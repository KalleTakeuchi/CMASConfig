package cMASConfig;

import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

/**
 * Use in process plans for Plan, not Skill.
 * @author kalle
 *
 */
public class SkillCallStatementFormPlan extends StatementForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8796964210908918296L;
	private List<Variable> inputs;
	private PlanResourceForm resource;
	private List<ResourceForm> planResources;
	private JComboBox<Skill> cmbSkills; 
	private List<JComboBox<String>> cmbInputs;
	
	public SkillCallStatementFormPlan(PlanResourceForm resource, List<ResourceForm> resources) {
		super(StatementForm.TypeOfStatementForm.SKILL_CALL);
		
		this.resource = resource;
		this.planResources = resources;
		createFormComponents(resource);
	}
	
	private void createFormComponents(ResourceForm resource) {
		
		this.removeAll();
		
		createTitleRow(resource.getName());
		
		this.add(new JLabel("Skill:"), "cell 0 1, align right");
		cmbSkills = new JComboBox<Skill>();
		
		for (Skill skill : resource.getSkills()) {
			cmbSkills.addItem(skill);
		}
		
		cmbSkills.setPreferredSize(new Dimension(300, 0));
		cmbSkills.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				Skill skill = (Skill)e.getItem();
				inputs = skill.getInputVariables();
				createFormComponents(resource);
				
				for (StatementFormListener listener : statementFormListeners) {
					listener.formChanged();
					listener.repaintRequest();
				}
			}
		});
		
		this.add(cmbSkills, "growx, wrap");
		
		inputs = ((Skill)cmbSkills.getSelectedItem()).getInputVariables();
		cmbInputs = new ArrayList<JComboBox<String>>();
		
		for (Variable input : inputs) {
			
			this.add(new JLabel(input + ":"), "align right");
			JComboBox<String> cmbInput = new JComboBox<String>();
			cmbInput.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent arg0) {
					for (StatementFormListener listener : statementFormListeners) {
						listener.formChanged();
					}
					
				}
			});
			
			for (String str : getValidVariables(input)) {
				cmbInput.addItem(str);
			}
			
			cmbInput.setPreferredSize(new Dimension(300, 0));
			cmbInputs.add(cmbInput);
			this.add(cmbInput, "growx, wrap");
		}
		
	}

	/**
	 * Returns variables valid to use as assignable values to the input
	 * such as agent variables, interface variables and 
	 * declared variables in process plan.
	 * @param input
	 * @return
	 */
	private List<String> getValidVariables(Variable input) {
		
		List<String> listToReturn = new ArrayList<String>();
		
		/*
		 * Look for matching variables belonging to all 
		 * agents with the goal in question. This goes for interface 
		 * variables as well.
		 * TODO: Add interface variables 
		 */
		List<Agent> agents = DataManager.getAgentsWithGoal(resource.getPlan().Name);
		
		for (Agent agent : agents) {
			for (Variable var : agent.Variables) {
				if (var.TypeOfVariable.equals(input.TypeOfVariable))
					listToReturn.add(var.Name);
			}
		}
		
		List<String> variablesToRemove = new ArrayList<String>();
		
		for (Agent agent : agents) {
			for (String var : listToReturn) {
				boolean variableExistsOnAgent = false;
				for (Variable agentVariable : agent.Variables) {
					if (var.equals(agentVariable.Name)) {
						variableExistsOnAgent = true;
						break;
					}
				}
				if (!variableExistsOnAgent)
					variablesToRemove.add(var);
			}
		}
		
		for (String var : variablesToRemove) {
			listToReturn.remove(var);
		}
		
		/*
		 * Find variables with matching type and name on 
		 * abstract interfaces (resources) declared in the process plan
		 */
		for (ResourceForm resource : planResources) {
			for (Variable var : resource.getVariables()) {
				if (var.TypeOfVariable.equals(input.TypeOfVariable))
					listToReturn.add(resource.getName() + "." + var.Name);
			}
		}
		
		/*
		 * TODO: add variables declared in plan
		 */
		
		return listToReturn;
	}
	
	public Skill getSkill() {
		
		if (cmbSkills == null)
			return null;
		if (cmbSkills.getSelectedItem() == null)
			return null;
		
		return (Skill)cmbSkills.getSelectedItem();
	}
	
	public String getInputValue(int index) {
		if (cmbInputs == null)
			return "";
		if (cmbInputs.size() < index + 1)
			return "";
		
		JComboBox<String> cmb = cmbInputs.get(index);
		
		if(cmb.getSelectedItem() == null)
			return "";
		
		return (String)cmb.getSelectedItem();
	}
	
	@Override
	public String toString() {
		String tempString = resource.resourceName;
		Skill skill = getSkill();
		
		if (skill == null)
			return "No skill selected";
		
		if (inputs.size() > 0) {
			tempString = tempString + 
					"." + skill.Name + "(\n";
			
			for (int i = 0; i < inputs.size(); i++) {
				
				if (i > 0)
					tempString = tempString + ",\n";
				
				tempString = tempString + "\t"
						+ inputs.get(i).Name 
						+ " := " + getInputValue(i);
			}
			
			tempString = tempString + "\n);";
		}
		else {
			tempString = tempString + 
					"." + skill.Name + "();";
		}
		
		return tempString;
	}
	
}
