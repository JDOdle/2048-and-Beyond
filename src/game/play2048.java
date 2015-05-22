package game;

import tools.AI2048;

public class play2048 {

	public static void main(String[] args) {
		
		AI2048 ai = new AI2048();
		
		if (!ai.startGame()) return;
  		
  		while (!ai.gameOver())
  			ai.makeMove(3);
  		
  		ai.quitGame();
	}

}
