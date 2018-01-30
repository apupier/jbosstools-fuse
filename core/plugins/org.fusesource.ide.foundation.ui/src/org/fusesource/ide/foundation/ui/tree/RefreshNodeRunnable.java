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
package org.fusesource.ide.foundation.ui.tree;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonViewer;

/**
 * @author Aurelien Pupier
 *
 */
public final class RefreshNodeRunnable implements Runnable {

	private final NodeSupport nodeSupport;

	/**
	 * @param nodeSupportToRefresh
	 */
	public RefreshNodeRunnable(NodeSupport nodeSupportToRefresh) {
		this.nodeSupport = nodeSupportToRefresh;
	}

	@Override
	public void run() {
		for (CommonViewer commonViewer : findCommonViewers()) {
			commonViewer.update(nodeSupport, null);
		}
	}
	
	private Set<CommonViewer> findCommonViewers() {
		Set<CommonViewer> res = new HashSet<>();
		final IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null) {
			final IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
			for (IViewReference viewRef : activePage.getViewReferences()) {
				IWorkbenchPart viewPart = viewRef.getPart(false);
				if (viewPart instanceof IAdaptable) {
					CommonViewer commonViewer = viewPart.getAdapter(CommonViewer.class);
					if (commonViewer != null) {
						res.add(commonViewer);
					}
				}
			}
		}
		return res;
	}
}