package cMASConfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cMASConfig.Variable.VariableType;

/**
 * Contains static array lists with agent and plan objects. 
 * Checks that IDs are unique and has methods that can 
 * retrieve objects
 * @author kalle
 *
 */

public class DataManager {

	private static ArrayList<Agent> agents;
	private static ArrayList<Plan> plans;
	
	static {
		agents = new ArrayList<Agent>();
		plans = new ArrayList<Plan>();
	}
	

	static Enumeration<Agent> getAgents() {
		return new AgentEnumeration(agents);
	}
	
	static Enumeration<Plan> getPlans() {
		return new PlanEnumeration(plans);
	}

	static void addAgent(Agent agent) {
		if (agent != null)
			agents.add(agent);
	}
	
	static void addPlan(Plan plan) {
		if (plan != null)
			plans.add(plan);
	}
	
	/**
	 * returns a hashmap with agent objects as keys and 
	 * lists of compatible interface names as values.
	 * @param agent
	 * @return
	 */
	static HashMap<Agent, List<String>> getCompatibleAgents(Agent agent) {
		
		HashMap<Agent, List<String>> myMap = new HashMap<Agent, List<String>>();
		
		for (Agent loopAgent : agents) {
			if (!loopAgent.equals(agent)) {
				for (Interface loopAgentInterface : loopAgent.Interfaces) {
					for (Interface agentInterface : agent.Interfaces) {
						if (agentInterface.Name.equals(loopAgentInterface.Name)
								&& (agentInterface.Skills.size() > 0 || loopAgentInterface.Skills.size() > 0)) {
							if (!myMap.containsKey(loopAgent))
								myMap.put(loopAgent, new ArrayList<String>());
							
							myMap.get(loopAgent).add(loopAgentInterface.Name);
						}
					}
				}
			}
		}
		
		return myMap;
	}
	

	/**
	 * Checks if ID is unique. 
	 * probably needs optimization for real world use.
	 */
	static boolean entityIDExists(String ID) {
		if (getAgent(ID) != null ||
				getInterface(ID) != null ||
				getSkill(ID) != null ||
				getVariable(ID) != null ||
				getPlan(ID) != null ||
				getGoal(ID) != null)
			return true;
		return false;
	}

	static Goal getGoal(String iD) {
		
		for (Agent myAgent : agents) {
			for (Goal myGoal : myAgent.Goals) {
				if (myGoal.ID.equals(iD))
					return myGoal;
			}
		}
		
		return null;
	}

	static Plan getPlan(String iD) {
		for (Plan myPlan : plans) {
			if (myPlan.ID.equals(iD))
				return myPlan;
		}
		return null;
	}

	static Variable getVariable(String iD) {
		
		for (Agent myAgent : agents) {
			
			for (Variable myVar : myAgent.Variables) {
				if (myVar.ID.equals(iD))
					return myVar;
			}
			
			for (Interface myInterface : myAgent.Interfaces) {
				for(Variable myVar : myInterface.Variables) {
					if (myVar.ID.equals(iD))
						return myVar;
				}
			}
		}
		
		return null;
	}

	static Skill getSkill(String iD) {
		
		for (Agent myAgent : agents) {
			for (Interface myInterface : myAgent.Interfaces) {
				for(Skill mySkill : myInterface.Skills) {
					if (mySkill.ID.equals(iD))
						return mySkill;
				}
			}
		}
				
		return null;
	}

	static Interface getInterface(String iD) {
		
		for (Agent myAgent : agents) {
			for (Interface myInterface : myAgent.Interfaces) {
				if (myInterface.ID.equals(iD))
					return myInterface;
			}
		}
		
		return null;
		
	}
	
	static Agent getAgent(String id) {
		
		Optional<Agent> agent = agents.stream()
			.filter(x -> x.ID == id)
			.findFirst();
		
		if (agent.isPresent()) {
			return agent.get();
		} 
		
		return null;
	}
	
