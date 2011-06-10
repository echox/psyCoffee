package org.oark.psycoffee.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class PacketTest {

	@Test
	public void testToString() {
		
		String example = "|\n" 
		   + ":_source\tpsyc://example.symlynX.com/~fippo\n"
		   + ":_target\tpsyc://ente.aquarium.example.org:-32872\n"
		   + "\n"
		   + ":_nick\tfippo\n"
		   + "_info_nickname\n"
		   + "Hello [_nick].\n"
		   + "|";
		
		Entity entity = new Entity();
		entity.setData("Hello [_nick].\n");
		entity.getVars().setVar("_nick", "fippo");
		entity.setMethod("_info_nickname");
		
		Packet packet = new Packet();
		packet.setEntity(entity);
		packet.getRoutingHeader().setVar("_source", "psyc://example.symlynX.com/~fippo");
		packet.getRoutingHeader().setVar("_target", "psyc://ente.aquarium.example.org:-32872");
		
		assertEquals(example, packet.toString());
		System.out.println(packet);
	}

}
