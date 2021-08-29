package cMASConfig;

public class Location extends Variable {

	public String x;
	public String y;
	public String z;
	public String rx;
	public String ry;
	public String rz;
	public String Relative;
	public String TypeOfLocation;
	
	public Location(String id) {
		super(id);
		
		TypeOfVariable = VariableType.LOCATION;
		
		x = "0";
		y = "0";
		z = "0";
		rx = "0";
		ry = "0";
		rz = "0";
		
		Relative = "";
		TypeOfLocation = "";
	}
	
	public Location(Variable variable) {
		super(variable.ID);
		
		StoreLongTerm = variable.StoreLongTerm;
		Address = variable.Address;
		ReadOnly = variable.ReadOnly;
		Source = variable.Source;
		TypeOfVariable = VariableType.LOCATION;
		
		x = "0";
		y = "0";
		z = "0";
		rx = "0";
		ry = "0";
		rz = "0";
		
		Relative = "";
		TypeOfLocation = "";
		
	}
}
