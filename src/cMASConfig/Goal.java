package cMASConfig;

import java.util.ArrayList;
import java.util.List;

public class Goal extends Entity {
	public String PreCondition;
	public List<Variable> Parameters;
	
	public Goal(String id) {
		InitializeEntity(id, "", "", EntityType.GOAL);
		PreCondition = "";
		Parameters = new ArrayList<Variable>();
	}
	
	@Override
	public String toString() {
		if (Name == null 
				|| Name == "")
			return ID;
		
		return Name;
	}
	
}
