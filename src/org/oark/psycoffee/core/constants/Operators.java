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
package org.oark.psycoffee.core.constants;


//TODO rethink this
/**
 * collected constants for operators
 * 
 * try to use the Operator enum if possible
 * 
 * @author Simon Koelsch
 *
 */
public interface Operators {
	
	final static String PERSIST = "=";
	final static String CURRENT = ":";
	final static String ADD = "+";
	final static String DELETE = "-";
	
	public static final String[] ALL_OPERATORS = {
		PERSIST,
		CURRENT,
		ADD,
		DELETE,
		"?","!","$","@","%","&","*","/","#",";",","
	};
	
}
