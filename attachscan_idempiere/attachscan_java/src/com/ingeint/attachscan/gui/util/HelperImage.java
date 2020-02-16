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

package com.ingeint.attachscan.gui.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public abstract class HelperImage {

	public static BufferedImage readImage(String path) throws IOException {
		return readImage(new File(path));
	}

	public static BufferedImage readImage(File file) throws IOException {
		return ImageIO.read(file);
	}

	public static boolean writeImage(BufferedImage image, String path) throws IOException {
		return writeImage(image, new File(path));
	}

	public static boolean writeImage(BufferedImage image, File file) throws IOException {
		String fileName = file.getName();
		String extension = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
		return ImageIO.write(image, extension, file);
	}

	public static byte[] bufferedImageToByteArray(BufferedImage image, String extension) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, extension, baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		return imageInByte;
	}

	public static BufferedImage byteArrayToBufferedImage(byte[] image) throws IOException {
		InputStream in = new ByteArrayInputStream(image);
		return ImageIO.read(in);
	}

	public static BufferedImage resizePercent(BufferedImage image, float percent) {
		if (percent > 1) {
			percent = percent / 100;
		}
		int width = (int) (image.getWidth() * percent);
		int height = (int) (image.getHeight() * percent);
		return resize(image, width, height);
	}

	public static BufferedImage resizeMaxHeight(BufferedImage image, int height) {
		int width = (int) (image.getWidth() * (height * 100 / image.getHeight()) / 100);
		return resize(image, width, height);
	}

	public static BufferedImage resizeMaxWidth(BufferedImage image, int width) {
		int height = (int) (image.getHeight() * (width * 100 / image.getWidth()) / 100);
		return resize(image, width, height);
	}

	public static BufferedImage resize(BufferedImage image, int width, int height) {
		int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		return resizedImage;
	}
}
