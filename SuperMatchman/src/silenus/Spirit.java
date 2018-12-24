package silenus;

public abstract class Spirit extends GameObject implements Rigidbody2D {

	protected boolean onGround=false;

	protected Vector2D velocity=new Vector2D();
	public Vector2D getVelocity() { return velocity; }
	public void setVelocity(Vector2D value) { velocity=value; }
	
	public Spirit(Prefab prefab)
	{
		super(prefab);
	}
	
	public void update()
	{
		bounds.add(velocity);
	}

}
