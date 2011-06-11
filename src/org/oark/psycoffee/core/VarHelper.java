package org.oark.psycoffee.core;

import java.util.ArrayList;
import java.util.List;

public class VarHelper {

	public static List<VarValue> getVarList(List<String> list, String operator) {
		
		List<VarValue> result = new ArrayList<VarValue>(list.size());
		
		for (String var : list) {
			VarValue value = new VarValue(var, operator);
			result.add(value);
		}
		
		return result;
	}
	
}
