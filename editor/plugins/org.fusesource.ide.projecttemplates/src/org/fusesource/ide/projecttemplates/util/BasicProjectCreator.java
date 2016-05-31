/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.fusesource.ide.projecttemplates.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.fusesource.ide.projecttemplates.internal.ProjectTemplatesActivator;

/**
 * a basic project creator which only creates the project folder
 * and some basic configuration
 * 
 * @author lhein
 */
public class BasicProjectCreator extends Job {

	private IPath location;
	private IProject project;
	private NewProjectMetaData metadata;
	
	/**
	 * 
	 */
	public BasicProjectCreator(IPath location, NewProjectMetaData metadata) {
		super("Project Creator");
		this.location = location;
		this.metadata = metadata;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			monitor.beginTask("Creating project " + metadata.getProjectName() + "...", 1);
		
			// first create the project
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			project = root.getProject(metadata.getProjectName());
			project.create(monitor);
			project.open(monitor);
			
//			IFacetedProject fp = ProjectFacetsManager.create(project, true, monitor);
//			// now add java facet
//			fp.installProjectFacet(ProjectFacetsManager.getProjectFacet("jst.java").getDefaultVersion(), null, monitor);
//			// and maven facet
//			fp.installProjectFacet(ProjectFacetsManager.getProjectFacet("jboss.m2").getDefaultVersion(), null, monitor);
		} catch (Exception ex) {
			ProjectTemplatesActivator.pluginLog().logError(ex);
			return Status.CANCEL_STATUS;
		} finally {
			monitor.done();
		}
		return Status.OK_STATUS;
	}	
	
	/**
	 * returns the created project
	 * 
	 * @return
	 */
	public IProject getProject() {
		return project;
	}
}
