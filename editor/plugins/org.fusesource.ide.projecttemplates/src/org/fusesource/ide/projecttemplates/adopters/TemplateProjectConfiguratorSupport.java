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
import org.fusesource.ide.projecttemplates.util.NewProjectMetaData;

/**
 * @author lhein
 */
public interface TemplateProjectConfiguratorSupport {
	
	static enum DSL_TYPE {
		BLUEPRINT,
		SPRING,
		JAVA,
		ROUTES
	};
	
	/**
	 * returns true if the configurator can configure a template project for the
	 * given Camel DSL
	 * 
	 * @param type	the camel dsl
	 * @return	true if supported
	 */
	boolean supportsDSL(DSL_TYPE type);

	/**
	 * allows to do initial setup / configure steps before the configureProject 
	 * method is called
	 *  
	 * @param project			the project to configure
	 * @param projectMetaData	all needed information for the new project
	 * @return	true on success
	 */
	boolean preConfigure(IProject project, NewProjectMetaData projectMetaData);
	
	/**
	 * configures the given project with the given template and dsl
	 * 
	 * @param project	the project to configure
	 * @param projectMetaData	all needed information for the new project
	 * @return	true on success otherwise false
	 * @throws CoreException if something went wrong
	 */
	boolean configureProject(IProject project, NewProjectMetaData projectMetaData) throws CoreException;
	
	/**
	 * allows to do setup / configure steps after the configureProject 
	 * method has been called
	 *  
	 * @param project			the project to configure
	 * @param projectMetaData	all needed information for the new project
	 * @return	true on success
	 */
	boolean postConfigure(IProject project, NewProjectMetaData projectMetaData);
}
