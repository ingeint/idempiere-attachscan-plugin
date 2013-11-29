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

package ve.net.dcs.window;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.AdempiereWebUI;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.factory.ButtonFactory;
import org.compiere.model.MSysConfig;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.South;
import org.zkoss.zul.Vlayout;

import ve.net.dcs.util.HelperImage;

import au.com.southsky.jfreesane.SaneDevice;
import au.com.southsky.jfreesane.SaneSession;

/**
 * Scanner Interface, Use SANE and JFreeSane is a pure-Java implementation of a
 * SANE client
 * 
 * @author Double Click Sistemas C.A. - http://dcs.net.ve
 * @author Saul Pina - spina@dcs.net.ve
 * @see https://code.google.com/p/jfreesane/
 * @see https://code.google.com/p/guava-libraries/
 */
public class WSelectScanner extends Window implements EventListener<Event> {

	private static final long serialVersionUID = -3951726291472195749L;
	private static CLogger log = CLogger.getCLogger(WSelectScanner.class);

	private Button bCancel = ButtonFactory.createNamedButton(ConfirmPanel.A_CANCEL, false, true);
	private Button bOk = ButtonFactory.createNamedButton(ConfirmPanel.A_OK, false, true);
	private Borderlayout mainPanel = new Borderlayout();
	private Listbox cbContent = new Listbox();
	private Hlayout confirmPanel = new Hlayout();
	private Hbox toolBar = new Hbox();
	private WAttachmentScanner wAttachmentScanner;

	public WSelectScanner(WAttachmentScanner wAttachmentScanner) {
		this.wAttachmentScanner = wAttachmentScanner;
		this.setAttribute(AdempiereWebUI.WIDGET_INSTANCE_NAME, "select_scanner");
		this.setMaximizable(false);
		this.setWidth("400px");
		this.setHeight("150px");
		this.setTitle(Msg.getMsg(Env.getCtx(), "SelectScanner"));
		this.setClosable(true);
		this.setSizable(false);
		this.setBorder("normal");
		this.setSclass("popup-dialog");
		this.setShadow(true);
		this.appendChild(mainPanel);
		mainPanel.setHeight("100%");
		mainPanel.setWidth("100%");

		Center centerPanel = new Center();

		cbContent.setMold("select");
		cbContent.setRows(0);
		cbContent.addEventListener(Events.ON_SELECT, this);

		Label cbLabel = new Label(Msg.getMsg(Env.getCtx(), "SelectScanner"));
		toolBar.setAlign("center");
		toolBar.setPack("start");
		centerPanel.setStyle("padding: 10px");
		toolBar.appendChild(cbLabel);
		toolBar.appendChild(cbContent);

		mainPanel.appendChild(centerPanel);
		Vlayout div = new Vlayout();
		div.appendChild(toolBar);
		centerPanel.appendChild(div);

		confirmPanel.setHflex("1");
		Hbox hbox = new Hbox();
		hbox.setPack("end");
		hbox.setHflex("1");
		confirmPanel.appendChild(hbox);
		hbox.appendChild(bOk);
		hbox.appendChild(bCancel);

		South southPane = new South();
		southPane.setSclass("dialog-footer");
		mainPanel.appendChild(southPane);
		southPane.appendChild(confirmPanel);
		southPane.setVflex("min");

		bCancel.addEventListener(Events.ON_CLICK, this);
		bOk.addEventListener(Events.ON_CLICK, this);
		loadScanner();
	}

	@Override
	public void onEvent(Event e) throws Exception {
		if (e.getTarget() == bCancel) {
			dispose();
		} else if (e.getTarget() == bOk) {
			getImage();
		}
	}

	private void getImage() {
		if (cbContent.getItems().size() == 0)
			return;

		SaneDevice device = cbContent.getSelectedItem().getValue();
		String extension = MSysConfig.getValue("EXTENSION_SCANNER_IMAGE", "jpg", Env.getAD_Client_ID(Env.getCtx()));
		byte[] image = null;

		try {
			device.open();
			BufferedImage fi = device.acquireImage();
			image = HelperImage.bufferedImageToByteArray(fi, extension);
			wAttachmentScanner.loadImageFromScanner(image, String.format("%s.%s", new Timestamp(System.currentTimeMillis()), extension));
			dispose();
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error getting scanner image", e);
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "ERROR_GET_SCANNER_IMAGE"));
		} finally {
			try {
				if (device.isOpen())
					device.close();
			} catch (IOException e) {
				log.log(Level.SEVERE, "Error closing scanner", e);
			}
		}
	}

	private void loadScanner() {
		String ip_scanners = MSysConfig.getValue("IP_SCANNERS", Env.getAD_Client_ID(Env.getCtx()));
		if (ip_scanners == null)
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "NO_IP_SCANNER_CONFIG"));

		String[] ips = ip_scanners.trim().split(";");

		if (ips.length == 0)
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "IP_SCANNER_CONFIG_EMPTY"));

		List<SaneDevice> devices = new ArrayList<SaneDevice>();

		for (int i = 0; i < ips.length; i++) {
			try {
				InetAddress address = InetAddress.getByName(ips[i]);
				SaneSession session = SaneSession.withRemoteSane(address);
				devices.addAll(session.listDevices());
			} catch (Exception e) {
				log.log(Level.SEVERE, "Error getting scanner list", e);
				e.printStackTrace();
			}
		}

		if (devices.size() == 0)
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "NO_FOUND_SCANNER"));
		else
			for (SaneDevice saneDevice : devices)
				cbContent.appendItem(saneDevice.getVendor() + "-" + saneDevice.getModel(), saneDevice);

	}

	public void dispose() {
		this.detach();
	}

}
