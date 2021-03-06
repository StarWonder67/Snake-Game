package snakeGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;



public class BoardPanel extends JPanel{
	
	//Variables 
	public static final int ROW_COUNT = 25;
	public static final int COL_COUNT = 25;
	public static final int TILE_SIZE = 20;
	private static final Font FONT = new Font("Tahoma", Font.BOLD, 25);
	private SnakeGame game;
	
	private TileType[] tiles;
	
	
	//CONSTRUCTOR
	public BoardPanel(SnakeGame game){
		this.game = game;
		this.tiles = new TileType[ROW_COUNT * COL_COUNT];
		
		setPreferredSize(new Dimension(COL_COUNT * TILE_SIZE, ROW_COUNT * TILE_SIZE));
		setBackground(Color.BLACK);
		
	}
	
	//Methods
	public void clearBoard() {
		for(int i=0;i<tiles.length;i++) {
			tiles[i] = TileType.EMPTY;
		}
	}
	
	public void setTile(Point p,TileType type) {
		setTile(p.x,p.y,type);
	}
	
	public void setTile(int x,int y,TileType type) {
		tiles[y*ROW_COUNT+x] = type;
	}
	
	public TileType getTile(int x,int y) {
		return tiles[y*ROW_COUNT+x];
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		/*
		 * Loop through each tile on the board and draw it if it
		 * is not null.
		 */
		for(int x = 0; x < COL_COUNT; x++) {
			for(int y = 0; y < ROW_COUNT; y++) {
				TileType type = getTile(x, y);
				if(type != null) {
					drawTile(x * TILE_SIZE, y * TILE_SIZE, type, g);
				}
			}
		}
		
		/*
		 * Draw the grid on the board. This makes it easier to see exactly
		 * where we in relation to the fruit.
		 * 
		 * The panel is one pixel too small to draw the bottom and right
		 * outlines, so we outline the board with a rectangle separately.
		 */
		g.setColor(Color.DARK_GRAY);
		g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		for(int x = 0; x < COL_COUNT; x++) {
			for(int y = 0; y < ROW_COUNT; y++) {
				g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, getHeight());
				g.drawLine(0, y * TILE_SIZE, getWidth(), y * TILE_SIZE);
				}
			}
		

		/*
		 * Show a message on the screen based on the current game state.
		 */
		if(game.isGameOver() || game.isNewGame() || game.isPaused()) {
			g.setColor(Color.WHITE);
			
			/*
			 * Get the center coordinates of the board.
			 */
			int centerX = getWidth() / 2;
			int centerY = getHeight() / 2;
			
			/*
			 * Allocate the messages for and set their values based on the game
			 * state.
			 */
			String largeMessage = null;
			String smallMessage = null;
			
			if(game.isNewGame()) {
				largeMessage = "Snake Game!";
				smallMessage = "Press Enter to Start";
			} else if(game.isGameOver()) {
				largeMessage = "Game Over!";
				smallMessage = "Press Enter to Restart";
				
			} else if(game.isPaused()) {
				largeMessage = "Paused";
				smallMessage = "Press P to Resume";
			}
			
			/*
			 * Set the message font and draw the messages in the center of the board.
			 */
			g.setFont(FONT);
			g.drawString(largeMessage, centerX - g.getFontMetrics().stringWidth(largeMessage) / 2, centerY - 30);
			//g.drawString(score,centerX - g.getFontMetrics().stringWidth(score) / 2, centerY - 20);
			g.drawString(smallMessage, centerX - g.getFontMetrics().stringWidth(smallMessage) / 2, centerY + 50);
		}
		
		
		}
		
		public void drawTile(int x,int y,TileType type,Graphics g) {
			
			switch(type) {
				case SNAKEBODY:{
					g.setColor(Color.GREEN);
					g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
					break;
				}
				case SNAKEHEAD:{
					g.setColor(Color.CYAN);
					g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
					break;
				}
				case FRUIT:{
					g.setColor(Color.RED);
					g.fillOval(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
					break;
				}
				case EMPTY:{
					g.setColor(Color.BLACK);
					g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
					break;
				}
			}
			
		}
			
			
		
		
		
		
}
