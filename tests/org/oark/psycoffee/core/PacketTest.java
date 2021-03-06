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

import static org.junit.Assert.*;

import org.junit.Test;

public class PacketTest {

	@Test
	public void testToString() {
		
		String example = ":_source\tpsyc://example.symlynX.com/~fippo\n"
		   + ":_target\tpsyc://ente.aquarium.example.org:-32872\n"
		   + "\n"
		   + ":_nick\tfippo\n"
		   + "_info_nickname\n"
		   + "Hello [_nick].\n"
		   + "|\n";
		
		Packet packet = new Packet();
		packet.setPayload("Hello [_nick].\n");
		packet.getEntityVars().setVar("_nick", "fippo");
		packet.setMethod("_info_nickname");
		packet.getRoutingVars().setVar("_source", "psyc://example.symlynX.com/~fippo");
		packet.getRoutingVars().setVar("_target", "psyc://ente.aquarium.example.org:-32872");
		
		assertEquals(example, packet.toString());
//		System.out.println(packet);
	}

}
