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
		int leftVal = 0;
		
		if ( (tools.isColumnCompressable(col) &&
				(tools.getColumnMax(col-1) > tools.getColumnMin(col))) ||
							(tools.getTile(col, 0) == 0) ){
					tools.pushUp();
		}
		 
		//check for possible merges
		else if (tools.isColumnFull(col) &&
				!tools.isColumnCompressable(col) &&
				!tools.columnCanMergeLeft(col)) {
			
			if ( (tools.columnFilled(col-1) == 3) &&
					( (tools.getTile(col, 1) == tools.getTile(col-1,0)) ||
					  (tools.getTile(col, 2) == tools.getTile(col-1, 1)) || 
					  (tools.getTile(col, 3) == tools.getTile(col-1, 2)) ) &&
					  (!tools.isColumnCompressable(col-1)) ) {
					tools.pushDown();
					tools.pushRight();
			}
			if ( (tools.columnFilled(col-1) == 2) &&
					( (tools.getTile(col, 2) == tools.getTile(col-1,0)) ||
					  (tools.getTile(col, 3) == tools.getTile(col-1,1)) ) &&
					  (!tools.isColumnCompressable(col-1))) {
					tools.pushDown();
					tools.pushRight();
					tools.pushUp();
			}
			if ( (tools.columnFilled(col-1) == 1) &&
					(tools.getTile(col, 3) == tools.getTile(col-1,0)) &&
						(!tools.isColumnCompressable(col-1)) ) {
					tools.pushDown();
					tools.pushRight();
					tools.pushUp();
			}
			//recursive Case
			else {
				if (tools.recoveryMode(col-1)[0] != -1)
					makeMove(col-1);
				else if (recTile[0] != -1)
					executeMoveHierarchy(col);
				else
					makeMove(col-1);
				return;
			}
		}
		else {
			//if hierarchy is invalid
			if (recTile[0] != -1) {
				executeMoveHierarchy(col);
			}
			//if hierarchy is valid
			else {
				//if column is not full
				if (!tools.isColumnFull(col)) {
					//if column can not merge to right
					if (!tools.columnCanMergeLeft(col))
						executeMoveHierarchy(col);
					else // if column can merge to right
						tools.pushRight();
				}
				//if column can merge to right
				else if (tools.columnCanMergeLeft(col))
					tools.pushRight();
				//if can not push down
				else if (!tools.pushDown()) {
					//if can not push right
					if (!tools.pushRight()) {
						//if can not push up
						if (!tools.pushUp()) {
							tools.pushLeft(); //last resort push
							tools.pushRight(); //attempt to recover from pushing left
						}
					}
				}
				//if can push down and result allows for merging right
				else if (tools.columnCanMergeLeft(col))
					tools.pushRight();
				else //if can push down but tiles can not merge right
					tools.pushUp();
			}
		}
	}
	
	public static void executeMoveHierarchy(int col) {
		int[] recTile = tools.recoveryMode(col);
		int recTileVal = recTile[0];
		int leftVal = 0;
		
		if (recTile[0] != -1) {
			
			leftVal = tools.getTile(recTile[0]-1, recTile[1]);
			
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
					if (tools.isColumnCompressable(col-1) ||
							tools.columnFilled(col-1) <= 2) {
						tools.pushDown();
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
}
