package cMASConfig;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

public class GoalGeneralTab extends Tab {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5841215281141446755L;

	Goal goal;
	Agent agent;
	List<GoalChangeListener> goalChangeListeners;
	//List<JCheckBox> checkboxes;
	JComboBox<Goal> cmbPrerequisite;

	public GoalGeneralTab(Goal goal, Agent agent) {
		super();
		this.setLayout(new MigLayout("fillx",
				"[shrink] [grow]",
				""));
		this.goal = goal;
		this.agent = agent;
		goalChangeListeners = new ArrayList<GoalChangeListener>();
		
		createIDComponents();
		createNameComponents();
		createDescriptionComponents();
		
		this.add(new JLabel("Precondition:"));
		cmbPrerequisite = new JComboBox<Goal>();
		cmbPrerequisite.removeAllItems();
		
		for (Goal loopGoal : agent.Goals) {
			if (!loopGoal.equals(goal))
				cmbPrerequisite.addItem(loopGoal);
		}
		
		this.add(cmbPrerequisite);
	}

	
	public void addGoalChangeListener(GoalChangeListener listener) {
		goalChangeListeners.add(listener);
	}
	
	public void removeGoalChangeListeners() {
		goalChangeListeners.clear();
	}

	private void createNameComponents() {
		JTextField txtName = new JTextField();
		txtName.setText(goal.Name);
		txtName.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txt = (JTextField) e.getSource();
				updateGoalName(txt.getText());
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
				updateGoalName(txtName.getText());
				
			}
			
		});
		
		this.add(txtName, "grow, wrap");
	}
	
	private void createDescriptionComponents() {
		
		this.add(new JLabel("Description: "), "align right");
		JTextField txtDescription = new JTextField();
		
		if (goal.Description == null)
			goal.Description = "";
		
		txtDescription.setText(goal.Description);
		txtDescription.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txt = (JTextField) e.getSource();
				updateGoalDescription(txt.getText());
				
			}
			
		});
		
		this.add(txtDescription, "grow, wrap");
	}
	
	private void updateGoalDescription(String text) {
		if (!goal.Description.equals(text)) {
			
			goal.Description = text;

		}
	}
	
	private void createIDComponents() {
		
		this.add(new JLabel("Entity ID: "), "align right");
		JTextField txtEntID = new JTextField();
		txtEntID.setText(goal.ID);
		txtEntID.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txt = (JTextField) e.getSource();
				updateGoalID(txt.getText());
				
			}
			
		});
		
		this.add(txtEntID, "grow, wrap");
		
	}
	
	private void updateGoalName(String text) {
		if (!goal.Name.equals(text)) {
			
			goal.Name = text;
			
			TreeManager.changeNode(goal.ID, new NavigationTreeNode(goal.ID, text, NavigationTreeNode.NodeType.GOAL));
			
			for (GoalChangeListener lstnr : goalChangeListeners)
				lstnr.goalNameChanged(goal);
		}
	}
	
	private void updateGoalID(String text) {
		if (!goal.ID.equals(text)) {
			
			String oldID = goal.ID;
			
			goal.ID = text;
			
			TreeManager.changeNode(oldID, new NavigationTreeNode(goal.ID, text, NavigationTreeNode.NodeType.GOAL));

			for (GoalChangeListener lstnr : goalChangeListeners) {
				lstnr.goalIDChanged(goal);
			}
			
		}
	}
	
	/*private void updateChecklist() {
		
		
		 * Old method with checkboxes
		 * Supported multiple PreConditions
		//remove checkboxes
		for (JCheckBox chk : checkboxes) {
			this.remove(chk);
		}
		
		checkboxes.clear();
		
		//add new checkboxes
		for (Goal aGoal : agent.Goals) {
			
			if (aGoal.equals(this))
				continue;
			
			String text;
			
			if (aGoal == null || aGoal.Name.equals("")) {
				text = aGoal.ID;
			}
			else {
				text = aGoal.Name;
			}
			
			JCheckBox chk = new JCheckBox(text);
			chk.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					if (chk.isSelected()) {
						Goal selectedGoal = getGoal(chk.getText());
						goal.PreCondition = selectedGoal.ID;
					}
					
				}
			});
			
			this.add(chk, "wrap");
			checkboxes.add(chk);
		}
		
		this.repaint();

		
	}*/
	
	public Goal getGoal(String nameOrID) {
		
		for (Goal goal : agent.Goals) {
			if (goal.Name.equals(nameOrID)
					|| goal.ID.equals(nameOrID))
				return goal;
		}
		
		return null;
		
	}
	
	@Override
	void updateContent() {
		cmbPrerequisite.removeAllItems();
		
		for (Goal loopGoal : agent.Goals) {
			cmbPrerequisite.addItem(loopGoal);
		}
	}

}
