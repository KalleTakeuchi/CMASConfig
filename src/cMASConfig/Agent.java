package cMASConfig;
import java.util.ArrayList;
import java.util.List;

public class Agent extends Entity {
	public List<Interface> Interfaces;
	public List<Variable> Variables;
	public BaseType AgentBaseType;
	public String AgentSpecificType;
	public List<Goal> Goals;
	public boolean Used;
	
	
	public Agent(String id) {
		InitializeEntity(id, "", "", EntityType.AGENT);
		initialize();
		ID = id;
	}
	
	/*
	 * Initializes agent properties so that null pointer exceptions
	 * are avoided.
	 */
	private void initialize() {
		Interfaces = new ArrayList<Interface>();
		Variables = new ArrayList<Variable>();
		AgentBaseType = BaseType.RESOURCE;
		AgentSpecificType = "";
		Goals = new ArrayList<Goal>();
		Used = false;
	}
}
