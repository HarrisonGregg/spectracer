package com.harrison.spectracer;

public class Rect extends Polygon {
	public Vector size;
	
	public Rect(Vector center, Vector size, Color color) {
		this.position = center;
		this.color = color;
		
		vertices.add(new Vector(-size.x/2,-size.y/2));
		vertices.add(new Vector(size.x/2,-size.y/2));
		vertices.add(new Vector(size.x/2,size.y/2));
		vertices.add(new Vector(-size.x/2,size.y/2));
	}

	public void setSize(Vector size){
		this.size = size;
		vertices.clear();

		vertices.add(new Vector(-size.x/2,-size.y/2));
		vertices.add(new Vector(size.x/2,-size.y/2));
		vertices.add(new Vector(size.x/2,size.y/2));
		vertices.add(new Vector(-size.x/2,size.y/2));
	}
	
	public boolean contains(Vector point){
		double left = position.x - size.x/2;
		double right = position.x + size.x/2;
		double bottom = position.y - size.y/2;
		double top = position.y + size.y/2;
		if(point.x > left && point.x < right && point.y > bottom && point.x < top){
			return true;
		}
		return false;
	}
}
