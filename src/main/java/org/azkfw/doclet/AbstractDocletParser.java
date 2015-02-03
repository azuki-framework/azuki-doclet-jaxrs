/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.azkfw.doclet;

import java.util.ArrayList;
import java.util.List;

import com.sun.javadoc.RootDoc;

/**
 * @since 1.0.0
 * @version 1.0.0 2015/02/03
 * @author kawakicchi
 */
public abstract class AbstractDocletParser implements DocletParser {

	private final DocletParserEvent event;
	private final List<DocletParserListener> listeners;

	public AbstractDocletParser() {
		event = new DocletParserEvent(this);
		listeners = new ArrayList<DocletParserListener>();
	}

	@Override
	public final void addListener(final DocletParserListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	@Override
	public final void removeListener(DocletParserListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	@Override
	public final void parse(final RootDoc root) throws DocletParserException {
		try {
			synchronized (listeners) {
				for (DocletParserListener listener : listeners) {
					listener.docletParserStarted(event);
				}
			}
			doParse(root);
		} finally {
			synchronized (listeners) {
				for (DocletParserListener listener : listeners) {
					listener.docletParserFinished(event);
				}
			}
		}
	}

	protected abstract void doParse(final RootDoc root) throws DocletParserException;
}
