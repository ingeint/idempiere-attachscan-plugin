/**
 * This file is part of Attach Scan.
 * <p>
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * <p>
 * Copyright (C) 2015 INGEINT <http://www.ingeint.com>.
 * Copyright (C) Contributors.
 * <p>
 * Contributors:
 * - 2015 Saúl Piña <spina@ingeint.com>.
 */

package com.ingeint.attachscan.gui.view;

import com.ingeint.attachscan.gui.feature.ASUIFeature;
import com.ingeint.attachscan.gui.feature.ASUILocale;
import com.ingeint.attachscan.gui.feature.ASUIStandard;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ViewAbout extends JDialog {

    private static final long serialVersionUID = -7176457186920104185L;

    public ViewAbout() {
        setTitle(ASUILocale.get("ViewAbout.title") + " " + ASUIFeature.get("APP_NAME"));
        setModal(true);
        setSize(450, 230);
        setLocationRelativeTo(this);
        setLayout(new BorderLayout());
        JLabel lblLogo = new JLabel();
        lblLogo.setIcon(ASUIStandard.LOGO);
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblLogo, BorderLayout.NORTH);
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        add(panel, BorderLayout.CENTER);

        panel.add(new JLabel(String.format("%s %s", ASUIFeature.get("APP_NAME"), ASUIFeature.get("VERSION"))), "width  100%, wrap");
        panel.add(new JLabel(ASUIFeature.get("VENDOR")), "grow, wrap");
        panel.add(new JLabel(ASUIFeature.get("WEB")), "grow, wrap");
        panel.add(new JLabel(ASUIFeature.get("DOCUMENTATION")), "grow, wrap");
    }

}
