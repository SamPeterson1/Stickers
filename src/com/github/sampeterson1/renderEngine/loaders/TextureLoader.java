/*
 *	Stickers Twisty Puzzle Simulator and Solver
 *	Copyright (C) 2022 Sam Peterson <sam.peterson1@icloud.com>
 *	
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.sampeterson1.renderEngine.loaders;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.github.sampeterson1.renderEngine.models.Texture;

import de.matthiasmann.twl.utils.PNGDecoder;

public class TextureLoader {
	
	private static final String TEXTURE_PATH = "textures/";
	
	public static Texture loadTexture(String fileName) throws IOException {
		System.out.println(TEXTURE_PATH + fileName);
	    PNGDecoder decoder = new PNGDecoder(ResourceLoader.getResourceStream(TEXTURE_PATH + fileName));
	    
	    ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());

	    decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
	    buffer.flip();

	    int id = GL11.glGenTextures();

	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

	    GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	    GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

	    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	    GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

	    return new Texture(id, decoder.getWidth(), decoder.getHeight()); 
	}
	
}
