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
