package cMASConfig;

public interface AgentChartListener {
	/**
	 * Fired when the agent in the chart was set using the setAgent method.
	 */
	void agentWasSet(Agent newAgent);
}
