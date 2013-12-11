/**
 * 
 * AttachmentScanner
 * 
 * Copyright (C) Double Click Sistemas C.A. RIF: J-31576020-7 
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 * Double Click Sistemas C.A. Barquisimeto, Venezuela, http://dcs.net.ve
 * 
 */

package ve.net.dcs.ui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import au.com.southsky.jfreesane.SaneDevice;
import au.com.southsky.jfreesane.SaneSession;

import ve.net.dcs.ui.view.ViewWait;
import ve.net.dcs.ui.component.SaneDeviceWrap;
import ve.net.dcs.ui.feature.SCUIFeature;
import ve.net.dcs.ui.feature.SCUILocale;
import ve.net.dcs.ui.util.HelperDate;
import ve.net.dcs.ui.util.HelperFile;
import ve.net.dcs.ui.util.HelperImage;
import ve.net.dcs.ui.view.ViewAbout;
import ve.net.dcs.ui.view.ViewScan;

/**
 * @author Double Click Sistemas C.A. - http://dcs.net.ve
 * @author Saul Pina - spina@dcs.net.ve
 */
public class ControllerScan implements ActionListener, WindowListener {

	private ViewScan viewScan;
	private BufferedImage image;
	private ViewWait viewWait;

	public ControllerScan() {
		viewScan = new ViewScan();
		initView();
		loadLocale();
		viewScan.addListener(this);
		viewScan.setVisible(true);
	}

	public void close() {
		viewScan.dispose();
		System.exit(0);
	}

	public void initView() {
		viewScan.getTxtHost().setText("127.0.0.1");
		viewScan.getTxtPort().setText("6566");
		for (String string : SCUILocale.list()) {
			JMenuItem item = new JMenuItem(string);
			item.addActionListener(this);
			viewScan.getMenuItems().add(item);
			viewScan.getMnuLocale().add(item);
		}
	}

	public void loadLocale() {
		viewScan.getMniAbout().setText(SCUILocale.get("ViewScan.mniAbout"));
		viewScan.getMniClose().setText(SCUILocale.get("ViewScan.mniClose"));
		viewScan.getMnuLocale().setText(SCUILocale.get("ViewScan.mnuLocale"));
		viewScan.getMnuHelp().setText(SCUILocale.get("ViewScan.mnuHelp"));
		viewScan.getMnuOptions().setText(SCUILocale.get("ViewScan.mnuOptions"));
		viewScan.getLblHost().setText(SCUILocale.get("ViewScan.lblHost"));
		viewScan.getBtnSearch().setText(SCUILocale.get("ViewScan.btnSearch"));
		viewScan.getLblDevice().setText(SCUILocale.get("ViewScan.lblDevice"));
		viewScan.getBtnScan().setText(SCUILocale.get("ViewScan.btnScan"));
		viewScan.getMniSave().setText(SCUILocale.get("ViewScan.mniSave"));
		viewScan.getLblPort().setText(SCUILocale.get("ViewScan.lblPort"));
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource().equals(viewScan.getMniClose()))
			close();
		else if (ae.getSource().equals(viewScan.getMniSave()))
			save();
		else if (ae.getSource().equals(viewScan.getMniAbout()))
			about();
		else if (ae.getSource().equals(viewScan.getBtnSearch()))
			search();
		else if (ae.getSource().equals(viewScan.getBtnScan()))
			scan();
		else if (ae.getSource().getClass().equals(JMenuItem.class))
			changeLocale(((JMenuItem) ae.getSource()).getText());

	}

	public void scan() {
		viewWait = new ViewWait();
		new Thread(new Runnable() {
			@Override
			public void run() {
				SaneDevice device = null;
				try {
					device = ((SaneDeviceWrap) viewScan.getCmbDevice().getSelectedItem()).getDevice();
					device.open();
					image = device.acquireImage();
					viewScan.getLblImage().setIcon(new ImageIcon(image));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(viewScan, SCUILocale.get("ViewScan.errorDisplayImage"), "Error", JOptionPane.ERROR_MESSAGE);
				} finally {
					viewWait.close();
					try {
						if (device != null)
							if (device.isOpen())
								device.close();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(viewScan, SCUILocale.get("ViewScan.unexpectedError"), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}).start();
		viewWait.display();
	}

	public void search() {
		viewWait = new ViewWait();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InetAddress address = InetAddress.getByName(viewScan.getTxtHost().getText().trim());
					SaneSession session = SaneSession.withRemoteSane(address, viewScan.getTxtPort().getInteger());
					viewScan.getCmbDeviceModel().removeAllElements();
					for (SaneDevice device : session.listDevices()) {
						viewScan.getCmbDeviceModel().addElement(new SaneDeviceWrap(device));
					}
				} catch (Exception e) {
					viewScan.getCmbDeviceModel().removeAllElements();
					viewScan.getLblImage().setIcon(null);
					image = null;
					JOptionPane.showMessageDialog(viewScan, SCUILocale.get("ViewScan.errorLoadDevice"), "Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				} finally {
					viewWait.close();
				}
			}
		}).start();
		viewWait.display();
	}

	public void changeLocale(String locale) {
		try {
			SCUILocale.load(locale);
			loadLocale();
			SCUIFeature.set("DEFAULT_LOCALE", locale);
			SCUIFeature.save();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(viewScan, SCUILocale.get("ViewScan.errorLoadLocale"), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public void about() {
		ViewAbout viewAbout = new ViewAbout();
		viewAbout.setVisible(true);
	}

	public void save() {
		if (image == null) {
			JOptionPane.showMessageDialog(viewScan, SCUILocale.get("ViewScan.noScan"), "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileFilter(new FileNameExtensionFilter("jpg", "jpg"));
			fileChooser.setFileFilter(new FileNameExtensionFilter("png", "png"));
			fileChooser.setSelectedFile(new File(HelperDate.nowFormat("yyyyMMddHHmmss")));
			fileChooser.showSaveDialog(viewScan);
			if (fileChooser.getSelectedFile() != null) {
				String path = fileChooser.getSelectedFile().getAbsolutePath();
				path = HelperFile.isFileType(path, "jpg", "png") ? path : path + "." + fileChooser.getFileFilter().getDescription();
				try {
					HelperImage.writeImage(image, path);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(viewScan, SCUILocale.get("ViewScan.noSave"), "Error", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {

	}

	@Override
	public void windowClosed(WindowEvent arg0) {

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		close();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {

	}

	@Override
	public void windowIconified(WindowEvent arg0) {

	}

	@Override
	public void windowOpened(WindowEvent arg0) {

	}

}
