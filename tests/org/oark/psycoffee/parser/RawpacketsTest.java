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

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.oark.psycoffee.core.Packet;

//TODO rewrite this into a real junit test ;-)

public class RawpacketsTest {
	
	QueueParser parser = new QueueParser(); 
	
	int work = 0;
	int fail = 0;
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void testEmptyPacket() {
	
		parser.parse("|");
		
		assertEquals(1,parser.getQueue().size());
		
		Packet packet = parser.getQueue().poll().packet;
		assertTrue(packet.getRoutingVars().isEmpty());
		assertTrue(packet.getEntityVars().isEmpty());
		assertTrue(packet.getPayload() == null);
		assertTrue(packet.getMethod() == null);
		
	}
	
	private void testParsingOfPacket(File file) throws IOException {
		FileInputStream fis = null;
	    BufferedInputStream bis = null;
	    DataInputStream dis = null;

	      fis = new FileInputStream(file);

	      // Here BufferedInputStream is added for fast reading.
	      bis = new BufferedInputStream(fis);
	      dis = new DataInputStream(bis);

	      StringBuffer raw = new StringBuffer();
	      while (dis.available() != 0) {
	    	  raw.append((char) dis.read());
	      }
	      System.out.println(file.getName() + " parsing...");
	      parser.parse(raw.toString());
	      Packet packet = new Packet();
	      boolean match = false;
	      if(parser.getQueue().size() != 0) {
	    	  packet = parser.getQueue().poll().packet;
	    	  match = packet.toString().equals(raw.toString());
	      }
	      
	      System.out.println("["+file.getName() +  "] matches: " + match);
	      
	      if(match) {
	    	  work++;
	      } else{
	    	  fail++;
	      }
	      
	      /*
	      if(!match) {
		      System.out.println("----[RAW");
		      System.out.println(raw);
		      System.out.println("----[PACKET");
		      System.out.println(packet);
		      System.out.println("----");
	      }
	      */
	      
	      // dispose all the resources after using them.
	      fis.close();
	      bis.close();
	      dis.close();
	   }

	
	
	
	public void parsePackets() throws IOException {

		
	    File dir = new File("doc/rawpackets/");
	    File[] files = dir.listFiles();
	    for(int i=0; i < files.length; i++) {
	    	try {
	    		testParsingOfPacket(files[i]);
	    	} catch (IOException e) {
	    		System.out.println("error reading " + files[i].getName());
	    	}
	    }
	    System.out.println("Result: " + work + "/" + (work+fail)+ " working (hint: incorrect packets should'nt work)");
	    
	}
	    
}
