package cMASConfig;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;

/**
 * Forms representing resources in a process plan.
 * @author kalle
 *
 */
public abstract class ResourceForm extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1694361107288718036L;
	protected String resourceName;
	protected List<JComboBox<Skill>> skills;
	protected List<JComboBox<Variable>> variables;
	protected List<ResourceChangeListener> resourceChangeListeners = new ArrayList<ResourceChangeListener>();
	
	/**
	 * Reference to self for use in listener methods
	 */
	protected ResourceForm reference = this;
	
	public ResourceForm() {
		
		this.setLayout(new MigLayout("fill"));
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		resourceName = "";
		
		skills = new ArrayList<JComboBox<Skill>>();
		variables = new ArrayList<JComboBox<Variable>>();
		resourceChangeListeners = new ArrayList<ResourceChangeListener>();
		
	}
	
	/**
	 * Updates the items in the combo boxes
	 */
	public void repopulateComboboxes() {
		for (JComboBox<Skill> cmb : skills) {
			populateSkillsCombobox(cmb);
		}
		
		for (JComboBox<Variable> cmb : variables) {
			populateVariablesCombobox(cmb);
		}
		
		for (ResourceChangeListener listener : resourceChangeListeners) {
			listener.repaintRequest();
		}
	}
	
	/**
	 * Tries to set the skill as a new skill. 
	 */
	public void setNewSkill(Skill skill) {
		createNewSkill();
		JComboBox<Skill> cmb = skills.get(skills.size() - 1);
		populateSkillsCombobox(cmb);
		if (cmb.isEditable()) {
			cmb.setEditable(false);
			cmb.setSelectedItem(skill);
			cmb.setEditable(true);
		}
		else {
			cmb.setSelectedItem(skill);
		}
		
	}
	
	/**
	 * Clears all controls and creates them again.
	 * Requests repaint from parent
	 */
	protected void createControls() {
		this.removeAll();
		
		createNameControls();
		createDeleteButton();
		createVariablesControls();
		createSkillsControls();
		createBottomButtons();
		
		for (ResourceChangeListener listener : resourceChangeListeners) {
			listener.repaintRequest();
		}
		
	}
	
	protected void createBottomButtons() {
		JButton btnArrow = new JButton("←");
		btnArrow.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!(skillsOK() && variablesOK()))
					return;
				
				if (resourceName == null)
					return;
				
				if (resourceName.equals(""))
					return;
				
				for (ResourceChangeListener listener : resourceChangeListeners) {
					listener.createStatementRequest(reference);
				}
			}
			
		});
		
		this.add(btnArrow);
		
		JButton btnNewVariable = new JButton("New variable");
		btnNewVariable.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createNewVariable();
				createControls();
			}
		});
		
		this.add(btnNewVariable, "align right");
		
		JButton btnNewSkill = new JButton("New skill");
		btnNewSkill.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createNewSkill();
				createControls();
			}
		});
		
		this.add(btnNewSkill);
		
	}

	protected boolean variablesOK() {
		
		for (JComboBox<Variable> cmb : variables) {
			if (cmb.getItemCount() == 0)
				return false;
		}
		
		return true;
	}

	protected boolean skillsOK() {
		
		for (JComboBox<Skill> cmb : skills) {
			if (cmb.getItemCount() == 0)
				return false;
		}
		
		return true;
	}

	protected void createNewVariable() {
		JComboBox<Variable> cmbVariable = new JComboBox<Variable>();
		populateVariablesCombobox(cmbVariable);
		
		cmbVariable.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				for (ResourceChangeListener listener : resourceChangeListeners) {
					listener.variablesChanged(reference);
				}
			}
		});
		
		variables.add(cmbVariable);
	}
	
	protected void createNewSkill() {
		JComboBox<Skill> cmbSkill = new JComboBox<Skill>();
		populateSkillsCombobox(cmbSkill);
		
		cmbSkill.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				for (ResourceChangeListener listener : resourceChangeListeners) {
					listener.skillsChanged(reference);
				}
			}
		});
		
		
		skills.add(cmbSkill);	
	}

	private void createDeleteButton() {
		JButton btnClose = new JButton("-");
		
		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (ResourceChangeListener listener : resourceChangeListeners) {
					listener.deleteRequest(reference);
				}
				
			}
		});
		
		this.add(btnClose, "cell 2 0, align right, wrap");
		
	}

	protected void createVariablesControls() {

		this.add(new JLabel("Variables"), "cell 0 2, wrap");
		
		for(JComboBox<Variable> cmb : variables) {
			cmb.setPreferredSize(new Dimension(300, 0));
			this.add(cmb, "growx, span 2");
			JButton btnDeleteCmb = new JButton("-");
			btnDeleteCmb.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					variables.remove(cmb);
					createControls();					
				}
			});
			this.add(btnDeleteCmb, "align right, wrap");
		}
		
	}

	protected void createSkillsControls() {
		
		this.add(new JLabel("Skills"), "wrap");
		
		for(JComboBox<Skill> cmb : skills) {
			cmb.setPreferredSize(new Dimension(300, 0));
			this.add(cmb, "growx, span 2");
			JButton btnDeleteCmb = new JButton("-");
			btnDeleteCmb.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					skills.remove(cmb);
					createControls();					
				}
			});
			
			this.add(btnDeleteCmb, "align right, wrap");
		}
	}

	private void createNameControls() {
		this.add(new JLabel("Name"), "span 2");
		
		JTextField txtName = new JTextField();
		
		if (resourceName != null) {
			txtName.setText(resourceName);
		}
		else {
			resourceName = "";
			txtName.setText(resourceName);
		}

		txtName.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				updateName(((JTextField)e.getSource()).getText());
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.add(txtName, "cell 0 1, span 3, growx");
	}

	public void addResourceChangeListener(ResourceChangeListener listener) {
		resourceChangeListeners.add(listener);
	}
	
	public void setResourceName(String text) {
		resourceName = text;
	}
	
	public String getResourceName() {
		return resourceName;
	}
	
	protected void updateName(String text) {
		if (!resourceName.equals(text)) {
			resourceName = text;
			
			for (ResourceChangeListener listener : resourceChangeListeners) {
				listener.nameChanged(this);
			}
		}
	}
	
	/*
	 *TODO: initialize with previous config 
	 */
	
	public List<Skill> getSkills() {
		
		List<Skill> listToReturn = new ArrayList<Skill>();
		
		for (JComboBox<Skill> cmb : skills) {
			if ((Skill)cmb.getSelectedItem() != null)
				listToReturn.add((Skill)cmb.getSelectedItem());
		}
		
		return listToReturn;
	}
	
	public List<Variable> getVariables() {
		
		List<Variable> listToReturn = new ArrayList<Variable>();
		
		for (JComboBox<Variable> cmb : variables) {
			listToReturn.add((Variable)cmb.getSelectedItem());
		}
		
		return listToReturn;
	}
	
	/**
	 * Fills combo box with available skills
	 */
	protected void populateSkillsCombobox(JComboBox<Skill> cmb) {
		
		cmb.removeAllItems();
		
		for (Skill skill : getAvailableSkills())
			cmb.addItem(skill);
		
	}
	
	/*
	 * Fills combo box with available variables
	 */
	protected void populateVariablesCombobox(JComboBox<Variable> cmb) {
		
		cmb.removeAllItems();
		
		for (Variable var : getAvailableVariables())
			cmb.addItem(var);
		
	}
	
	/**
	 * Returns a list of skills possible to select in
	 * the current context. The skills have inputs specified
	 * in parenthesis.
	 */
	protected abstract List<Skill> getAvailableSkills();
	
	/**
	 * Returns a list of variables possible to select in
	 * the current context.
	 */
	protected abstract List<Variable> getAvailableVariables();
}
