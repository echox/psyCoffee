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
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.oark.psycoffee.core.Packet;
import org.oark.psycoffee.core.VarValue;

//TODO rewrite this into a real junit test ;-)

public class RawpacketsTest {
	
	QueueParser parser = new QueueParser(); 
	
	int work = 0;
	int fail = 0;
	
	@Before
	public void setup() {
		
	}
	
	private Packet parse(String raw) {
		parser.parse(raw);
		assertEquals(1,parser.getQueue().size());
		Packet packet = parser.getQueue().poll().packet;
		return packet;
	}
	
	@Test
	public void testEmptyPacket() {
		
		Packet packet = parse("|");
		
		assertTrue(packet.getRoutingVars().isEmpty());
		assertTrue(packet.getEntityVars().isEmpty());
		assertTrue(packet.getPayload().isEmpty());
		assertTrue(packet.getMethod().isEmpty());
	}
	
	@Test
	public void testBodyOnly() {
		
		String raw = "\n" + 
			"_message_private\n" +
			"OHAI\n" +
			"|\n";
		
		Packet packet = parse(raw);
		
		assertTrue(packet.getRoutingVars().isEmpty());
		assertTrue(packet.getEntityVars().isEmpty());
		assertEquals("_message_private", packet.getMethod());
		assertEquals("OHAI\n", packet.getPayload());
	}
	
	@Test
	public void testMethodOnly() {
		String raw = "\n" + 
			"_notice_foo_bar\n" +
			"|\n";
		
		Packet packet = parse(raw);
		
		assertTrue(packet.getRoutingVars().isEmpty());
		assertTrue(packet.getEntityVars().isEmpty());
		assertEquals("_notice_foo_bar",packet.getMethod());
		assertTrue(packet.getPayload().isEmpty());
	}
	
	@Test
	public void testMethodOnly1() {
		String raw ="\n" +
			"_\n" +
			"|\n";
		
		Packet packet = parse(raw);
		
		assertTrue(packet.getRoutingVars().isEmpty());
		assertTrue(packet.getEntityVars().isEmpty());
		assertEquals("_",packet.getMethod());
		assertTrue(packet.getPayload().isEmpty());
	}
	
	@Test
	public void testNoContent() {
		
		String raw = ":_source\tpsyc://foo.example.com/\n" +
			":_target\tpsyc://bar.example.com/\n"+
			"|\n";
		
		Packet packet = parse(raw);
		
		assertEquals("psyc://foo.example.com/", packet.getRoutingVars().getVarValue("_source").getValue());
		assertEquals(":", packet.getRoutingVars().getVarValue("_source").getOperator());
		assertEquals("psyc://bar.example.com/", packet.getRoutingVars().getVarValue("_target").getValue());
		assertEquals(":", packet.getRoutingVars().getVarValue("_target").getOperator());
		assertTrue(packet.getEntityVars().isEmpty());
		
		assertTrue(packet.getMethod().isEmpty());
		assertTrue(packet.getPayload().isEmpty());
	}
	
	@Test
	public void testNoData() {
		String raw = ":_source\tpsyc://foo.example.com/\n" +
				":_target\tpsyc://bar.example.com/\n" +
				"\n" +
				"_notice_foo_bar\n" +
				"|\n";

		Packet packet = parse(raw);
		
		assertEquals("psyc://foo.example.com/", packet.getRoutingVars().getVarValue("_source").getValue());
		assertEquals(":", packet.getRoutingVars().getVarValue("_source").getOperator());
		assertEquals("psyc://bar.example.com/", packet.getRoutingVars().getVarValue("_target").getValue());
		assertEquals(":", packet.getRoutingVars().getVarValue("_target").getOperator());
		assertTrue(packet.getEntityVars().isEmpty());
		
		assertTrue(packet.getMethod().equals("_notice_foo_bar"));
		assertTrue(packet.getPayload().isEmpty());
	}
	
	@Test
	public void testNoEntity() {
		String raw =":_source\tpsyc://foo.example.com/\n" +
		":_target\tpsyc://bar.example.com/\n" +
		"\n" +
		"_message_private\n" +
		"OHAI\n" +
		"|\n";
		
		Packet packet = parse(raw);
		
		assertEquals("psyc://foo.example.com/", packet.getRoutingVars().getVarValue("_source").getValue());
		assertEquals(":", packet.getRoutingVars().getVarValue("_source").getOperator());
		assertEquals("psyc://bar.example.com/", packet.getRoutingVars().getVarValue("_target").getValue());
		assertEquals(":", packet.getRoutingVars().getVarValue("_target").getOperator());
		assertTrue(packet.getEntityVars().isEmpty());
		
		assertTrue(packet.getMethod().equals("_message_private"));
		assertTrue(packet.getPayload().equals("OHAI\n"));
	}
	
