package com.harrison.spectracer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class Polygon implements Drawable{
	public Vector position;

	public Vector velocity;
	
	public double rotation;

	public double rotVel;
	
	public Color color;
	
	public boolean delete;
	
	protected List<Vector> vertices; 
	
	protected double left;
	protected double right;
	protected double bottom;
	protected double top;
	
	public Polygon(List<Vector> vertices, Vector position, Color color){
		this.vertices = vertices;
		this.position = position;
		this.color = color;
		velocity = new Vector(0,0);
		rotation = 0;
		rotVel = 0;
		delete = false;
	}

	public Polygon(){
		vertices =  new ArrayList<Vector>();;
		position = new Vector(0,0);
		color = new Color(0,0,0);
		velocity = new Vector(0,0);
		rotation = 0;
		rotVel = 0;
		delete = false;
	}
	
	public void addVertex(Vector vertex){
		vertices.add(vertex);
	}

	public void update(double dt){
		position.x += dt*velocity.x;
		position.y += dt*velocity.y;
		rotation += dt*rotVel;
	}
	
	public void draw(GL10 gl){
		int n = vertices.size();
		float [] vertexArray = new float[3*n];
		for(int i = 0; i < vertices.size(); i++){
			vertexArray[3*i] = (float) (vertices.get(i).x);
			vertexArray[3*i+1] = (float) (vertices.get(i).y);
			vertexArray[3*i+2] = 0.0f;
		}

		short [] indices;
		indices = new short[n];
		for(short i = 0; i < n; i++){
			indices[i] = i;
		}

		ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		FloatBuffer vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertexArray);
		vertexBuffer.position(0);

		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		ShortBuffer indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);

		// Counter-clockwise winding.
		gl.glFrontFace(GL10.GL_CCW);
		// Enable face culling.
//		gl.glEnable(GL10.GL_CULL_FACE);
		// What faces to remove with the face culling.
//		gl.glCullFace(GL10.GL_BACK);

		// Enabled the vertices buffer for writing and to be used during rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glColor4f((float)(color.r/255.0), (float)(color.g/255.0), (float)(color.b/255.0), 1.0f);

		gl.glPushMatrix();
		gl.glTranslatef((float)position.x, (float)position.y, 0);
		gl.glRotatef((float)rotation, 0, 0, 1);
		
		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0,
	                             vertexBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLE_FAN, indices.length,
				  GL10.GL_UNSIGNED_SHORT, indexBuffer);

		gl.glPopMatrix();
		
		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); 
		// Disable face culling.
		gl.glDisable(GL10.GL_CULL_FACE);
	}

	public boolean contains(Vector v){
		for(int i = 0; i < vertices.size(); i++){
			double x1 = vertices.get(i).x+position.x;
			double y1 = vertices.get(i).y+position.y;
			double x2 = vertices.get((i+1)%vertices.size()).x+position.x;
			double y2 = vertices.get((i+1)%vertices.size()).y+position.y;

			double dx = x2-x1;
			double dy = y2-y1;
			double m = 1000000.0;
			if(dx != 0.0){
				m = dy/dx;
			}
			double b = y1-m*x1;
//			Log.w("dx",Double.toString(dx));
//			Log.w("dy",Double.toString(dy));
//			Log.w("m",Double.toString(m));
//			Log.w("b",Double.toString(b));
//			Log.w("v.x",Double.toString(v.x));
//			Log.w("v.y",Double.toString(v.y));
			if(dx>0 == v.y<m*v.x+b){
				return false;
			}
		}
		return true;
	}

	public boolean intersects(Polygon p){
		for(Vector v : p.vertices){
			if(contains(v)){
				return true;
			}
		}
		for(Vector v : vertices){
			if(p.contains(v)){
				return true;
			}
		}
		
		return false;
	}
}
