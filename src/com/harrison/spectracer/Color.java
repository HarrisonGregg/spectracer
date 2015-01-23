package com.harrison.spectracer;

public class Color {
	public int r;
	public int g;
	public int b;
	
	public Color(int r, int g, int b){
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public static Color RED(){
		Color c = new Color(255,0,0);
		return c;
	}

	public static Color GREEN(){
		Color c = new Color(0,255,0);
		return c;
	}

	public static Color BLUE(){
		Color c = new Color(0,0,255);
		return c;
	}

	public static Color WHITE(){
		Color c = new Color(255,255,255);
		return c;
	}
}
