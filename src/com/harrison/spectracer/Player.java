package com.harrison.spectracer;

public class Player extends Polygon{
	public int side;
	
	public Player(Vector position, Color color) {
		this.position = position;
		this.color = color;
		
		side = -1;
		
		vertices.add(new Vector(-.173,-0.1));
		vertices.add(new Vector(.173,-0.1));
		vertices.add(new Vector(0.0,.2));
	}

	public void update(double dt){
		velocity.x = 8*side;
		if(rotVel < 0){
			rotVel = (float) Math.min(rotVel + 100*dt, -300);
		}else if(rotVel > 0){
			rotVel = (float) Math.max(rotVel - 100*dt, 300);
		}
		
		super.update(dt);		

		if(position.x < -.88){
			position.x = -.88;
		} else if(position.x > .88){
			position.x = .88;
		}

		position.y = 1.5-position.x*position.x/7;
	}
}
