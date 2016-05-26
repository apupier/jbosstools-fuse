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
package org.fusesource.ide.projecttemplates.adopters;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.fusesource.ide.projecttemplates.util.camel.ICamelFacetDataModelProperties;
import org.fusesource.ide.projecttemplates.util.NewProjectMetaData;
import org.fusesource.ide.projecttemplates.util.camel.CamelFacetDataModelProvider;

/**
 * @author lhein
 */
public abstract class AbstractProjectTemplateConfigurator implements TemplateProjectConfiguratorSupport {

	/* (non-Javadoc)
	 * @see org.fusesource.ide.projecttemplates.adopters.TemplateProjectConfiguratorSupport#supportsDSL(org.fusesource.ide.projecttemplates.adopters.TemplateProjectConfiguratorSupport.DSL_TYPE)
	 */
	@Override
	public boolean supportsDSL(DSL_TYPE type) {
		// by default we support all DSL types
		return true;
	}

	/* (non-Javadoc)
	 * @see org.fusesource.ide.projecttemplates.adopters.TemplateProjectConfiguratorSupport#preConfigure(org.eclipse.core.resources.IProject, org.fusesource.ide.projecttemplates.util.NewProjectMetaData)
	 */
	@Override
	public boolean preConfigure(IProject project, NewProjectMetaData projectMetaData) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.fusesource.ide.projecttemplates.adopters.TemplateProjectConfiguratorSupport#postConfigure(org.eclipse.core.resources.IProject, org.fusesource.ide.projecttemplates.util.NewProjectMetaData)
	 */
	@Override
	public boolean postConfigure(IProject project, NewProjectMetaData projectMetaData) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.fusesource.ide.projecttemplates.adopters.TemplateProjectConfiguratorSupport#configureProject(org.eclipse.core.resources.IProject, org.fusesource.ide.projecttemplates.util.NewProjectMetaData)
	 */
	@Override
	public final boolean configureProject(IProject project, NewProjectMetaData projectMetaData) throws CoreException {
		// call pre actions
		if (!preConfigure(project, projectMetaData)) {
			return false;
		}
		
		// call configure actions
		if (!doConfiguration(project, projectMetaData)) {
			return false;
		}
		
		// call post actions
		if (!postConfigure(project, projectMetaData)) {
			return false;
		}
		
		// if all went fine we are done
		return true;
	}
	
	/**
	 * creates the datamodel used for the facet installation delegate
	 * 
	 * @param projectMetaData	the projects metadata
	 * @return	a facet configuration
	 */
	protected IDataModel getDataModel(NewProjectMetaData projectMetaData) {
		CamelFacetDataModelProvider dmProv = new CamelFacetDataModelProvider();
		dmProv.create();
		IDataModel dm = dmProv.getDataModel();
		dm.setStringProperty(ICamelFacetDataModelProperties.CAMEL_CONTENT_FOLDER, ICamelFacetDataModelProperties.DEFAULT_CAMEL_CONFIG_RESOURCE_FOLDER);
		dm.setStringProperty(ICamelFacetDataModelProperties.CAMEL_DSL, projectMetaData.getDslType().toString());
		dm.setStringProperty(ICamelFacetDataModelProperties.CAMEL_PROJECT_VERSION, projectMetaData.getCamelVersion());
		dm.setBooleanProperty(ICamelFacetDataModelProperties.UPDATE_PROJECT_STRUCTURE, true);
		dm.setProperty(ICamelFacetDataModelProperties.CAMEL_PROJECT_METADATA, projectMetaData);
		return dm;
	}
	
	/**
	 * installs the given facet with the given version and config in the project
	 * 
	 * @param project		the project to install the facet into
	 * @param facetName		the name / id of the facet
	 * @param facetVersion	the facet version to use or null to use default
	 * @param config		the datamodel or null if not needed
	 * @param monitor		the progress monitor to use
	 * @throws CoreException on errors
	 */
	protected void installFacet(IProject project, String facetName, String facetVersion, IDataModel config, IProgressMonitor monitor) throws CoreException {
		IFacetedProject fp = ProjectFacetsManager.create(project, true, monitor);
		if (facetVersion != null) {
			fp.installProjectFacet(ProjectFacetsManager.getProjectFacet(facetName).getVersion(facetVersion), config, monitor);
		} else {
			fp.installProjectFacet(ProjectFacetsManager.getProjectFacet(facetName).getDefaultVersion(), config, monitor);
		}
	}
	
	/**
	 * call back function which is invoked after preConfigure and before 
	 * postConfigure. put in all your configuration logic here.
	 * 
	 * @param project
	 * @param projectMetaData
	 * @return	true on success
	 * @throws CoreException
	 */
	protected abstract boolean doConfiguration(IProject project, NewProjectMetaData projectMetaData) throws CoreException;
}
