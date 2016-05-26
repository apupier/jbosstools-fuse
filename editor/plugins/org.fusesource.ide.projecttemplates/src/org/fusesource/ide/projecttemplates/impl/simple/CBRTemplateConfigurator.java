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
package org.fusesource.ide.projecttemplates.impl.simple;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.zip.ZipInputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IProjectConfigurationManager;
import org.eclipse.m2e.core.project.ResolverConfiguration;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.fusesource.ide.projecttemplates.adopters.UnzipProjectTemplateConfigurator;
import org.fusesource.ide.projecttemplates.internal.ProjectTemplatesActivator;
import org.fusesource.ide.projecttemplates.util.NewProjectMetaData;
import org.fusesource.ide.projecttemplates.util.camel.ICamelFacetDataModelProperties;

/**
 * @author lhein
 */
public class CBRTemplateConfigurator extends UnzipProjectTemplateConfigurator {

	/* (non-Javadoc)
	 * @see org.fusesource.ide.projecttemplates.adopters.TemplateProjectConfiguratorSupport#supportsDSL(org.fusesource.ide.projecttemplates.adopters.TemplateProjectConfiguratorSupport.DSL_TYPE)
	 */
	@Override
	public boolean supportsDSL(DSL_TYPE type) {
		switch (type) {
			case BLUEPRINT:	return true;
			case SPRING:	return false;
			case JAVA:		return false;
			case ROUTES:	return false;
			default:		return false;
		}		
	}
	
	/* (non-Javadoc)
	 * @see org.fusesource.ide.projecttemplates.adopters.UnzipProjectTemplateConfigurator#getArchiveStream()
	 */
	@Override
	protected ZipInputStream getArchiveStream() {
		URL archiveUrl = ProjectTemplatesActivator.getBundleContext().getBundle().getEntry("templates/simple-fuse-cbr.zip");
		if (archiveUrl != null) {
			InputStream is = null;
			try {
				is = archiveUrl.openStream();
				return new ZipInputStream(is, Charset.forName("UTF-8"));
			} catch (IOException ex) {
				ProjectTemplatesActivator.pluginLog().logError(ex);
			}			
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.fusesource.ide.projecttemplates.adopters.AbstractProjectTemplateConfigurator#doConfiguration(org.eclipse.core.resources.IProject, org.fusesource.ide.projecttemplates.util.NewProjectMetaData)
	 */
	@Override
	protected boolean doConfiguration(IProject project, NewProjectMetaData projectMetaData) throws CoreException {
		IProgressMonitor monitor = new NullProgressMonitor();

		// install needed facets first
		installFacets(project, projectMetaData, monitor);
				
		// setup the source folder links for deployment
		IVirtualComponent newComponent = ComponentCore.createComponent(project);
		final IVirtualFolder jbiRoot = newComponent.getRootFolder();
		jbiRoot.removeLink(new Path("/src"), IResource.FORCE, monitor); //$NON-NLS-1$
		jbiRoot.createLink(new Path("/src/main/java"), IResource.FORCE, monitor); //$NON-NLS-1$
		jbiRoot.createLink(new Path("/src/main/resources"), IResource.FORCE, monitor); //$NON-NLS-1$

		// now deflate the zip
		boolean proceed = super.doConfiguration(project, projectMetaData);
		
		if (proceed) {
            configureMaven(project, monitor);
		}
		
		return proceed;
	}
	
	private boolean configureMaven(IProject project, IProgressMonitor monitor) {
		try {
			ResolverConfiguration configuration = new ResolverConfiguration();
			configuration.setResolveWorkspaceProjects(true);
			configuration.setSelectedProfiles(""); //$NON-NLS-1$
			IProjectConfigurationManager configurationManager = MavenPlugin.getProjectConfigurationManager();
			configurationManager.enableMavenNature(project, configuration, monitor);
			configurationManager.updateProjectConfiguration(project, monitor);
        } catch(CoreException ex) {
        	ProjectTemplatesActivator.pluginLog().logError(ex.getMessage(), ex);
        	return false;
        }
		return true;
	}
	
	private void installFacets(IProject project, NewProjectMetaData projectMetaData, IProgressMonitor monitor) throws CoreException {
		// now add jst utility
		installFacet(project, "jst.utility", null, null, monitor);
		// now add camel facet
		String[] camelVersionParts = projectMetaData.getCamelVersion().split("\\.");
		if (camelVersionParts.length>1) {
			String camelFacetVersion = String.format("%s.%s", camelVersionParts[0], camelVersionParts[1]); 
			IDataModel dm = getDataModel(projectMetaData);
			installFacet(project, ICamelFacetDataModelProperties.CAMEL_PROJECT_FACET, camelFacetVersion, dm, monitor);
		}
	}
}
