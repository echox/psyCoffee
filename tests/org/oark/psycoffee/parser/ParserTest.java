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

import org.junit.Test;
import org.oark.psycoffee.core.constants.Operators;

public class ParserTest {

	@Test
	public void testIsMethod() {
		
		assertFalse(Parser.isMethod("85qjgqg"));
		assertFalse(Parser.isMethod("-_gqg"));
		assertFalse(Parser.isMethod("85?@#jgqg"));
		assertFalse(Parser.isMethod("?"));
		assertFalse(Parser.isMethod("_21	2!"));
		assertFalse(Parser.isMethod(" fwefwe"));
		
		assertTrue(Parser.isMethod("_method"));
		assertTrue(Parser.isMethod("_method_foo"));
		assertTrue(Parser.isMethod("_list"));
		assertTrue(Parser.isMethod("_list_foobar"));
	}
	
	@Test
	public void testIsOperator() {
		
		assertTrue(Parser.isOperator(Operators.ADD));
		assertTrue(Parser.isOperator(Operators.DELETE));
		assertTrue(Parser.isOperator(Operators.CURRENT));
		assertTrue(Parser.isOperator(Operators.PERSIST));
		assertTrue(Parser.isOperator("?"));
		
		assertFalse(Parser.isOperator("9"));
		assertFalse(Parser.isOperator("a"));
		assertFalse(Parser.isOperator("A"));
		assertFalse(Parser.isOperator("_"));
		assertFalse(Parser.isOperator("}"));
	}
}
