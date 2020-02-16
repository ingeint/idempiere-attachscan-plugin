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

package ve.net.dcs.ui.app;

import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import jline.ConsoleReader;

import org.jvnet.substance.SubstanceLookAndFeel;

import au.com.southsky.jfreesane.SaneDevice;
import au.com.southsky.jfreesane.SaneSession;

import ve.net.dcs.ui.controller.ControllerScan;
import ve.net.dcs.ui.feature.SCUIFeature;
import ve.net.dcs.ui.feature.SCUILocale;
import ve.net.dcs.ui.util.HelperDate;
import ve.net.dcs.ui.util.HelperFile;
import ve.net.dcs.ui.util.HelperImage;

/**
 * @author Double Click Sistemas C.A. - http://dcs.net.ve
 * @author Saul Pina - spina@dcs.net.ve
 */
public class Main {

	private static Logger logger = Logger.getLogger(Main.class.getName());

	private static void loadFeaturesUI() {
		try {
			SCUIFeature.load();
			SCUILocale.load(SCUIFeature.get("DEFAULT_LOCALE"));
		} catch (Exception e) {
			logger.severe("Error loading features");
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error loading features", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	private static void loadFeatures() {
		try {
			SCUIFeature.load();
		} catch (Exception e) {
			logger.severe("Error loading features");
			e.printStackTrace();
			System.exit(0);
		}
	}

	private static void initUI() {
		loadFeaturesUI();
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		SubstanceLookAndFeel.setSkin("org.jvnet.substance.skin.CremeSkin");
		UIManager.put(SubstanceLookAndFeel.NO_EXTRA_ELEMENTS, Boolean.TRUE);
		new ControllerScan();
	}

	public static void main(String[] args) {
		if (args.length > 0) {
			initConsole(args);
		} else {
			initUI();
		}
	}

	private static void initConsole(String[] args) {
		loadFeatures();

		String ip = "127.0.0.1";
		String port = "6566";
		String format = "png";
		String iname = null;

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-host"))
				ip = args[i + 1];
			else if (args[i].equals("-port"))
				port = args[i + 1];
			else if (args[i].equals("-format"))
				format = args[i + 1];
			else if (args[i].equals("-iname"))
				iname = args[i + 1];
			else if (args[i].equals("-help")) {
				System.out.println(String.format("%10s\t%s", "-version", "Displays the version of the application"));
				System.out.println(String.format("%10s\t%s", "-help", "Displays the commands that can be used"));
				System.out.println(String.format("%10s\t%s", "-host", "Host IP Sane"));
				System.out.println(String.format("%10s\t%s", "-format", "Image format, jpg png"));
				System.out.println(String.format("%10s\t%s", "-port", "Host Port Sane"));
				System.out.println(String.format("%10s\t%s", "-iname", "Image name"));
				System.out.println();
				System.exit(0);
			} else if (args[i].equals("-version")) {
				System.out.println(String.format("%s %s", SCUIFeature.get("APP_NAME"), SCUIFeature.get("VERSION")));
				System.out.println(String.format(SCUIFeature.get("VENDOR")));
				System.out.println(String.format(SCUIFeature.get("WEB")));
				System.out.println();
				System.exit(0);
			}
		}

		try {
			ConsoleReader console = new ConsoleReader();
			System.out.println("Start: " + ip + ":" + port);
			InetAddress address = InetAddress.getByName(ip);
			SaneSession session = SaneSession.withRemoteSane(address, Integer.parseInt(port));
			List<SaneDevice> devices = session.listDevices();
			System.out.println("Devices: " + devices.size());

			if (devices.size() == 0)
				System.exit(0);

			for (int i = 0; i < devices.size(); i++) {
				System.out.println((i + 1) + ". " + devices.get(i));
			}

			boolean getSelect;
			String select;
			do {
				select = console.readLine("Enter device number: ");
				if (select.trim().isEmpty()) {
					getSelect = false;
				} else if (!select.matches("[0-9]+")) {
					System.out.println("Input must be numbers");
					getSelect = false;
				} else if (Integer.parseInt(select) - 1 < 0 || Integer.parseInt(select) - 1 >= devices.size()) {
					getSelect = false;
				} else {
					getSelect = true;
				}
			} while (!getSelect);

			SaneDevice device = devices.get(Integer.parseInt(select) - 1);
			System.out.println("Connecting to: " + device.getModel());
			device.open();
			System.out.println("Connected");
			System.out.println("Scanning");
			BufferedImage image = device.acquireImage();
			System.out.println("Saving image");
			iname = (iname == null ? HelperDate.nowFormat("yyyyMMddHHmmss") : iname);
			HelperImage.writeImage(image, (HelperFile.isFileType(iname, "jpg", "png") ? iname : iname + "." + format));
			device.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
