/**
 * 
 */
package cMASConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * @author kalle
 *Is used to parse a process plan stored in the Execute attribute
 */
public class ExecutionParser {

	public List<PlanResourceForm> getPlanResources(Plan plan) {
		
		String program = plan.Execution;
		
		List<PlanResourceForm> resourceList = new ArrayList<PlanResourceForm>();
		List<String> skillNames = new ArrayList<String>();
		List<String> resourceNames = new ArrayList<String>();
		
		int previousIndex = 0;
		
		while (true) {
			//Find open parenthesis
			int openParenthesisIndex = program.indexOf("(", previousIndex);
			
			if (openParenthesisIndex == -1)
				break;
			
			/*
			 * Check that this is actually a function call- 
			 * There should be a sequence of characters not separated by operators 
			 * or whitespace before the opening parenthesis
			 */
			int i = 1;
			while(true) {
				
				if ((openParenthesisIndex - i <= 0)
						|| isOperatorOrWhitespace(program.charAt(openParenthesisIndex - i))) {
					break;
				}
				
				i++;
			}
			
			/*
			 * if the number of characters in between 
			 * the operator/whitespacespace/statement terminator and 
			 * the open parenthesis is one or more, the 
			 * parenthesis belongs to a function call
			 */
			if (i > 1) {
				int closeParenthesisIndex = program.indexOf(")", openParenthesisIndex);
				
				/*
				 * Open parenthesis should always be followed by close parenthesis.
				 * Something is wrong if this is not the case.
				 */
				if (closeParenthesisIndex == -1)
					break;
				
				previousIndex = closeParenthesisIndex;
				
				//Get resource and skill name
				String resourceAndSkill = program.substring(openParenthesisIndex - i, openParenthesisIndex);
				
				//There should be a . between resource and skill
				if (!resourceAndSkill.contains(".")) 
					break;
				
				String resourceName = resourceAndSkill.substring(0, resourceAndSkill.indexOf('.'));
				String skillString = resourceAndSkill.substring(resourceAndSkill.indexOf('.') + 1, openParenthesisIndex);
				
				skillNames.add(skillString);
				resourceNames.add(resourceName);
				
			}
				
		}
		
		/*
		 * resource name list and skill list should have equal length
		 */
		if (resourceNames.size() != skillNames.size())
			return resourceList;
		
		/*
		 * Create a hashmap of resource forms
		 */
		Map<String, PlanResourceForm> resourceMap = new HashMap<String, PlanResourceForm>();
		
		for (String loopResource : resourceNames) {
			if (!resourceMap.keySet().contains(loopResource)) {
				PlanResourceForm resourceForm = new PlanResourceForm(plan);
				resourceForm.setResourceName(loopResource);
				resourceMap.put(loopResource, resourceForm);
			}
		}
		
		for (int i = 0; i < resourceNames.size(); i++) {
			PlanResourceForm resourceForm = resourceMap.get(resourceNames.get(i));
			Skill resourceSkill = null;
			
			for (Skill loopSkill : resourceForm.getAvailableSkills()) {
				if (loopSkill.toString().equals(skillNames.get(i))) {
					resourceSkill = loopSkill;
					break;
				}
			}
			
			if (resourceSkill != null) {
				resourceForm.setNewSkill(resourceSkill);
			}
			
			resourceList.add(resourceForm);
			
		}
	
		
		return resourceList;
	}
	
	/**
	 * Removes Pascal comments from string.
	 *"//" is not standard Pascal and is not removed.
	 * @param text
	 * @return
	 */
	private String removeComments(String text) {
		StringBuffer sb = new StringBuffer(text);
		boolean commentsRemain = true;
		boolean curly = false;
		
		while (commentsRemain) {
			int openCommentIndex = sb.indexOf("(*");
			int closeCommentIndex = sb.indexOf("*)");
			
			if (openCommentIndex == -1) {
				openCommentIndex = sb.indexOf("{");
				closeCommentIndex = sb.indexOf("}");
				curly = true;
			}
			
			if (closeCommentIndex == -1 
					|| openCommentIndex == -1 
					|| openCommentIndex > closeCommentIndex) {
				commentsRemain = false;
				continue;
			}
			
			if (curly) {
				sb.replace(openCommentIndex, closeCommentIndex + 1, "");
			}
			else {
				sb.replace(openCommentIndex, closeCommentIndex + 2, "");
			}
			
		}
		
		return sb.toString();
		
	}
	