	@Test
	public void testNoRouting() {
		String raw = "\n" +
		":_foo\tbar\n" +
		"_notice_foo_bar\n" + 
		"|\n";

		Packet packet = parse(raw);
		
		assertTrue(packet.getRoutingVars().isEmpty());
		assertEquals("bar", packet.getEntityVars().getVarValue("_foo").getValue());
		assertEquals("_notice_foo_bar",packet.getMethod());
		assertTrue(packet.getPayload().isEmpty());
	}
	
	@Test
	public void testNoValue() {
		String raw =":_source\tpsyc://foo.example.com/\n" +
		":_target\tpsyc://bar.example.com/\n" +
		"\n" +
		":_foo\t\n" +
		"_message_private\n" +
		"OHAI\n" +
		"|\n";
		
		Packet packet = parse(raw);
		
		assertEquals("psyc://foo.example.com/", packet.getRoutingVars().getVarValue("_source").getValue());
		assertEquals(":", packet.getRoutingVars().getVarValue("_source").getOperator());
		assertEquals("psyc://bar.example.com/", packet.getRoutingVars().getVarValue("_target").getValue());
		assertEquals(":", packet.getRoutingVars().getVarValue("_target").getOperator());
		assertFalse(packet.getEntityVars().isEmpty());
		assertEquals(":",packet.getEntityVars().getVarValue("_foo").getOperator());
		assertEquals("", packet.getEntityVars().getVarValue("_foo").getValue());
		
		assertTrue(packet.getMethod().equals("_message_private"));
		assertTrue(packet.getPayload().equals("OHAI\n"));
		
		raw =":_source\tpsyc://foo.example.com/\n" +
		":_target\tpsyc://bar.example.com/\n" +
		"\n" +
		":_foo\n" +
		"_message_private\n" +
		"OHAI\n" +
		"|\n";
		
		packet = parse(raw);
		
		assertEquals("psyc://foo.example.com/", packet.getRoutingVars().getVarValue("_source").getValue());
		assertEquals(":", packet.getRoutingVars().getVarValue("_source").getOperator());
		assertEquals("psyc://bar.example.com/", packet.getRoutingVars().getVarValue("_target").getValue());
		assertEquals(":", packet.getRoutingVars().getVarValue("_target").getOperator());
		assertFalse(packet.getEntityVars().isEmpty());
		assertEquals(":",packet.getEntityVars().getVarValue("_foo").getOperator());
		assertEquals("", packet.getEntityVars().getVarValue("_foo").getValue());
		
		assertTrue(packet.getMethod().equals("_message_private"));
		assertTrue(packet.getPayload().equals("OHAI\n"));
	}
	
	@Test
	public void testContextEnter() {
		String raw = ":_target\tpsyc://p5B084547.dip.t-dialin.net/@test\n" +
			":_source\tsomething"+
			"\n" +
			"_request_context_enter\n" +
			"|\n";
		
		Packet packet = parse(raw);
		
		assertEquals("psyc://p5B084547.dip.t-dialin.net/@test", packet.getRoutingVars().getVarValue("_target").getValue());
		assertEquals(":", packet.getRoutingVars().getVarValue("_target").getOperator());
		assertEquals("something", packet.getRoutingVars().getVarValue("_source").getValue());
		assertEquals(":", packet.getRoutingVars().getVarValue("_source").getOperator());
		assertTrue(packet.getEntityVars().isEmpty());
		
		assertTrue(packet.getMethod().equals("_request_context_enter"));
		assertTrue(packet.getPayload().isEmpty());
	}
	
	@Test
	public void testLength() {
		String raw = ":_source\tpsyc://foo/~bar\n" +
			":_target\tpsyc://bar/~baz\n"+
			":_tag\tsch1828hu3r2cm\n"+
			"86\n"+
			":_foo\tbar baz\n"+
			":_abc_def 11\tfoo bar\n"+
			"baz\n"+
			":_foo_bar\tyay\n"+
			"_message_foo_bar\n"+
			"ohai there!\n"+
			"\\o/\n"+
			"|\n";
		
		Packet packet = parse(raw);
		
		assertEquals("psyc://foo/~bar",packet.getRoutingVars().getVarValue("_source").getValue());
		assertEquals(":",packet.getRoutingVars().getVarValue("_source").getOperator());
		assertEquals("psyc://bar/~baz",packet.getRoutingVars().getVarValue("_target").getValue());
		assertEquals(":",packet.getRoutingVars().getVarValue("_target").getOperator());
		assertEquals("sch1828hu3r2cm",packet.getRoutingVars().getVarValue("_tag").getValue());
		assertEquals(":",packet.getRoutingVars().getVarValue("_tag").getOperator());
		assertEquals(86, packet.getEntityLength());
		
		assertEquals("bar baz",packet.getEntityVars().getVarValue("_foo").getValue());
		assertEquals(":",packet.getEntityVars().getVarValue("_foo").getOperator());
		assertEquals("foo bar\nbaz",packet.getEntityVars().getVarValue("_abc_def").getValue());
		assertEquals(":",packet.getEntityVars().getVarValue("_abc_def").getOperator());
		//TODO include length for vars and add assert here
		
		assertEquals("ohai there!\n\\o/\n", packet.getPayload());
	
		
	}
	
