package com.yoshiapolis.ui;

import com.yoshiapolis.cube.pieces.Mathf;

import processing.core.PApplet;

public class Sticker {
  
  private float rx, ry, rz;
  private float arx, ary, arz;
  private float x, y, z;
  private float size;
  
  private int c; 
  private PApplet app;
  
  public Sticker(PApplet app, float size, float x, float y, float z, float rx, float ry, float rz, int c) {
    this.app = app;
    this.x = x;
    this.y = y;
    this.z = z;
    this.rx = rx;
    this.ry = ry;
    this.rz = rz;
    this.c = c;
    this.size = size;
  }
  
  public void clearRotation() {
	  arx = 0;
	  ary = 0;
	  arz = 0;
  }
  
  public void setXRotation(float x, float rx) {
    if(Mathf.abs(this.x - x) <= size/2 + 0.1) arx = rx;
  }
  
  public void setYRotation(float y, float ry) {
    if(Mathf.abs(this.y - y) <= size/2 + 0.1) ary = ry;
  }
  
  public void setZRotation(float z, float rz) {
    if(Mathf.abs(this.z - z) <= size/2 + 0.1) arz = rz;
  }

  private void updateRotation(float cubeDrawSize) {
    rx = 0;
    ry = 0;
    rz = 0;
    if(Mathf.abs(Mathf.abs(this.x) - cubeDrawSize/2) < 0.1) {
      ry = Mathf.PI/2;
    } else if(Mathf.abs(Mathf.abs(this.y) - cubeDrawSize/2) < 0.1) {
      rx = Mathf.PI/2;
    }
  }
  
  public void applyRotationX(boolean cw, float x, float cubeDrawSize) {
    if(Mathf.abs(this.x - x) <= size/2 + 0.001) {
      if(cw) {
        float temp = y;
        y = -z;
        z = temp;
      } else {
        float temp = z;
        z = -y;
        y = temp;
      }
      arx = 0;
      updateRotation(cubeDrawSize);
    }
  }
  
  public void applyRotationY(boolean cw, float y, float cubeDrawSize) {
    if(Mathf.abs(this.y - y) <= size/2 + 0.001) {
      if(cw) {
        float temp = z;
        z = -x;
        x = temp;
      } else {
        float temp = x;
        x = -z;
        z = temp;
      }
      ary = 0;
      updateRotation(cubeDrawSize);
    }
  }
  
  public void applyRotationZ(boolean cw, float z, float cubeDrawSize) {
    if(Mathf.abs(this.z - z) <= size/2 + 0.001) {
      if(cw) {
        float temp = x;
        x = -y;
        y = temp;
      } else {
        float temp = y;
        y = -x;
        x = temp;
      }
      arz = 0;
      updateRotation(cubeDrawSize);
    }
  }
  
  public void drawSticker() {
    app.pushMatrix();
    app.fill(c);
    app.rotateX(arx);
    app.rotateY(ary);
    app.rotateZ(arz);
    app.translate(x, y, z);
    app.rotateX(rx);
    app.rotateY(ry);
    app.rotateZ(rz);
    app.rect(-size/2, -size/2, size, size);
    app.popMatrix();
  }
}
