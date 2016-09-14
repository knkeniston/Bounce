package bounce;

import java.util.Iterator;

import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


/**
 * This state is active when the Game is being played. In this state, sound is
 * turned on, the bounce counter begins at 0 and increases until 10 at which
 * point a transition to the Game Over state is initiated. The user can also
 * control the ball using the WAS & D keys.
 * 
 * Transitions From StartUpState
 * 
 * Transitions To GameOverState
 */
class PlayingState extends BasicGameState {
	int bounces;
	int lives;
	int timeX;
	int timeY;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		bounces = 0;
		lives = 3;
		timeX = 0;
		timeY = 0;
		container.setSoundOn(true);
	}
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		BounceGame bg = (BounceGame)game;
		
		bg.ball.render(g);
		bg.paddle.render(g);
		
		g.drawString("Bounces: " + bounces, 10, 30);
		g.drawString("Lives: " + lives, 10, 50);
		
		for (Bang b : bg.explosions)
			b.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		BounceGame bg = (BounceGame)game;
		
		if (input.isKeyDown(Input.KEY_A)) {
			bg.paddle.setVelocity(new Vector(-.3f, 0));
		}
		else if (input.isKeyDown(Input.KEY_D)) {
			bg.paddle.setVelocity(new Vector(.3f, 0f));
		}
		else {
			bg.paddle.setVelocity(new Vector(0f, 0f));
		}
		
		// bounce the ball...
		boolean bounced = false;		
		if ((bg.ball.getCoarseGrainedMaxX() > bg.ScreenWidth
				|| bg.ball.getCoarseGrainedMinX() < 0) && timeX <= 0) {
			timeX = 10;
			bg.ball.bounce(90);
			bounced = true;
		} else if (bg.ball.getCoarseGrainedMinY() < 0 && timeY <= 0) {
			timeY = 10;
			bg.ball.bounce(0);
			bounced = true;
		}
		if (bg.ball.getCoarseGrainedMaxY() > bg.ScreenHeight) {
			this.lives -= 1;
			bg.ball.setPosition(bg.ScreenWidth / 2, bg.ScreenHeight / 2);
			bg.ball.setLifeWait();
		}
		timeX--;
		timeY--;
		
		if (bounced) {
			bg.explosions.add(new Bang(bg.ball.getX(), bg.ball.getY()));
			bounces++;
		}
		
		bg.ball.update(delta);
		
		bg.paddle.update(delta);
		
		// check if there are any finished explosions, if so remove them
		for (Iterator<Bang> i = bg.explosions.iterator(); i.hasNext();) {
			if (!i.next().isActive()) {
				i.remove();
			}
		}

		if (lives <= 0) {
			((GameOverState)game.getState(BounceGame.GAMEOVERSTATE)).setUserScore(bounces);
			game.enterState(BounceGame.GAMEOVERSTATE);
		}
		
	}

	@Override
	public int getID() {
		return BounceGame.PLAYINGSTATE;
	}
	
}