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
package org.jboss.tools.fuse.quickstarts.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.egit.core.op.CloneOperation;
import org.eclipse.egit.core.op.CloneOperation.PostCloneTask;
import org.eclipse.egit.ui.internal.provisional.wizards.GitRepositoryInfo;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.eclipse.m2e.core.project.LocalProjectScanner;
import org.eclipse.m2e.core.project.MavenProjectInfo;
import org.eclipse.m2e.core.project.ProjectImportConfiguration;
import org.eclipse.m2e.core.ui.internal.wizards.ImportMavenProjectsJob;
import org.fusesource.ide.camel.editor.utils.BuildAndRefreshJobWaiterUtil;
import org.fusesource.ide.camel.tests.util.MarkerUtil;
import org.jboss.tools.openshift.internal.common.ui.application.importoperation.MavenProjectImportOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CheckGithubQuickstartIT {
	
	private static final String REFS_HEADS_MASTER = "refs/heads/master";
	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	private String gitRepo;
	private String name;
	private String referenceName;
	private String folderToClone;
	
	@Before
	public void setup() throws CoreException {
		ResourcesPlugin.getWorkspace().delete(ResourcesPlugin.getWorkspace().getRoot().getProjects(), true, new NullProgressMonitor());
	}
	
	@After
	public void tearDown() throws CoreException {
		ResourcesPlugin.getWorkspace().delete(ResourcesPlugin.getWorkspace().getRoot().getProjects(), true, new NullProgressMonitor());
	}
	
	@Parameters(name = "{0} - {2}")
	public static Collection<Object[]> data(){
		return Arrays.asList(
				new String[] {"wildfly", "wildfly-extras/wildfly-camel-examples", REFS_HEADS_MASTER, "."},
				new String[] {"fuse", "jboss-fuse/quickstarts", REFS_HEADS_MASTER, "."},
				new String[] {"camel",  "apache/camel", REFS_HEADS_MASTER, "examples"},
				new String[] {"fabric8 - spring-camel", "fabric8-quickstarts/spring-camel", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - spring-boot-camel", "fabric8-quickstarts/spring-boot-camel", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - spring-boot-camel-amq", "fabric8-quickstarts/spring-boot-camel-amq", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - spring-boot-camel-config", "fabric8-quickstarts/spring-boot-camel-config", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - spring-boot-camel-drools", "fabric8-quickstarts/spring-boot-camel-drools", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - spring-boot-camel-infinispan", "fabric8-quickstarts/spring-boot-camel-infinispan", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - spring-boot-camel-teiid", "fabric8-quickstarts/spring-boot-camel-teiid", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - spring-boot-camel-xml", "fabric8-quickstarts/spring-boot-camel-xml", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - cdi-camel", "fabric8-quickstarts/cdi-camel", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - cdi-camel-amq", "fabric8-quickstarts/cdi-camel-amq", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - cdi-camel-http-client", "fabric8-quickstarts/cdi-camel-http-client", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - cdi-camel-jetty", "fabric8-quickstarts/cdi-camel-amq", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - cdi-camel-swagger", "fabric8-quickstarts/cdi-camel-swagger", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - swarm-camel", "fabric8-quickstarts/swarm-camel", "refs/head/master", "."},
				new String[] {"fabric8 - karaf-camel-amq", "fabric8-quickstarts/karaf-camel-amq", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - karaf-camel-log", "fabric8-quickstarts/karaf-camel-log", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - karaf-camel-rest-sql", "fabric8-quickstarts/karaf-camel-rest-sql", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - karaf2-camel-amq", "fabric8-quickstarts/karaf2-camel-amq", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - karaf2-camel-log", "fabric8-quickstarts/karaf2-camel-log", REFS_HEADS_MASTER, "."},
				new String[] {"fabric8 - war-camel-servlet", "fabric8-quickstarts/war-camel-servlet", REFS_HEADS_MASTER, "."},
				new String[] {"rhtconsulting - fuse-quickstarts", "rhtconsulting/fuse-quickstarts","refs/heads/jboss-fuse-6.3", "."}
				);
	}
	
	
	public CheckGithubQuickstartIT(String name, String gitRepo, String referenceName, String folderToClone) {
		this.name = name;
		this.gitRepo = gitRepo;
		this.referenceName = referenceName;
		this.folderToClone = folderToClone;
	}
	
	@Test
	public void testImport() throws Exception {
		GitRepositoryInfo repositoryInfo = new GitRepositoryInfo("https://github.com/"+gitRepo);
		
		URIish uri = new URIish(repositoryInfo.getCloneUri());
		File workDir = tmp.newFolder(name);
		CloneOperation cloneOperation = new CloneOperation(uri, true, Collections.emptyList(), workDir, referenceName, "origin", 5*60*1000);
		cloneOperation.run(new NullProgressMonitor());
		
		importProjects(new File(workDir, folderToClone));
		
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		assertThat(projects).isNotEmpty();
		for (IProject project : projects) {
			new MarkerUtil().checkNoValidationError(project);
		}
		
		//check build working: mvn clean install? or something else?
		
		//check no warning in workspace (one day...)
		
		
	}
	
	private void importProjects(final File localClone) throws InterruptedException, NoWorkTreeException, IOException, CoreException {
		LocalProjectScanner projectScanner = getProjectScanner(localClone);
		projectScanner.run(new NullProgressMonitor());
		Collection<MavenProjectInfo> projects = projectScanner.getProjects();
		projects.addAll(findSubProjects(projects));
		ImportMavenProjectsJob importJob = new ImportMavenProjectsJob(projects, Collections.emptyList(), new ProjectImportConfiguration());
		importJob.setRule(MavenPlugin.getProjectConfigurationManager().getRule());


		importJob.schedule();
		importJob.join();
		new BuildAndRefreshJobWaiterUtil().waitJob(new NullProgressMonitor());
	}

	private Set<MavenProjectInfo> findSubProjects(Collection<MavenProjectInfo> projects) {
		Set<MavenProjectInfo> res = new HashSet<>();
		for (MavenProjectInfo mavenProjectInfo : projects) {
			Collection<MavenProjectInfo> subProjects = mavenProjectInfo.getProjects();
			res.addAll(subProjects);
			res.addAll(findSubProjects(subProjects));
		}
		return res;
	}

	private LocalProjectScanner getProjectScanner(File localClone) throws NoWorkTreeException, IOException {
		File root = ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile();
		MavenModelManager modelManager = MavenPlugin.getMavenModelManager();
		return new LocalProjectScanner(root, localClone.getCanonicalPath(), false, modelManager);
	}

}
