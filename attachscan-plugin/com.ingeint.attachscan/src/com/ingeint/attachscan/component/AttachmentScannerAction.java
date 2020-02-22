/**
 * This file is part of Attach Scan.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Copyright (C) 2015 INGEINT <http://www.ingeint.com>.
 * Copyright (C) Contributors.
 * 
 * Contributors:
 *    - 2015 Saúl Piña <spina@ingeint.com>.
 */

package com.ingeint.attachscan.component;

import org.adempiere.webui.action.IAction;
import org.adempiere.webui.adwindow.ADWindow;
import org.adempiere.webui.adwindow.ADWindowContent;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.MRole;

import com.ingeint.attachscan.window.WAttachmentScanner;

/**
 * Attachment Scanner Action
 */
public class AttachmentScannerAction implements IAction {

	public AttachmentScannerAction() {

	}

	@Override
	public void execute(Object target) {
		ADWindow adwindow = (ADWindow) target;
		ADWindowContent panel = adwindow.getADWindowContent();

		if (!MRole.getDefault().isCanExport()) {
			FDialog.error(panel.getWindowNo(), "AccessTableNoView");
			return;
		}

		WAttachmentScanner win = new WAttachmentScanner(panel.getWindowNo(), panel.getActiveGridTab().getAD_AttachmentID(), panel.getActiveGridTab().getAD_Table_ID(), panel.getActiveGridTab().getRecord_ID(), null);
		AEnv.showWindow(win);
	}

}
