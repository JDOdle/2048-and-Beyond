package tools;


public class AI2048 {
	
	private GameTools tools;
	
	public AI2048() {
		tools = new GameTools();
	}
	
	private AI2048(int[][] state) {
		tools = GameTools.getTestingGameTools(state);
	}
	
	public boolean startGame() {
		
		boolean started = tools.open2048();
		
		tools.pushUp();
		tools.pushRight();
		
		return started;
	}
	
	public boolean gameOver() {
		return tools.gameOver();
	}
	
	public void quitGame() {
		tools.driver.quit();
	}
	public void makeMove() {
		makeMove(3);
	}
	
	public void makeMove(int col) {

 		if (col == 0) return;
		
		int[] recTile = tools.recoveryMode(col);
		
		//always compress the current column when possible;
		if (tools.isColumnCompressable(col))
			tools.pushUp();
		//if hierarchy is invalid
		else if (tools.recoveryMode(col)[0] != -1)
			columnRecoveryMove(col);
		else if (recTile[0] != -1)
			executeMoveHierarchy(col);
		//if hierarchy is valid and merges are possible
		else if (tools.columnCanMergeLeft(col))
			tools.pushRight();
		//if a pair of moves can be executed to merge tiles
		//and the column is full
		else if (columnUpRightMerge(col)) {
			tools.pushUp();
			tools.pushRight();
		}
		else if (columnDownRightMerge(col) && tools.isColumnFull(col)) {
			tools.pushDown();
			tools.pushRight();
			tools.pushUp();
		}
		//recursive case
		else if (tools.isColumnFull(col) && col != 0)
			makeMove(col-1);
		//base case and last resort move
		else
			executeMoveHierarchy(col);
	}
	
	public void columnRecoveryMove(int col) {
		
		int[] recTile = tools.recoveryMode(col);
		int recTileVal;
		int leftVal = 0;
		
		recTileVal = tools.getTile(recTile[0], recTile[1]);
		leftVal = tools.getTile(recTile[0]-1, recTile[1]);
		
		//if the misplaced tile is blocked by a larger tile
		//then we want to move that tile out of the way
		if (recTileVal < leftVal) {
			if (recTile[1] >= 2) {
				if (tools.isColumnCompressable(col-1) ||
						tools.columnFilled(col-1) <= 2) {
					if (!tools.pushUp()) {
						tools.pushDown();
						tools.pushUp();
					}
				}
				else {
					if (!tools.pushRight()) {
						if (!tools.pushUp()) {
							if (!tools.pushDown()) {
								tools.pushLeft();
								tools.pushRight();
							}
							else
								tools.pushUp();
						}
					} 
				}
			}
			else {
				if ( ( (tools.isColumnCompressable(col-1) ||
						tools.columnFilled(col-1) <= 2) && col != 3) || 
						(tools.isColumnFull(3) && !tools.isColumnCompressable(3)) ) {
					if (!tools.pushDown()) {
						if (!tools.pushRight()) {
							if (!tools.pushUp()) {
								tools.pushLeft();
								tools.pushRight();
							}
							tools.pushDown();

						}
					}
				}
				else {
					if (!tools.pushRight()) {
						if (!tools.pushDown()) {
                            if (!tools.pushUp()) {
								tools.pushLeft();
								tools.pushRight();
							}
							else
								tools.pushDown();
						}
					}
				}
			}
		}
		//otherwise we want to make a tile that can be merged with it
		else {
			//case 1: can execute merge with left tile
			if (tools.tileCanMergeLeft(recTile[0], recTile[1]))
				tools.pushRight();
			//case 2: can execute up right maneuver to merge with tile
			else if (tileUpRightMerge(recTile[0], recTile[1])) {
				tools.pushUp();
				tools.pushRight();
			}
			//case 3: can execute down right maneuver to merge with tile
			else if (tileDownRightMerge(recTile[0], recTile[1])) {
				tools.pushDown();
				tools.pushRight();
				tools.pushUp();
			}
			//case 4: execute next best moves until one is executed
			else if (recTile[1] >= 2) {
				if (!tools.pushUp()) {
					if (!tools.pushRight()) {
                        if (!tools.pushDown()) {
							tools.pushLeft();
							tools.pushRight();
						}
						else
							tools.pushUp();
					}
				}
			}
			else {
				if (!tools.pushDown()) {
					if (!tools.pushRight()) {
						if (!tools.pushUp()) {
							tools.pushLeft();
							tools.pushRight();
						}
						else
							tools.pushDown();
					}
				}
			}
		}
	}
	
