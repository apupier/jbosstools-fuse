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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilderFactory;

import org.fusesource.ide.camel.model.service.core.model.AbstractCamelModelElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3c.dom.Element;

@RunWith(MockitoJUnitRunner.class)
public class SpringBeansContributorTest {
	
	@Mock
	AbstractCamelModelElement cme;

	@Test
	public void testCanHandleSpringBeans() throws Exception {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		Element nodeToHandle = documentBuilderFactory.newDocumentBuilder()
				.parse(new ByteArrayInputStream(
						("<beans xmlns=\"http://www.springframework.org/schema/beans\">"
						+ "	<bean class=\"com.example.MyBean\" id=\"myBean\" name=\"MyBean\"/>"
						+ "</beans>")
								.getBytes(StandardCharsets.UTF_8.name())))
				.getDocumentElement();
		doReturn(nodeToHandle.getChildNodes().item(1)).when(cme).getXmlNode();
		assertThat(new SpringBeansContributor().canHandle(cme)).isTrue();
	}
	
	@Test
	public void testCan_NOT_HandleBlueprintBeans() throws Exception {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		Element nodeToHandle = documentBuilderFactory.newDocumentBuilder()
				.parse(new ByteArrayInputStream(
						("<blueprint xmlns=\"http://www.osgi.org/xmlns/blueprint/v1.0.0\">"
						+ "<bean class=\"com.example.MyBean\" id=\"MyBean\"/>"
						+ "</blueprint>")
								.getBytes(StandardCharsets.UTF_8.name())))
				.getDocumentElement();
		doReturn(nodeToHandle.getChildNodes().item(1)).when(cme).getXmlNode();
		assertThat(new SpringBeansContributor().canHandle(cme)).isFalse();
	}

}
