package com.harrison.spectracer;

import java.util.Random;

public class Colorf {
	public float r;
	public float g;
	public float b;
	
	public Colorf(float r, float g, float b){
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Color intColor(){
		return  new Color(
				Math.max(Math.min((int)(r*255),255),0),
				Math.max(Math.min((int)(g*255),255),0),
				Math.max(Math.min((int)(b*255),255),0));
	}

	public static Colorf random(Random r){
		Colorf c = new Colorf(r.nextFloat(), r.nextFloat(), r.nextFloat());
		float total = c.r + c.g + c.b;
		// 
		if(total < 1){
			switch(r.nextInt()%3){
			case 0:
				c.r = 1;
				break;
			case 1:
				c.g = 1;
				break;
			case 2:
				c.b = 1;
				break;
			}
		}
		return c;
	}

	public static Colorf RED(){
		Colorf c = new Colorf(1,0,0);
		return c;
	}

	public static Colorf GREEN(){
		Colorf c = new Colorf(0,1,0);
		return c;
	}
	
	public static Colorf BLUE(){
		Colorf c = new Colorf(0,0,1);
		return c;
	}

	public static Colorf YELLOW(){
		Colorf c = new Colorf(1,1,0);
		return c;
	}

	public static Colorf PURPLE(){
		Colorf c = new Colorf(1,0,1);
		return c;
	}

	public static Colorf ORANGE(){
		Colorf c = new Colorf(1,(float)0.5,0);
		return c;
	}

	public static Colorf WHITE(){
		Colorf c = new Colorf(1,1,1);
		return c;
	}

	public static Colorf GRAY(){
		Colorf c = new Colorf((float).5,(float).5,(float).5);
		return c;
	}
}