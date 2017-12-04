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

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.internal.index.IIndex;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.ui.internal.search.util.IndexSearchEngine;
import org.eclipse.m2e.core.ui.internal.search.util.Packaging;
import org.fusesource.ide.camel.model.service.core.internal.CamelModelServiceCoreActivator;

public class OnlineBomVersionSearcher {
	
	public String findLatestBomVersionOnAvailableRepo(IProject project, IProgressMonitor monitor) {
		SubMonitor subMon = SubMonitor.convert(monitor, 3);
		String bomVersion = null;
		try {
			IMavenProjectFacade mavenProjectFacade = new CamelMavenUtils().getMavenProjectFacade(project);
			MavenProject mavenProject = mavenProjectFacade.getMavenProject(subMon.split(1));
			
			Dependency bomUsed = retrieveAnyFuseBomUsed(mavenProject);
			
			if(bomUsed != null) {
				//search with m2e Index, it goes faster in case m2e indexing is activated
				IIndex index = MavenPlugin.getIndexManager().getIndex(project);
				IndexSearchEngine indexSearchEngine = new IndexSearchEngine(index);
				Collection<String> versions = indexSearchEngine.findVersions(bomUsed.getGroupId(), bomUsed.getArtifactId(), null, Packaging.POM);
				subMon.setWorkRemaining(2);
				if(!versions.isEmpty()) {
					bomVersion = versions.iterator().next();
				} else {
					//search with Aether APi
					bomVersion = MavenPlugin.getMaven().createExecutionContext().execute(mavenProject, new SearchLatestBomVersionAvailableM2ECallable(mavenProject, bomUsed), subMon.split(1));
				}
			}
		} catch (CoreException e) {
			CamelModelServiceCoreActivator.pluginLog().logError(e);
		}
		subMon.setWorkRemaining(0);
		return bomVersion;
	}

	public Dependency retrieveAnyFuseBomUsed(MavenProject mavenProject) {
		DependencyManagement dependencyManagement = mavenProject.getDependencyManagement();
		if(dependencyManagement != null) {
			List<Dependency> managedDependencies = dependencyManagement.getDependencies();
			if(managedDependencies != null) {
				Optional<Dependency> bomDependency = managedDependencies.stream()
				.filter(new FuseBomFilter())
				.findAny();
				if(bomDependency.isPresent()) {
					return bomDependency.get();
				}
			}
		}
		return null;
	}
	
}