	/**
	 * Returns the variables used on the right side of :=
	 * in program. Whitespace, parenthesis and literals are 
	 * filtered out.
	 * @param program The Pascal program to be parsed.
	 */
	public List<String> getUsedVariables(String program) {
		
		
		program = removeComments(program);
		
		int previousIndex = 0;
		
		ArrayList<String> stringList = new ArrayList<String>();
		
		/*
		 * Variable names used in assignments.
		 */
		while (true) {
			
			int assignmentIndex = program.indexOf(":=", previousIndex);
			
			if (assignmentIndex == -1) {
				break;
			}
			else {
				assignmentIndex += 2;
			}
		
			int endStatementIndex = program.indexOf(';', assignmentIndex);
			
			/*
			 * Check is needed if variable is an argument written using the form
			 * func(arg := val);
			 */
			if (endStatementIndex > program.indexOf(',', assignmentIndex) 
					&& program.indexOf(',', assignmentIndex) != -1)
				endStatementIndex = program.indexOf(',', assignmentIndex);
			
			String assignment = program.substring(assignmentIndex, endStatementIndex);
			
			stringList.add(assignment);
			
			previousIndex = endStatementIndex;
			
		}
		
		previousIndex = 0;
		
		/*
		 * Get variables in function calls
		 */
		while(true) {
			//Find open parenthesis
			int openParenthesisIndex = program.indexOf("(", previousIndex);
			
			if (openParenthesisIndex == -1)
				break;
			
			/*
			 * Check that this is actually a function call- 
			 * There should be a sequence of characters not separated by operators 
			 * or whitespace before the opening parenthesis
			 */
			int i = 1;
			while(true) {
				
				if ((openParenthesisIndex - i < 0)
						|| isOperatorOrWhitespace(program.charAt(openParenthesisIndex - i))) {
					break;
				}
				
				i++;
			}
			
			/*
			 * if the number of characters in between 
			 * the operator/whitespacespace/statement terminator and 
			 * the open parenthesis is one or more, the 
			 * parenthesis belongs to a function call
			 */
			if (i > 1) {
				int closeParenthesisIndex = program.indexOf(")", openParenthesisIndex);
				
				/*
				 * Open parenthesis should always be followed by close parenthesis.
				 * Something is wrong if this is not the case.
				 */
				if (closeParenthesisIndex == -1)
					break;
				
				String arguments = program.substring(openParenthesisIndex, closeParenthesisIndex);
				
				//Commas separate multiple arguments
				for (String str : arguments.split(",")) {
					
					if (str.contains(":=")) {
						stringList.add(str.substring(str.indexOf(":=") + 2));
					}
					else {
						stringList.add(str);
					}
				}
				
				previousIndex = closeParenthesisIndex;
				
			}
			else {
				previousIndex = openParenthesisIndex + 1;
			}
			
		}
		
		List<String> stringList2;
		ArrayList<String> stringList3 = new ArrayList<String>();
		
		for (String s : stringList) {
			
			stringList2 = splitByOperators(s);
			
			for (String s2 : stringList2) {
				stringList3.add(s2);
			}
			
		}
		
		stringList2 = new ArrayList<String>();
		
		for (String s : stringList3) {
			String tempString = removePraenthesisAndWhitespace(s);
			
			if (!isLiteral(tempString))
				stringList2.add(removePraenthesisAndWhitespace(s));
		}
		
		stringList.clear();
		
		/*
		 * Inputs are never abstract interfaces, so 
		 * any variable found with a . must be a location
		 * or is not an input
		 * 
		 * Also doubles and empty strings are removed
		 */
		for (String str : stringList2) {
			
			String tempString = str;
			
			if (tempString.contains(".")) {
				String[] stringSplit = tempString.split("\\.");
				String coordinate = stringSplit[1];
				
				if ((coordinate.equals("x") 
						|| coordinate.equals("y") 
						|| coordinate.equals("z")
						|| coordinate.equals("rx")
						|| coordinate.equals("ry")
						|| coordinate.equals("rz"))) {
					tempString = stringSplit[0]; 
				} else {
					if (stringList.contains(stringSplit[0]))
						stringList.remove(stringSplit[0]);
					if (stringList.contains(tempString))
						stringList.remove(tempString);
					continue;
				}
			}
			
			if (!stringList.contains(tempString)
					&& !tempString.equals(""))
				stringList.add(tempString);
		}
		
		return stringList;
		
	}
	
