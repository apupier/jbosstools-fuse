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

import java.util.Collections;
import java.util.List;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.fusesource.ide.camel.editor.provider.ext.GlobalConfigElementType;
import org.fusesource.ide.camel.editor.provider.ext.GlobalConfigurationTypeWizard;
import org.fusesource.ide.camel.editor.provider.ext.ICustomGlobalConfigElementContribution;
import org.fusesource.ide.camel.model.service.core.catalog.Dependency;
import org.fusesource.ide.camel.model.service.core.model.AbstractCamelModelElement;
import org.fusesource.ide.camel.model.service.core.model.CamelFile;
import org.fusesource.ide.foundation.core.util.CamelUtils;
import org.springframework.ide.eclipse.config.core.schemas.BeansSchemaConstants;
import org.springframework.ide.eclipse.config.ui.editors.AbstractConfigFormPage;
import org.springframework.ide.eclipse.config.ui.editors.SpringConfigEditor;
import org.springframework.ide.eclipse.config.ui.editors.beans.BeansFormPage;
import org.w3c.dom.Node;

public class SpringBeansContributor implements ICustomGlobalConfigElementContribution {

	private static final String BEAN = "bean";
	private static final String HTTP_WWW_SPRINGFRAMEWORK_ORG_SCHEMA_BEANS = "http://www.springframework.org/schema/beans";

	@Override
	public GlobalConfigurationTypeWizard createGlobalElement(CamelFile camelFile) {
		return new GlobalConfigSpringBeanWizard(camelFile.getResource());
	}

	@Override
	public List<Dependency> getElementDependencies() {
		return Collections.emptyList();
	}

	@Override
	public GlobalConfigurationTypeWizard modifyGlobalElement(CamelFile camelFile) {
		try {
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IEditorPart openEditor = activePage.openEditor(new FileEditorInput(camelFile.getResource()), SpringConfigEditor.ID_EDITOR, true, IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
			if(openEditor instanceof SpringConfigEditor){
				BeansFormPage beansPage = (BeansFormPage) ((SpringConfigEditor) openEditor).getFormPageForUri(BeansSchemaConstants.URI);
				((SpringConfigEditor) openEditor).setFocus();
				((SpringConfigEditor) openEditor).setActiveEditor(beansPage);
				beansPage.setFocus();
				//((SpringConfigEditor) openEditor).revealElement(new SpringBeanUtil().findCorrespondingNode(beanNode, nodeToFind));
			}
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onGlobalElementDeleted(AbstractCamelModelElement camelModelElement) {
		// no specific action
	}

	@Override
	public boolean canHandle(AbstractCamelModelElement camelModelElementToHandle) {
		Node xmlNode = camelModelElementToHandle.getXmlNode();
		return xmlNode != null
				&& HTTP_WWW_SPRINGFRAMEWORK_ORG_SCHEMA_BEANS.equals(xmlNode.getNamespaceURI())
				&& BEAN.equals(CamelUtils.getTranslatedNodeName(xmlNode));
	}

	@Override
	public GlobalConfigElementType getGlobalConfigElementType() {
		return GlobalConfigElementType.GLOBAL_ELEMENT;
	}

}
