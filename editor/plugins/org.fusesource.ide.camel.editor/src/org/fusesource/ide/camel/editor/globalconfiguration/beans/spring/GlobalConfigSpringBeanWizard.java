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
package org.fusesource.ide.camel.editor.globalconfiguration.beans.spring;

import org.eclipse.core.resources.IFile;
import org.fusesource.ide.camel.editor.provider.ext.GlobalConfigurationTypeWizard;
import org.springframework.ide.eclipse.wizard.ui.BeanWizard;
import org.w3c.dom.Element;

public class GlobalConfigSpringBeanWizard extends BeanWizard implements GlobalConfigurationTypeWizard {

	public GlobalConfigSpringBeanWizard(IFile camelFile) {
		super(camelFile, false);
	}

	@Override
	public Element getGlobalConfigurationElementNode() {
		return getNewBean();
	}

	@Override
	public void setGlobalConfigurationElementNode(Element node) {
		throw new UnsupportedOperationException("Edition need to be done using properties view"); //$NON-NLS-1$
	}

}
