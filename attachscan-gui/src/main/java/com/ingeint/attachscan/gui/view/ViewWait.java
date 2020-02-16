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

import com.ingeint.attachscan.gui.feature.ASUILocale;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewWait extends JDialog implements ActionListener {

    private static final long serialVersionUID = -2622435195146409448L;
    private Timer timer;
    private JLabel lblMessage;
    private String message;
    private String title;
    private String tempMessage;
    private int sec;
    private boolean close;

    public ViewWait(String title, String message) {
        this.title = title;
        this.message = message;
        init();
    }

    public ViewWait(String message) {
        title = ASUILocale.get("ViewWait.title");
        this.message = message;
        init();
    }

    public ViewWait() {
        title = ASUILocale.get("ViewWait.title");
        message = ASUILocale.get("ViewWait.message");
        init();
    }

    private void init() {
        setTitle(title);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        removeCloseButton(this);
        setSize(250, 100);
        setLocationRelativeTo(this);

        lblMessage = new JLabel(message);
        lblMessage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Font font = new Font(lblMessage.getFont().getName(), Font.PLAIN, 16);
        lblMessage.setFont(font);
        add(lblMessage);
        timer = new Timer(1000, this);
        sec = 0;
        close = false;
    }

    public void display() {
        if (!close) {
            tempMessage = message;
            timer.start();
            setVisible(true);
        }
    }

    public void close() {
        timer.stop();
        close = true;
        dispose();
    }

    private void removeCloseButton(Component comp) {
        if (comp instanceof JMenu) {
            Component[] children = ((JMenu) comp).getMenuComponents();
            for (int i = 0; i < children.length; ++i)
                removeCloseButton(children[i]);
        } else if (comp instanceof AbstractButton) {
            Action action = ((AbstractButton) comp).getAction();
            String cmd = (action == null) ? "" : action.toString();
            if (cmd.contains("CloseAction")) {
                comp.getParent().remove(comp);
            }
        } else if (comp instanceof Container) {
            Component[] children = ((Container) comp).getComponents();
            for (int i = 0; i < children.length; ++i)
                removeCloseButton(children[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        sec++;
        if (sec > 15) {
            tempMessage = message;
            sec = 0;
        } else {
            tempMessage += ".";
        }
        lblMessage.setText(tempMessage);
    }

}
