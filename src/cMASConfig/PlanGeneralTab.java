package cMASConfig;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class PlanGeneralTab extends Tab {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6763553060322887914L;
	private Plan plan;
	private List<PlanChangeListener> planChangeListeners;
	
	public PlanGeneralTab(Plan plan) {
		
		super();
		
		this.plan = plan;
		planChangeListeners = new ArrayList<PlanChangeListener>();
		this.setLayout(new MigLayout("fillx", "[shrink] [grow]", ""));
		createIDComponents();
		createNameComponents();
		createDescriptionComponents();
		
	}
	
	public void removePlanChangeListeners() {
		planChangeListeners = new ArrayList<PlanChangeListener>();
	}
	
	public void addPlanChangeListener(PlanChangeListener listener) {
		planChangeListeners.add(listener);
	}
	
	
	private void createIDComponents() {
		this.add(new JLabel("Entity ID: "), "align right");
		JTextField txtEntID = new JTextField();
		txtEntID.setText(plan.ID);
		txtEntID.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txt = (JTextField) e.getSource();
				updatePlanID(txt.getText());
				
			}
			
		});
		
		this.add(txtEntID, "grow, wrap");
	}
	
	private void createDescriptionComponents() {
		
		this.add(new JLabel("Description: "), "align right");
		JTextField txtDescription = new JTextField();
		
		if (plan.Description == null)
			plan.Description = "";
		
		txtDescription.setText(plan.Description);
		txtDescription.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txt = (JTextField) e.getSource();
				updatePlanDescription(txt.getText());
				
			}
			
		});
		
		this.add(txtDescription, "grow, wrap");
	}
	
	protected void updatePlanDescription(String text) {
		
		if (!plan.Description.equals(text))	
			plan.Description = text;
				
	}

	private void createNameComponents() {
		JTextField txtName = new JTextField();
		txtName.setText(plan.Name);
		txtName.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txt = (JTextField) e.getSource();
				updatePlanName(txt.getText());
			}
			
		});
		
		this.add(new JLabel("Name: "), "align right");
		this.add(txtName, "grow, wrap");
	}
	
	
	
	protected void updatePlanName(String text) {
		if (!plan.Name.equals(text)) {
			
			plan.Name = text;
			
			for (PlanChangeListener lstnr : planChangeListeners)
				lstnr.planNameChanged(plan);
		}
	}

	public void updatePlanID(String ID) {
		if (!plan.ID.equals(ID)) {
			
			String oldID = plan.ID;
			
			plan.ID = ID;
			
			for (PlanChangeListener lstnr : planChangeListeners) {
				lstnr.planIDChanged(oldID, plan);
			}
			
		}

	}
	
	public Plan getPlan() {
		return plan;
	}

	@Override
	void updateContent() {
		// TODO Auto-generated method stub
		
	}
	
}
