package com.harrison.spectracer;

public class Vector {
	public double x;
	public double y;
	
	public Vector(double d, double e){
		this.x = d;
		this.y = e;
	}
	
	public String toString(){
		String s = x + ", " + y;
		return s;		
	}
}
