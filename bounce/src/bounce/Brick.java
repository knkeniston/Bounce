package bounce;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * The Brick class is an object that is stationary in the game. When
 * the ball hits a brick, depending on the brick's level, it may break
 * and will bounce the ball back downwards.
 * 
 */
 class Brick extends Entity {

	public Brick(final float x, final float y) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager
				.getImage(BounceGame.BALL_BALLIMG_RSC));
	}

	/**
	 * Update the Ball based on how much time has passed...
	 * 
	 * @param delta
	 *            the number of milliseconds since the last update
	 */
	public void update(final int delta) {

	}
}
