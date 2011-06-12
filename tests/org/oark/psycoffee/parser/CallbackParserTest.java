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

import org.junit.Test;
import static org.junit.Assert.*;
import org.oark.psycoffee.core.Context;
import org.oark.psycoffee.core.Packet;

public class CallbackParserTest {

	public static String example = "|\n" 
		   + ":_source\tpsyc://example.symlynX.com/~fippo\n"
		   + ":_target\tpsyc://ente.aquarium.example.org:-32872\n"
		   + "\n"
		   + ":_nick\tfippo\n"
		   + "_info_nickname\n"
		   + "Hello [_nick].\n"
		   + "\n"
		   + "more text\n"
		   + "|\n" 
		   + "######TRASH\n" 
		   +"|\n" 
		   + ":_source\tpsyc://example.symlynX.com/~fippo\n" 
		   + ":_target\tpsyc://ente.aquarium.example.org:-32872\n" 
		   + "\n" 
		   + ":_nick\tfippo\n" 
		   + "_info_nickname\n"
		   + "Hello [_nick].\n"
		   + "\n"
		   + "more text\n"
		   + "|";
	
	public static String onePacket = "|\n" 
		   + ":_source\tpsyc://example.symlynX.com/~fippo\n" 
		   + ":_target\tpsyc://ente.aquarium.example.org:-32872\n" 
		   + "\n" 
		   + ":_nick\tfippo\n" 
		   + "_info_nickname\n"
		   + "Hello [_nick].\n"
		   + "\n"
		   + "more text\n"
		   + "|";
	
	public class cb implements Callback {

		@Override
		public void parsed(Packet packet, Context context) {
			
			assertTrue(onePacket.equals(packet.toString()));
		}
		
	}
	
	//TODO remove later
	@Test
	public void quicktest() {
		
		CallbackParser parser = new CallbackParser();
		parser.addCallback(new cb());
		parser.parse(example);
	}
}
