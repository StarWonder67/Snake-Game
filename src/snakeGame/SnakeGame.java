package snakeGame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JFrame;


import java.awt.BorderLayout;


public class SnakeGame extends JFrame{
	
	//Variables
	/**
	 * The number of milliseconds that should pass between each frame.
	 */
	private static final long FRAME_TIME = 1000L / 50L;
	
	/**
	 * The minimum length of the snake. This allows the snake to grow
	 * right when the game starts, so that we're not just a head moving
	 * around on the board.
	 */
	private static final int MIN_SNAKE_LENGTH = 5;
	
	/**
	 * The Clock instance for handling the game logic.
	 */
	private Clock logicTimer;
	private Random random;
	
	private BoardPanel board;
	private SidePanel side;
	public int totalScore;
	public int fruitScore;
	public int fruitsEaten;
	public boolean isGameOver;
	public boolean isPaused;
	public boolean isNewGame;
	private static final int MAX_DIRECTIONS = 3;
	private static int incScore;
	private LinkedList<Point> snake;
	private LinkedList<Direction> directions;
	
	
	//Constructor
	public SnakeGame() {	
	super("Snake Game");
	
	setLayout(new BorderLayout());
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setResizable(false);
	
	//this->represents the object on which SnakeGame is called in the main function
	//this object is game
	this.board = new BoardPanel(this);
	this.side = new SidePanel(this);
	
	
	//adding board and side panel components to frame
	add(board, BorderLayout.CENTER);
	add(side, BorderLayout.EAST);
	
	//adding a new key listener to frame to process input
	
	addKeyListener(new KeyAdapter() {
		
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {

			/*
			 * If the game is not paused, and the game is not over...
			 * 
			 * Ensure that the direction list is not full, and that the most
			 * recent direction is adjacent to North before adding the
			 * direction to the list.
			 */
			case KeyEvent.VK_W:
			case KeyEvent.VK_UP:
				if(!isPaused && !isGameOver) {
					if(directions.size() < MAX_DIRECTIONS) {
						Direction last = directions.peekLast();
						if(last != Direction.South && last != Direction.North) {
							directions.addLast(Direction.North);
						}
					}
				}
				break;

			/*
			 * If the game is not paused, and the game is not over...
			 * 
			 * Ensure that the direction list is not full, and that the most
			 * recent direction is adjacent to South before adding the
			 * direction to the list.
			 */	
			case KeyEvent.VK_S:
			case KeyEvent.VK_DOWN:
				if(!isPaused && !isGameOver) {
					if(directions.size() < MAX_DIRECTIONS) {
						Direction last = directions.peekLast();
						if(last != Direction.North && last != Direction.South) {
							directions.addLast(Direction.South);
						}
					}
				}
				break;
			
			/*
			 * If the game is not paused, and the game is not over...
			 * 
			 * Ensure that the direction list is not full, and that the most
			 * recent direction is adjacent to West before adding the
			 * direction to the list.
			 */						
			case KeyEvent.VK_A:
			case KeyEvent.VK_LEFT:
				if(!isPaused && !isGameOver) {
					if(directions.size() < MAX_DIRECTIONS) {
						Direction last = directions.peekLast();
						if(last != Direction.East && last != Direction.West) {
							directions.addLast(Direction.West);
						}
					}
				}
				break;
		
			/*
			 * If the game is not paused, and the game is not over...
			 * 
			 * Ensure that the direction list is not full, and that the most
			 * recent direction is adjacent to East before adding the
			 * direction to the list.
			 */		
			case KeyEvent.VK_D:
			case KeyEvent.VK_RIGHT:
				if(!isPaused && !isGameOver) {
					if(directions.size() < MAX_DIRECTIONS) {
						Direction last = directions.peekLast();
						if(last != Direction.West && last != Direction.East) {
							directions.addLast(Direction.East);
						}
					}
				}
				break;
			
			/*
			 * If the game is not over, toggle the paused flag and update
			 * the logicTimer's pause flag accordingly.
			 */
			case KeyEvent.VK_P:
				if(!isGameOver) {
					isPaused = !isPaused;
					logicTimer.setPaused(isPaused);
				}
				break;
			
			/*
			 * Reset the game if one is not currently in progress.
			 */
			case KeyEvent.VK_ENTER:
				if(isNewGame || isGameOver) {
					resetGame();
				}
				break;
			}
		}
		
	});
	
	
	/*
	 * Resize the window to the appropriate size, center it on the
	 * screen and display it.
	 */
	pack();
	setLocationRelativeTo(null);
	setVisible(true);
	
	}
	

	//Methods
	public void startGame(){
		this.totalScore = 0;
		this.fruitsEaten = 0;
		this.fruitScore = 100;
		this.isNewGame = true;
		this.snake = new LinkedList<>();
		this.logicTimer = new Clock(9.0f);
		this.random = new Random();
		this.directions = new LinkedList<>();
		this.incScore = 0;
		//Set the timer to paused initially.
		logicTimer.setPaused(true);
		
		
		while(true) {
			
			long start = System.nanoTime();
			
			//update the logic timer
			logicTimer.update();
			
			if(logicTimer.hasElapsedCycle()) {
				updateGame();
			}
			
			//Repaint the board and side panel with the new content.
			board.repaint();
			side.repaint();
			
			/*
			 * Calculate the delta time between since the start of the frame
			 * and sleep for the excess time to cap the frame rate. While not
			 * incredibly accurate, it is sufficient for our purposes.
			 */
			long delta = (System.nanoTime() - start) / 1000000L;
			if(delta < FRAME_TIME) {
				try {
					Thread.sleep(FRAME_TIME - delta);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}

		}
		
	}
	
	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}
	
	public void setFruitsEaten(int fruitsEaten) {
		this.fruitsEaten = fruitsEaten;
	}
	
	public void setFruitScore(int fruitScore) {
		this.fruitScore = fruitScore;
	}
	
	public int getTotalScore() {
		return totalScore;
	}
	
	public int getFruitsEaten() {
		return fruitsEaten;
	}
	
	public int getFruitScore() {
		return fruitScore;
	}
	
	/**
	 * Gets the flag that indicates whether or not we're playing a new game.
	 * @return The new game flag.
	 */
	public boolean isNewGame() {
		return isNewGame;
	}
	
	/**
	 * Gets the flag that indicates whether or not the game is over.
	 * @return The game over flag.
	 */
	public boolean isGameOver() {
		return isGameOver;
	}
	
	/**
	 * Gets the flag that indicates whether or not the game is paused.
	 * @return The paused flag.
	 */
	public boolean isPaused() {
		return isPaused;
	}
	
	public void resetGame() {
		/*
		 * Reset the score statistics. (Note that nextFruitPoints is reset in
		 * the spawnFruit function later on).
		 */
		this.totalScore = 0;
		this.fruitsEaten = 0;
		this.incScore = 0;
		/*
		 * Reset both the new game and game over flags.
		 */
		this.isNewGame = false;
		this.isGameOver = false;
		
		/*
		 * Create the head at the center of the board.
		 */
		Point head = new Point(BoardPanel.COL_COUNT / 2, BoardPanel.ROW_COUNT / 2);

		/*
		 * Clear the snake list and add the head.
		 */
		snake.clear();
		snake.add(head);
		
		/*
		 * Clear the board and add the head.
		 */
		board.clearBoard();
		board.setTile(head, TileType.SNAKEHEAD);
		
		/*
		 * Clear the directions and add north as the
		 * default direction.
		 */
		directions.clear();
		directions.add(Direction.North);
		
		/*
		 * Reset the logic timer.
		 */
		logicTimer.reset();
		
		/*
		 * Spawn a new fruit.
		 */
		spawnFruit();
	}
	public void updateGame() {
		
		TileType collision = updateSnake();
		
		if(collision==TileType.FRUIT) {
			fruitsEaten++;
			totalScore += fruitScore;
			spawnFruit();
		}
		else if(collision==TileType.SNAKEBODY) {
			isGameOver = true;
			logicTimer.setPaused(true);
		}
		else if(fruitScore>10) {
			fruitScore--;
		}
		
	}
	
	public TileType updateSnake() {
		
		/*
		 * Here we peek at the next direction rather than polling it. While
		 * not game breaking, polling the direction here causes a small bug
		 * where the snake's direction will change after a game over (though
		 * it will not move).
		 */
		Direction direction = directions.peekFirst();
				
		/*
		 * Here we calculate the new point that the snake's head will be at
		 * after the update.
		 */		
		Point head = new Point(snake.peekFirst());
		switch(direction) {
		case North:
			head.y--;
			break;
			
		case South:
			head.y++;
			break;
			
		case West:
			head.x--;
			break;
			
		case East:
			head.x++;
			break;
		}
		
		/*
		 * If the snake has moved out of bounds ('hit' a wall), we can just
		 * return that it's collided with itself, as both cases are handled
		 * identically.
		 */
		if(head.x < 0 || head.x >= BoardPanel.COL_COUNT || head.y < 0 || head.y >= BoardPanel.ROW_COUNT) {
			return TileType.SNAKEBODY; //Pretend we collided with our body.
		}
		
		/*
		 * Here we get the tile that was located at the new head position and
		 * remove the tail from of the snake and the board if the snake is
		 * long enough, and the tile it moved onto is not a fruit.
		 * 
		 * If the tail was removed, we need to retrieve the old tile again
		 * incase the tile we hit was the tail piece that was just removed
		 * to prevent a false game over.
		 */
		TileType old = board.getTile(head.x, head.y);
		if(old != TileType.FRUIT && snake.size() > MIN_SNAKE_LENGTH) {
			Point tail = snake.removeLast();
			board.setTile(tail, TileType.EMPTY);
			old = board.getTile(head.x, head.y);
		}
		
		/*
		 * Update the snake's position on the board if we didn't collide with
		 * our tail:
		 * 
		 * 1. Set the old head position to a body tile.
		 * 2. Add the new head to the snake.
		 * 3. Set the new head position to a head tile.
		 * 
		 * If more than one direction is in the queue, poll it to read new
		 * input.
		 */
		if(old != TileType.SNAKEBODY) {
			board.setTile(snake.peekFirst(), TileType.SNAKEBODY);
			snake.push(head);
			board.setTile(head, TileType.SNAKEHEAD);
			if(directions.size() > 1) {
				directions.poll();
			}
		}
				
		return old;
		
	}
	private void spawnFruit() {
		//Reset the score for this fruit to 100.
				this.fruitScore = 100+incScore;

				/*
				 * Get a random index based on the number of free spaces left on the board.
				 */
				int index = random.nextInt(BoardPanel.COL_COUNT * BoardPanel.ROW_COUNT - snake.size());
				
				/*
				 * While we could just as easily choose a random index on the board
				 * and check it if it's free until we find an empty one, that method
				 * tends to hang if the snake becomes very large.
				 * 
				 * This method simply loops through until it finds the nth free index
				 * and selects uses that. This means that the game will be able to
				 * locate an index at a relatively constant rate regardless of the
				 * size of the snake.
				 */
				int freeFound = -1;
				for(int x = 0; x < BoardPanel.COL_COUNT; x++) {
					for(int y = 0; y < BoardPanel.ROW_COUNT; y++) {
						TileType type = board.getTile(x, y);
						if(type == TileType.EMPTY || type == TileType.FRUIT) {
							if(++freeFound == index) {
								board.setTile(x, y, TileType.FRUIT);
								break;
							}
						}
					}
				}
				incScore += 20;
	}
	
}
