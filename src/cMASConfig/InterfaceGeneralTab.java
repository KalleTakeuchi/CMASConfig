package cMASConfig;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

public class InterfaceGeneralTab extends Tab {
	/**
	 * 
	 */
	private static final long serialVersionUID = -573859128356770786L;
	
	Interface intrfce;
	Agent agent;
	List<InterfaceChangeListener> interfaceChangeListeners;
	

	public InterfaceGeneralTab(Interface intrfce, Agent agent) {
		super();
		this.setLayout(new MigLayout("fillx",
				"[shrink] [grow]",
				""));
		this.intrfce = intrfce;
		this.agent = agent;
		interfaceChangeListeners = new ArrayList<InterfaceChangeListener>();
		
		createIDComponents();
		createNameComponents();
		createDescriptionComponents();
		createPositionComponents();
	}
	
	private void createIDComponents() {
		
		this.add(new JLabel("Entity ID: "), "align right");
		JTextField txtEntID = new JTextField();
		txtEntID.setText(intrfce.ID);
		txtEntID.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txt = (JTextField) e.getSource();
				updateInterfaceID(txt.getText());
				
			}
			
		});
		
		this.add(txtEntID, "grow, wrap");
		
	}
	
	public void addInterfaceChangeListener(InterfaceChangeListener listener) {
		interfaceChangeListeners.add(listener);
	}
	
	private void createPositionComponents() {
		
		VariableComponent varCmp = new VariableComponent(intrfce.Position);
		varCmp.deactivateChangeType();
		varCmp.deactivateRemove();
		this.add(new JLabel("Position:"), "align right, top");
		this.add(varCmp);
		/*
		JTextField txtPosition = varCmp.
		txtPosition.setDropMode(DropMode.INSERT);
		txtPosition.setTransferHandler(new NodeTransferHandler());
		txtPosition.setName("txtInterfacePosition");
		txtPosition.setToolTipText("Drag and drop a LOCATION from navigation tree ");
		txtPosition.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
		txtPosition.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent arg0) {
				updateAttachedTo();
				
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		this.add(txtPosition, "grow, wrap");*/
		
	}
	
	private void createDescriptionComponents() {
		
		this.add(new JLabel("Description: "), "align right");
		JTextField txtDescription = new JTextField();
		
		if (intrfce.Description == null)
			intrfce.Description = "";
		
		txtDescription.setText(intrfce.Description);
		txtDescription.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txt = (JTextField) e.getSource();
				updateInterfaceDescription(txt.getText());
				
			}
			
		});
		
		this.add(txtDescription, "grow, wrap");
	}
	
	private void updateInterfaceDescription(String text) {
		if (!intrfce.Description.equals(text)) {
			
			intrfce.Description = text;

		}
	}

	private void createNameComponents() {
		JTextField txtName = new JTextField();
		txtName.setText(intrfce.Name);
		txtName.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void focusLost(FocusEvent e) {
				JTextField txt = (JTextField) e.getSource();
				updateInterfaceName(txt.getText());
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
				updateInterfaceName(txtName.getText());
				
			}
			
		});
		
		this.add(txtName, "grow, wrap");
	}
	
	private void updateInterfaceName(String text) {
		if (!intrfce.Name.equals(text)) {
			
			intrfce.Name = text;
			
			for (InterfaceChangeListener lstnr : interfaceChangeListeners)
				lstnr.interfaceNameChanged(agent.ID, intrfce);
		}
	}
	
	private void updateInterfaceID(String text) {
		if (!intrfce.ID.equals(text)) {
			
			String oldID = intrfce.ID;
			
			intrfce.ID = text;

			for (InterfaceChangeListener lstnr : interfaceChangeListeners) {
				lstnr.interfaceIDChanged(oldID, agent.ID, intrfce);
			}
			
		}
	}

	@Override
	void updateContent() {
		// TODO Auto-generated method stub
		
	}
	

}
