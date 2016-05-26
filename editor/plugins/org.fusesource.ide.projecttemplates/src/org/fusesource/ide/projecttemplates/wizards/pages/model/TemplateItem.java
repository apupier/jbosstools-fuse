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
package org.fusesource.ide.projecttemplates.wizards.pages.model;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.ide.foundation.core.util.Strings;
import org.fusesource.ide.projecttemplates.adopters.TemplateProjectConfiguratorSupport;

/**
 * @author lhein
 */
public class TemplateItem {
	private String id;
	private String name;
	private String description;
	private List<String> keywords = new ArrayList<>();
	private int weight;
	private CategoryItem category;
	private TemplateProjectConfiguratorSupport configurator;
	
	/**
	 * creates a new template item
	 * 
	 * @param id			the id of the template
	 * @param name			the name of the template 
	 * @param description	the description of the template
	 * @param weight		the weight for sorting
	 * @param category		the category
	 * @param configurator	the configurator class
	 */
	public TemplateItem(String id, String name, String description, int weight, CategoryItem category, TemplateProjectConfiguratorSupport configurator, String keywords) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.category = category;
		this.configurator = configurator;
		initKeywords(keywords);
	}
	
	private void initKeywords(String keywordsString) {
		String[] words = keywordsString.split(" ");
		for (String word : words) {
			word = word.trim();
			if (!Strings.isBlank(word) && !keywords.contains(word)) {
				keywords.add(word);
			}
		}
	}
	
	/**
	 * @return the keywords
	 */
	public List<String> getKeywords() {
		return this.keywords;
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * @return the weight
	 */
	public int getWeight() {
		return this.weight;
	}
	
	/**
	 * @return the category
	 */
	public CategoryItem getCategory() {
		return this.category;
	}
	
	/**
	 * @return the configurator
	 */
	public TemplateProjectConfiguratorSupport getConfigurator() {
		return this.configurator;
	}
}
