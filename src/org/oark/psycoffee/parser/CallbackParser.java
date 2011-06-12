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
package org.oark.psycoffee.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oark.psycoffee.core.Context;
import org.oark.psycoffee.core.Packet;
import org.oark.psycoffee.core.VarCollection;
import org.oark.psycoffee.core.constants.Operators;

public class CallbackParser {

	private List<Callback> callbacks = new ArrayList<Callback>();
	private Map<String,Context> contextMap = new HashMap<String,Context>();
	
	public CallbackParser() {
		super();
	}
	
	public CallbackParser(Callback callback) {
		addCallback(callback);
	}
	
	public CallbackParser(List<Callback> callbacks) {
		addCallbacks(callbacks);
	}
	
	public void addCallback(Callback callback) {
		this.callbacks.add(callback);
	}
	
	public void addCallbacks(List<Callback> callbacks) {
		this.callbacks.addAll(callbacks);
	}
	
	public boolean removeCallback(Callback callback) {
		return this.callbacks.remove(callback);
	}
	
	public boolean removeCallbacks(List<Callback> callbacks) {
		return this.callbacks.removeAll(callbacks);
	}

	public void parse(String raw) {
		parse(raw, null);
	}
	
	public void parse(String raw, Context context) {
		
		//TODO cleanup this mess! :-)
		
		//TODO parse input, create packet
		Packet packet = new Packet();
		
		//TODO replace with a more efficient method
		String[] lines = raw.split("\n");
		
		boolean inPacket = false;
		boolean gotMethod = false;
		VarCollection vars = packet.getRoutingVars();
		StringBuffer payload = new StringBuffer();
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			
			if(inPacket) {

				String first = "";
				if(!"".equals(line)) {
					first = line.substring(0,1);
				}
				if (isOperator(first)) {
					
					String lineParts[] = line.split("\t");
					String operator = lineParts[0].substring(0,1) ;
					String name = lineParts[0].substring(1);
					String value = lineParts[1];
					
					vars.addVar(name,value,operator);
					
				} else {
					
					//TODO add parsing of length
					if("".equals(line) && gotMethod == false) {
						vars = packet.getEntityVars();
					} else if ("_".equals(first) && gotMethod == false) {
						//TODO check method format
						packet.setMethod(line);
						gotMethod = true;
					} else if (gotMethod == true) {
						if ("|".equals(line)) {
							packet.setPayload(payload.toString());
							inPacket = false;
						} else {
							payload.append(line+"\n");
						}
					}
				}
				
			} else {
				if("|".equals(line)) {
					inPacket = true;
				}
			}
		}
		
		
		
		doCallbacks(packet, context);
			
	}
	
	private boolean isOperator(String operator) {

		//TODO should be a little more efficient ;-)
		
		for(int i=0; i < Operators.ALL_OPERATORS.length; i++) {
			if(Operators.ALL_OPERATORS[i].equals(operator)) {
				return true;
			}
		}
		return false;
	}

	private void doCallbacks(Packet packet, Context context) {
		for (Callback callback : callbacks) {
			callback.parsed(packet, context);
		}
	}
	
	public void addContext(Context context) {
		this.contextMap.put(context.getName(), context);
	}
	
	public void removeContext(String name) {
		this.contextMap.remove(name);
	}
	
}
