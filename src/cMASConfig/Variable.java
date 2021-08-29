package cMASConfig;

public class Variable extends Entity {
	
	public enum VariableType {
		INTEGER,
		STRING,
		REAL,
		BOOL,
		NONE,
		JSON,
		LOCATION;
		
		public static boolean belongsToEnum(String valueType) {
			for (VariableType type : VariableType.values()) {
				if (type.toString().equals(valueType))
					return true;
			}
			return false;
		}
		
	}
	
	public boolean ReadOnly;
	public boolean StoreLongTerm;
	public String Error;
	public String Address;
	public String Source;
	public VariableType TypeOfVariable;
	
    public Variable(String id) {
		InitializeEntity(id, "", "", EntityType.VARIABLE);
    	this.Error = "";
    	this.StoreLongTerm = false;
    	this.Address = "";
    	this.Source = "";
    	this.TypeOfVariable = VariableType.NONE; 	
    }

    
    @Override
    public String toString() {
    	
    	String stringToReturn = TypeOfVariable.toString() + " " + Name;
    	
    	return stringToReturn;
    	
    }
}