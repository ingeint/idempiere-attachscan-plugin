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

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;
import ve.net.dcs.ui.feature.SCUIFeature;
import ve.net.dcs.ui.feature.SCUILocale;
import ve.net.dcs.ui.feature.SCUIStandard;

/**
 * @author Double Click Sistemas C.A. - http://dcs.net.ve
 * @author Saul Pina - spina@dcs.net.ve
 */
public class ViewAbout extends JDialog {

	private static final long serialVersionUID = -7176457186920104185L;

	public ViewAbout() {
		setTitle(SCUILocale.get("ViewAbout.title") + " " + SCUIFeature.get("APP_NAME"));
		setModal(true);
		setSize(450, 230);
		setLocationRelativeTo(this);
		setLayout(new BorderLayout());
		JLabel lblLogo = new JLabel();
		lblLogo.setIcon(SCUIStandard.LOGO);
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblLogo, BorderLayout.NORTH);
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		add(panel, BorderLayout.CENTER);

		panel.add(new JLabel(String.format("%s %s", SCUIFeature.get("APP_NAME"), SCUIFeature.get("VERSION"))), "width  100%, wrap");
		panel.add(new JLabel(SCUIFeature.get("VENDOR")), "grow, wrap");
		panel.add(new JLabel(SCUIFeature.get("WEB")), "grow, wrap");
		panel.add(new JLabel(SCUIFeature.get("DOCUMENTATION")), "grow, wrap");
	}

}
