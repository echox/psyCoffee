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
	protected int getStringByBytes(int bytes, StringBuffer target, String raw[], int idx) {
		
		int bytesToRead = bytes;
		String[] lines = raw.clone();
		
		//quickhack
		lines[idx] = lines[idx].split("\t")[1];
		
		int lineIdx;
		for(lineIdx = idx; bytesToRead > 0; lineIdx++) {
			String line = lines[lineIdx];
			if (line.getBytes().length < bytesToRead) {
				target.append(line+"\n");
				bytesToRead -= line.getBytes().length+1;
			} else if (line.getBytes().length == bytesToRead) {
				target.append(line);
				bytesToRead -= line.getBytes().length;
			} else {
				byte[] tmp;
				tmp = line.getBytes();
				byte[] part = new byte[bytesToRead];
				for(int i=0;i<bytesToRead;i++) {
					part[i] = tmp[i];
				}
				target.append(new String(part));
				bytesToRead = 0;
			}
		}
		
		return lineIdx;
	}
	
	//TODO write junit tests
	protected int parseVar(String var, VarCollection vars, String raw[], int idx) {
		
		String first = "";
		if(!"".equals(var)) {
			first = var.substring(0,1);
		}
		if (isOperator(first)) {
			
			int size = 0;
			String lineParts[] = var.split("\t");
			
			String operator = lineParts[0].substring(0,1) ;
			
			String name = lineParts[0].substring(1);
			String nameParts[] = name.split("\\s");
			if(nameParts.length == 2) {
				if (nameParts[1].matches("[0-9]*")) {
					size = new Integer(nameParts[1]);
				} else {
					//TODO PANIC MODE! SOMETHING BAD HAPPENED!!! BROKEN VAR!!! \o/
				}
				name = nameParts[0];
			}
			
			StringBuffer value = new StringBuffer();
			if(nameParts.length == 2) {
				idx = getStringByBytes(size, value, raw, idx);
			} else {
				if(lineParts.length == 2) {
					value.append(lineParts[1]);
				}
			}
			
			vars.addVar(name,value.toString(),operator);
		}
		
		return idx;
		
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
			
			i = parseVar(line, vars,raw,i);
			
		}
		return i;
	}
	
	public void parse(String raw, Context context) {
		
		//TODO cleanup this mess! :-)
		
		String[] lines = raw.split("\n");
		
		boolean gotMethod = false;
		Packet packet = new Packet();
		packet.setPayload("");
		packet.setMethod("");
		VarCollection vars = packet.getRoutingVars();
		StringBuffer payload = new StringBuffer();
		long bytesToRead = 0;
		int entityStart = 0;
		
		for (int lineIdx = 0; lineIdx < lines.length; lineIdx++) {

				if (gotMethod == false) {
					lineIdx = parseVars(lines,vars,lineIdx);
				}
				
				String line = lines[lineIdx];	
					
				if(("".equals(line) || line.matches("[0-9]+")) && gotMethod == false) {
					if (gotMethod == false && line.matches("[0-9]+")) {
						packet.setEntityLength(new Long(line));
						bytesToRead = packet.getEntityLength();
						entityStart = lineIdx+1;
					}
					vars = packet.getEntityVars();
				} else if (isMethod(line) && gotMethod == false) {
					packet.setMethod(line);
					gotMethod = true;
					
					if(entityStart != 0) {
						//calc length to read
						for(int i = entityStart; i <= lineIdx; i++) {
							bytesToRead -= lines[i].getBytes().length + 1;
						}
					}
				} else if (gotMethod == true) {
					
					if(entityStart == 0) {
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
					} else if (bytesToRead > 0) {
						
						bytesToRead -= line.getBytes().length + 1;
						if (bytesToRead == 0) {
							payload.append(line);
						} else {
							payload.append(line+"\n");							
						}
					} else if (bytesToRead <= 0) {
						//TODO cleanup copy and paste section
						//finish packet and do callbacks
						packet.setPayload(payload.toString());
						dispatch(packet, context);
						
						//reset
						gotMethod = false;
						packet = new Packet();
						vars = packet.getRoutingVars();
						payload = new StringBuffer();
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
