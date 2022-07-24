package com.yoshiapolis.ui;

import java.util.ArrayList;

import com.yoshiapolis.cube.pieces.CubeMoveUtil;
import com.yoshiapolis.cube.pieces.Mathf;
import com.yoshiapolis.puzzle.Face;
import com.yoshiapolis.puzzle.Move;

import processing.core.PApplet;

public class CubeVisualizer {
  
  private float drawSize = 450;
  
  private ArrayList<Sticker> stickers;
  private int size;
  
  private PApplet app;
  
  private long lastTime;
  
  private float animationSpeed = Mathf.PI;
  
  private float rotX = 0;
  private float rotY = 0;
  
  private float currentRotation = 0;
  private boolean animate = true;
  private Move animatingMove;
  
  public CubeVisualizer(PApplet app, int size) {
    this.stickers = new ArrayList<Sticker>();
    this.size = size;
    this.app = app;
    this.lastTime = System.currentTimeMillis();
    
    if(size % 2 == 1) {
      for(int x = -size/2; x <= size/2; x ++) {
        for(int y = -size/2; y <= size/2; y ++) {
          float x2 = x*(float)drawSize/size;
          float y2 = y*(float)drawSize/size;
          float z2 = drawSize/2.0f;
          float sz = (float)drawSize/size;
          
          stickers.add(new Sticker(app, sz, x2, y2, z2, 0, 0, 0, app.color(0, 255, 0))); 
          stickers.add(new Sticker(app, sz, x2, y2, -z2, 0, 0, 0, app.color(0, 0, 255)));
          stickers.add(new Sticker(app, sz, x2, z2, y2, Mathf.PI/2, 0, 0, app.color(255, 255, 0))); 
          stickers.add(new Sticker(app, sz, x2, -z2, y2, Mathf.PI/2, 0, 0, app.color(255, 255, 255)));
          stickers.add(new Sticker(app, sz, z2, x2, y2, 0, Mathf.PI/2, 0, app.color(255, 0, 0))); 
          stickers.add(new Sticker(app, sz, -z2, x2, y2, 0, Mathf.PI/2, 0, app.color(255, 125, 0)));
        }
      }
    } else {
      for(int x = -size/2; x < size/2; x ++) {
        for(int y = -size/2; y < size/2; y ++) {
          float x2 = (x+0.5f)*(float)drawSize/size;
          float y2 = (y+0.5f)*(float)drawSize/size;
          float z2 = drawSize/2.0f;
          float sz = (float)drawSize/size;
          
          stickers.add(new Sticker(app, sz, x2, y2, z2, 0, 0, 0, app.color(0, 255, 0))); 
          stickers.add(new Sticker(app, sz, x2, y2, -z2, 0, 0, 0, app.color(0, 0, 255)));
          stickers.add(new Sticker(app, sz, x2, z2, y2, Mathf.PI/2, 0, 0, app.color(255, 255, 0))); 
          stickers.add(new Sticker(app, sz, x2, -z2, y2, Mathf.PI/2, 0, 0, app.color(255, 255, 255)));
          stickers.add(new Sticker(app, sz, z2, x2, y2, 0, Mathf.PI/2, 0, app.color(255, 0, 0))); 
          stickers.add(new Sticker(app, sz, -z2, x2, y2, 0, Mathf.PI/2, 0, app.color(255, 125, 0)));
        }
      }
    }
  }
  
  private float calculateLayerCoordinate(int layer) {
      return (float)drawSize*layer/size - drawSize/2.0f + drawSize/(2.0f*size);
  }
  
  private void applyMoveToStickers(Move move) {
	  Face face = move.getFace();
	  float layerCoord = calculateLayerCoordinate(move.getLayer());
	  boolean cw = move.isCW();
	  
	  for(Sticker s : stickers) {
		  s.clearRotation();
		  if(face == Face.R) {
			  s.applyRotationX(cw, -layerCoord, drawSize);
		  } else if(face == Face.U) {
			  s.applyRotationY(!cw, layerCoord, drawSize);
		  } else if(face == Face.F) {
			  s.applyRotationZ(cw, -layerCoord, drawSize);
		  }
	  }	  
  }
  
