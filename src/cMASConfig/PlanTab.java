package cMASConfig;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 * A tab for editing the root entity Plan. Contains tabs for editing general entity attributes,
 * a tab with a text editor for writing the process plan and a graphical editing tab.
 * @author kalle
 *
 */
public class PlanTab extends Tab {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7161674238683342508L;
	public static final String GENERAL_TAB_TITLE = "General";
	public static final String TEXT_EDITOR_TITLE = "Text editor";
	public static final String FORMS_TAB_TITLE = "Graphical editor";

	private Plan plan;
	private JTabbedPane tabbedPane;
	private PlanGeneralTab generalTab;
	private ProcessPlanTextEditorTab txtEditTab;
	private ProcessPlanFormsTab formsTab;
	
	public PlanGeneralTab getGeneralTab() {
		return generalTab;
	}
	
	public PlanTab(Plan plan) {
		
		this.setLayout(new BorderLayout());
		tabbedPane = new JTabbedPane();
		this.add(tabbedPane, BorderLayout.CENTER);
		
		generalTab = new PlanGeneralTab(plan);
		generalTab.addPlanChangeListener(new PlanChangeListener() {
			
			@Override
			public void planNameChanged(Plan plan) {
				formsTab.updateContent();
				
			}
			
			@Override
			public void planIDChanged(String oldID, Plan plan) {
				//formsTab.updateContent();
				
			}

			@Override
			public void executionChanged() {
				// TODO Auto-generated method stub
				
			}
		});
		JScrollPane generalSP = new JScrollPane(generalTab);
		tabbedPane.add(generalSP, GENERAL_TAB_TITLE);
		
		txtEditTab = new ProcessPlanTextEditorTab(plan);
		JScrollPane txtEditSP = new JScrollPane(txtEditTab);
		tabbedPane.add(txtEditSP, TEXT_EDITOR_TITLE);
		
		formsTab = new ProcessPlanFormsTab(plan);
		txtEditTab.addChangeListeners(formsTab);
		formsTab.addChangeListeners(txtEditTab);
		JScrollPane formsTabSP = new JScrollPane(formsTab);
		tabbedPane.add(formsTabSP, FORMS_TAB_TITLE);
		
		this.plan = plan;
	}
	
	public Plan getPlan() {
		return plan;
	}
	
	public String toString() {
		
		if (plan.Name != null) {
			if (!plan.Name.equals(""))
				return "Plan: " + plan.Name;
		}
		
		return "Plan: " + plan.ID;
	}

	@Override
	void updateContent() {
		// TODO Auto-generated method stub
		
	}
}
