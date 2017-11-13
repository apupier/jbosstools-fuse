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
package org.fusesource.ide.branding.tests.integration.wizards;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.fusesource.ide.branding.wizards.NewCamelTestWizard;
import org.fusesource.ide.camel.model.service.core.model.CamelFile;
import org.fusesource.ide.camel.model.service.core.tests.integration.core.io.FuseProject;
import org.junit.Rule;
import org.junit.Test;

public class NewCamelTestWizardIT {
	
	@Rule
	public FuseProject fuseProject = new FuseProject(NewCamelTestWizardIT.class.getSimpleName());
	
	@Test
	public void testCreateTestForRouteUsingCamelContext() throws Exception {
		createTestFor(fuseProject.createEmptyCamelFile());
	}
	
	@Test
	public void testCreateTestForRouteUsingRoutesForContainer() throws Exception {
		createTestFor(fuseProject.createEmptyCamelFileWithRoutes());
	}

	protected void createTestFor(CamelFile camelFile) throws JavaModelException {
		JavaCore.create(fuseProject.getProject()).open(new NullProgressMonitor());
		NewCamelTestWizard newCamelTestWizard = new NewCamelTestWizard();
		newCamelTestWizard.init(PlatformUI.getWorkbench(), new StructuredSelection(camelFile.getResource()));
		WizardDialog wizardDialog = new WizardDialog(Display.getDefault().getActiveShell(), newCamelTestWizard);
		wizardDialog.setBlockOnOpen(false);
		wizardDialog.open();
		((NewTypeWizardPage)newCamelTestWizard.getStartingPage()).setTypeName("UniqueTestClassNameTest", true);
		assertThat(newCamelTestWizard.performFinish()).isTrue();
		
		assertThat(fuseProject.getProject().getFile(new Path("src/main/java/UniqueTestClassNameTest.java")).getFullPath().toFile()).exists();
	}

}