	/**
	 * This method executes the next best
	 * move until a move has been executed
	 * @param col
	 */
	public void executeMoveHierarchy(int col) {
		
		if (!tools.pushRight()) {
			if (!tools.pushUp()) {
				if (tools.isColumnFull(col) &&
						!tools.isColumnCompressable(col)) {
					if (!tools.pushDown()) {
						tools.pushLeft();
						tools.pushRight();
					}
					tools.pushUp();
				}
				else if (tools.isRowFull(0) &&
						tools.isRowCompressable(0) && col == 3) {
					if (!tools.pushLeft()) {
						tools.pushDown();
						tools.pushUp();
					}
					else
						tools.pushRight();
				}
				else {
					if (!tools.pushDown()) {
						tools.pushLeft();
						tools.pushRight();
					}
					tools.pushUp();
                }
            }
		}
	}

	/**
	 * This method determines if the given column will be able
	 * to merge with the column to its left after pushing up
	 * @param col
	 * @return
	 */
	public boolean columnUpRightMerge(int col) {
		for (int i = 0; i < 4; i++) {
			if (tools.getTile(col, i) != 0 && tileUpRightMerge(col, i))
				return true;
		}
		return false;
	}
	
	/**
	 * This method determines if the given column will be able
	 * to merge with the column to its left after pushing down
	 * @param col
	 * @return
	 */
	public boolean columnDownRightMerge(int col) {

		for (int i = 0; i < 4; i++) {
			if (tools.getTile(col, i) != 0 && tileDownRightMerge(col, i))
				return true;
		}
		return false;
	}
	
	/**
	 * This method determines if a merge can be made with
	 * a tile with the move sequence pushUp > pushRight.
	 * TODO: add support for when column x can compress
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean tileUpRightMerge(int x, int y) {
		
		//y must be the tiles final row value
		for (int i = y-1; i >= 0; i--) {
			if (tools.getTile(x, i) == 0)
				y--;
		}
		
		int filler = 0;
		
		boolean[] matches = new boolean[4];
		
		/*
		 *This loop goes through the left column with a filler count
		 *any time it finds a non zero tile the filler count increase
		 */
		for (int i = 0; i < 4; i++) {
			if (tools.getTile(x-1, i) != 0) {
				//if a mergeable tile is found then the position goes to
				//the next tile after the second tile in the mergeable pair
				if (tools.tileCanMergeDown(x-1, i)) {
					if (tools.getTile(x-1, i)*2 == tools.getTile(x, y))
						matches[filler] = true;
					for (int j = i+1; j < 4; j++) {
						if (tools.tileCanMergeUp(x-1, j)) {
							i = j;
							break;
						}
					}
				}
				//if a matching tile is found then a boolean
				//corresponding to the fill is set to true
				else if (tools.getTile(x-1, i) == tools.getTile(x, y))
					matches[filler] = true;
				
				filler++;
			}
		}
		
		return matches[y];
	}
	
	/**
	 * This method determines if a merge can be made with
	 * a tile with the move sequence pushRight > pushRight.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean tileRightRightMerge(int x, int y) {
		if (x < 2) return false;
		else return (tools.getTile(x-1, y)+tools.getTile(x-2, y)) == tools.getTile(x, y);
	}
	
	/**
	 * This method determines if a merge can be made with
	 * a tile with the move sequence pushDown > pushRight.
	 * TODO: add support for when column x can compress
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean tileDownRightMerge(int x, int y) {
		
		//y must be the tiles final row value
		for (int i = y+1; i < 4; i++) {
			if (tools.getTile(x, i) == 0)
				y++;
		}
				
		int filler = 0;
		
		boolean[] matches = new boolean[4];
		
		/*
		 *This loop goes through the left column with a filler count
		 *any time it finds a non zero tile the filler count increase
		 */
		for (int i = 3; i >= 0; i--) {
			if (tools.getTile(x-1, i) != 0) {
				//if a mergeable tile is found then the position goes to
				//the next tile after the second tile in the mergeable pair
				if (tools.tileCanMergeUp(x-1, i)) {
					if (tools.getTile(x-1, i)*2 == tools.getTile(x, y))
						matches[filler] = true;
					for (int j = i-1; j >= 0; j--) {
						if (tools.tileCanMergeDown(x-1, j)) {
							i = j;
							break;
						}
					}
				}
				//if a matching tile is found then a boolean
				//corresponding to the fill is set to true
				else if (tools.getTile(x-1, i) == tools.getTile(x, y))
					matches[filler] = true;
				
				filler++;
			}
		}
		
		return matches[3-y];
	}

	/**
	 * This is to be used only for testing
	 * @param state
	 * @return
	 */
	public static AI2048 getTestingAI(int[][] state) {
		return new AI2048(state);
	}
	
	/**
	 * This is to be used only for testing
	 * @param x
	 * @param y
	 * @return
	 */
	public int getTile(int x, int y) {
		return tools.getTile(x, y);
	}

	public String toString() {
		return tools.curStateToString();
	}
}
