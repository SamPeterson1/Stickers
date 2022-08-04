package com.github.yoshiapolis.renderEngine.models;

public class Texture {

	private int id;
	private int width;
	private int height;

    public Texture(int id, int width, int height){
        this.id = id;
        this.width = width;
        this.height = height;
    }
    
    public int getWidth() {
    	return this.width;
    }
    
    public int getHeight() {
    	return this.height;
    }

    public int getID(){
        return id;
    }
	
}
