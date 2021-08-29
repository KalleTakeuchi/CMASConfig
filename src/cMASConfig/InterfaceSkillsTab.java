package cMASConfig;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import cMASConfig.NavigationTreeNode.NodeType;
import net.miginfocom.swing.MigLayout;

/**
 * Contains components for editing skills
 * @author kalle
 *
 */
public class InterfaceSkillsTab extends Tab {

	private Interface intrfce;
	private List<SkillChangeListener> skillChangeListeners;
	private JList<Skill> skillsList;
	private JTabbedPane tabbedPane;
	
	public static final String GENERAL_TAB_TITLE = "General";
	public static final String TEXT_EDITOR_TITLE = "Text editor";
	public static final String FORMS_TAB_TITLE = "Graphical editor";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8052448958606773786L;

	
	public InterfaceSkillsTab(Interface intrfce) {
		this.intrfce = intrfce;
		this.setLayout(new BorderLayout());
		tabbedPane = new JTabbedPane();
		this.add(tabbedPane, BorderLayout.CENTER);
		createSideBar();
	}
	
	public void addSkillChangeListener(SkillChangeListener listener) {
		skillChangeListeners.add(listener);
	}
	
	public void removeSkillChangeListeners() {
		skillChangeListeners.clear();
	}
	
	private void createSideBar() {
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new MigLayout("fill"));
		myPanel.setBorder(new EmptyBorder(5,5,5,5));
		
		//Label
		myPanel.add(new JLabel("Skills"), "north, gapy 0px 5px");
		
		//List of skills
		Skill[] mySkills = new Skill[intrfce.Skills.size()];
		
		for (int i = 0; i < intrfce.Skills.size(); i++) {
			mySkills[i] = (intrfce.Skills.get(i));
		}
		
		
		skillsList = new JList<Skill>(mySkills);
		skillsList.setPreferredSize(new Dimension(100, 0));
		skillsList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		skillsList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
							
				Skill selection = (Skill) ((JList<Skill>) e.getSource()).getSelectedValue();				
				updateTabs(selection);
				
			}
		});
		
		myPanel.add(skillsList, "dock center, gapy 0px 5px, wrap");
		
		//South button panel
		JPanel southSubPanel = new JPanel();
		southSubPanel.setLayout(new FlowLayout());
		
		//Button to remove skill
		JButton btnRemoveSkill = new JButton("-");
		btnRemoveSkill.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeSelectedSkill();				
			}
		});
		
		southSubPanel.add(btnRemoveSkill);
		
		//Button to add skill
		JButton btnNewSkill= new JButton("+");
		btnNewSkill.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addNewSkill();				
			}
		});
		
		southSubPanel.add(btnNewSkill);
		
		myPanel.add(southSubPanel, "south, gapy 0px 5px");
		this.add(myPanel, BorderLayout.WEST);
		
	}
	
	private void removeSelectedSkill() {
		Skill selection = (Skill)skillsList.getSelectedValue();
		
		if (selection == null)
			return;
		
		Integer res = null;
		
		if (selection.Name == "") {
			res = JOptionPane.showConfirmDialog(this, 
					"Are you sure you want to delete the interface with ID " +
					selection.ID + "?", 
					"Delete interface",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
		}
		else {
			res = JOptionPane.showConfirmDialog(this, 
					"Are you sure you want to delete the interface with name " +
					selection.Name + "?", 
					"Delete interface",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
		}
		
		
		if (res.equals(JOptionPane.YES_OPTION)) {
			
			intrfce.Skills.remove(selection);
			TreeManager.removeNode(selection.ID);
			updateSkillsList();
			removeTabs();
			
			tabbedPane.validate();
			tabbedPane.repaint();
			
			for(SkillChangeListener listener : skillChangeListeners) {
				listener.skillRemoved(selection);
			}
		}
		
	}
	
	private void updateSkillsList() {
		
		Skill mySkills[] = new Skill[intrfce.Skills.size()];
		
		for (int i = 0; i < intrfce.Skills.size(); i++) {
			mySkills[i] = intrfce.Skills.get(i);
		}
		
		skillsList.setListData(mySkills);
		
	}
	
	private void removeTabs() {
		if (tabbedPane.getTabCount() > 0) {
			tabbedPane.remove(tabbedPane.indexOfTab(GENERAL_TAB_TITLE));
			tabbedPane.remove(tabbedPane.indexOfTab(TEXT_EDITOR_TITLE));
			tabbedPane.remove(tabbedPane.indexOfTab(FORMS_TAB_TITLE));
		}
	}
	
	private void addNewSkill() {
		
		Skill mySkill = new Skill(DataManager.createUniqueID());
		
		intrfce.Skills.add(mySkill);
		
		TreeManager.addNode(mySkill.ID, "", NavigationTreeNode.NodeType.SKILL, Optional.of(intrfce.ID));
		
		updateSkillsList();
		
	}
	
	private void updateTabs(Skill selectedSkill) {

		SkillGeneralTab generalTab = new SkillGeneralTab(selectedSkill);
		generalTab.addSkillChangeListener(new SkillChangeListener() {
			
			@Override
			public void skillRemoved(Skill skill) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void skillNameChanged(Skill skill) {
				updateSkillsList();
				TreeManager.changeNode(skill.ID, new NavigationTreeNode(skill.ID, skill.Name, NodeType.SKILL));
				
				for (SkillChangeListener listener : skillChangeListeners) {
					listener.skillNameChanged(skill);
				}
				
			}
			
			@Override
			public void skillIDChanged(String oldID, Skill skill) {
				updateSkillsList();
				TreeManager.changeNode(oldID, new NavigationTreeNode(skill.ID, skill.Name, NodeType.SKILL));
				
				for (SkillChangeListener listener : skillChangeListeners) {
					listener.skillIDChanged(oldID, skill);
				}
				
			}

			
			@Override
			public void executionChanged() {
				// TODO Auto-generated method stub
				
			}
		});
		ProcessPlanTextEditorTab editorTab = new ProcessPlanTextEditorTab(selectedSkill);
		ProcessPlanFormsTab formsTab = new ProcessPlanFormsTab(selectedSkill);
		editorTab.addChangeListeners(formsTab);
		formsTab.addChangeListeners(editorTab);
		removeTabs();
		tabbedPane.add(generalTab, GENERAL_TAB_TITLE);
		tabbedPane.add(editorTab, TEXT_EDITOR_TITLE);
		tabbedPane.add(formsTab, FORMS_TAB_TITLE);
	}
	
	
	@Override
	void updateContent() {
		// TODO Auto-generated method stub

	}

}
