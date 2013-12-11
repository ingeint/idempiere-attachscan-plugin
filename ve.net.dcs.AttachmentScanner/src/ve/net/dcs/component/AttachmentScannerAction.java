/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/

package ve.net.dcs.component;

import org.adempiere.webui.action.IAction;
import org.adempiere.webui.adwindow.ADWindow;
import org.adempiere.webui.adwindow.ADWindowContent;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.MRole;

import ve.net.dcs.window.WAttachmentScanner;

/**
 * Attachment Scanner Action
 * 
 * @author Double Click Sistemas C.A. - http://dcs.net.ve
 * @author Saul Pina - spina@dcs.net.ve
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