  public boolean isAnimating() {
	  return (animatingMove != null);
  }
  
  public void setAnimationSpeed(float speed) {
	  this.animationSpeed = speed;
  }
  
  public void setAnimate(boolean animate) {
	  this.animate = animate;
  }
  
  public void makeMove(Move move) {
	  if(animate) {
		  this.animatingMove = CubeMoveUtil.normalize(move, size);
	  } else {
		  applyMoveToStickers(CubeMoveUtil.normalize(move, size));
	  }
  }
  
  public void setRotX(float x) {
	  this.rotX = x;
  }
  
  public void setRotY(float y) {
	  this.rotY = y;
  }
  
  public int getSize() {
	  return this.size;
  }
  
  private void updateAnimation(float deltaTime) {
	  if(animatingMove != null) {
		  Face face = animatingMove.getFace();
		  float layerCoord = calculateLayerCoordinate(animatingMove.getLayer());
		  boolean cw = animatingMove.isCW();
	  		
		  currentRotation += (cw ? 1 : -1) * deltaTime * animationSpeed;
	  	
		  for(Sticker s : stickers) {
			  if(face == Face.R) {
				  s.setXRotation(-layerCoord, currentRotation);
			  } else if(face == Face.U) {
				  s.setYRotation(layerCoord, -currentRotation);
			  } else if(face == Face.F) {
				  s.setZRotation(-layerCoord, currentRotation);
			  }
		  }
	  	
		  if(Mathf.abs(currentRotation) >= Mathf.PI/2) {
			  applyMoveToStickers(animatingMove);
			  animatingMove = null;
			  currentRotation = 0;
		  }
	  }
  }
  
  private void drawLayerBlockers() {
	  if(animatingMove != null) {
		  app.pushMatrix();
	      Face face = animatingMove.getFace();
	      int layer = animatingMove.getLayer();
	      float layerCoord = calculateLayerCoordinate(layer);
	      float rotation = 0;
	      app.fill(0, 0, 0);
	      
	      if(face == Face.R) {
	    	  app.translate(-layerCoord-(float)drawSize/(2.0f*size), 0, 0);
	    	  app.rotateY(Mathf.PI/2);
	    	  rotation = currentRotation;
	      } else if(face == Face.U) {
	    	  app.translate(0, layerCoord+(float)drawSize/(2.0f*size), 0);
	    	  app.rotateX(Mathf.PI/2);
	    	  rotation = currentRotation;
	      } else if(face == Face.F) {
	    	  app.translate(0, 0, -layerCoord-(float)drawSize/(2.0f*size));
	    	  rotation = currentRotation;
	      }
	      
	      if(layer != size-1) app.rect(-drawSize/2, -drawSize/2, drawSize, drawSize);
	      app.translate(0, 0, (float)drawSize/size);
	      if(layer != 0) app.rect(-drawSize/2, -drawSize/2, drawSize, drawSize);
	      app.rotateZ(rotation);
	      if(layer != 0) app.rect(-drawSize/2, -drawSize/2, drawSize, drawSize);
	      app.translate(0, 0, -(float)drawSize/size);
	      if(layer != size-1) app.rect(-drawSize/2, -drawSize/2, drawSize, drawSize);
	      
	      app.popMatrix();
	  }
  }
  
  public void drawCube() {
	  float deltaTime = (System.currentTimeMillis() - lastTime)/1000.0f;
	  lastTime = System.currentTimeMillis();
	  
	  app.background(0);
	  app.lights();
	    
	  app.translate(app.width/2, app.height/2);
	  app.rotateX(rotX);
	  app.rotateY(rotY);
    
	  updateAnimation(deltaTime);
	  drawLayerBlockers();
	  
	  for(Sticker s : stickers) {
		  s.drawSticker();
	  }
  }
}