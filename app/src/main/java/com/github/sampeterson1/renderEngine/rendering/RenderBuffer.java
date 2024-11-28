package com.github.sampeterson1.renderEngine.rendering;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import com.github.sampeterson1.renderEngine.models.Texture;
import com.github.sampeterson1.renderEngine.window.Window;

public class RenderBuffer {
	
	private static final int NUM_SAMPLES = 8;
	
	private int width;
	private int height;

	private int fboID;
	private int colorBufferID;
	private int depthBufferID;
	
	private Texture texture;
	
	private boolean multisample;
	
	public RenderBuffer(int width, int height) {
		this(width, height, false);
	}
	
	public RenderBuffer(int width, int height, boolean multisample) {
		this.width = width;
		this.height = height;
		this.fboID = createFrameBuffer();
		GL13.glEnable(GL13.GL_MULTISAMPLE);
		this.multisample = multisample;
		if(multisample) {
			this.colorBufferID = createMultisampleColorAttatchment();
		} else {
			this.texture = createTextureAttachment(width, height);
		}
		this.depthBufferID = createDepthBufferAttachment(width, height);
		unbind();
	}
	
	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public void renderTo(RenderBuffer buffer) {
		unbind();
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, buffer.fboID);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fboID);
		GL11.glDrawBuffer(GL11.GL_BACK);
		GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, buffer.width, buffer.height, 
				GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
		unbind();
	}
	
	public void render(int x1, int y1, int x2, int y2) {
		unbind();
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fboID);
		GL11.glDrawBuffer(GL11.GL_BACK);
		GL30.glBlitFramebuffer(0, 0, width, height, x1, y1, x2, y2, 
				GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
		unbind();
	}
	
	public Texture getTexture() {
		return this.texture;
	}
	
	public void cleanUp() {
		GL30.glDeleteFramebuffers(fboID);
		if(texture != null) GL11.glDeleteTextures(texture.getID());
		GL30.glDeleteRenderbuffers(depthBufferID);
		GL30.glDeleteRenderbuffers(colorBufferID);
	}
	
	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboID);
		GL11.glViewport(0, 0, width, height);
	}
	
	public void unbind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Window.getWidth(), Window.getHeight());
	}

	private int createFrameBuffer() {
		int frameBuffer = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);

		return frameBuffer;
	}

	private Texture createTextureAttachment( int width, int height) {
		int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height,
				0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
				textureID, 0);
		return new Texture(textureID, width, height);
	}
	
	private int createMultisampleColorAttatchment() {
		int colorBufferID = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, colorBufferID);
		GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, NUM_SAMPLES, GL11.GL_RGBA8, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_RENDERBUFFER, colorBufferID);
	
		return colorBufferID;
	}

	private int createDepthBufferAttachment(int width, int height) {
		int depthBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		if(multisample) {
			GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, NUM_SAMPLES, GL14.GL_DEPTH_COMPONENT24, width,
					height);
		} else {
			GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width,
				height);
		}
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
				GL30.GL_RENDERBUFFER, depthBuffer);
		return depthBuffer;
	}
	
}
