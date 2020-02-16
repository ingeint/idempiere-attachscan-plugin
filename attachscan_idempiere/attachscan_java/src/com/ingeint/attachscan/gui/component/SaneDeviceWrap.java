/**
 * This file is part of Attach Scan.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Copyright (C) 2015 INGEINT <http://www.ingeint.com>.
 * Copyright (C) Contributors.
 * 
 * Contributors:
 *    - 2015 Saúl Piña <spina@ingeint.com>.
 */

package com.ingeint.attachscan.gui.component;

import au.com.southsky.jfreesane.SaneDevice;

public class SaneDeviceWrap {
	private SaneDevice device;

	public SaneDevice getDevice() {
		return device;
	}

	public void setDevice(SaneDevice device) {
		this.device = device;
	}

	public SaneDeviceWrap(SaneDevice device) {
		super();
		this.device = device;
	}

	public SaneDeviceWrap() {
	}

	@Override
	public String toString() {
		return device.getVendor() + "-" + device.getModel();
	}

}
