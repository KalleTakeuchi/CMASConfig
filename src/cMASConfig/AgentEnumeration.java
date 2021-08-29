package cMASConfig;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.ArrayList;

public class AgentEnumeration implements Enumeration<Agent> {

	int counter;
	ArrayList<Agent> agents;
	
	public AgentEnumeration(ArrayList<Agent> agents) {
		counter = 0;
		this.agents = agents;
	}
	
	@Override
	public boolean hasMoreElements() {
		if (counter > agents.size() - 1)
			return false;
					
		return true;
	}

	@Override
	public Agent nextElement() {
		if(!hasMoreElements())
			throw new NoSuchElementException();
		
		Agent myAgent = agents.get(counter);
		counter++;
		return myAgent;
	}

}
