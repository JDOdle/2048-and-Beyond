package algorithm;

import tools.GameTools;

public class AI2048 {
	
	private static GameTools tools = new GameTools();
	
	public static void main(String args[]) {
		  		
  		if (!tools.open2048()) return;
  		
  		if (tools.getTile(3, 0) == 0) {
			tools.pushUp();
			tools.pushRight();
  		}
  		
  		while (!tools.gameOver())
  			makeMove(3);
  		
  		tools.driver.quit();
	}
	
	public static void makeMove(int col) {
		
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
		else if (twoMoveMerge(col) && tools.isColumnFull(col)) {
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
	private static void executeMoveHierarchy(int col) {
		int[] recTile = tools.recoveryMode(col);
		int recTileVal;
		int leftVal = 0;
		
		//if the tiles are not ordered properly then the move priority changes
		if (recTile[0] != -1) {
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
								tools.pushDown();
							}
						}
					}
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
	 * to merge with the column to its left after pushing down
	 * @param col
	 * @return
	 */
	private static boolean twoMoveMerge(int col) {
		
		//case 1: left column has 4 tiles
		if (tools.isColumnFull(col-1)) {
			//case 1: bottom two tiles can merge
			if (tools.tileCanMergeUp(col-1, 3)) {
				//case 1: top two tiles can merge
				if (tools.tileCanMergeUp(col-1, 1)) {
					if (tools.getTile(col-1, 0)*2 == tools.getTile(col, 2)) return true;
					if (tools.getTile(col-1, 2)*2 == tools.getTile(col, 3)) return true;
					return false;
				}
				//case 2: top two tiles can not merge
				else {
					if (tools.getTile(col-1, 0) == tools.getTile(col, 1)) return true;
					if (tools.getTile(col-1, 1) == tools.getTile(col, 2)) return true;
					if (tools.getTile(col-1, 2)*2 == tools.getTile(col, 3)) return true;
					return false;
				}
			}
			//case 2: middle two tiles can merge
			else if (tools.tileCanMergeUp(col-1, 2)) {
				if (tools.getTile(col-1, 0) == tools.getTile(col, 1)) return true;
				if (tools.getTile(col-1, 1)*2 == tools.getTile(col, 2)) return true;
				return false;
			}
			//case 3: top two tiles can merge
			else if (tools.tileCanMergeUp(col-1, 1)) {
				return (tools.getTile(col-1, 0)*2 == tools.getTile(col, 1));
			}
			else
				return false;
		}
		//case 2: left column has 3 tiles
		if (tools.columnFilled(col-1) == 3) {
			//case 1: top column is empty
			if (tools.getTile(col-1, 0) == 0) {
				//case 1: top two tiles can merge
				if (tools.tileCanMergeDown(col-1, 1)) {
					if (tools.getTile(col-1, 1)*2 == tools.getTile(col, 2))
						return true;
					return false;
				}
				//case 2: bottom two tiles can merge
				else if (tools.tileCanMergeDown(col-1, 2)) {
					if (tools.getTile(col-1, 2)*2 == tools.getTile(col, 3))
						return true;
					return false;
				}
				//case 3: no tiles can merge
				else
					return false;
			}
			//case 2: top column is not empty
			else {
				//case 1: top two tiles can merge
				if (tools.tileCanMergeDown(col-1, 0)) {
					if (tools.getTile(col-1, 0)*2 == tools.getTile(col, 2)) return true;
					if (tools.getTile(col-1, 2) == tools.getTile(col, 3)) return true;
					return false;
				}
				//case 2: bottom two tiles can merge
				else if (tools.tileCanMergeDown(col-1, 1)) {
					if (tools.getTile(col-1, 0) == tools.getTile(col, 2)) return true;
					if (tools.getTile(col-1, 1)*2 == tools.getTile(col, 3)) return true;
					return false;
				}
				//case 3: no tiles can merge
				else {
					if (tools.getTile(col-1, 0) == tools.getTile(col, 1)) return true;
					if (tools.getTile(col-1, 1) == tools.getTile(col, 2)) return true;
					if (tools.getTile(col-1, 2) == tools.getTile(col, 3)) return true;
					return false;
				}
			}
		}
		//case 3: left column has 2 tiles
		else if (tools.columnFilled(col-1) == 2) {
			
			//case 1: the tiles can merge
			if (tools.getColumnMax(col-1) == tools.getColumnMin(col-1)) {
				return (tools.getColumnMax(col-1)*2 == tools.getTile(col, 3));
			}
			//case 2: the tiles can not merge and top tile is empty
			else if (tools.getTile(col-1, 0) == 0){
				if (tools.getTile(col-1, 1) == tools.getTile(col, 2))
					return true;
				else
					return tools.getTile(col-1, 2) == tools.getTile(col, 3);
			}
			//case 3: the tiles can not merge and top tile is not empty
			else {
				if (tools.getTile(col-1, 0) == tools.getTile(col, 2))
					return true;
				else
					return tools.getTile(col-1, 1) == tools.getTile(col, 3);
			}
		}
		//case 4: left column has 1 tile
		else if (tools.columnFilled(col-1) == 1) {
			return (tools.getColumnMax(col-1) == tools.getTile(col, 3));
		}
		
		else 
			return false;
	}
}
