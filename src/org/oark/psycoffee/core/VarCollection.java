package org.oark.psycoffee.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
