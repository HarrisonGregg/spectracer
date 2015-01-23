package com.harrison.spectracer;

public class Obstacle extends Polygon {
	public Obstacle(int side, double gameTime, double y){
		if(side == -1){
			vertices.add(new Vector(0,-0.12));
			vertices.add(new Vector(0.8,-0.1));
			vertices.add(new Vector(0.85,0.09));
			vertices.add(new Vector(0,0.1));
		} else if (side == 1) {
//			vertices.add(new Vector(0.05,-0.1));
//			vertices.add(new Vector(0.8,-0.1));
//			vertices.add(new Vector(.85,0.12));
//			vertices.add(new Vector(0,0.1));
			vertices.add(new Vector(-0.8,-0.1));
			vertices.add(new Vector(0,-0.12));
			vertices.add(new Vector(-0.01,0.09));
			vertices.add(new Vector(-0.85,0.1));
		}
		
		color = Color.RED();
		position.x = side;
		position.y = y;
		velocity.y = -1.5*((30000.0+gameTime)/30000.0);
	}

	public void update(double dt){
		super.update(dt);
		if(position.y < -1){
			delete = true;
		}
	}
}
