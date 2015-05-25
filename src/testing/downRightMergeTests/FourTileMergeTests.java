package testing.downRightMergeTests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import tools.AI2048;

@RunWith(Parameterized.class)
public class FourTileMergeTests {

	private static AI2048 ai;
	private boolean canMerge;
	private static int count = 0;
	
	public FourTileMergeTests(int[][] state, boolean canMerge) {
		
		ai = AI2048.getTestingAI(state);
		this.canMerge = canMerge;
	}

	@Parameters
	public static Collection<Object[]> params() {
		
		List<Object[]> states = new ArrayList<Object[]>();
		
		states.addAll(getStatesRow0());
		states.addAll(getStatesRow1());
		states.addAll(getStatesRow2());
		states.addAll(getStatesRow3());
		
		return states;
	}
	
	@Test
	public void testDownRightMerge() {
		
		for (int i = 0; i < 4; i++) {
			if (ai.getTile(3,i) == 16) {
				assertTrue("\nTest " + count + " returned " + !canMerge + "\n" + ai.toString(), ai.tileDownRightMerge(3, i) == canMerge);
				System.out.println("Test " + count + " passed");
				System.out.println("   - returned " + canMerge + "\n" + ai.toString());
			}
		}
		
		count++;
	}

	public static List<int[][]> getDefaultStates() {
		
		List<int[][]> states = new ArrayList<int[][]>();
		
		int[][] state = new int[4][4];
		
		//two tiles do not match, two tiles can merge to match
		state[2][0] = 64;
		state[2][1] = 128;
		state[2][2] = 8;
		state[2][3] = 8;
		states.add(state);

		state = new int[4][4];
		state[2][2] = 64;
		state[2][3] = 128;
		state[2][0] = 8;
		state[2][1] = 8;
		states.add(state);
		
		//two tiles are not a match but can merge
		state = new int[4][4];
		state[2][0] = 64;
		state[2][1] = 64;
		state[2][2] = 128;
		state[2][3] = 16;
		states.add(state);
		
		state = new int[4][4];
		state[2][1] = 64;
		state[2][2] = 64;
		state[2][0] = 128;
		state[2][3] = 16;
		states.add(state);
		
		state = new int[4][4];
		state[2][1] = 64;
		state[2][2] = 64;
		state[2][3] = 128;
		state[2][0] = 16;
		states.add(state);
		
		state = new int[4][4];
		state[2][2] = 64;
		state[2][3] = 64;
		state[2][1] = 128;
		state[2][0] = 16;
		states.add(state);
		
		//three tiles do not match
		state = new int[4][4];
		state[2][0] = 64;
		state[2][1] = 128;
		state[2][2] = 256;
		state[2][3] = 16;
		states.add(state);
		
		state = new int[4][4];
		state[2][1] = 64;
		state[2][2] = 128;
		state[2][3] = 256;
		state[2][0] = 16;
		states.add(state);
		
		//two tiles can merge but do not match
		//two tiles can merge to match
		state = new int[4][4];
		state[2][0] = 64;
		state[2][1] = 64;
		state[2][2] = 8;
		state[2][3] = 8;
		states.add(state);
		
		state = new int[4][4];
		state[2][2] = 64;
		state[2][3] = 64;
		state[2][0] = 8;
		state[2][1] = 8;
		states.add(state);
		
		for (int[][] s : states) {
			s[3][0] = 2;
			s[3][1] = 4;
			s[3][2] = 2;
			s[3][3] = 4;
		}
		
		return states;
	}
	
	public static List<Object[]> getStatesRow0() {
		List<Object[]> params = new ArrayList<Object[]>();
		List<int[][]> states = getDefaultStates();
		
		for (int i = 0; i < states.size(); i++) {
			states.get(i)[3][0] = 16;
			Object[] o = {states.get(i), false};
			params.add(o);
		}
		
		//two tiles do not match, two tiles can merge to match
		params.get(0)[1] = false;
		params.get(1)[1] = false;
				
		//two tiles are not a match but can merge
		params.get(2)[1] = false;
		params.get(3)[1] = false;
		params.get(4)[1] = false;
		params.get(5)[1] = false;
		
		//three tiles do not match
		params.get(6)[1] = false;
		params.get(7)[1] = true;
		
		//two tiles can merge but do not match
		//two tiles can merge to match
		params.get(8)[1] = false;
		params.get(9)[1] = false;
		
		return params;
	}
	
	public static List<Object[]> getStatesRow1() {
		List<Object[]> params = new ArrayList<Object[]>();
		List<int[][]> states = getDefaultStates();
		
		for (int i = 0; i < states.size(); i++) {
			states.get(i)[3][1] = 16;
			Object[] o = {states.get(i), false};
			params.add(o);
		}
		
		//two tiles do not match, two tiles can merge to match
		params.get(0)[1] = false;
		params.get(1)[1] = true;
				
		//two tiles are not a match but can merge
		params.get(2)[1] = false;
		params.get(3)[1] = false;
		params.get(4)[1] = true;
		params.get(5)[1] = true;
		
		//three tiles do not match
		params.get(6)[1] = false;
		params.get(7)[1] = false;
		
		//two tiles can merge but do not match
		//two tiles can merge to match
		params.get(8)[1] = false;
		params.get(9)[1] = false;
				
		return params;
	}
	
	public static List<Object[]> getStatesRow2() {
		List<Object[]> params = new ArrayList<Object[]>();
		List<int[][]> states = getDefaultStates();
		
		for (int i = 0; i < states.size(); i++) {
			states.get(i)[3][2] = 16;
			Object[] o = {states.get(i), false};
			params.add(o);
		}
		
		//two tiles do not match, two tiles can merge to match
		params.get(0)[1] = false;
		params.get(1)[1] = false;
				
		//two tiles are not a match but can merge
		params.get(2)[1] = false;
		params.get(3)[1] = false;
		params.get(4)[1] = false;
		params.get(5)[1] = false;
		
		//three tiles do not match
		params.get(6)[1] = false;
		params.get(7)[1] = false;
		
		//two tiles can merge but do not match
		//two tiles can merge to match
		params.get(8)[1] = false;
		params.get(9)[1] = true;
		
		return params;
	}
	
	public static List<Object[]> getStatesRow3() {
		List<Object[]> params = new ArrayList<Object[]>();
		List<int[][]> states = getDefaultStates();
		
		for (int i = 0; i < states.size(); i++) {
			states.get(i)[3][3] = 16;
			Object[] o = {states.get(i), false};
			params.add(o);
		}
		
		//two tiles do not match, two tiles can merge to match
		params.get(0)[1] = true;
		params.get(1)[1] = false;
				
		//two tiles are not a match but can merge
		params.get(2)[1] = true;
		params.get(3)[1] = true;
		params.get(4)[1] = false;
		params.get(5)[1] = false;
		
		//three tiles do not match
		params.get(6)[1] = true;
		params.get(7)[1] = false;
		
		//two tiles can merge but do not match
		//two tiles can merge to match
		params.get(8)[1] = true;
		params.get(9)[1] = false;
		
		return params;
	}
}
