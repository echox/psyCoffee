package org.oark.psycoffee.core;

/**
 * payload of the package
 * 
 * @author Simon Koelsch
 *
 */
public class Entity {

	private VarCollection vars = new VarCollection();
	private String method = new String();
	private String data = new String();
	
	public VarCollection getVars() {
		return vars;
	}
	public void setVars(VarCollection vars) {
		this.vars = vars;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		StringBuilder entity = new StringBuilder();
		entity.append(vars.toString());
		entity.append(method + "\n");
		entity.append(data);
		return entity.toString();
	}
}
