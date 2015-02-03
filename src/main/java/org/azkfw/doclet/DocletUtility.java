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

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationDesc.ElementValuePair;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.Tag;

/**
 * @since 1.0.0
 * @version 1.0.0 2015/02/03
 * @author kawakicchi
 */
public final class DocletUtility {

	public static Object getAnnotationValue(final AnnotationDesc annotation) {
		return getAnnotationValue(annotation, "value");
	}

	public static Object getAnnotationValue(final AnnotationDesc annotation, final String name) {
		for (final ElementValuePair elementValuePair : annotation.elementValues()) {
			if (elementValuePair.element().name().equals(name)) {
				return elementValuePair.value().value();
			}
		}
		return null;
	}

	public static AnnotationDesc findAnnotation(final AnnotationDesc[] annotations, final Class<?>... classes) {
		if (null != annotations && null != classes) {
			for (final AnnotationDesc annotation : annotations) {
				AnnotationTypeDoc docAnnotation = annotation.annotationType();
				for (final Class<?> clazz : classes) {
					if (docAnnotation.qualifiedName().equals(clazz.getName())) {
						return annotation;
					}
				}
			}
		}
		return null;
	}

	public static String getTagText(final Tag[] tags, final String name) {
		for (final Tag tag : tags) {
			if (tag.name().equals(name)) {
				return tag.text();
			}
		}
		return null;
	}

}
