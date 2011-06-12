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

import java.util.ArrayList;
import java.util.List;

public class CallbackParser {

	private List<Callback> callbacks = new ArrayList<Callback>();
	
	public CallbackParser() {
		super();
	}
	
	public CallbackParser(Callback callback) {
		addCallback(callback);
	}
	
	public CallbackParser(List<Callback> callbacks) {
		addCallbacks(callbacks);
	}
	
	public void addCallback(Callback callback) {
		this.callbacks.add(callback);
	}
	
	public void addCallbacks(List<Callback> callbacks) {
		this.callbacks.addAll(callbacks);
	}
	
	public boolean removeCallback(Callback callback) {
		return this.callbacks.remove(callback);
	}
	
	public boolean removeCallbacks(List<Callback> callbacks) {
		return this.callbacks.removeAll(callbacks);
	}

	public void parse(String raw) {
		
	}
}
