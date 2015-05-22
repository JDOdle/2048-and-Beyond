package tools;


public class AI2048 {
	
	private static GameTools tools = new GameTools();
	
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
		else if (recTile[0] != -1)
			executeMoveHierarchy(col);
		//if hierarchy is valid and merges are possible
		else if (tools.columnCanMergeLeft(col))
			tools.pushRight();
		//if a pair of moves can be executed to merge tiles
		//and the column is full
		else if (columnUpRightMerge(col) && tools.isColumnFull(col)) {
			tools.pushUp();
			tools.pushRight();
		}
		else if (columnDownRightMerge(col) && tools.isColumnFull(col)) {
			tools.pushDown();
			tools.pushRight();
			tools.pushUp();
		}
		else if (tools.isColumnFull(col))
			makeMove(col-1);
		else
			executeMoveHierarchy(col);
	}
	
	/**
	 * This method executes the next best
	 * move until a move has been executed
	 * @param col
	 */
	private void executeMoveHierarchy(int col) {
		int[] recTile = tools.recoveryMode(col);
		int recTileVal;
		int leftVal = 0;
		
		if (col == 0) return;
		//if the tiles are not ordered properly then the move priority changes
		if (recTile[0] != -1) {
			recTileVal = tools.getTile(recTile[0], recTile[1]);
			leftVal = tools.getTile(recTile[0]-1, recTile[1]);
			
			if (tools.recoveryMode(col-1)[0] != -1 && tools.isColumnCompressable(col-1)) {
				executeMoveHierarchy(col-1);
			}
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
								tools.pushDown();
							}
						}
					}
				}
			}
			//otherwise we want to make a tile that can be merged with it
			else {
				//case 1: can execute up right maneuver to merge with tile
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
		else {
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
	}

	/**
	 * This method determines if the given column will be able
	 * to merge with the column to its left after pushing up
	 * @param col
	 * @return
	 */
	private boolean columnUpRightMerge(int col) {
		
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
	private boolean columnDownRightMerge(int col) {
		
		for (int i = 0; i < 4; i++) {
			if (tools.getTile(col, i) != 0 && tileDownRightMerge(col, i))
				return true;
		}
		return false;
	}
	
	/**
	 * This method determines if a merge can be made with
	 * a tile with the move sequence pushUp > pushRight.
	 * It is assumed that the tile will not move and the
	 * left column does not have any holes.
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean tileUpRightMerge(int x, int y) {
		//TODO: add support for case where left tile is not compressible
		//case 1: tile is at the top
		if (y == 0)  {
			if (tools.getTile(x-1, y)*2 == tools.getTile(x, y))
				return tools.tileCanMergeDown(x-1, y);
			else
				return false;
		}
		//case 2: tile is second from top
		else if (y == 1) {
			if (tools.isColumnCompressable(x-1)) {
				//case 1: left column has 4 tiles
				if (tools.isColumnFull(x-1)) {
					//case 1: top two tiles can merge
					if (tools.tileCanMergeDown(x-1, 0)) {
						//case 1: bottom two tiles can merge
						if (tools.tileCanMergeDown(x-1, 2))
							return (tools.getTile(x-1, 2)*2 == tools.getTile(x, y));
						//case 2: bottom two tiles can not merge
						else
							return (tools.getTile(x-1, 2) == tools.getTile(x, y));
					}
					//case 2: middle two tiles can merge
					else if (tools.tileCanMergeDown(x-1, 1))
						return tools.getTile(x-1, 2)*2 == tools.getTile(x, y);
					else
						return false;
				}
				//case 2: left column has 3 tiles
				if (tools.columnFilled(x-1) == 3) {
					if (tools.getTile(x-1, 0) != 0) {
						//case 1: top two tiles can merge
						if (tools.tileCanMergeDown(x-1, 0))
							return (tools.getTile(x-1, 2) == tools.getTile(x, y));
						//case 2: bottom two tiles can merge
						else if (tools.tileCanMergeDown(x-1, 1))
							return (tools.getTile(x-1, 1)*2 == tools.getTile(x, y));
						else
							return false;
					}
					else {
						//case 1: top two tiles can merge
						if (tools.tileCanMergeDown(x-1, 1))
							return (tools.getTile(x-1, 3) == tools.getTile(x, y));
						//case 2: bottom two tiles can merge
						else if (tools.tileCanMergeDown(x-1, 2))
							return (tools.getTile(x-1, 2)*2 == tools.getTile(x, y));
						else
							return false;
					}
				}
				//case 3: left column has 2 tiles
				else
					return false;
			}
			else
				return false;
		}
		//case 3: tile is third from the top
		else if (y == 2) {
			if (tools.getTile(x-1, y)*2 == tools.getTile(x, y))
				return tools.tileCanMergeDown(x-1, y);
			else
				return false;
		}
		else		
			return false;
	}
	
	/**
	 * This method determines if a merge can be made with
	 * a tile with the move sequence pushDown > pushRight.
	 * It is assumed that the tile will not move and the
	 * left column does not have any holes.
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean tileDownRightMerge(int x, int y) {
		//TODO: add support for case where left tile is not compressible
		//case 1: tile is at the bottom
		if (y == 3)  {
			//case 1: left column is compressible
			if (tools.isColumnCompressable(x-1)) {
				if (tools.getTile(x-1, y)*2 == tools.getTile(x, y))
					return tools.tileCanMergeUp(x-1, y);
				else
					return false;
			}
			//case 2: left column has 1 tile
			else if (tools.columnFilled(x-1) == 1)
				return (tools.getTile(x-1, 0) == tools.getTile(x, y));
			//case 3: left column has 2 tiles
			else if (tools.columnFilled(x-1) == 2)
				return (tools.getTile(x-1, 1) == tools.getTile(x, y));
			//case 4: left column has 3 tiles
			else if (tools.columnFilled(x-1) == 3)
				return (tools.getTile(x-1, 2) == tools.getTile(x, y));
			else return false;
		}
		//case 2: tile is second from bottom
		else if (y == 2) {
			if (tools.isColumnCompressable(x-1)) {
				//case 1: left column has 4 tiles
				if (tools.isColumnFull(x-1)) {
					//case 1: bottom two tiles can merge
					if (tools.tileCanMergeUp(x-1, 3)) {
						//case 1: bottom two tiles can merge
						if (tools.tileCanMergeUp(x-1, 1))
							return (tools.getTile(x-1, 1)*2 == tools.getTile(x, y));
						//case 2: bottom two tiles can not merge
						else
							return (tools.getTile(x-1, 1) == tools.getTile(x, y));
					}
					//case 2: middle two tiles can merge
					else if (tools.tileCanMergeUp(x-1, 2))
						return tools.getTile(x-1, 1)*2 == tools.getTile(x, y);
					else
						return false;
				}
				//case 2: left column has 3 tiles
				if (tools.columnFilled(x-1) == 3) {
					if (tools.getTile(x-1, 3) != 0) {
						//case 1: bottom two tiles can merge
						if (tools.tileCanMergeUp(x-1, 3))
							return (tools.getTile(x-1, 1) == tools.getTile(x, y));
						//case 2: top two tiles can merge
						else if (tools.tileCanMergeUp(x-1, 2))
							return (tools.getTile(x-1, 2)*2 == tools.getTile(x, y));
						else
							return false;
					}
					else {
						//case 1: bottom two tiles can merge
						if (tools.tileCanMergeUp(x-1, 2))
							return (tools.getTile(x-1, 0) == tools.getTile(x, y));
						//case 2: top two tiles can merge
						else if (tools.tileCanMergeUp(x-1, 1))
							return (tools.getTile(x-1, 1)*2 == tools.getTile(x, y));
						else
							return false;
					}
				}
				//case 3: left column has 2 tiles
				else
					return false;
			}
			else
				return false;
		}
		//case 3: tile is third from the bottom
		else if (y == 1) {
			if (tools.getTile(x-1, y)*2 == tools.getTile(x, y))
				return tools.tileCanMergeUp(x-1, y);
			else
				return false;
		}
		else		
			return false;
	}

}
