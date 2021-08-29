package cMASConfig;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class SkillGeneralTab extends Tab {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5210793637643556170L;
	Skill skill;
	List<SkillChangeListener> skillChangelisteners;
	
	public SkillGeneralTab(Skill skill) {
		this.skill = skill;
		
		this.setLayout(new MigLayout("fillx",
				"[shrink] [grow]",
				""));
		
		skillChangelisteners = new ArrayList<SkillChangeListener>();
		createIDComponents();
		createNameComponents();
		createDescriptionComponents();
	}
	
	public void addSkillChangeListener(SkillChangeListener listener) {
		skillChangelisteners.add(listener);
	}
	
	private void createIDComponents() {
		
		this.add(new JLabel("Entity ID: "), "align right");
		JTextField txtEntID = new JTextField();
		txtEntID.setText(skill.ID);
		txtEntID.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txt = (JTextField) e.getSource();
				updateSkillID(txt.getText());
				
			}
			
		});
		
		this.add(txtEntID, "grow, wrap");
		
	}
	
	protected void updateSkillID(String text) {
		if (!skill.ID.equals(text)) {
			String oldId = skill.ID;
			skill.ID = text;
			
			for (SkillChangeListener listener : skillChangelisteners) {
				listener.skillIDChanged(oldId, skill);
			}
		}
	}
	
	private void createNameComponents() {
		JTextField txtName = new JTextField();
		txtName.setText(skill.Name);
		txtName.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txt = (JTextField) e.getSource();
				updateSkillName(txt.getText());
			}
			
		});
		
		this.add(new JLabel("Name: "), "align right");
		txtName.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent arg0) {
				updateSkillName(txtName.getText());
				
			}
			
		});
		
		this.add(txtName, "grow, wrap");
	}

	private void updateSkillName(String text) {
		if (!skill.Name.equals(text)) {
			
			skill.Name = text;
			
			for (SkillChangeListener lstnr : skillChangelisteners)
				lstnr.skillNameChanged(skill);
		}
	}
	
	private void createDescriptionComponents() {
		
		this.add(new JLabel("Description: "), "align right");
		JTextField txtDescription = new JTextField();
		
		txtDescription.setText(skill.Description);
		txtDescription.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txt = (JTextField) e.getSource();
				updateSkillDescription(txt.getText());
				
			}
			
		});
		
		this.add(txtDescription, "grow, wrap");
	}
	
	private void updateSkillDescription(String text) {
		if (!skill.Description.equals(text)) {
			
			skill.Description = text;

		}
	}
	
	@Override
	void updateContent() {
		// TODO Auto-generated method stub
		
	}

}
