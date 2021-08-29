package cMASConfig;

public class Value extends Variable {

	public String StringValue;
	public String UpperBound;
	public String LowerBound;
	
	public Value(String id) {
		super(id);
		
		StringValue = "";
		UpperBound = "";
		LowerBound = "";
		
	}
	
	public Value(Variable variable) {
		super(variable.ID);
		
		StringValue = "";
		UpperBound = "";
		LowerBound = "";
		
		StoreLongTerm = variable.StoreLongTerm;
		Address = variable.Address;
		ReadOnly = variable.ReadOnly;
		Source = variable.Source;
		
	}
	
}
