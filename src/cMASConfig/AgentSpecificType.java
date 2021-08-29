package cMASConfig;

public enum AgentSpecificType {
	
	PART,
	MATERIAL;
	
	public static boolean belongsToEnum(String agentSpecificType) {
		for (AgentSpecificType type : AgentSpecificType.values()) {
			if (type.toString().equals(agentSpecificType))
				return true;
		}
		return false;
	}

}
