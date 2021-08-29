package cMASConfig;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.ArrayList;

public class PlanEnumeration implements Enumeration<Plan> {

	int counter;
	ArrayList<Plan> plans;
	
	public PlanEnumeration(ArrayList<Plan> plans) {
		counter = 0;
		this.plans = plans;
	}
	
	@Override
	public boolean hasMoreElements() {
		if (counter > plans.size() - 1)
			return false;
					
		return true;
	}

	@Override
	public Plan nextElement() {
		if(!hasMoreElements())
			throw new NoSuchElementException();
		
		Plan myPlan = plans.get(counter);
		counter++;
		return myPlan;
	}

}

