package org.fusesource.ide.camel.editor.globalconfiguration.beans.spring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.sse.core.internal.provisional.IndexedRegion;
import org.eclipse.wst.xml.core.internal.modelhandler.XMLModelLoader;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.fusesource.ide.camel.model.service.core.model.GlobalDefinitionCamelModelElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SpringBeanUtil {
	
	
	public IDOMElement findCorrespondingNode(GlobalDefinitionCamelModelElement beanNode, Node nodeToFind) {
		XMLModelLoader xmlModelLoader = new XMLModelLoader();
		IDOMModel xmlModel = (IDOMModel)xmlModelLoader.createModel();
		try {
			xmlModelLoader.load(((GlobalDefinitionCamelModelElement) beanNode).getCamelFile().getResource(), xmlModel);
			IDOMDocument document = xmlModel.getDocument();
			NodeList nodes = document.getChildNodes();
			String nodeName = nodeToFind.getNodeName();
			Node idToFind = nodeToFind.getAttributes().getNamedItem("id");
			for (int i = 0; i < nodes.getLength(); i++) {
				Node currentNode = nodes.item(i);
				if(nodeName.equals(currentNode.getNodeName())
						&& currentNode.getAttributes() != null
						&& idToFind.getNodeValue().equals(currentNode.getAttributes().getNamedItem("id").getNodeValue())){
					return (IDOMElement) currentNode;
				} else {
					IDOMElement correspondingNodeInChildren = findCorrespondingNode(currentNode.getChildNodes(), nodeToFind);
					if(correspondingNodeInChildren != null){
						return correspondingNodeInChildren;
					}
				}
			}
		} catch (IOException | CoreException e) {
			return null;
		}
		return null;
	}
	
	public IDOMElement findCorrespondingNode(NodeList nodes, Node nodeToFind) {
		String nodeName = nodeToFind.getNodeName();
		Node idToFind = nodeToFind.getAttributes().getNamedItem("id");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node currentNode = nodes.item(i);
			if(nodeName.equals(currentNode.getNodeName())
					&& currentNode.getAttributes() != null
					&& idToFind.getNodeValue().equals(currentNode.getAttributes().getNamedItem("id").getNodeValue())){
				return (IDOMElement) currentNode;
			} else {
				IDOMElement correspondingNodeInChildren = findCorrespondingNode(currentNode.getChildNodes(), nodeToFind);
				if(correspondingNodeInChildren != null){
					return correspondingNodeInChildren;
				}
			}
		}
		return null;
	}
	
	public List<IDOMElement> getChildrenBeans(GlobalDefinitionCamelModelElement parent) {
		XMLModelLoader xmlModelLoader = new XMLModelLoader();
		try {
			IDOMModel xmlModel = (IDOMModel)xmlModelLoader.createModel();
			xmlModelLoader.load(((GlobalDefinitionCamelModelElement) parent).getCamelFile().getResource(), xmlModel);
			IDOMDocument document = xmlModel.getDocument();
			IDOMElement domElement = findCorrespondingNode(document.getChildNodes(), parent.getXmlNode());
			if(domElement != null){
				List<IDOMElement> res = new ArrayList<>();
				NodeList childNodes = domElement.getChildNodes();
				for (int i = 0; i < childNodes.getLength(); i++) {
					Node child = childNodes.item(i);
					if(child instanceof IDOMElement && child.getNodeType() == Node.ELEMENT_NODE) {
						res.add((IDOMElement)child);
					}
				}
				return res;
			}
			
		} catch (IOException | CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

}
