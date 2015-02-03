package org.azkfw.doclet.jaxrs;

import org.azkfw.doclet.jaxrs.parser.JAXRSDocletParser;
import org.azkfw.doclet.jaxrs.writer.JAXRSXlsxDocletWriter;

import com.sun.javadoc.RootDoc;

public class JAXRSDoclet {

	public static boolean start(final RootDoc root) {
		JAXRSDoclet doclet = new JAXRSDoclet(root);
		return doclet.run();
	}

	private final RootDoc root;

	public JAXRSDoclet(final RootDoc root) {
		this.root = root;
	}

	public boolean run() {
		JAXRSXlsxDocletWriter writer = new JAXRSXlsxDocletWriter(new JAXRSDocletParser());
		writer.write(root);
		return true;
	}
}
