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

	@Override
	//TODO escape |, use content length
	public String toString() {
		StringBuilder packet = new StringBuilder();
		packet.append("|\n");
		packet.append(routingHeader.toString());
		packet.append("\n");
		packet.append(entity.toString());
		packet.append("|");
		return packet.toString();
	}
	
	
}
