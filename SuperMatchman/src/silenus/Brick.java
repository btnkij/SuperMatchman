package silenus;

public class Brick extends StaticObject implements Collider2D
{

	public Brick() {
		super(Prefab.BRICK);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getTag()
	{
		return GameObject.TAG_BRICK;
	}
}
