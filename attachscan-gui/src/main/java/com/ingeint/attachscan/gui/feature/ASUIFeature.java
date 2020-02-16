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

package com.ingeint.attachscan.gui.feature;

import java.io.IOException;
import java.util.Properties;

public class ASUIFeature {

    private static Properties properties = new Properties();

    public static void load() throws IOException {
        properties.load(ASUIFeature.class.getClassLoader().getResourceAsStream("features.properties"));
        properties.put("OS", System.getProperty("os.name"));
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static void set(String key, String value) {
        properties.put(key, value);
    }

}