	static void createTestAgents() {
		
		/*
		 * Workpiece
		 */
		Agent agent = new Agent(createUniqueID());
		agent.AgentBaseType = BaseType.COMPONENT;
		agent.AgentSpecificType = AgentSpecificType.PART.toString();
		agent.Name = "Workpiece";
		Goal goal = new Goal(createUniqueID());
		goal.Name = "GetNails";
		agent.Goals.add(goal);
		
		Interface intrfce = new Interface(createUniqueID());
		intrfce.Name = "VacuumInterface";
		agent.Interfaces.add(intrfce);
		
		Location loc = new Location(createUniqueID());
		loc.Name = "VacuumLocation";
		agent.Variables.add(loc);
		
		intrfce = new Interface(createUniqueID());
		intrfce.Name = "NailingInterface";
		agent.Interfaces.add(intrfce);
		
		loc = new Location(createUniqueID());
		loc.Name = "NailTarget1";
		agent.Variables.add(loc);
		
		loc = new Location(createUniqueID());
		loc.Name = "NailTarget2";
		agent.Variables.add(loc);
		
		
		agents.add(agent);
		
		
		/*
		 * VacuumTool
		 */
		agent = new Agent(createUniqueID());
		agent.Name = "VacuumTool";
		agent.AgentBaseType = BaseType.RESOURCE;
		
		intrfce = new Interface(createUniqueID());
		intrfce.Name = "VacuumInterface";
		agent.Interfaces.add(intrfce);
				
		loc = new Location(createUniqueID());
		loc.Name = "from";
		intrfce.Variables.add(loc);
		
		loc = new Location(createUniqueID());
		loc.Name = "to";
		intrfce.Variables.add(loc);
		
		Skill skill = new Skill(createUniqueID());
		skill.Name = "Transport";
		skill.Execution = "aRobot.MoveTool(\n"
				+ "    moveTo := from,\n"
				+ "    toolData := ToolData\n"
				+ ");\n"
				+ "aRobot.ActivateTool();\n"
				+ "aRobot.MoveTool(\n"
				+ "    moveTo := to,\n"
				+ "    toolData := ToolData\n"
				+ ");\n"
				+ "aRobot.DeactivateTool();\n";
		
		intrfce.Skills.add(skill);
		
		intrfce = new Interface(createUniqueID());
		intrfce.Name = "ToolInterface";
		agent.Interfaces.add(intrfce);
		
		Value val = new Value(createUniqueID());
		val.Name = "ToolData";
		val.TypeOfVariable = VariableType.STRING;
		agent.Variables.add(val);
		
		agents.add(agent);
		
		
		/*
		 * NailingStation
		 */
		agent = new Agent(createUniqueID());
		agent.Name = "NailingStation";
		agent.AgentBaseType = BaseType.RESOURCE;
		
		intrfce = new Interface(createUniqueID());
		intrfce.Name = "NailingInterface";
		agent.Interfaces.add(intrfce);
				
		loc = new Location(createUniqueID());
		loc.Name = "BufferLocation";
		intrfce.Variables.add(loc);
		
		loc = new Location(createUniqueID());
		loc.Name = "target1";
		intrfce.Variables.add(loc);
		
		loc = new Location(createUniqueID());
		loc.Name = "target2";
		intrfce.Variables.add(loc);
		
		skill = new Skill(createUniqueID());
		skill.Name = "NailSurface";
		skill.Execution = "aNailGun.ShootNail(\n"
				+ "    target := target1\n"
				+ ");\n"
				+ "aNailGun.ShootNail(\n"
				+ "    target := target2\n"
				+ ");\n";
		intrfce.Skills.add(skill);
		
		agents.add(agent);
		
		/*
		 *Nailgun 
		 */
		agent = new Agent(createUniqueID());
		agent.Name = "NailGun";
		agent.AgentBaseType = BaseType.RESOURCE;
		
		intrfce = new Interface(createUniqueID());
		intrfce.Name = "NailingInterface";
		agent.Interfaces.add(intrfce);
		
		loc = new Location(createUniqueID());
		loc.Name = "target";
		intrfce.Variables.add(loc);
		
		skill = new Skill(createUniqueID());
		skill.Name = "ShootNail";
		skill.Execution = "aRobot.MoveTool(\n"
				+ "    moveTo := target,\n"
				+ "    toolData := toolData\n"
				+ ");\n"
				+ "aRobot.ActivateTool();\n"
				+ "aRobot.DeactivateTool();";
		intrfce.Skills.add(skill);
		
		intrfce = new Interface(createUniqueID());
		intrfce.Name = "ToolInterface";
		agent.Interfaces.add(intrfce);
				
		val = new Value(createUniqueID());
		val.Name = "ToolData";
		val.TypeOfVariable = VariableType.STRING;
		agent.Variables.add(val);
		
		agents.add(agent);
		
		/*
		 *Robot 
		 */
		agent = new Agent(createUniqueID());
		agent.Name = "Robot";
		agent.AgentBaseType = BaseType.RESOURCE;
		
		intrfce = new Interface(createUniqueID());
		intrfce.Name = "ToolInterface";
		agent.Interfaces.add(intrfce);
		
		val = new Value(createUniqueID());
		val.Name = "toolData";
		val.TypeOfVariable = VariableType.STRING;
		val.ReadOnly = false;
		intrfce.Variables.add(val);
		
		val = new Value(createUniqueID());
		val.Name = "moveTo";
		val.TypeOfVariable = VariableType.LOCATION;
		val.ReadOnly = false;
		intrfce.Variables.add(val);		
		
		skill = new Skill(createUniqueID());
		skill.Name = "ActivateTool";
		intrfce.Skills.add(skill);
		
		skill = new Skill(createUniqueID());
		skill.Name = "DeactivateTool";
		intrfce.Skills.add(skill);
		
		skill = new Skill(createUniqueID());
		skill.Name = "MoveTool";
		skill.Execution = "dest := moveTo;\n"
				+ "dest2 := toolData;";
		intrfce.Skills.add(skill);
	
		agents.add(agent);
		
		
/*
		ArrayList<Interface> agentInterfaces;
		
		for (int i = 0; i <= 5; i++) {
			
			agentInterfaces = new ArrayList<Interface>();
			agentInterfaces.add(new Interface(DataManager.createUniqueID()));
			agentInterfaces.get(0).Name = "test interface";
			Agent myAgent = new Agent(DataManager.createUniqueID());
			myAgent.Name = "Agent " + String.valueOf(i);
			myAgent.Interfaces = agentInterfaces;
			agents.add(myAgent);
			
		}
		
		for (int i = 6; i <= 300; i++) {
			
			agentInterfaces = new ArrayList<Interface>();
			agentInterfaces.add(new Interface(DataManager.createUniqueID()));
			agentInterfaces.get(0).Name = "other interface";
			Agent myAgent = new Agent(DataManager.createUniqueID());
			myAgent.Name = "Agent " + String.valueOf(i);
			myAgent.Interfaces = agentInterfaces;
			agents.add(myAgent);
			
		}
		*/

	}
	
