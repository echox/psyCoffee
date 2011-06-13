/* 
 * Copyright (c) 2011, Simon Kölsch
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package org.oark.psycoffee.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.oark.psycoffee.core.constants.Operators;

/**
 * This class represents a set of variables
 * 
 * variables can be single values or a list of variables prefixed with the LIST_PREFIX
 * 
 * //TODO proper escaping with length in toString() 
 * 
 * @author Simon Koelsch
 *
 */
public class VarCollection {
	
	public static final String LIST_PREFIX = "_list";

	private Map<String, VarValue> vars = new HashMap<String, VarValue>();
	private Map<String, List<VarValue>> listVars = new HashMap<String, List<VarValue>>();
	
	/**
	 * adds the value as a variable
	 * 
	 * if a value with this name already exists, it will be added with the existing value as a list variable
	 * if the operators differ, the old value will be overwritten
	 * 
	 * @param name
	 * @param value
	 */
	public synchronized void addVar(String name, String content, String operator) {
		
		VarValue value = new VarValue(content, operator);
		
		if (isListKey(name)) {
			// add explicitly as list
			List<String> list = new ArrayList<String>();
			list.add(content);
			addList(name, list, operator);
		} else {
			if (vars.containsKey(name)) {
				// remove from var and add to lists
				VarValue existingValue = this.vars.get(name);
				existingValue.setOperator(operator);
				List<VarValue> list = new ArrayList<VarValue>();
				list.add(existingValue);
				list.add(value);
				this.listVars.put(getListKey(name),list);
				this.vars.remove(name);
			} else {
				this.vars.put(name, value);
			}
		}

	}
	
	public synchronized VarValue getVarValue(String name) {
		return this.vars.get(name);
	}
	
	public synchronized List<VarValue> getVarValues(String name) {
		List<VarValue> list;
		if (this.listVars.containsKey(name)) {
			list = this.listVars.get(name);
		} else {
			list = new ArrayList<VarValue>();
			if (this.vars.containsKey(name)) {
				list.add(this.vars.get(name));
			}
		}
		
		return list;
	}
	
	public synchronized void addVar(String name, String content) {
		addVar(name, content, Operators.CURRENT);
	}
	
	public synchronized void setVar(String name, String content, String operator) {
		if (content == null) {
			removeVar(name);
		} else {
			VarValue value = new VarValue(content, operator);
			this.vars.put(name, value);
		}
	}
	
	public synchronized void setVar(String name, String content) {
		setVar(name, content, Operators.CURRENT);
	}
	
	public synchronized void setList(String name, List<String> list, String operator) {
		
		List<VarValue> varList = VarHelper.getVarList(list, operator);

		this.listVars.put(getListKey(name), varList);
	}
	
	public synchronized void setList(String name, List<String> list) {
		setList(name, list, Operators.CURRENT);
	}
	
	public synchronized void addList(String name, List<String> list, String operator) {
		
		if (!name.startsWith(LIST_PREFIX)) {
			name = getListKey(name);
		}
		
		if (this.vars.containsKey(name)) {
			List<VarValue> varList = VarHelper.getVarList(list, operator);
			List<VarValue> existingList = this.listVars.get(name);
			existingList.addAll(varList);
		} else {
			setList(name, list, operator);
		}
	}
	
	public synchronized void addList(String name, List<String> list) {
		addList(name, list, Operators.CURRENT);
	}
	
	public synchronized void removeVar(String name) {
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
	public synchronized boolean removeValue(String name, String content) {
		
		boolean removed = false;
		
		if(this.listVars.containsKey(getListKey(name))) {
			List<VarValue> list = listVars.get(name);
			for (Iterator<VarValue> it = list.iterator(); it.hasNext();) {
				VarValue varValue = it.next();
				if(varValue.getValue() == content) {
					it.remove();
					removed = true;
				}
			}
			
			if (list.isEmpty()) {
				removeVar(name);
			}
		}
		
		return removed;
	}
	
	@Override
	public synchronized String toString() {
		StringBuilder vars = new StringBuilder();
		for (Entry<String, VarValue> var : this.vars.entrySet()) {
			VarValue value = var.getValue();
			vars.append(value.getOperator() + var.getKey() + "\t" + value.getValue() + "\n");
		}
		for (Entry<String, List<VarValue>> listVar : this.listVars.entrySet()) {
			vars.append(listVar.getValue().get(0).getOperator() + listVar.getKey() + "\t");
			List<VarValue> list = listVar.getValue();
			vars.append(list.get(0).getValue());
			for(int i=1; i < list.size(); i++) {
				vars.append("|" + list.get(i).getValue());
			}
			vars.append("\n");
		} 
		return vars.toString();
	}

	protected static boolean isListKey(String key) {
		return key != null && key.startsWith(LIST_PREFIX+"_") && (!key.endsWith(LIST_PREFIX+"_"));
	}
	
	protected static String getListKey(String key) {
		if (!isListKey(key)) {
			if(!key.startsWith("_")) {
				key = "_" + key;
			}
			key = LIST_PREFIX + key;
		} 
		return key;
	}
	
}
