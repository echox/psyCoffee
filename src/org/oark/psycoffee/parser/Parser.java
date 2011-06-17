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

import java.util.regex.Pattern;

import org.oark.psycoffee.core.Context;
import org.oark.psycoffee.core.Packet;
import org.oark.psycoffee.core.VarCollection;
import org.oark.psycoffee.core.constants.Operators;

public abstract class Parser {
	
	public void parse(String raw) {
		parse(raw, null);
	}
	
	//TODO write junit tests
	//TODO parse var size
	protected void parseVar(String var, VarCollection vars) {
		
		String first = "";
		if(!"".equals(var)) {
			first = var.substring(0,1);
		}
		if (isOperator(first)) {
			
			long size = 0;
			String lineParts[] = var.split("\t");
			
			String operator = lineParts[0].substring(0,1) ;
			
			String name = lineParts[0].substring(1);
			String nameParts[] = name.split("\\s");
			if(nameParts.length == 2) {
				if (nameParts[1].matches("[0-9]*")) {
					size = new Long(nameParts[1]);
				} else {
					//TODO PANIC MODE! SOMETHING BAD HAPPENED!!! BROKEN VAR!!! \o/
				}
				name = nameParts[0];
			}
			
			String value = "";
			if(lineParts.length == 2) {
				value = lineParts[1];
			}
			
			vars.addVar(name,value,operator);
		}
		
	}
	
	
	//TODO write junit tests
	protected int parseVars(String raw[], VarCollection vars, int idx) {
		
		int i;
		for (i = idx; i < raw.length; i++) {
			String line = raw[i];
			
			if ("".equals(line) || line.matches("[0-9]*")) {
				break;
			} else if (isMethod(line)) {
				break;
			} else if ("|".equals(line)) {
				break;
			}
			
			parseVar(line, vars);
			
		}
		return i;
	}
	
	public void parse(String raw, Context context) {
		
		//TODO cleanup this mess! :-)
		
		//TODO replace with a more efficient method
		String[] lines = raw.split("\n");
		
		boolean gotMethod = false;
		Packet packet = new Packet();
		packet.setPayload("");
		packet.setMethod("");
		VarCollection vars = packet.getRoutingVars();
		StringBuffer payload = new StringBuffer();
		
		
		for (int i = 0; i < lines.length; i++) {

				if (gotMethod == false) {
					i = parseVars(lines,vars,i);
				}
				
				String line = lines[i];	
					
				if("".equals(line) && gotMethod == false) {
					vars = packet.getEntityVars();
				} else if (gotMethod == false && line.matches("[0-9]*")) {
					//TODO add parsing of length							
					packet.setEntityLength(new Long(line));
				} else if (isMethod(line) && gotMethod == false) {
					packet.setMethod(line);
					gotMethod = true;
				} else if (gotMethod == true) {
					if ("|".equals(line)) {
						
						//finish packet and do callbacks
						packet.setPayload(payload.toString());
						dispatch(packet, context);
						
						//reset
						gotMethod = false;
						packet = new Packet();
						vars = packet.getRoutingVars();
						payload = new StringBuffer();
					} else {
						payload.append(line+"\n");
					}
				} else {
					if("|".equals(line)) {
						dispatch(packet, context);
					}
				}
		}
		
	}
	
	abstract protected void dispatch(Packet packet, Context context);
	
	protected static boolean isMethod(String method) {
		
		//TODO should be a little more efficient ;-)
		
		 Pattern p = Pattern.compile("[a-zA-Z_]+");
		 return p.matcher(method).matches();
	}
	
	protected static boolean isOperator(String operator) {

		//TODO should be a little more efficient ;-)
		
		for(int i=0; i < Operators.ALL_OPERATORS.length; i++) {
			if(Operators.ALL_OPERATORS[i].equals(operator)) {
				return true;
			}
		}
		return false;
	}
}
