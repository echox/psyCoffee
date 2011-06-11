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

import java.util.List;

import org.junit.Test;
import org.oark.psycoffee.core.constants.Operators;

public class VarCollectionTest {

	@Test
	public void testGetListKey() {
		String key = "foo";
		String listKey = VarCollection.getListKey(key);
		assertEquals("_list_foo", listKey);
		
		key = "_bar";
		listKey = VarCollection.getListKey(key);
		assertEquals("_list_bar",listKey);
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
	
	@Test
	public void testAddVar() {
		VarCollection collection = new VarCollection();
		
		collection.addVar("key1", "value1");
		collection.addVar("key2", "value2", Operators.PERSIST);
		collection.addVar("key3", "value3", Operators.ADD);
		collection.addVar("val", "list1", Operators.CURRENT);
		collection.addVar("val", "list2", Operators.PERSIST);
		collection.addVar("_list_single", "single", Operators.DELETE);
		
		assertEquals("value1",collection.getVarValue("key1").getValue());
		assertEquals(Operators.CURRENT, collection.getVarValue("key1").getOperator());
		assertEquals("value2", collection.getVarValue("key2").getValue());
		assertEquals(Operators.PERSIST, collection.getVarValue("key2").getOperator());
		
		List<VarValue> list = collection.getVarValues("key3");
		assertEquals(1, list.size());
		assertEquals("value3", list.get(0).getValue());
		assertEquals(Operators.ADD, list.get(0).getOperator());
		
		assertNull(collection.getVarValue("val"));
		assertNull(collection.getVarValue("_list_val"));
		assertEquals(2, collection.getVarValues("_list_val").size());
		assertEquals("list1", collection.getVarValues("_list_val").get(0).getValue());
		assertEquals(Operators.PERSIST, collection.getVarValues("_list_val").get(0).getOperator());
		assertEquals("list2",collection.getVarValues("_list_val").get(1).getValue());
		assertEquals(Operators.PERSIST, collection.getVarValues("_list_val").get(1).getOperator());
		
		assertNull(collection.getVarValue("_list_single"));
		assertEquals(1, collection.getVarValues("_list_single").size());
		assertEquals("single", collection.getVarValues("_list_single").get(0).getValue());
		assertEquals(Operators.DELETE, collection.getVarValues("_list_single").get(0).getOperator());
	}
	
	@Test
	public void testGetVar() {
		VarCollection collection = new VarCollection();

		collection.addVar("key1", "value1", Operators.PERSIST);

		assertEquals(1,collection.getVarValues("key1").size());
		assertEquals("value1", collection.getVarValues("key1").get(0).getValue());
		assertEquals(Operators.PERSIST, collection.getVarValues("key1").get(0).getOperator());
	}
	
	//TODO add test for setList
	//TODO add test for addList
	//TODO add test for setVar
	//TODO add test for removeValue
	//TODO add test for removeVar
	
	//TODO maybe add detailed test for toString
}
