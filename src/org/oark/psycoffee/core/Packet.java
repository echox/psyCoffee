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

public class Packet {

	private VarCollection routingVars = new VarCollection();
	private VarCollection entityVars = new VarCollection();
	private String method = new String();
	private String payload = new String();
	
	//flag for invalid packetparts or incomplete parsing
	private boolean invalid;

	public VarCollection getRoutingVars() {
		return routingVars;
	}

	public void setRoutingVars(VarCollection routingVars) {
		this.routingVars = routingVars;
	}

	public VarCollection getEntityVars() {
		return entityVars;
	}

	public void setEntityVars(VarCollection entityVars) {
		this.entityVars = entityVars;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public boolean isInvalid() {
		return invalid;
	}

	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}

	@Override
	//TODO escape |, use content length
	public String toString() {
		StringBuilder packet = new StringBuilder();
		packet.append(routingVars.toString());
		packet.append("\n");
		packet.append(entityVars.toString());
		packet.append(method + "\n");
		packet.append(payload);
		packet.append("|\n");
		return packet.toString();
	}
	
	
}
