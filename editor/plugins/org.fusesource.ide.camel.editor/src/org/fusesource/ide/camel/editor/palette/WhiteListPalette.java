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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
import org.fusesource.ide.preferences.PreferenceManager;

public class WhiteListPalette {

	
	public Set<String> getWhiteList() {
		String paletteWhiteList = getPreferenceStore().getString(WhiteListPaletteConstants.PREFERENCE_PALETTE_WHITELIST);
		return new HashSet<>(Arrays.asList(paletteWhiteList.split(WhiteListPaletteConstants.PREFERENCE_SEPARATOR)));
	}
	
	IPreferenceStore getPreferenceStore() {
		return PreferenceManager.getInstance().getUnderlyingStorage();
	}

	
}
