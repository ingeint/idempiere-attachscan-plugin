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

package ve.net.dcs.ui.view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import ve.net.dcs.ui.feature.SCUIFeature;
import ve.net.dcs.ui.feature.SCUILocale;
import ve.net.dcs.ui.feature.SCUIStandard;
import ve.net.dcs.ui.util.JIntegerField;
import ve.net.dcs.ui.component.SaneDeviceWrap;
import ve.net.dcs.ui.controller.ControllerScan;

/**
 * @author Double Click Sistemas C.A. - http://dcs.net.ve
 * @author Saul Pina - spina@dcs.net.ve
 */
public class ViewScan extends JFrame {

	private static final long serialVersionUID = -852528628596429017L;

	private List<JButton> buttons;
	private List<JMenuItem> menuItems;
	private JMenuBar mnuBar;
	private JMenu mnuOptions;
	private JMenu mnuHelp;
	private JMenu mnuLocale;
	private JMenuItem mniClose;
	private JMenuItem mniAbout;
	private JMenuItem mniDocumentation;
	private JPanel noPanel;
	private JLabel lblLogo;
	private JLabel lblHost;
	private JTextField txtHost;
	private JButton btnSearch;
	private JLabel lblDevice;
	private JComboBox<SaneDeviceWrap> cmbDevice;
	private JButton btnScan;
	private DefaultComboBoxModel<SaneDeviceWrap> cmbDeviceModel;
	private JMenuItem mniSave;
	private JLabel lblImage;
	private JLabel lblPort;
	private JIntegerField txtPort;
	private JLabel lblResolution;
	private JIntegerField txtResolution;

	public ViewScan() {
		setSize(600, 600);
		setLocationRelativeTo(this);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setIconImage(SCUIStandard.ICON);
		setLayout(new BorderLayout());
		setTitle(String.format("%s - %s", SCUIFeature.get("APP_NAME"), SCUILocale.get("ViewScan.title")));
		buttons = new ArrayList<JButton>();
		menuItems = new ArrayList<JMenuItem>();

		// MENU BAR
		mnuBar = new JMenuBar();
		setJMenuBar(mnuBar);

		mnuOptions = new JMenu();
		mnuHelp = new JMenu();
		mnuBar.add(mnuOptions);
		mnuBar.add(mnuHelp);

		mniSave = new JMenuItem();
		mnuLocale = new JMenu();
		mniClose = new JMenuItem();
		mnuOptions.add(mniSave);
		mnuOptions.add(mnuLocale);
		mnuOptions.add(mniClose);

		mniDocumentation = new JMenuItem();
		mniAbout = new JMenuItem();
		mnuHelp.add(mniDocumentation);
		mnuHelp.add(mniAbout);

		menuItems.add(mniSave);
		menuItems.add(mniDocumentation);
		menuItems.add(mniAbout);
		menuItems.add(mniClose);

		// NORTH PANEL
		noPanel = new JPanel();
		noPanel.setLayout(new MigLayout());

		lblLogo = new JLabel();
		lblLogo.setIcon(SCUIStandard.LOGO);

		lblHost = new JLabel();
		txtHost = new JTextField();
		lblPort = new JLabel();
		txtPort = new JIntegerField();
		btnSearch = new JButton();

		lblDevice = new JLabel();
		cmbDevice = new JComboBox<SaneDeviceWrap>();
		cmbDeviceModel = new DefaultComboBoxModel<SaneDeviceWrap>();
		cmbDevice.setModel(cmbDeviceModel);
		btnScan = new JButton();

		lblResolution = new JLabel();
		txtResolution = new JIntegerField();

		buttons.add(btnScan);
		buttons.add(btnSearch);

		noPanel.add(lblLogo, "span 1 3");
		noPanel.add(lblHost, "width 60, height 26");
		noPanel.add(txtHost, "width 200, height 26");
		noPanel.add(lblPort, "width 40, height 26");
		noPanel.add(txtPort, "width 80, height 26");
		noPanel.add(btnSearch, "width 60, height 26, wrap");
		noPanel.add(lblDevice, "grow, height 26");
		noPanel.add(cmbDevice, "growx, span 3, height 26");
		noPanel.add(btnScan, "width 60, height 26, wrap");
		noPanel.add(lblResolution, "width 40, height 26");
		noPanel.add(txtResolution, "width 80, height 26");
		add(noPanel, BorderLayout.NORTH);

		// CENTER PANEL

		lblImage = new JLabel();
		JScrollPane scroll = new JScrollPane(lblImage);
		add(scroll, BorderLayout.CENTER);

	}

	public void addListener(ControllerScan listener) {
		for (JButton button : buttons) {
			button.addActionListener(listener);
		}

		for (JMenuItem menuItem : menuItems) {
			menuItem.addActionListener(listener);
		}

		addWindowListener(listener);
	}

	public JMenu getMnuOptions() {
		return mnuOptions;
	}

	public JMenu getMnuHelp() {
		return mnuHelp;
	}

	public JMenuItem getMnuLocale() {
		return mnuLocale;
	}

	public JMenuItem getMniClose() {
		return mniClose;
	}

	public JMenuItem getMniAbout() {
		return mniAbout;
	}

	public JLabel getLblHost() {
		return lblHost;
	}

	public JTextField getTxtHost() {
		return txtHost;
	}

	public JButton getBtnSearch() {
		return btnSearch;
	}

	public JComboBox<SaneDeviceWrap> getCmbDevice() {
		return cmbDevice;
	}

	public JButton getBtnScan() {
		return btnScan;
	}

	public DefaultComboBoxModel<SaneDeviceWrap> getCmbDeviceModel() {
		return cmbDeviceModel;
	}

	public JLabel getLblDevice() {
		return lblDevice;
	}

	public JMenuItem getMniSave() {
		return mniSave;
	}

	public List<JButton> getButtons() {
		return buttons;
	}

	public List<JMenuItem> getMenuItems() {
		return menuItems;
	}

	public JLabel getLblImage() {
		return lblImage;
	}

	public JLabel getLblPort() {
		return lblPort;
	}

	public JIntegerField getTxtPort() {
		return txtPort;
	}

	public JMenuItem getMniDocumentation() {
		return mniDocumentation;
	}

	public JLabel getLblResolution() {
		return lblResolution;
	}

	public JIntegerField getTxtResolution() {
		return txtResolution;
	}

}
