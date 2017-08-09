/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.fusesource.ide.camel.editor.palette;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.fusesource.ide.camel.editor.provider.ToolBehaviourProvider;
import org.fusesource.ide.preferences.PreferenceManager;

public class WhiteListPreferenceInitializer extends AbstractPreferenceInitializer {

	private static final Set<String> CONNECTORS_WHITELIST;

	static {
		CONNECTORS_WHITELIST = new HashSet<>();

		CONNECTORS_WHITELIST.add("activemq");
		CONNECTORS_WHITELIST.add("atom");
		CONNECTORS_WHITELIST.add("controlbus");
		CONNECTORS_WHITELIST.add("cxf");
		CONNECTORS_WHITELIST.add("cxfrs");
		CONNECTORS_WHITELIST.add("cxfbean");
		CONNECTORS_WHITELIST.add("direct");
		CONNECTORS_WHITELIST.add("direct-vm");
		CONNECTORS_WHITELIST.add("ejb");
		CONNECTORS_WHITELIST.add("file");
		CONNECTORS_WHITELIST.add("ftp");
		CONNECTORS_WHITELIST.add("ftps");
		CONNECTORS_WHITELIST.add("sftp");
		CONNECTORS_WHITELIST.add("imap");
		CONNECTORS_WHITELIST.add("imaps");
		CONNECTORS_WHITELIST.add("jdbc");
		CONNECTORS_WHITELIST.add("jgroups");
		CONNECTORS_WHITELIST.add("jms");
		CONNECTORS_WHITELIST.add("language");
		CONNECTORS_WHITELIST.add("linkedin");
		CONNECTORS_WHITELIST.add("mina2");
		CONNECTORS_WHITELIST.add("mqtt");
		CONNECTORS_WHITELIST.add("mvel");
		CONNECTORS_WHITELIST.add("netty");
		CONNECTORS_WHITELIST.add("netty-http");
		CONNECTORS_WHITELIST.add("netty4");
		CONNECTORS_WHITELIST.add("netty4-http");
		CONNECTORS_WHITELIST.add("pop3");
		CONNECTORS_WHITELIST.add("pop3s");
		CONNECTORS_WHITELIST.add("quartz");
		CONNECTORS_WHITELIST.add("quartz2");
		CONNECTORS_WHITELIST.add("restlet");
		CONNECTORS_WHITELIST.add("rss");
		CONNECTORS_WHITELIST.add("salesforce");
		CONNECTORS_WHITELIST.add("sap-netweaver");
		CONNECTORS_WHITELIST.add("scheduler");
		CONNECTORS_WHITELIST.add("seda");
		CONNECTORS_WHITELIST.add("servlet");
		CONNECTORS_WHITELIST.add("smtp");
		CONNECTORS_WHITELIST.add("smtps");
		CONNECTORS_WHITELIST.add("snmp");
		CONNECTORS_WHITELIST.add("sql");
		CONNECTORS_WHITELIST.add("timer");
		CONNECTORS_WHITELIST.add("vm");
		CONNECTORS_WHITELIST.add("xquery");
		CONNECTORS_WHITELIST.add("xslt");
	}
	
	public WhiteListPreferenceInitializer() {
		// Keep for reflection instantiation by Eclipse framework
	}
	
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore preferenceStore = getPreferenceStore();
		Set<String> allDefaultConnectors = new HashSet<>(CONNECTORS_WHITELIST);
		allDefaultConnectors.addAll(ToolBehaviourProvider.COMPONENTS_FROM_EXTENSION_POINTS);
		preferenceStore.setDefault(WhiteListPaletteConstants.PREFERENCE_PALETTE_WHITELIST, String.join(WhiteListPaletteConstants.PREFERENCE_SEPARATOR, allDefaultConnectors));
	}
	
	IPreferenceStore getPreferenceStore() {
		return PreferenceManager.getInstance().getUnderlyingStorage();
	}

}
