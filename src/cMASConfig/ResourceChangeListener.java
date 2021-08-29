package cMASConfig;

public interface ResourceChangeListener {

	void deleteRequest(ResourceForm sender);
	void nameChanged(ResourceForm sender);
	void skillsChanged(ResourceForm sender);
	void variablesChanged(ResourceForm sender);
	void repaintRequest();
	void createStatementRequest(ResourceForm sender);
	
}
