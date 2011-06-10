package org.oark.psycoffee.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class represents a set of variables
 * 
 * variables can be single values or a list of variables prefixed with the LIST_PREFIX
 * 
 * //TODO proper escaping with length in toString() 
 * //TODO add support for operators
 * 
 * @author Simon Koelsch
 *
 */
public class VarCollection {
	
	public static final String LIST_PREFIX = "_list_";

	//TODO maybe breakup in content types
	private Map<String, String> vars = new HashMap<String, String>();
	private Map<String, List<String>> listVars = new HashMap<String, List<String>>();
	
	/**
	 * adds the value as a variable
	 * 
	 * if a value with this name already exists, it will be added with the existing value as a list variable
	 * 
	 * @param name
	 * @param value
	 */
	public void addVar(String name, String value) {
		if (isListKey(name)) {
			// add explicitly as list
			List<String> list = new ArrayList<String>();
			list.add(value);
			addList(name, list);
		} else {
			if (vars.containsKey(name)) {
				// remove from var and add to lists
				String existingValue = this.vars.get(name);
				List<String> list = new ArrayList<String>();
				list.add(existingValue);
				list.add(value);
				addList(name, list);
				this.vars.remove(name);
			} else {
				this.vars.put(name, value);
			}
		}

	}
	
	public void setVar(String name, String value) {
		if (value == null) {
			removeVar(name);
		} else {
			this.vars.put(name, value);
		}
	}
	
	public void setList(String name, List<String> list) {
		this.listVars.put(getListKey(name), list);
	}
	
	public void addList(String name, List<String> list) {
		
		if (!name.startsWith(LIST_PREFIX)) {
			name = getListKey(name);
		}
		
		if (this.vars.containsKey(name)) {
			List<String> existingList = this.listVars.get(name);
			existingList.addAll(list);
		} else {
			setList(name, list);
		}
	}
	
	public void removeVar(String name) {
		if (isListKey(name)) {
			this.listVars.remove(name);
		} else {
			this.vars.remove(name);
		}
	}
	
	/**
	 * removes a value from a list variable
	 * if the list is empty, it will also be removed
	 * 
	 * if you wan't to explicitly remove the whole variable, use removeVar(String) 
	 * 
	 * @param name
	 * @param value
	 * @return false if the value wasn't found.
	 */
	public boolean removeValue(String name, String value) {
		
		if(this.listVars.containsKey(getListKey(name))) {
			List<String> list = listVars.get(name);
			if(list.contains(value)) {
				list.remove(list.indexOf(value));
				if (list.isEmpty()) {
					removeVar(name);
				}
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	//TODO operators missing, : is always used
	public String toString() {
		StringBuffer vars = new StringBuffer();
		for (Entry<String, String> var : this.vars.entrySet()) {
			vars.append(":" + var.getKey() + " " + var.getValue() + "\n");
		}
		for (Entry<String, List<String>> listVar : this.listVars.entrySet()) {
			vars.append(":" + listVar.getKey() + " ");
			List<String> list = listVar.getValue();
			vars.append(list.get(0));
			for(int i=1; i <= list.size(); i++) {
				vars.append("|" + list.get(i));
			}
			vars.append("\n");
		} 
		return vars.toString();
	}

	private boolean isListKey(String key) {
		return key.startsWith(LIST_PREFIX);
	}
	
	private String getListKey(String key) {
		if (!isListKey(key)) {
			key = LIST_PREFIX + key;
		} 
		return key;
	}
	
}
