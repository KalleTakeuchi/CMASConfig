package cMASConfig;

import java.util.ArrayList;
import java.util.List;

public class Interface extends Entity {
	public List<Skill> Skills;
	public List<Variable> Variables;
	public Boolean Booked;
	public Location Position;
	
	public Interface(String id) {
		InitializeEntity(id, "", "", EntityType.INTERFACE);
		Skills = new ArrayList<Skill>();
		Variables = new ArrayList<Variable>();
		Position = new Location(DataManager.createUniqueID());
	}
}
