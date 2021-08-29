package cMASConfig;

public interface InterfaceChangeListener {
	
	void interfaceIDChanged(String oldID, String agentID, Interface intrfce);
	void interfaceNameChanged(String agentID, Interface intrfce);
	void interfaceRemoved(String agentID, Interface intrfceId);

}
