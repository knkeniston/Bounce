package bounce;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * The paddle is an entity with a velocity that is determined whether the player
 * presses the A key to move left, the D key to move right, or standing still when
 * neither is pressed. When a ball hits the paddle, it bounces back towards the top.
 * 
 */
 class Paddle extends Entity {

	private Vector velocity;

	public Paddle(final float x, final float y) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager
				.getImage(BounceGame.PADDLE_PADDLEIMG_RSC));
		velocity = new Vector(0, 0);
	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}

	/**
	 * Update the Paddle
	 * 
	 * @param delta
	 *            the number of milliseconds since the last update
	 */
	public void update(final int delta) {
		translate(velocity.scale(delta));
	}
}
