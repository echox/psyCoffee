package org.oark.psycoffee.core;

public class Packet {

	private VarCollection routingHeader = new VarCollection();
	
	public VarCollection getRoutingHeader() {
		return this.routingHeader;
	}
	
	public void setRoutingHeader(VarCollection header) {
		this.routingHeader = header;
	}
}
