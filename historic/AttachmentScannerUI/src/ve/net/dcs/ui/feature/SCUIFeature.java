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

package ve.net.dcs.ui.feature;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author Double Click Sistemas C.A. - http://dcs.net.ve
 * @author Saul Pina - spina@dcs.net.ve
 */
public class SCUIFeature {

	private static Properties properties = new Properties();

	public static void load() throws FileNotFoundException, IOException  {
		properties.load(new FileInputStream("resource/features.properties"));
		properties.put("OS", System.getProperty("os.name"));
	}

	public static String get(String key) {
		return properties.getProperty(key);
	}

	public static Enumeration<Object> getKeys() {
		return properties.keys();
	}
	
	public static void set(String key, String value) {
		properties.put(key, value);
	}

	public static void save() throws FileNotFoundException, IOException {
		properties.store(new FileOutputStream("resource/features.properties"), "AttachmentScannerUI");
	}


}
