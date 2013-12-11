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

package ve.net.dcs.ui.test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import ve.net.dcs.ui.util.HelperImage;

import au.com.southsky.jfreesane.SaneDevice;
import au.com.southsky.jfreesane.SaneException;
import au.com.southsky.jfreesane.SaneSession;

/**
 * @author Double Click Sistemas C.A. - http://dcs.net.ve
 * @author Saul Pina - spina@dcs.net.ve
 */
public class ScanTest {

	public static void main(String[] args) throws IOException, SaneException {

		String ip = "127.0.0.1";
		String port = "6566";

		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-host"))
				ip = args[i + 1];
			else if (args[i].equals("-port"))
				port = args[i + 1];
		}

		System.out.println("START: " + ip);
		InetAddress address = InetAddress.getByName(ip);
		SaneSession session = SaneSession.withRemoteSane(address, Integer.parseInt(port));
		List<SaneDevice> devices = session.listDevices();
		System.out.println("DEVICES: " + devices.size());
		int i = 1;
		for (SaneDevice device : devices) {

			System.out.println(device);
			System.out.println("CONNECTING TO: " + device.getModel());
			device.open();
			System.out.println("CONNECTED");
			System.out.println("SCANNING");
			BufferedImage image = device.acquireImage();
			System.out.println("SAVING IMAGE");
			HelperImage.writeImage(image, "image" + i + ".png");
			device.close();
			i++;
		}

	}

}
