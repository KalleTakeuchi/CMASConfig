package cMASConfig;

public interface AgentChangeListener {
	void agentIDChanged(String oldID, Agent agent);
	void agentNameChanged(Agent agent);
	void agentRemoved(String agentID);
}
