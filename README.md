azuki-doclet-jaxrs
=============

Azuki Framework JAX-RS doclet library



	<reporting>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-javadoc-plugin</artifactId>
				<version>2.4</version>
				<reportSets>
					<reportSet>
						<id>jax-rs</id>
						<reports>
							<report>javadoc</report>
						</reports>
						<configuration>
							<doclet>org.azkfw.doclet.jaxrs.JAXRSDoclet</doclet>
							<name>JAX-RSDocs</name>
							<destDir>jaxrsdoc</destDir>
							<encoding>UTF-8</encoding>
							<charset>UTF-8</charset>
							<docletPath>.;${project.build.outputDirectory};${m2Repository}/javax/ws/rs/javax.ws.rs-api/2.0/javax.ws.rs-api-2.0.jar;${m2Repository}/org/apache/poi/poi/3.10-FINAL/poi-3.10-FINAL.jar;${m2Repository}/org/apache/poi/poi-ooxml/3.10-FINAL/poi-ooxml-3.10-FINAL.jar;${m2Repository}/org/apache/poi/poi-ooxml-schemas/3.10-FINAL/poi-ooxml-schemas-3.10-FINAL.jar;${m2Repository}/org/apache/xmlbeans/xmlbeans/2.3.0/xmlbeans-2.3.0.jar;${m2Repository}/dom4j/dom4j/1.6.1/dom4j-1.6.1.jar;
							</docletPath>
							<docletArtifacts>
								<docletArtifact>
									<groupId>org.azkfw</groupId>
									<artifactId>azuki-doclet-jaxrs</artifactId>
									<version>0.0.1</version>
								</docletArtifact>
								<docletArtifact>
									<groupId>javax.ws.rs</groupId>
									<artifactId>javax.ws.rs-api</artifactId>
									<version>2.0</version>
								</docletArtifact>
								<docletArtifact>
									<groupId>org.apache.poi</groupId>
									<artifactId>poi</artifactId>
									<version>3.10-FINAL</version>
								</docletArtifact>
								<docletArtifact>
									<groupId>org.apache.poi</groupId>
									<artifactId>poi-ooxml-schemas</artifactId>
									<version>3.10-FINAL</version>
								</docletArtifact>
								<docletArtifact>
									<groupId>org.apache.poi</groupId>
									<artifactId>poi-ooxml</artifactId>
									<version>3.10-FINAL</version>
								</docletArtifact>
								<docletArtifact>
									<groupId>org.apache.xmlbeans</groupId>
									<artifactId>xmlbeans</artifactId>
									<version>2.3.0</version>
								</docletArtifact>
								<docletArtifact>
									<groupId>dom4j</groupId>
									<artifactId>dom4j</artifactId>
									<version>1.6.1</version>
								</docletArtifact>
							</docletArtifacts>
						</configuration>
					</reportSet>
				</reportSets>
			</plugin>
		</plugins>
	</reporting>


    mvn site -Ddependency.locations.enabled=false

