package com.harrison.spectracer;

import javax.microedition.khronos.opengles.GL10;

interface Drawable {
	public void update(double dt);

	public void draw(GL10 gl);
}
