package tools;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class GameTools {

	public WebDriver driver;
	public WebElement game;
	private int[][] lstState;
	private int[][] curState;
	
	public GameTools() {
		driver = new FirefoxDriver();
		curState = new int[4][4];
		lstState = new int[4][4];
	}
	
	private GameTools(int[][] state) {
		curState = state;
	}
	
	/**
	 * This method opens a browser with 2048
	 * @return
	 */
	public boolean open2048() {
		
		driver.get("http://2048game.com");
		new WebDriverWait(driver, 5000).until(ExpectedConditions.titleContains("2048"));
		game = driver.findElement(By.className("game-container"));
		
		if (game == null) return false;
		else return true;
	}

	//Methods for generic columns go here
	
	/**
	 * This method returns an integer determining
	 * the number of tiles filling a column
	 * @param col
	 * @return
	 */
	public int columnFilled(int col) {
		
		int cur = 0;
		int filled = 0;
		
		for (int i = 0; i < 4; i++) {			
			cur = curState[col][i];
			if (cur != 0) filled++;
		}
		
		return filled;
	}
	
	/**
	 * This method determines if a given
	 * column is filled and not compressible
	 * @param col
	 * @return
	 */
	public boolean isColumnFull(int col) {
		return columnFilled(col) == 4;
	}
	
	/**
	 * This method determines if a given column is empty
	 * @param col
	 * @return
	 */
	public boolean isColumnEmpty(int col) {
		return columnFilled(col) == 0;
	}
	
	/**
	 * This method determines whether a column can be compressed
	 * @param col
	 * @return
	 */
	public boolean isColumnCompressable(int col) {
		for (int i = 0; i < 3; i++)
			if (tileCanMergeDown(col, i)) return true;
		return false;
	}
	
	/**
	 * This method determines if a given column
	 * can merge with the column to the left
	 * @param col
	 * @return
	 */
	public boolean columnCanMergeLeft(int col) {
		for (int i = 0; i < 4; i++)
			if (tileCanMergeLeft(col, i)) return true;
		return false;
	}

	/**
	 * This method determines if a given column
	 * can merge with the column to the right
	 * @param col
	 * @return
	 */
	public boolean columnCanMergeRight(int col) {
		for (int i = 0; i < 4; i++)
			if (tileCanMergeRight(col, i)) return true;
		return false;
	}
	
	/**
	 * This method returns the maximum value in a given column
	 * @param col
	 * @return
	 */
	public int getColumnMax(int col) {
		int max = 0;
		for (int i = 0; i < 4; i++) {
			if (curState[col][i] > max)
				max = curState[col][i];
		}
		return max;
	}
	
	/**
	 * This method returns the minimum value in a given column
	 * @param col
	 * @return
	 */
	public int getColumnMin(int col) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < 4; i++) {
			if (curState[col][i] < min && curState[col][i] != 0)
				min = curState[col][i];
		}
		return min;
	}
	
	//Methods for generic rows go here
	
	/**
	 * This method returns an integer determining
	 * the number of tiles filling a row
	 * @param row
	 * @return
	 */
	public int rowFilled(int row) {

		int filled = 0;
		
		for (int i = 0; i < 4; i++)			
			if (curState[i][row] != 0) filled++;
		
		return filled;
	}
	
	/**
	 * This method checks if a given row is full
	 * @param row
	 * @return
	 */
	public boolean isRowFull(int row) {
		return rowFilled(row) == 4;
	}
	
	/**
	 * This method checks if a given row is empty
	 * @param row
	 * @return
	 */
	public boolean isRowEmpty(int row) {
		return rowFilled(row) == 0;
	}
	
	/**
	 * This method determines whether a row can be compressed
	 * @return
	 */
	public boolean isRowCompressable(int row) {
		for (int i = 0; i < 3; i++)
			if (tileCanMergeRight(i, row)) return true;
		return false;
	}
	
	
	/**
	 * This method determines if a row
	 * can merge with the row below
	 * @param row
	 * @return
	 */
	public boolean rowCanMergeDown(int row) {
		for (int i = 0; i < 4; i++)
			if (tileCanMergeDown(i, row)) return true;
		return false;
	}
	
	/**
	 * This method determines if a row
	 * can merge with the row above
	 * @param row
	 * @return
	 */
	public boolean rowCanMergeUp(int row) {
		for (int i = 0; i < 4; i++)
			if (tileCanMergeUp(i, row)) return true;
		return false;
	}
	
	/**
	 * This method returns the max value in a row
	 * @param row
	 * @return
	 */
	public int getRowMax(int row) {
		int max = 0;
		for (int i = 0; i < 4; i++) {
			if (curState[i][row] > max)
				max = curState[i][row];
		}
		return max;
	}
	
	/**
	 * This method returns the max value in a row
	 * @param row
	 * @return
	 */
	public int getRowMin(int row) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < 4; i++) {
			if (curState[i][row] < min && curState[i][row] != 0)
				min = curState[i][row];
		}
		return min;
	}
	
	//Methods for generic tiles go here
	
	/**
	 * This method returns the value of a given tile
	 * @param x
	 * @param y
	 * @return
	 */
	public int getTile(int x, int y) {
		return curState[x][y];
	}
	
	/**
	 * This method checks if a tile
	 * can merge with the tile above
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean tileCanMergeUp(int x, int y) {
		for (int i = y-1; i >= 0; i--) {
			if (getTile(x,i) != 0) {
				if (getTile(x,i) == getTile(x,y))
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	/**
	 * This method checks if a tile
	 * can merge with the tile below
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean tileCanMergeDown(int x, int y) {
		for (int i = y+1; i < 4; i++) {
			if (getTile(x,i) != 0) {
				if (getTile(x,i) == getTile(x,y))
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	/** 
	 * This method checks if a tile
	 * can merge with the tile to the left
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean tileCanMergeLeft(int x, int y) {
		for (int i = x-1; i >= 0; i--) {
			if (getTile(i,y) != 0) {
				if (getTile(i,y) == getTile(x,y))
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	/**
	 * This method checks if a tile
	 * can merge with the tile to the right
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean tileCanMergeRight(int x, int y) {
		for (int i = x+1; i < 4; i++) {
			if (getTile(i,y) != 0) {
				if (getTile(i,y) == getTile(x,y))
					return true;
				else
					return false;
			}
		}
		return false;
	}
	
	//Methods fore game states go here
	
	/**
	 * This method sets the current state of the game
	 * @return
	 */
	private boolean getCurState() {
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				lstState[i][j] = curState[i][j];
				curState[i][j] = 0;
			}
		}
		
		List<WebElement> tiles = driver.findElements(By.className("tile"));
		String pos = null;
		
		int x = 0, y = 0;
		
		if (tiles.isEmpty()) return false;
		
		for (int i = 0; i < tiles.size(); i++) {
			WebElement t = tiles.get(i);
			pos = t.getAttribute("class");
			pos = pos.substring(pos.indexOf("pos")+9, pos.indexOf("pos")+12);
			x = Integer.parseInt(pos.substring(0, 1))-1;
			y = Integer.parseInt(pos.substring(2, 3))-1;
			curState[x][y] = Integer.parseInt(t.findElement(By.className("tile-inner")).getText());
		}
		
		System.out.println(curStateToString());
		System.out.println("===============");
		return true;
	}
	

	/**
	 * This method checks if the current state
	 * is different than the last state
	 * @return
	 */
	public boolean didStateChange() {
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (curState[i][j] != lstState[i][j]) return true;
			}
		}
		
		return false;
	}
	
	/** 
	 * This method checks if the strategy should enter recovery mode.
	 * It is determined by values of tiles from top to bottom.
	 * @return
	 */
	public int[] recoveryMode(int col) {
		int[] tile = {-1, -1};
		for (int i = 0; i < 3; i++) {
			if (getTile(col,i) != 0) {
				if (getTile(col,i) < getTile(col,i+1)) {
					tile[0] = col;
					tile[1] = i;
					return tile;
				}
			}
		}
		return tile;
	}

	
	/**
	 * This method determines if the game is over
	 * @return
	 */
	public boolean gameOver() {
		if (driver.findElement(By.cssSelector(".game-message > p")).getText().equals("Game over!")) return true;
		else return false;
	}
	
	//Methods for moves go here
	
	/**
	 * This method attempts to push the tiles left
	 * it returns the value of didStateChange()
	 * @return
	 * @throws InterruptedException
	 */
	public boolean pushLeft() {
		try { game.sendKeys("a"); }catch (Throwable t) { return false; }
		getCurState();
		return didStateChange();
	}
	
	/**
	 * This method attempts to push the tiles right
	 * it returns the value of didStateChange()
	 * @return
	 * @throws InterruptedException
	 */
	public boolean pushRight() {
		try { game.sendKeys("d"); }catch (Throwable t) { return false; }
		getCurState();
		return didStateChange();
	}
	
	/**
	 * This method attempts to push the tiles up
	 * it returns the value of didStateChange()
	 * @return
	 * @throws InterruptedException
	 */
	public boolean pushUp() {
		try { game.sendKeys("w"); }catch (Throwable t) { return false; }
		getCurState();
		return didStateChange();
	}
	
	/**
	 * This method attempts to push the tiles down
	 * it returns the value of didStateChange()
	 * @return
	 * @throws InterruptedException
	 */
	public boolean pushDown() {
		try { game.sendKeys("s"); }catch (Throwable t) { return false; }
		getCurState();
		return didStateChange();
	}
	
	//Print methods go here
	
	/**
	 * toString method for current game state
	 * @return
	 */
	public String curStateToString() {
		
		String str = "\n";
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				str += curState[j][i] + " ";
			}
			str += "\n";
		}
		
		return str;
	}
	
	/**
	 * toString method for previous game state
	 * @return
	 */
	public String lstStateToString() {
		
		String str = "";
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				str += lstState[j][i] + " ";
			}
			str += "\n";
		}
		return str;
	}
	
	/**
	 * This is to be used only for test cases
	 * @param state
	 */
	public void setCurrentState(int[][] state) {
		curState = state;
	}
	
	/**
	 * This is to be used only for test cases
	 * @param state
	 * @return
	 */
	public static GameTools getTestingGameTools(int[][] state) {
		return new GameTools(state);
	}
}