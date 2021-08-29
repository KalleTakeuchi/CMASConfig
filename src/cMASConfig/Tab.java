package cMASConfig;

import javax.swing.JPanel;

public abstract class Tab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1632379139670844409L;
	public EntityType typeOfEntity = EntityType.ENTITY;
	public String entityID = "";
	
	abstract void updateContent();
}