	static ArrayList<Agent> filterAgents(String str) {
		return (ArrayList<Agent>) agents.stream()
		.filter(x -> x.ID.contains(str) || x.Name.contains(str))
		.collect(Collectors.toList());
	}
	
	static ArrayList<Plan> filterPlans(String str) {
		return (ArrayList<Plan>) plans.stream()
		.filter(x -> x.ID.contains(str) || x.Name.contains(str))
		.collect(Collectors.toList());
	}
	
	static String createUniqueID() {
		Random rnd = new Random();
		String myId = Long.valueOf(rnd.nextLong()).toString();
		
		while (entityIDExists(myId))
			myId = Long.valueOf(rnd.nextLong()).toString();
		
		return myId;
	}
	
	static void removeAgent(String ID) {
		Agent myAgent = getAgent(ID);
		
		if (myAgent == null)
			return;
		
		agents.remove(myAgent);
	}
	
	static void removeInterface(String ID) {
		for (Agent myAgent : agents) {
			for (Interface myInterface : myAgent.Interfaces) {
				if (myInterface.ID.equals(ID)) {
					myAgent.Interfaces.remove(myInterface);
					return;
				}
			}
		}
	}
	
	static void removeSkill(String ID) {
		for (Agent myAgent : agents) {
			for (Interface myInterface : myAgent.Interfaces) {
				for(Skill mySkill : myInterface.Skills) {
					if (mySkill.ID.equals(ID)) {
						myInterface.Skills.remove(mySkill);
						return;
					}
				}
			}
		}
	}
	
	static void removePlan(String ID) {
		Plan myPlan = getPlan(ID);
		
		if (myPlan == null)
			return;
		
		plans.remove(myPlan);
	}

	public static ArrayList<Agent> getAgentsWithGoal(String goalName) {
		
		ArrayList<Agent> agentsList = new ArrayList<Agent>();
		
		for (Agent agent : agents) {
			for (Goal goal : agent.Goals) {
				if (goal.Name.equals(goalName)) {
					agentsList.add(agent);
					break;
				}
			}
		}
		
		return agentsList;
	}
	
