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
package org.fusesource.ide.projecttemplates.impl.simple;

import java.nio.file.Path;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.fusesource.ide.camel.model.service.core.util.CamelCatalogUtils;
import org.fusesource.ide.camel.model.service.core.util.FuseBomFilter;
import org.fusesource.ide.foundation.core.util.VersionUtil;
import org.fusesource.ide.projecttemplates.adopters.AbstractProjectTemplate;
import org.fusesource.ide.projecttemplates.adopters.configurators.TemplateConfiguratorSupport;
import org.fusesource.ide.projecttemplates.adopters.creators.DSLDependentUnzipStreamCreator;
import org.fusesource.ide.projecttemplates.adopters.creators.TemplateCreatorSupport;
import org.fusesource.ide.projecttemplates.adopters.util.CamelDSLType;
import org.fusesource.ide.projecttemplates.util.CommonNewProjectMetaData;

public class OSEKarafXMLTemplateForFuse7 extends AbstractProjectTemplate {
	
	private static final String TEMPLATE_KARAF = "template-simple-ose-log-karaf-fuse7";
	private static final String PLACEHOLDER_KARAFMAVENPLUGIN_VERSION = "%%%PLACEHOLDER_KARAFMAVENPLUGIN_VERSION%%%";
	private static final String MINIMAL_COMPATIBLE_CAMEL_VERSION = "2.20.0";

	@Override
	public TemplateConfiguratorSupport getConfigurator() {
		String bomVersion = getArtifactLastVersion(FuseBomFilter.BOM_FUSE_FIS_KARAF.getGroupId(), FuseBomFilter.BOM_FUSE_FIS_KARAF.getArtifactId());
		return new MavenConfiguratorForFuseOnOpenShift7Template(bomVersion) {
			@Override
			protected void configureVersions(IProject project, IProgressMonitor monitor) throws CoreException {
				SubMonitor subMonitor = SubMonitor.convert(monitor, 3);
				super.configureVersions(project, subMonitor.split(1));
				IFile pom = project.getFile("pom.xml");
				Path pomAbsolutePath = pom.getLocation().toFile().toPath();
				String karafMavenPluginVersion = CamelCatalogUtils.getKarafMavenPluginVersionForBomVersion(bomVersion, subMonitor.split(1));
				replace(karafMavenPluginVersion, PLACEHOLDER_KARAFMAVENPLUGIN_VERSION, pomAbsolutePath, pomAbsolutePath);
				project.refreshLocal(IResource.DEPTH_ONE, subMonitor.split(1));
			}
		};
	}
	
	@Override
	public boolean supportsDSL(CamelDSLType type) {
		return type == CamelDSLType.BLUEPRINT;
	}

	@Override
	public TemplateCreatorSupport getCreator(CommonNewProjectMetaData projectMetaData) {
		return new DSLDependentUnzipStreamCreator(TEMPLATE_KARAF, null, null, "");
	}
	
	@Override
	public boolean isCompatible(String camelVersion) {
		return new VersionUtil().isGreaterThan(camelVersion, MINIMAL_COMPATIBLE_CAMEL_VERSION);
	}

}
