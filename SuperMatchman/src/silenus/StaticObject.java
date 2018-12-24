package silenus;

public class StaticObject extends GameObject implements Collider2D {

	public StaticObject(Prefab prefab) {
		super(prefab);

	}

	@Override
	public Rect2D getCollider() {
		return getBounds();
	}

}
