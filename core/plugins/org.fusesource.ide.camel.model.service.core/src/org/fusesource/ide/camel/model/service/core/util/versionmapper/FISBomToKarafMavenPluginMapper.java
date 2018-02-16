/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.fusesource.ide.camel.model.service.core.util.versionmapper;

import java.util.Collections;
import java.util.Map;

import org.fusesource.ide.foundation.core.util.OnlineVersionMapper;

public class FISBomToKarafMavenPluginMapper extends OnlineVersionMapper {
	
	private static final String FISBOM_TOKARAFMAVENPLUGIN_MAPPING_FUSE_7_PROPERTY = "org.jboss.tools.fuse.fisbom2karafMavenVersion.fuse7.url";
	private static final String FISBOM_TOKARAFMAVENPLUGIN_MAPPING_FUSE_7_DEFAULT_URL = "https://raw.githubusercontent.com/jbosstools/jbosstools-fuse/master/configuration/fisBomToKarafMavenPlugin.fuse7.properties";

	
	public FISBomToKarafMavenPluginMapper() {
		super(FISBOM_TOKARAFMAVENPLUGIN_MAPPING_FUSE_7_PROPERTY, FISBOM_TOKARAFMAVENPLUGIN_MAPPING_FUSE_7_DEFAULT_URL);
	}

	@Override
	protected Map<String, String> createFallbackMapping() {
		return Collections.emptyMap();
	}

}
