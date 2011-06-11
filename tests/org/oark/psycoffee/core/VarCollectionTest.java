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

public class VarCollectionTest {

	@Test
	public void testGetListKey() {
		String key = "foo";
		String listKey = VarCollection.getListKey(key);
		assertEquals(listKey, "_list_foo");
		
		key = "_bar";
		listKey = VarCollection.getListKey(key);
		assertEquals(listKey, "_list_bar");
	}
	
	@Test
	public void testIsListKey() {

		String key = "_list_key";
		String key2 = "_list_list";
		String noKey = "_";
		String noKey2 = "list_";
		String noKey3 = "_list124";
		String noKey4 = "_list";
		String noKey5 = "_list_";
		
		assertTrue(VarCollection.isListKey(key));
		assertTrue(VarCollection.isListKey(key2));
		assertFalse(VarCollection.isListKey(noKey));
		assertFalse(VarCollection.isListKey(noKey2));
		assertFalse(VarCollection.isListKey(noKey3));
		assertFalse(VarCollection.isListKey(noKey4));
		assertFalse(VarCollection.isListKey(noKey5));
	}
}
