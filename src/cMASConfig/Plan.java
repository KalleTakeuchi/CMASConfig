package cMASConfig;

public class Plan extends Entity {
	public String Execution;
	

	public Plan(String ID) {
		InitializeEntity(ID, "", "", EntityType.PROCESSPLAN);
		Execution = "";
	}
}
