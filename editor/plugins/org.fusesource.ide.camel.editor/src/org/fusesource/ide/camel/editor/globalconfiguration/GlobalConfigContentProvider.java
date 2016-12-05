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
package org.fusesource.ide.camel.editor.globalconfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.fusesource.ide.camel.editor.globalconfiguration.beans.spring.SpringBeanUtil;
import org.fusesource.ide.camel.editor.globalconfiguration.beans.spring.SpringBeansContributor;
import org.fusesource.ide.camel.model.service.core.model.AbstractCamelModelElement;
import org.fusesource.ide.camel.model.service.core.model.GlobalDefinitionCamelModelElement;
import org.springframework.ide.eclipse.config.ui.editors.SpringConfigContentProvider;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.modelhandler.XMLModelLoader;
import org.eclipse.wst.sse.core.internal.provisional.IModelLoader;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class GlobalConfigContentProvider implements ITreeContentProvider {

	private final CamelGlobalConfigEditor camelGlobalConfigEditor;

	GlobalConfigContentProvider(CamelGlobalConfigEditor camelGlobalConfigEditor) {
		this.camelGlobalConfigEditor = camelGlobalConfigEditor;
	}

	@Override
	public void dispose() {
		// Nothing to dispose
	}

	@Override
	public Object[] getChildren(Object parent) {
		return getElements(parent);
	}

	@Override
	public Object[] getElements(Object parent) {
		if (parent instanceof Map) {
			Object[] catIds = ((Map<?, ?>) parent).keySet().toArray();
			Arrays.sort(catIds, new Comparator<Object>() {
				@Override
				public int compare(Object o1, Object o2) {
					if (CamelGlobalConfigEditor.DEFAULT_CAT_ID.equals(o1.toString())) {
						return 1;
					} else if (CamelGlobalConfigEditor.DEFAULT_CAT_ID.equals(o2.toString())) {
						return -1;
					} else {
						return o1.toString().compareTo(o2.toString());
					}
				}
			});
			return catIds;
		} else if (parent instanceof String) {
			return this.camelGlobalConfigEditor.getModel().get((String)parent).toArray();
		} else if(parent instanceof GlobalDefinitionCamelModelElement && new SpringBeansContributor().canHandle((AbstractCamelModelElement) parent)) {
			return new SpringBeanUtil().getChildrenBeans((GlobalDefinitionCamelModelElement) parent).toArray();
		}
		
		return new Object[0];
	}

	



	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return element instanceof String || element instanceof AbstractCamelModelElement && new SpringBeansContributor().canHandle((AbstractCamelModelElement) element);
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// Nothing special to update
	}
}