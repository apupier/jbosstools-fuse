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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class WhiteListPaletteFieldEditor extends ListEditor {
	
	public WhiteListPaletteFieldEditor(Composite parent) {
		init(WhiteListPaletteConstants.PREFERENCE_PALETTE_WHITELIST, "List of Camel components provided directly in the palette:");
		createControl(parent);
	}
	
	@Override
	protected String[] parseString(String stringList) {
		return stringList.split(WhiteListPaletteConstants.PREFERENCE_SEPARATOR);
	}

	@Override
	protected String getNewInputObject() {
		InputDialog inputDialog = new InputDialog(getShell(), "", "Which Camel component do you want to have directly on your", "",new IInputValidator() {
			
			@Override
			public String isValid(String newText) {
				if(newText == null || newText.isEmpty()) {
					return "Please provide a value";
				} else if(Arrays.asList(getList().getItems()).contains(newText)) {
					return "This camel component is already available";
				} else {
					return null;
				}
			}
		});
		
		if(Dialog.OK == inputDialog.open()) {
			return inputDialog.getValue();
		} else {
			return null;
		}
	}

	@Override
	protected String createList(String[] items) {
		return String.join(WhiteListPaletteConstants.PREFERENCE_SEPARATOR, items);
	}
	
}