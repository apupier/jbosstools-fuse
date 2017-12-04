/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.fusesource.ide.camel.model.service.core.util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.maven.model.Dependency;

public class FuseBomFilter implements Predicate<Dependency> {
	
	private static Set<Dependency> possibleBoms = new HashSet<>();
	
	static {
		initPossibleBomWith("org.jboss.fuse.bom", "jboss-fuse-parent");
		initPossibleBomWith("org.wildfly.camel", "wildfly-camel-bom");
		initPossibleBomWith("org.jboss.fuse", "jboss-fuse-parent");
		initPossibleBomWith("io.fabric8", "fabric8-project-bom-camel-spring-boot");
	}
	
	private static void initPossibleBomWith(String groupId, String artifactId) {
		Dependency bom = new Dependency();
		bom.setGroupId(groupId);
		bom.setArtifactId(artifactId);
		bom.setType("pom");
		possibleBoms.add(bom);
	}
	

	@Override
	public boolean test(Dependency dependency) {
		return possibleBoms.stream().filter(
				possibleBom -> 
				possibleBom.getArtifactId().equals(dependency.getArtifactId()) && possibleBom.getGroupId().equals(dependency.getGroupId()))
				.findAny()
				.isPresent();
	}
	
	
}