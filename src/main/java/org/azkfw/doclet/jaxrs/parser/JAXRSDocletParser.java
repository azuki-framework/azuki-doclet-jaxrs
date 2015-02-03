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
package org.azkfw.doclet.jaxrs.parser;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.azkfw.doclet.AbstractDocletParser;
import org.azkfw.doclet.DocletParser;
import org.azkfw.doclet.DocletParserException;
import org.azkfw.doclet.DocletUtility;
import org.azkfw.doclet.jaxrs.model.APIModel;
import org.azkfw.doclet.jaxrs.model.AreasModel;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationValue;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;

/**
 * @since 1.0.0
 * @version 1.0.0 2015/02/03
 * @author kawakicchi
 */
public class JAXRSDocletParser extends AbstractDocletParser implements DocletParser {

	private final JAXRSDocletParserEvent event;
	private final List<JAXRSDocletParserListener> listeners;

	public JAXRSDocletParser() {
		event = new JAXRSDocletParserEvent(this);
		listeners = new ArrayList<JAXRSDocletParserListener>();
	}

	public final void addListener(final JAXRSDocletParserListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}

	public final void removeListener(final JAXRSDocletParserListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	@Override
	public void doParse(final RootDoc root) throws DocletParserException {
		ClassDoc[] classDocs = root.classes();
		for (ClassDoc classDoc : classDocs) {
			parseClassDoc(classDoc);
		}
	}

	private void parseClassDoc(final ClassDoc classDoc) {
		//System.out.println(classDoc.qualifiedName());
		// call event listener
		synchronized (listeners) {
			for (JAXRSDocletParserListener listener : listeners) {
				listener.jaxrsDocletParserFindClassDoc(event, classDoc);
			}
		}

		// get areas info
		AreasModel areas = new AreasModel();

		AnnotationDesc[] annotations = classDoc.annotations();
		areas.setPath(getPathValue(annotations));
		areas.addConsumes(getConsumesValue(annotations));
		areas.addProduces(getProducesValue(annotations));

		// method
		List<APIModel> apis = new ArrayList<APIModel>();
		MethodDoc[] methodDocs = classDoc.methods();
		for (MethodDoc methodDoc : methodDocs) {
			APIModel api = parseMethodDoc(classDoc, methodDoc, areas);
			if (null != api) {
				apis.add(api);
			}
		}

		if (0 < apis.size()) {
			synchronized (listeners) {
				for (JAXRSDocletParserListener listener : listeners) {
					listener.jaxrsDocletParserFindAreas(event, areas);
				}
			}
		}
	}

	private APIModel parseMethodDoc(final ClassDoc classDoc, final MethodDoc methodDoc, final AreasModel areas) {
		//System.out.println(methodDoc.qualifiedName());
		// call event listener
		synchronized (listeners) {
			for (JAXRSDocletParserListener listener : listeners) {
				listener.jaxrsDocletParserFindMethodDoc(event, classDoc, methodDoc);
			}
		}

		// get api info
		APIModel api = new APIModel();

		AnnotationDesc[] annotations = methodDoc.annotations();
		{ // path
			String path = getPathValue(annotations);
			if (null != path && 0 < path.length()) {
				if (null != areas.getPath() && 0 < areas.getPath().length()) {
					api.setPath(Paths.get(areas.getPath(), path).toString());
				} else {
					api.setPath(path);
				}
			} else {
				if (null != areas.getPath() && 0 < areas.getPath().length()) {
					api.setPath(Paths.get(areas.getPath()).toString());
				}
			}
		}
		api.addConsumes(areas.getConsumes());
		api.addConsumes(getConsumesValue(annotations));
		api.addProduces(areas.getProduces());
		api.addProduces(getProducesValue(annotations));
		api.addMethods(getMethodsValue(annotations));

		Tag[] tags = methodDoc.tags();
		api.setId(DocletUtility.getTagText(tags, "@id"));
		api.setName(DocletUtility.getTagText(tags, "@name"));

		if ((null != api.getPath() && 0 < api.getPath().length()) || (null != api.getMethods() && 0 < api.getMethods().size())) {
			synchronized (listeners) {
				for (JAXRSDocletParserListener listener : listeners) {
					listener.jaxrsDocletParserFindAPI(event, api);
				}
			}

			return api;
		}

		return null;
	}

	private Set<String> getMethodsValue(final AnnotationDesc[] annotations) {
		Set<String> result = new HashSet<String>();
		if (null != DocletUtility.findAnnotation(annotations, GET.class)) {
			result.add("GET");
		}
		if (null != DocletUtility.findAnnotation(annotations, POST.class)) {
			result.add("POST");
		}
		if (null != DocletUtility.findAnnotation(annotations, PUT.class)) {
			result.add("PUT");
		}
		if (null != DocletUtility.findAnnotation(annotations, DELETE.class)) {
			result.add("DELETE");
		}
		if (null != DocletUtility.findAnnotation(annotations, HEAD.class)) {
			result.add("HEAD");
		}
		return result;
	}

	private Set<String> getProducesValue(final AnnotationDesc[] annotations) {
		Set<String> result = new HashSet<String>();
		AnnotationDesc annotation = DocletUtility.findAnnotation(annotations, Produces.class);
		if (null != annotation) {
			AnnotationValue[] values = (AnnotationValue[]) DocletUtility.getAnnotationValue(annotation);
			if (null != values) {
				for (AnnotationValue value : values) {
					result.add(value.value().toString());
				}
			}
		}
		return result;
	}

	private Set<String> getConsumesValue(final AnnotationDesc[] annotations) {
		Set<String> result = new HashSet<String>();
		AnnotationDesc annotation = DocletUtility.findAnnotation(annotations, Consumes.class);
		if (null != annotation) {
			AnnotationValue[] values = (AnnotationValue[]) DocletUtility.getAnnotationValue(annotation);
			if (null != values) {
				for (AnnotationValue value : values) {
					result.add(value.value().toString());
				}
			}
		}
		return result;
	}

	private String getPathValue(final AnnotationDesc[] annotations) {
		AnnotationDesc annotation = DocletUtility.findAnnotation(annotations, Path.class);
		if (null != annotation) {
			String value = (String) DocletUtility.getAnnotationValue(annotation);
			if (null != value) {
				return value.toString();
			}
		}
		return null;
	}
}
