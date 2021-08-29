package cMASConfig;

public enum BaseType {
	COMPONENT,
	RESOURCE;

	public static boolean belongsToEnum(String agentBaseType) {
		for (BaseType type : BaseType.values()) {
			if (type.toString().equals(agentBaseType))
				return true;
		}
		return false;
	}
}
