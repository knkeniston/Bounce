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
	int level;
	int health;

	public Brick(final float x, final float y, int level) {
		super(x, y);
		if (level == 1) {
			addImageWithBoundingBox(ResourceManager
					.getImage(BounceGame.BRICK_BRICK_1_IMG_RSC));
		} else if (level == 2) {
			addImageWithBoundingBox(ResourceManager
					.getImage(BounceGame.BRICK_BRICK_2_1_IMG_RSC));
		} else {
			addImageWithBoundingBox(ResourceManager
					.getImage(BounceGame.BRICK_BRICK_3_1_IMG_RSC));
		}
		this.level = level;
		this.health = level;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void decHealth() {
		this.health -= 1;
		updateImage();
	}
	
	private void updateImage() {
		if (level == 2 && health == 1) {
			removeImage(ResourceManager.getImage(BounceGame.BRICK_BRICK_2_1_IMG_RSC));
			addImageWithBoundingBox(ResourceManager
					.getImage(BounceGame.BRICK_BRICK_2_2_IMG_RSC));
		} else if (level == 3 && health == 2) {
			removeImage(ResourceManager.getImage(BounceGame.BRICK_BRICK_3_1_IMG_RSC));
			addImageWithBoundingBox(ResourceManager
					.getImage(BounceGame.BRICK_BRICK_3_2_IMG_RSC));
		} else if (level == 3 && health == 1) {
			removeImage(ResourceManager.getImage(BounceGame.BRICK_BRICK_3_2_IMG_RSC));
			addImageWithBoundingBox(ResourceManager
					.getImage(BounceGame.BRICK_BRICK_3_3_IMG_RSC));
		}
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
