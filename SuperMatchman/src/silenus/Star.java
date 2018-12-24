package silenus;

public class Star extends StaticObject {

	public Star() {
		super(Prefab.STAR);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getTag()
	{
		return GameObject.TAG_STAR;
	}
}
