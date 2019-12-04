import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel{
	private static final long serialVersionUID = 1L;
	
	/**
	 * This imports is the import file
	 */
	
	private Font myFont = new Font("Calibri", 1, 30);
	private Font gameEnd = new Font("Bold", 1, 40);
	private Shapes shape = new Shapes();
	private Random rand = new Random();// importing random
	private ArrayList<Integer> piecesList = new ArrayList<Integer>();
	private Highscore fileScore = new Highscore();
	
	private static boolean play = true;
	private static final Game game = new Game();
	
	
	private int currentPiece;
	private int rotation = 0;
	private long score;
	private long highScore = 0;
	private int squareLength = 26;
	
	
	private Color[][] board;//this array holds the color values of tiles on the grid
	private Point[][][] shapes = shape.shape();
	private Color[] colors = shape.shapeColors();
	
	//points that allow for 
	private Point pieceOrigin;
	private Point currentPlace;
	
	/**
	 * this creates the board using a color array 
	 */
	public void init() {
		
		board = new Color[16][25];
		for (int row = 0; row < 15; row++) {
			for (int column = 0; column < 24; column++) {
				if (row == 0 || row == 10 || column == 22) {
					board[row][column] = Color.BLUE;
				} else {
					board[row][column] = Color.BLACK;
				}
			}
			
		} 
		
		newPiece();
		
		}
				
	
	/**
	 * this takes all of the shapes and adds them to an array list
	 */
	public void newPiece() {
		int number = 0;
		pieceOrigin = new Point(5, 2);
		currentPlace = pieceOrigin;
		Collections.addAll(piecesList, 0,1,2,3,4,5,6);
		number = rand.nextInt(6);
		currentPiece = piecesList.indexOf(number);
	}
	
	/**
	 * this checks for collision
	 * 
	 * @param x is the place on the grid horizontally
	 * @param y is the place on the grid vertically
	 * @param rotation is an integer on how the shape should be oriented
	 * @return it returns whether there is a collision at each point
	 */
		private boolean collidesAt(int x, int y, int rotation) {
			for (Point p : shapes[currentPiece][rotation]) {
				if (board[p.x + x][p.y + y] != Color.BLACK) {
					return true;
				}
				
				
			}
			return false;
			
		}
		
		/**
		 * This sets the rotation of the shapes
		 * @param i is the addition to the integer rotation
		 */
		public void rotate(int i) {
			int newRotation = (rotation + i);
			if (newRotation == 4) {
				newRotation = 0;
			}
			rotation = newRotation;
			
			repaint();
		}
		
		/**
		 * This changes the current location of the shapes.
		 * @param i 
		 */
		public void move(int i) {
		 
			if (!collidesAt(currentPlace.x + i, currentPlace.y, rotation)) {
				pieceOrigin.x += i;	
			}
			repaint();
		}
		
		/**
		 * It moves the piece down by increasing y-value the array 
		 */
		public void moveDown() {
			if (!collidesAt(currentPlace.x, currentPlace.y + 1, rotation)) {
				currentPlace.y += 1;
				game.score += 1;
			} else {
				fixToWell();
			}
			repaint();
		}
		
		/**
		 * This sets the shape in the spot after the collision
		 */
		public void fixToWell() {
			for (Point p : shapes[currentPiece][rotation]) {
				board[currentPlace.x + p.x][currentPlace.y + p.y] = colors[currentPiece];
			}
			clearRows();
			if(play == true) {
				newPiece();
				end();
			}
			
		}
		
		/**
		 * this deletes the row after clear row if the entire thing is black
		 * @param row
		 */
		
		public void deleteRow(int row) {
			for (int j = row-1; j > 0; j--) {
				for (int i = 1; i < 11; i++) {
					board[i][j+1] = board[i][j];
				}
			}
		}
		
		/** 
		 * This checks if there is collision at the top row
		 * if there is it stops the game
		 */
		public void end() {
			if (collidesAt(pieceOrigin.x, pieceOrigin.y - 1, 0)) {
				play = false;
			}
		}
		
		
		public long clearRows() {
			boolean emptyLine;
			int numClears = 0;
			
			for (int j = 21; j > 0; j--) {
				emptyLine = false;
				for (int i = 1; i < 11; i++) {
					if (board[i][j] == Color.BLACK) {
						emptyLine = true;
						break;
					}
				}
				if (!emptyLine) {
					deleteRow(j);
					j += 1;
					numClears += 1;
				}
			}
			
			// This bases the score gained by number of cleared rows
			switch (numClears) {
			case 1:
				score += 100;
				break;
			case 2:
				score += 300;
				break;
			case 3:
				score += 500;
				break;
			case 4:
				score += 800;
				break;
			}
			return score;
		}
		
		// Draw the falling piece
		private void drawPiece(Graphics g) {		
			g.setColor(colors[currentPiece]);
			for (Point p : shapes[currentPiece][rotation]) {
				g.fillRect((p.x + pieceOrigin.x) * squareLength, 
						   (p.y + pieceOrigin.y) * squareLength, 
						   25, 25);
			}
		}
		
		@Override 
		public void paint(Graphics g)
		{	
			int boardWidth = 14;
			int boardHieght = 23;
			// Painting the board
			g.fillRect(0, 0, squareLength*boardWidth, squareLength*boardHieght);
			for (int i = 0; i < 14; i++) {
				for (int j = 0; j < 23; j++) {
					g.setColor(board[i][j]);
					g.fillRect(26*i, 26*j, 25, 25);
				}
			}
			
			// Displays the score
			g.setColor(Color.WHITE);
			g.drawString("Score: " + score, 290, 25);
			
			if (score < highScore) {
				g.drawString("High: " , 290, 50);
				g.drawString(""+highScore, 290, 65);
			}else {
				g.drawString("High: "+ score, 290, 50);
			}
			// This displays the Tetris title name
			g.setFont(myFont);
			g.setColor(Color.WHITE);
			g.drawString("Tetris", 125, 35);
			
			if(play == false) {
				g.setFont(gameEnd);
				g.setColor(Color.WHITE);
				g.drawString("GAME OVER", 75, 325);
				try {
					saveScore();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			drawPiece(g);
		}
		public void saveScore() throws IOException {
			if (score > highScore) {
				fileScore.writeHighScore(score);
			}else {
				fileScore.writeHighScore(highScore);
			}
		}

		
		/**
		 * within the main it creates a jFrame and runs the game
		 * @param args
		 */
		public static void main(String[] args) {
			JFrame frame = new JFrame("Tetris");
			int width = 500;
			int height = 650;
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(width, height);
			frame.setVisible(true);
			
			 
			game.init();//initiates the game
			frame.add(game);// adds the game data to the jframe
		
			
		
			// Keyboard controls
			frame.addKeyListener(new KeyListener() {
				public void keyTyped(KeyEvent e) {
				}
				
				/**
				 * This sets the controls for the 
				 */
				public void keyPressed(KeyEvent e) {
					switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						game.rotate(+1);
						break;
					case KeyEvent.VK_DOWN:
						game.moveDown();
						break;
					case KeyEvent.VK_LEFT:
						game.move(-1);
						break;
					case KeyEvent.VK_RIGHT:
						game.move(+1);
						break;
					case KeyEvent.VK_ENTER:
						play = false;
						break;
				
					} 
				}
				
				public void keyReleased(KeyEvent e) {
				}
			});
			
			/**
			 * This uses threading to have the shape fall every second.
			 */
			new Thread() {
				@Override public void run() {
					while (true) {
						try {
							Thread.sleep(1000);
							game.moveDown();
						} catch ( InterruptedException e ) {}
					}
				}
			}.start();
		}

	
	
}
	