	/**
	 * Check if a char is an operator (or part of an operator)
	 * or a whitespace character.
	 * Also checks for statement terminators.
	 * @param ch
	 * @return
	 */
	private static boolean isOperatorOrWhitespace(char ch) {
		
		if (ch == '+'
				|| ch == '-'
				|| ch == '*'
				|| ch == '/'
				|| ch == '='
				|| ch == '&'
				|| ch == '|'
				|| ch == '~'
				|| ch == '%'
				|| ch == '<'
				|| ch == '>'
				|| ch == '\s'
				|| ch == ';'
				)
			return true;
		
		return false;
	}
	
	/**
	 * Does what it says on the can
	 * @param text
	 * @return
	 */
	private String removePraenthesisAndWhitespace(String text) {
		String tempString = text.replaceAll("\\(", "");
		tempString = tempString.replaceAll("\\)", "");
		tempString = tempString.replaceAll("\\s", "");
		
		return tempString;
	}
	
	/**
	 * Splits a string into substrings at indexes where 
	 * operator characters occur (like + - / % etc).
	 * @return
	 */
	private List<String> splitByOperators(String text) {
		List<String> stringList;
		
		stringList = splitByOperator(text, "\\+");
		stringList = splitByOperator(stringList, "-");
		stringList = splitByOperator(stringList, "/");
		stringList = splitByOperator(stringList, "\\*");
		stringList = splitByOperator(stringList, "%");
		stringList = splitByOperator(stringList, "=");
		stringList = splitByOperator(stringList, "<");
		stringList = splitByOperator(stringList, ">");
		stringList = splitByOperator(stringList, "<>");
		stringList = splitByOperator(stringList, ">=");
		stringList = splitByOperator(stringList, "<=");
		stringList = splitByOperator(stringList, "and then");
		stringList = splitByOperator(stringList, "AND THEN");
		stringList = splitByOperator(stringList, "and");
		stringList = splitByOperator(stringList, "AND");
		stringList = splitByOperator(stringList, "or else");
		stringList = splitByOperator(stringList, "OR ELSE");
		stringList = splitByOperator(stringList, "or");
		stringList = splitByOperator(stringList, "OR");
		stringList = splitByOperator(stringList, "not");
		stringList = splitByOperator(stringList, "NOT");
		stringList = splitByOperator(stringList, "&");
		stringList = splitByOperator(stringList, "\\|");
		stringList = splitByOperator(stringList, "~");
		
		return stringList;
	}
	
	private List<String> splitByOperator(List<String> strings, String regex) {
		
		ArrayList<String> stringList = new ArrayList<String>();
		
		for (String str : strings) {
			String[] array = str.split(regex);
			
			for (String str2 : array) {
				stringList.add(str2);
			}
			
		}
		
		return stringList;
		
	}
	
	private List<String> splitByOperator(String string, String regex) {
		
		ArrayList<String> stringList = new ArrayList<String>();
		
		for (String str : string.split(regex)) {
			stringList.add(str);	
		}
		
		return stringList;
		
	}
	
	/**
	 * Returns true if text is a string or numeric literal.
	 * This probably needs work to catch some special cases
	 * @return 
	 */
	private boolean isLiteral(String text) {
		
		if (text == null)
			return false;
		if (text.equals(""))
			return false;
		
		//Check for string literal
		if (text.charAt(0) == '"')
			return true;
		
		//Check for char literal
		if (text.charAt(0) == '\'' 
				|| text.charAt(0) == '#')
			return true;
		
		//Check for numeric literal
		if (Character.isDigit(text.charAt(0)))
			return true;
		
		//Check for boolean literals
		if (text.equals("true")
				|| text.equals("TRUE")
				|| text.equals("True")
				|| text.equals("false")
				|| text.equals("FALSE")
				|| text.equals("False"))
			return true;
		
		return false;
	}
	
	
}
