package org.oark.psycoffee.core;

public class Packet {

	private VarCollection routingHeader = new VarCollection();
	private Entity entity = new Entity();
	
	public VarCollection getRoutingHeader() {
		return this.routingHeader;
	}
	
	public void setRoutingHeader(VarCollection header) {
		this.routingHeader = header;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	
}
