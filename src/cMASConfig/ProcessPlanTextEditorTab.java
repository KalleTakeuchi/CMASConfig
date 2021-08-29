package cMASConfig;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

public class ProcessPlanTextEditorTab extends Tab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5398748853284472771L;
	private Plan plan = null;
	private Skill skill = null;
	private LineNumberTextArea textEditor;
	private List<SkillChangeListener> skillChangeListeners = new ArrayList<SkillChangeListener>();
	private List<PlanChangeListener> planChangeListeners = new ArrayList<PlanChangeListener>();
	
	/**
	 * The editor is for a Plan process plan
	 * @param plan
	 */
	public ProcessPlanTextEditorTab(Plan plan) {
		this.plan = plan;
		initialize();
	}
	
	public void addSkillChangeListener(SkillChangeListener listener) {
		skillChangeListeners.add(listener);		
	}
	
	public void addPlanChangeListener(PlanChangeListener listener) {
		planChangeListeners.add(listener);	
	}
	
	/**
	 * Adds listeners to the forms tab so that
	 * this tab can react to changes over there.
	 */
	public void addChangeListeners(ProcessPlanFormsTab formsTab) {
		formsTab.addSkillChangeListener(new SkillChangeListener() {
			
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
				
				updateTextEditor();
				
			}
		});
		
		formsTab.addPlanChangeListener(new PlanChangeListener() {
			
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
				
				updateTextEditor();
				
			}
		});
	}
	
	/**
	 * The editor is for a Skill process plan
	 * @param skill
	 */
	public ProcessPlanTextEditorTab(Skill skill) {
		this.skill = skill;
		initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(new JLabel("Process plan "), BorderLayout.NORTH);
		textEditor = new LineNumberTextArea();
		textEditor.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				updateExecution();
				
				for (PlanChangeListener listener : planChangeListeners) {
					listener.executionChanged();
				}
				
				for (SkillChangeListener listener : skillChangeListeners) {
					listener.executionChanged();
				}
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		this.add(textEditor, BorderLayout.CENTER);
		
		updateTextEditor();
	}
	
	private void updateTextEditor() {
		if (skill == null) {
			if (plan == null)
				return;
			textEditor.setText(plan.Execution);
		}
		
		if (plan == null) {
			if (skill == null)
				return;
			textEditor.setText(skill.Execution);
		}
		
	}
	
	/**
	 * Updates the Execution attribute of the plan or skill
	 */
	private void updateExecution() {
		if (skill == null) {
			if (plan == null)
				return;
			plan.Execution = textEditor.getText();
		}
		
		if (plan == null) {
			if (skill == null)
				return;
			skill.Execution = textEditor.getText();
		}
	}
	
	@Override
	void updateContent() {
		updateTextEditor();

	}

}
