package cMASConfig;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import net.miginfocom.swing.MigLayout;

/**
 * A tab that allows for creation of process plans using forms.
 * @author kalle
 *
 */
public class ProcessPlanFormsTab extends Tab {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5211131944289020943L;
	private Plan plan;
	private Skill skill;
	private List<ResourceForm> resources;
	private List<StatementForm> statements;
	private JPanel resourcePanel;
	private JPanel statementPanel;
	private List<SkillChangeListener> skillChangeListeners = new ArrayList<SkillChangeListener>();
	private List<PlanChangeListener> planChangeListeners = new ArrayList<PlanChangeListener>();

	public ProcessPlanFormsTab(Plan plan) {
		
		this.plan = plan;
		statements = new ArrayList<StatementForm>();
		resources = new ArrayList<ResourceForm>();
		this.setLayout(new BorderLayout());
		createResourceSidebar();
		createStatementArea();
		
	}
	
	public ProcessPlanFormsTab(Skill skill) {
		this.skill = skill;
		statements = new ArrayList<StatementForm>();
		resources = new ArrayList<ResourceForm>();
		this.setLayout(new BorderLayout());
		createResourceSidebar();
		createStatementArea();
	}
	
	public void addSkillChangeListener(SkillChangeListener listener) {
		skillChangeListeners.add(listener);
	}
	
	public void addPlanChangeListener(PlanChangeListener listener) {
		planChangeListeners.add(listener);
	}
	
	private void createStatementArea() {

		statementPanel = new JPanel();
		statementPanel.setLayout(new MigLayout("fillx"));
		
		JPanel statementArea = new JPanel();
		statementArea.setLayout(new BorderLayout());
		statementArea.add(new JLabel("Process plan"), BorderLayout.NORTH);
		
		
		JPanel buttonRow = new JPanel();
		buttonRow.setLayout(new MigLayout());
		
		JButton btnNewVariable = new JButton("New variable");
		btnNewVariable.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		buttonRow.add(btnNewVariable);
		
		JButton btnNewAssignment = new JButton("New assignment");
		btnNewAssignment.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		buttonRow.add(btnNewAssignment);
	
		JButton btnNewIf= new JButton("New if");
		btnNewIf.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		buttonRow.add(btnNewIf);
		
		JButton btnNewFor= new JButton("New for");
		btnNewFor.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		buttonRow.add(btnNewFor);
		
		JButton btnNewWhile= new JButton("New while");
		btnNewWhile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		buttonRow.add(btnNewWhile);
		
		statementArea.add(buttonRow, BorderLayout.SOUTH);
		
		statementArea.add(statementPanel, BorderLayout.CENTER);
		
		JScrollPane statementAreaSP = new JScrollPane(statementArea);
		this.add(statementAreaSP, BorderLayout.CENTER);
	}