	@Test
	public void testOnlyVarLength() {
		String raw = ":_source\tpsyc://foo/~bar\n" +
			":_target\tpsyc://bar/~baz\n"+
			":_tag\tsch1828hu3r2cm\n"+
			"\n"+
			":_foo\tbar baz\n"+
			":_abc_def 11\tfoo bar\n"+
			"baz\n"+
			":_foo_bar\tyay\n"+
			"_message_foo_bar\n"+
			"ohai there!\n"+
			"\\o/\n"+
			"|\n";
		
		Packet packet = parse(raw);
		
		assertEquals("psyc://foo/~bar",packet.getRoutingVars().getVarValue("_source").getValue());
		assertEquals(":",packet.getRoutingVars().getVarValue("_source").getOperator());
		assertEquals("psyc://bar/~baz",packet.getRoutingVars().getVarValue("_target").getValue());
		assertEquals(":",packet.getRoutingVars().getVarValue("_target").getOperator());
		assertEquals("sch1828hu3r2cm",packet.getRoutingVars().getVarValue("_tag").getValue());
		assertEquals(":",packet.getRoutingVars().getVarValue("_tag").getOperator());

		assertEquals("bar baz",packet.getEntityVars().getVarValue("_foo").getValue());
		assertEquals(":",packet.getEntityVars().getVarValue("_foo").getOperator());
		assertEquals("foo bar\nbaz",packet.getEntityVars().getVarValue("_abc_def").getValue());
		assertEquals(":",packet.getEntityVars().getVarValue("_abc_def").getOperator());
		//TODO include length for vars and add assert here
		
		assertEquals("ohai there!\n\\o/\n", packet.getPayload());
	}
	
	@Test
	public void testUTF8() {
		String raw = ":_source\tpsyc://foo/~bar\n" +
			":_target\tpsyc://bar/~baz\n"+
			":_tag\tsch1828hu3r2cm\n"+
			"\n"+
			":_foo\tbar baz\n"+
			":_abc_def 15\tfóö bär\n"+
			"báz\n"+
			":_foo_bar\tyay\n"+
			"_message_foo_bar\n"+
			"ohai there!\n"+
			"\\o/\n"+
			"|\n";
		
		Packet packet = parse(raw);
		
		assertEquals("psyc://foo/~bar",packet.getRoutingVars().getVarValue("_source").getValue());
		assertEquals(":",packet.getRoutingVars().getVarValue("_source").getOperator());
		assertEquals("psyc://bar/~baz",packet.getRoutingVars().getVarValue("_target").getValue());
		assertEquals(":",packet.getRoutingVars().getVarValue("_target").getOperator());
		assertEquals("sch1828hu3r2cm",packet.getRoutingVars().getVarValue("_tag").getValue());
		assertEquals(":",packet.getRoutingVars().getVarValue("_tag").getOperator());
		
		assertEquals("bar baz",packet.getEntityVars().getVarValue("_foo").getValue());
		assertEquals(":",packet.getEntityVars().getVarValue("_foo").getOperator());
		assertEquals("fóö bär\nbáz",packet.getEntityVars().getVarValue("_abc_def").getValue());
		assertEquals(":",packet.getEntityVars().getVarValue("_abc_def").getOperator());
		//TODO include length for vars and add assert here
		
		assertEquals("ohai there!\n\\o/\n", packet.getPayload());
	}
	
	@Test
	public void testCircuit() {
		String raw = ":_list_understand_modules\t|_state|_fragments|_context" +
			"\n" +
			"_request_features\n" +
			"|\n";
		
		Packet packet = parse(raw);
		
		List<VarValue> modules = packet.getRoutingVars().getVarValues("_list_understand_modules");
		assertEquals(":",packet.getRoutingVars().getVarValue("_list_understand_modules").getOperator());
		
		boolean state = false;
		boolean fragments = false;
		boolean context = false;
		
		for (VarValue varValue : modules) {
			if ("_state".equals(varValue.getValue())) {
				state = true;
			} else if("_fragments".equals(varValue.getValue())) {
				fragments = true;
			} else if("_context".equals(varValue.getValue())) {
				context = true;
			}
		}
		
		assertTrue(state);
		assertTrue(fragments);
		assertTrue(context);
		
		assertEquals("_request_features", packet.getPayload());
		assertTrue(packet.getEntityVars().isEmpty());
		assertTrue(packet.getPayload().isEmpty());
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
