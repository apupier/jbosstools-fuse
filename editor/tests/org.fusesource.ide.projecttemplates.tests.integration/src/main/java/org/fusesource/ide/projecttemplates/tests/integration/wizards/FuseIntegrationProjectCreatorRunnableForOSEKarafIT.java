/*******************************************************************************
 * Copyright (c) 2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.fusesource.ide.projecttemplates.tests.integration.wizards;

import org.fusesource.ide.camel.model.service.core.util.CamelCatalogUtils;
import org.fusesource.ide.projecttemplates.adopters.util.CamelDSLType;
import org.junit.Test;

public class FuseIntegrationProjectCreatorRunnableForOSEKarafIT extends FuseIntegrationProjectCreatorRunnableIT {

	public FuseIntegrationProjectCreatorRunnableForOSEKarafIT() {
		this.camelVersion = CamelCatalogUtils.CAMEL_VERSION_LATEST_COMMUNITY;
	}
	
	@Test
	public void testOSEKarafProjectCreation() throws Exception {
        testProjectCreation("-OSEKarafProject-"+camelVersion, CamelDSLType.BLUEPRINT, "src/main/resources/OSGI-INF/blueprint/camel-log.xml", null);
	}
	
}