	private void createResourceSidebar() {
		
		resourcePanel = new JPanel();
		resourcePanel.setLayout(new MigLayout());
		
		JPanel sidebar = new JPanel();
		sidebar.setLayout(new BorderLayout());
		sidebar.add(new JLabel("Resources"), BorderLayout.NORTH);
		
		if (belongsToPlan()) {
			PlanResourceForm prf = createPlanResourceForm();
			resourcePanel.add(prf, "wrap");
			resources.add(prf);
		}
		else if (belongsToSkill()) {
			SkillResourceForm srf = createSkillResourceForm();
			resourcePanel.add(srf, "wrap");
			resources.add(srf);
		}
		else {
			return;
		}

		
		JScrollPane resourcePanelSP = new JScrollPane(resourcePanel);
		
		sidebar.add(resourcePanelSP, BorderLayout.CENTER);
		JButton btnNewResource = new JButton("New resource");
		btnNewResource.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (belongsToPlan()) {
					PlanResourceForm prf = createPlanResourceForm();
					resources.add(prf);
					resourcePanel.add(prf, "wrap");
				}
				else if (belongsToSkill()) {
					SkillResourceForm srf = createSkillResourceForm();
					resourcePanel.add(srf, "wrap");
					resources.add(srf);
				}
				else {
					return;
				}

				validate();
				repaint();
				
			}
		});
		
		sidebar.add(btnNewResource, BorderLayout.SOUTH);
		this.add(sidebar, BorderLayout.EAST);
		
	}
	
	private SkillResourceForm createSkillResourceForm() {
		SkillResourceForm srf = new SkillResourceForm(skill);
		srf.addResourceChangeListener(new ResourceChangeListener() {
			
			@Override
			public void variablesChanged(ResourceForm sender) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void skillsChanged(ResourceForm sender) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void repaintRequest() {
				validate();
				repaint();
				
			}
			
			@Override
			public void nameChanged(ResourceForm sender) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void deleteRequest(ResourceForm sender) {
				resources.remove(sender);
				resourcePanel.remove(sender);
				validate();
				repaint();
				
			}
			
			@Override
			public void createStatementRequest(ResourceForm sender) {
				if (sender.getSkills().size() == 0)
					return;
				
				createSkillCallFormSkill((SkillResourceForm)sender);
				generateExecutionString();
				
			}
		});
		
		return srf;
	}

	private boolean belongsToSkill() {
		return (skill != null && plan == null);
	}
	
	private boolean belongsToPlan() {
		return (plan != null && skill == null);
	}
	
	private PlanResourceForm createPlanResourceForm() {
		PlanResourceForm prf = new PlanResourceForm(plan);
		prf.addResourceChangeListener(new ResourceChangeListener() {
			
			@Override
			public void variablesChanged(ResourceForm sender) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void skillsChanged(ResourceForm sender) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void nameChanged(ResourceForm sender) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void deleteRequest(ResourceForm sender) {
				resourcePanel.remove(sender);
				resources.remove(sender);
				validate();
				repaint();
			}

			@Override
			public void repaintRequest() {
				validate();
				repaint();
			}

			@Override
			public void createStatementRequest(ResourceForm sender) {
				
				if (sender.getSkills().size() == 0)
					return;
				
				createSkillCallFormPlan((PlanResourceForm)sender);
			}

		});
		
		return prf;
	}
	
	private void createSkillCallFormSkill(SkillResourceForm resource) {
		if (resource.getSkills().size() == 0)
			return;
		
		SkillCallStatementFormSkill skillCallForm = new SkillCallStatementFormSkill(resource, resources);
		skillCallForm.addStatementFormListener(new StatementFormListener() {
			
			@Override
			public void repaintRequest() {
				generateExecutionString();
				validate();
				repaint();
								
			}
			
			@Override
			public void moveUp(StatementForm sender) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void moveDown(StatementForm sender) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void deleteRequest(StatementForm sender) {
				statements.remove(sender);
				statementPanel.remove(sender);
				generateExecutionString();
				validate();
				repaint();
			}

			@Override
			public void formChanged() {
				generateExecutionString();
				
			}
		});
		
		statements.add(skillCallForm);
		statementPanel.add(skillCallForm, "growx, wrap");
		validate();
		repaint();
		
	}

	/**
	 * Adds listeners to the text editor tab so that
	 * this tab can react to changes over there.
	 */
	public void addChangeListeners(ProcessPlanTextEditorTab textTab) {
		textTab.addSkillChangeListener(new SkillChangeListener() {
			
			@Override
			public void skillRemoved(Skill skill) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void skillNameChanged(Skill skill) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void skillIDChanged(String oldID, Skill skill) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void executionChanged() {
				if (skill == null)
					return;
				
				updateGraphicalEditor();
				
			}
		});
		
		textTab.addPlanChangeListener(new PlanChangeListener() {
			
			@Override
			public void planNameChanged(Plan plan) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void planIDChanged(String oldID, Plan plan) {
				// TODO Auto-generated method stub
				
			}
			
			
			@Override
			public void executionChanged() {
				if (plan == null)
					return;
				
				updateGraphicalEditor();
				
			}
		});
	}
	
	protected void updateGraphicalEditor() {
		if (plan == null) {
			if (skill == null)
				return;
		}
		else {
			if (skill != null)
				return;
			ExecutionParser parser = new ExecutionParser();
			
			List<PlanResourceForm> resources = parser.getPlanResources(plan);
			
			for (PlanResourceForm resource : resources) {
				
				boolean resourceSkillCallsOK = true;
				
				for (JComboBox<Skill> cmb : resource.skills) {
					int itemCount = cmb.getItemCount();
					if (itemCount == 0) {
						resourceSkillCallsOK = false;
					}
				}
				
				if (!resourceSkillCallsOK) {
					JOptionPane.showMessageDialog(this, "Resource " + 
							resource.getResourceName() + 
							" is calling one or more skills that cannot be called."
							);
				}
				
				//TODO
			}
			
		}
		
	}

	private void createSkillCallFormPlan(PlanResourceForm resource) {
		if (resource.getSkills().size() == 0)
			return;
		
		SkillCallStatementFormPlan skillCallForm  = new SkillCallStatementFormPlan(resource, resources);
		skillCallForm.addStatementFormListener(new StatementFormListener() {
			
			@Override
			public void repaintRequest() {
				validate();
				repaint();
				
			}
			
			@Override
			public void deleteRequest(StatementForm sender) {
				statementPanel.remove(sender);
				statements.remove(sender);
				generateExecutionString();
				validate();
				repaint();
			}
			
			@Override
			public void moveUp(StatementForm sender) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void moveDown(StatementForm sender) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void formChanged() {
				generateExecutionString();
				
			}
		});
		
		statements.add(skillCallForm);
		statementPanel.add(skillCallForm, "growx, wrap");
		generateExecutionString();
		validate();
		repaint();
	}
	
	/**
	 * Generates the Pascal program code corresponding to
	 * the process plan configured here.
	 */
	public void generateExecutionString() {
		String tempExecution = "";
		
		for (StatementForm statement : statements) {
			tempExecution = tempExecution + statement.toString() + "\n\n";
		}
		
		if (plan == null) {
			if (skill == null)
				return;
			skill.Execution = tempExecution;
			
			for (SkillChangeListener listener : skillChangeListeners) {
				listener.executionChanged();
			}
		}
		else {
			if (skill != null)
				return;
			plan.Execution = tempExecution;
			
			for (PlanChangeListener listener : planChangeListeners) {
				listener.executionChanged();
			}
		}
	}
	
	@Override
	void updateContent() {
		
		for (ResourceForm resource : resources) {
			resource.repopulateComboboxes();
		}
		
		for (StatementForm statement : statements) {
			//TODO: Check statements
		}
 
	}

}
