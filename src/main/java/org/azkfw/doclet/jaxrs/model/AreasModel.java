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
package org.azkfw.doclet.jaxrs.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @since 1.0.0
 * @version 1.0.0 2015/02/03
 * @author kawakicchi
 */
public class AreasModel {

	private String path;
	private Set<String> consumes;
	private Set<String> produces;

	public AreasModel() {
		path = null;
		consumes = new HashSet<String>();
		produces = new HashSet<String>();
	}

	public void setPath(final String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void addConsume(final String consume) {
		consumes.add(consume);
	}

	public void addConsumes(final Collection<String> consumes) {
		this.consumes.addAll(consumes);
	}

	public List<String> getConsumes() {
		return new ArrayList<String>(consumes);
	}

	public void addProduce(final String produce) {
		produces.add(produce);
	}

	public void addProduces(final Collection<String> produces) {
		this.produces.addAll(produces);
	}

	public List<String> getProduces() {
		return new ArrayList<String>(produces);
	}
}
