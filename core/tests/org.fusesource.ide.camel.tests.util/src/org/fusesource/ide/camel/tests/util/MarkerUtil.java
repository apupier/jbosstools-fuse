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
package org.fusesource.ide.camel.tests.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class MarkerUtil {

	public void checkNoValidationIssueOfType(IProject project, Predicate<IMarker> filter) throws CoreException {
		final IMarker[] markers = project.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
		final List<Object> readableMarkers = Arrays.asList(markers).stream()
				.filter(filter)
				.map(marker -> {
						try {
							return extractMarkerInformation(marker);
						} catch (Exception e) {
							Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.ID, "Cannot extract marker information", e));
							try {
								return "type: "+marker.getType()+"\n"+
										"attributes:\n"+
										marker.getAttributes().entrySet().stream()
							            .map(entry -> entry.getKey() + " - " + entry.getValue())
							            .collect(Collectors.joining(", "));
							} catch (CoreException e1) {
								Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.ID, "Cannot extract marker information", e1));
								return marker;
							}
						}
					})
				.collect(Collectors.toList());
		assertThat(readableMarkers).isEmpty();
	}

	public Object extractMarkerInformation(IMarker marker) throws CoreException, IOException {
		Map<String, Object> markerInformations = marker.getAttributes() != null ? marker.getAttributes() : new HashMap<>();
		IResource resource = marker.getResource();
		if(resource != null){
			markerInformations.put("resource affected", resource.getLocation().toOSString());
			if(resource instanceof IFile){
				InputStream contents = ((IFile) resource).getContents();
				try (BufferedReader buffer = new BufferedReader(new InputStreamReader(contents))) {
					markerInformations.put("resource affected content", buffer.lines().collect(Collectors.joining("\n")));
				}
			}
		}
		markerInformations.put("type: ", marker.getType());
		markerInformations.put("Creation time: ", marker.getCreationTime());
		return markerInformations;
	}
	
	public void checkNoValidationError(IProject project) throws CoreException {
		new MarkerUtil().checkNoValidationIssueOfType(project, filterError());
	}
	
	private Predicate<IMarker> filterError(){
		return marker -> {
			try {
				Object severity = marker.getAttribute(IMarker.SEVERITY);
				return severity == null || severity.equals(IMarker.SEVERITY_ERROR);
			} catch (CoreException e1) {
				return true;
			}
		};
	}
	
}
