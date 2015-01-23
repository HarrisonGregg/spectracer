package com.harrison.spectracer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

public class MyGLRenderer implements Renderer {
	public Game b;
	int width = 1;
	int height = 1;
	
	public MyGLRenderer(Game b){
		super();
		
		this.b = b;
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);  		// Set the background color to black ( rgba ).
		gl.glShadeModel(GL10.GL_SMOOTH);		// Enable Smooth Shading, default not really needed.
		gl.glClearDepthf(1.0f);		// Depth buffer setup.
		gl.glEnable(GL10.GL_DEPTH_TEST);		// Enables depth testing.
		gl.glDepthFunc(GL10.GL_LEQUAL);		// The type of depth testing to do.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);	// Really nice perspective calculations.
	}

	public void onDrawFrame(GL10 gl) {		
		b.draw(gl, width, height);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.width = width;
		this.height = height;
		float aspectRatio = (float)height/(float)width;
		
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
//		gl.glOrthof(0, width, 0, height, -1, 1);
		gl.glOrthof(-1, 1, 0, 2*aspectRatio, -1, 1);
		b.resize(width, height);
	}
}