	public static void importConfiguration(String directory) {
		
		File dir = new File(directory);
		
		if (!dir.isDirectory())
			return;
		
		agents.clear();
		plans.clear();
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson =	builder.create();
		String jsonString = "";
		BufferedReader br;
		FileInputStream fis = null;
		
		for (File file : dir.listFiles()) {
				
			if (!file.canRead())
				continue;
				
			try {
				fis = new FileInputStream(file);
				br = new BufferedReader(new InputStreamReader(fis));
				jsonString = br.readLine();
				
				if (jsonString != null) {
					Agent agent = gson.fromJson(jsonString, Agent.class);
					if (agent != null) {
						if (!entityIDExists(agent.ID))
							agents.add(agent);
					}
				}
				
				jsonString = "";

			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				if (fis != null) {
					try {
						fis.close();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		DefaultMutableTreeNode root = TreeManager.createTreeStructure(
				new AgentEnumeration(agents), 
				new PlanEnumeration(plans)
				);
		
		TreeManager.setTreeModel(new DefaultTreeModel(root));
		
	}
	
	public static void exportConfiguration(String directory) {
		
		File dir = new File(directory);
		
		if (!dir.isDirectory())
			return;
		
		GsonBuilder builder = new GsonBuilder();
		Gson gson =	builder.create();
		String jsonString;
		BufferedWriter writer;
		String extension = ".txt";
		
		try {
			for (Agent agent : agents) {
				writer = new BufferedWriter(new FileWriter(dir.getPath() + "/" + agent.ID + extension));
				jsonString = gson.toJson(agent);
				writer.write(jsonString);
				writer.close();
			}
			for (Plan plan : plans) {
				writer = new BufferedWriter(new FileWriter(dir.getPath() + "/" + plan.ID + extension));
				jsonString = gson.toJson(plan);
				writer.write(jsonString);
				writer.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Returns the agent that has the skill with the given ID.
	 * @param id
	 * @return
	 */
	public static Agent getAgentWithSkill(String id) {
		
		for (Agent agent : agents) {
			for (Interface intrfce : agent.Interfaces ) {
				for (Skill skill : intrfce.Skills) {
					if (skill.ID.equals(id))
						return agent;
				}
			}
		}
		
		return null;
		
	}

	public static ArrayList<Skill> getSkillsOnResourceInterfaces(List<Interface> interfaces) {
		
		ArrayList<Skill> skills = new ArrayList<Skill>();
		
		for (Agent agent : agents) {
			if (!agent.AgentBaseType.equals(BaseType.RESOURCE))
				continue;
			for (Interface agentIntrfce : agent.Interfaces) {
				for(Interface intrfce : interfaces) {
					if (agentIntrfce.Name.equals(intrfce.Name)) {
						for (Skill skill : agentIntrfce.Skills)
							skills.add(skill);
					}
				}
			}
		}
		
		return skills;
	}
	
	public static List<Variable> getVariablesOnResourceInterfaces(List<Interface> interfaces) {
		
		ArrayList<Variable> variables = new ArrayList<Variable>();
		
		for (Agent agent : agents) {
			if (!agent.AgentBaseType.equals(BaseType.RESOURCE))
				continue;
			for (Interface agentIntrfce : agent.Interfaces) {
				for (Interface intrfce : interfaces) {
					if (agentIntrfce.Name.equals(intrfce.Name)) {
						for (Variable var : agentIntrfce.Variables)
							variables.add(var);
					}
				}
			}
		}
		
		return variables;
	}

	/**
	 * Gets interfaces that exist on all agents that has the
	 * given goal
	 * @return
	 */
	public static List<Interface> getInterfacesAccessibleFromGoalPlan(String goalName) {
		/*
		 * Get all agents with the goal solved by the plan
		 */
		List<Agent> agentsWithGoal = DataManager.getAgentsWithGoal(goalName);
		
		/*
		 * Make a list of all interfaces that exist on all agents previously found. 
		 */
		List<Interface> interfacesOnAllAgents = new ArrayList<Interface>();
		List<Interface> interfacesToRemove = new ArrayList<Interface>();
		
		for (Agent agent : agentsWithGoal) {
			if (agentsWithGoal.indexOf(agent) == (0)) {
				for (Interface intrfce : agent.Interfaces) {
					interfacesOnAllAgents.add(intrfce);
				}
			}
			else {
				for (Interface agentIntrfce : agent.Interfaces) {
					for (Interface listInterface : interfacesOnAllAgents) {
						if (agentIntrfce.Name != listInterface.Name) {
							interfacesToRemove.add(listInterface);
						}
					}
				}
				
				//Remove interfaces marked for removal
				for (Interface interfaceToRemove : interfacesToRemove) {
					interfacesOnAllAgents.remove(interfaceToRemove);
				}
				
				interfacesToRemove.clear();
				
			}
		}
		
		return interfacesOnAllAgents;
	}

	/**
	 * returns all resource interfaces that share names
	 * with the argument interfaces
	 * @param interfaces
	 * @return
	 */
	public static List<Interface> getResourceInterfacesWithNames(List<Interface> interfaces) {
		List<Interface> listToReturn = new ArrayList<Interface>();
		
		for (Agent agent : agents) {
			if (!agent.AgentBaseType.equals(BaseType.RESOURCE))
				continue;
			
			for (Interface agentIntrfce : agent.Interfaces) {
				for (Interface intrfce : interfaces) {
					if (intrfce.Name.equals(agentIntrfce.Name))
						listToReturn.add(agentIntrfce);
				}

			}
		}
		
		return listToReturn;
	}
	
}
