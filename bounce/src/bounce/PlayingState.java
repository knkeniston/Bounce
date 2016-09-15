package bounce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jig.Collision;
import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;


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
	int timeLastCol;
	boolean addBricks;
	Map<Brick, Integer> timeBricks;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		lives = 3;
		bounces = 0;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		timeX = 0;
		timeY = 0;
		timeLastCol = 0;
		timeBricks = new HashMap<Brick, Integer>();
		container.setSoundOn(true);
		BounceGame bg = (BounceGame)game;
		bg.bricks.clear();
		
		if (bg.level == 1) {
			for (int i = 1; i <= 4; i++) {
				bg.bricks.add(new Brick(i * (bg.ScreenWidth / 5), 200, bg.level));
			}
			bg.ball.setVelocity(new Vector(.1f, .2f));
		}
		if (bg.level == 2) {
			for (int i = 1; i <= 5; i++) {
				bg.bricks.add(new Brick(i * (bg.ScreenWidth / 6), 100, bg.level));
			}
			for (int i = 1; i <= 4; i++) {
				bg.bricks.add(new Brick(i * (bg.ScreenWidth / 5), 250, bg.level));
			}
			bg.ball.setVelocity(new Vector(.2f, .3f));
		} else if (bg.level == 3) {
			for (int i = 1; i <= 5; i++) {
				bg.bricks.add(new Brick(i * (bg.ScreenWidth / 6), 50, bg.level));
			}
			for (int i = 1; i <= 4; i++) {
				bg.bricks.add(new Brick(i * (bg.ScreenWidth / 5), 200, bg.level));
			}
			for (int i = 1; i <= 3; i++) {
				bg.bricks.add(new Brick(i * (bg.ScreenWidth / 4), 350, bg.level));
			}
			bg.ball.setVelocity(new Vector(.3f, .4f));
		}
		bg.ball.setPosition(bg.ScreenWidth / 2, bg.ScreenHeight / 2);
	}
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		BounceGame bg = (BounceGame)game;
		
		bg.ball.render(g);
		bg.paddle.render(g);
		for (Brick b: bg.bricks) {
			b.render(g);
		}
		
		g.drawString("Lives: " + lives, 10, 50);
		g.drawString("Level: " + bg.level, 10, 30);
		
		for (Bang b : bg.explosions)
			b.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		BounceGame bg = (BounceGame)game;
		
		// Control user input
		if (input.isKeyDown(Input.KEY_A)) {
			bg.paddle.setVelocity(new Vector(-.3f, 0));
		}
		else if (input.isKeyDown(Input.KEY_D)) {
			bg.paddle.setVelocity(new Vector(.3f, 0f));
		} 
		else if (input.isKeyDown(Input.KEY_1)) {
			bg.level = 1;
			game.enterState(BounceGame.STARTUPSTATE, new EmptyTransition(), new HorizontalSplitTransition());
		} 
		else if (input.isKeyDown(Input.KEY_2)) {
			bg.level = 2;
			game.enterState(BounceGame.STARTUPSTATE, new EmptyTransition(), new HorizontalSplitTransition());
		} 
		else if (input.isKeyDown(Input.KEY_3)) {
			bg.level = 3;
			game.enterState(BounceGame.STARTUPSTATE, new EmptyTransition(), new HorizontalSplitTransition());
		} 
		else {
			bg.paddle.setVelocity(new Vector(0f, 0f));
		}
		
		// Collision between ball and paddle
		Collision paddleCol = bg.ball.collides(bg.paddle);
		if (paddleCol != null && timeLastCol <= 0) {
			float ballX = bg.ball.getX();
			float paddleX = bg.paddle.getX();
			float diffX = Math.abs(paddleX - ballX);
			float oldSpeed = bg.ball.getVelocity().length();
			float newX = oldSpeed * (diffX / 100);
			float newY = (float)Math.sqrt(Math.pow(oldSpeed, 2) - Math.pow(newX, 2));
			if (ballX < paddleX) {
				bg.ball.setVelocity(new Vector(-1 * newX, -1 * newY));
			} else {
				bg.ball.setVelocity(new Vector(newX, -1 * newY));
			}
			ResourceManager.getSound(BounceGame.HITPADDLE_RSC).play();
			timeLastCol = 10;
		}
		timeLastCol--;
		
		// Collision between ball and brick
		ArrayList<Brick> toRemove = new ArrayList<Brick>();
		for (Brick b : bg.bricks) {
			Collision brickCol = bg.ball.collides(b);
			boolean cont = true;
			if (timeBricks.containsKey(b) && timeBricks.get(b) > 0) {
				cont = false;
			}
			if (brickCol != null && cont) { 
				bg.ball.bounce(0);
				b.decHealth();
				timeBricks.put(b, 10);
				if (b.getHealth() == 0) {
					toRemove.add(b);
					bg.explosions.add(new Bang(b.getX(), b.getY()));
				} else {
					ResourceManager.getSound(BounceGame.HITBRICK_RSC).play();
				}
			}
		}
		for (Brick b : timeBricks.keySet()) {
			timeBricks.put(b, timeBricks.get(b) - 1);
		}
		for (Brick b : toRemove) {
			bg.bricks.remove(b);
		}
		
		// Collision between ball and wall
		boolean bounced = false;		
		if ((bg.ball.getCoarseGrainedMaxX() > bg.ScreenWidth
				|| bg.ball.getCoarseGrainedMinX() < 0) && timeX <= 0) {
			timeX = 10;
			ResourceManager.getSound(BounceGame.HITWALL_RSC).play();
			bg.ball.bounce(90);
			bounced = true;
		} else if (bg.ball.getCoarseGrainedMinY() < 0 && timeY <= 0) {
			timeY = 10;
			ResourceManager.getSound(BounceGame.HITWALL_RSC).play();
			bg.ball.bounce(0);
			bounced = true;
		}
		if (bg.ball.getCoarseGrainedMaxY() > bg.ScreenHeight) {
			ResourceManager.getSound(BounceGame.LOSELIFE_RSC).play();
			this.lives -= 1;
			bg.ball.setPosition(bg.ScreenWidth / 2, bg.ScreenHeight / 2);
			bg.ball.setLifeWait();
		}
		timeX--;
		timeY--;
		
		//Updating animations
		if (bounced) {
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
		
		// Change levels
		if (bg.bricks.size() == 0) {
			bg.level++;
			if (bg.level == 4) {
				game.enterState(BounceGame.GAMEOVERSTATE, new EmptyTransition(), new HorizontalSplitTransition());
			} else {
				game.enterState(BounceGame.STARTUPSTATE, new EmptyTransition(), new HorizontalSplitTransition());
			}
		}

		// Game over state if no lives left
		if (lives <= 0) {
			((GameOverState)game.getState(BounceGame.GAMEOVERSTATE)).setUserScore(bounces);
			bg.level = 1;
			lives = 3;
			game.enterState(BounceGame.GAMEOVERSTATE);
		}
		
	}

	@Override
	public int getID() {
		return BounceGame.PLAYINGSTATE;
	}
	
}