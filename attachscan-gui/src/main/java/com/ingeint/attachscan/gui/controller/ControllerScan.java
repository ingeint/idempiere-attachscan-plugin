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

package com.ingeint.attachscan.gui.controller;

import au.com.southsky.jfreesane.SaneDevice;
import au.com.southsky.jfreesane.SaneSession;
import com.ingeint.attachscan.gui.component.SaneDeviceWrap;
import com.ingeint.attachscan.gui.feature.ASUIFeature;
import com.ingeint.attachscan.gui.feature.ASUILocale;
import com.ingeint.attachscan.gui.util.HelperDate;
import com.ingeint.attachscan.gui.util.HelperFile;
import com.ingeint.attachscan.gui.util.HelperImage;
import com.ingeint.attachscan.gui.view.ViewAbout;
import com.ingeint.attachscan.gui.view.ViewScan;
import com.ingeint.attachscan.gui.view.ViewWait;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;

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
        viewScan.getTxtHost().setText(ASUIFeature.get("DEFAULT_HOST"));
        viewScan.getTxtResolution().setText(ASUIFeature.get("DEFAULT_RESOLUTION"));
        viewScan.getTxtPort().setText(ASUIFeature.get("DEFAULT_PORT"));
        for (String string : ASUILocale.list()) {
            JMenuItem item = new JMenuItem(string);
            item.addActionListener(this);
            viewScan.getMenuItems().add(item);
            viewScan.getMnuLocale().add(item);
        }
    }

    public void loadLocale() {
        viewScan.getMniAbout().setText(ASUILocale.get("ViewScan.mniAbout"));
        viewScan.getMniClose().setText(ASUILocale.get("ViewScan.mniClose"));
        viewScan.getMnuLocale().setText(ASUILocale.get("ViewScan.mnuLocale"));
        viewScan.getMnuHelp().setText(ASUILocale.get("ViewScan.mnuHelp"));
        viewScan.getMnuOptions().setText(ASUILocale.get("ViewScan.mnuOptions"));
        viewScan.getLblHost().setText(ASUILocale.get("ViewScan.lblHost"));
        viewScan.getBtnSearch().setText(ASUILocale.get("ViewScan.btnSearch"));
        viewScan.getLblDevice().setText(ASUILocale.get("ViewScan.lblDevice"));
        viewScan.getBtnScan().setText(ASUILocale.get("ViewScan.btnScan"));
        viewScan.getMniSave().setText(ASUILocale.get("ViewScan.mniSave"));
        viewScan.getMniDocumentation().setText(ASUILocale.get("ViewScan.mniDocumentation"));
        viewScan.getLblPort().setText(ASUILocale.get("ViewScan.lblPort"));
        viewScan.getLblResolution().setText(ASUILocale.get("ViewScan.lblResolution"));
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(viewScan.getMniClose()))
            close();
        else if (ae.getSource().equals(viewScan.getMniSave()))
            save();
        else if (ae.getSource().equals(viewScan.getMniAbout()))
            about();
        else if (ae.getSource().equals(viewScan.getMniDocumentation()))
            goDocumentation();
        else if (ae.getSource().equals(viewScan.getBtnSearch()))
            search();
        else if (ae.getSource().equals(viewScan.getBtnScan()))
            scan();
        else if (ae.getSource().getClass().equals(JMenuItem.class))
            changeLocale(((JMenuItem) ae.getSource()).getText());

    }

    public void goDocumentation() {
        try {
            Desktop.getDesktop().browse(new URI(ASUIFeature.get("DOCUMENTATION")));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(viewScan, ASUILocale.get("ViewScan.errorGoDocumentation"), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }

    public void scan() {
        viewWait = new ViewWait();
        new Thread(() -> {
            SaneDevice device = null;
            try {
                device = ((SaneDeviceWrap) viewScan.getCmbDevice().getSelectedItem()).getDevice();
                if (!device.isOpen()) {
                    device.open();
                }
                device.getOption("resolution").setIntegerValue(viewScan.getTxtResolution().getInteger());
                image = device.acquireImage();
                viewScan.getLblImage().setIcon(new ImageIcon(image));
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(viewScan, ASUILocale.get("ViewScan.errorDisplayImage"), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                viewWait.close();
                try {
                    if (device != null && device.isOpen()) {
                        device.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(viewScan, ASUILocale.get("ViewScan.unexpectedError"), "Error", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(viewScan, ASUILocale.get("ViewScan.errorLoadDevice"), "Error", JOptionPane.ERROR_MESSAGE);
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
            ASUILocale.load(locale);
            loadLocale();
            ASUIFeature.set("DEFAULT_LOCALE", locale);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(viewScan, ASUILocale.get("ViewScan.errorLoadLocale"), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void about() {
        ViewAbout viewAbout = new ViewAbout();
        viewAbout.setVisible(true);
    }

    public void save() {
        if (image == null) {
            JOptionPane.showMessageDialog(viewScan, ASUILocale.get("ViewScan.noScan"), "Error", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(viewScan, ASUILocale.get("ViewScan.noSave"), "Error", JOptionPane.ERROR_MESSAGE);
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
