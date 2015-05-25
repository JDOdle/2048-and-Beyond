package testing.upRightMergeTests;

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
public class ThreeTileMergeTests {

	private static AI2048 ai;
	private boolean canMerge;
	private static int count = 0;
	
	public ThreeTileMergeTests(int[][] state, boolean canMerge) {
		
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
	public void testUpRightMerge() {
		count++;
		for (int i = 0; i < 4; i++) {
			if (ai.getTile(3,i) == 16) {
				assertTrue("\nTest " + count + " returned " + !canMerge + "\n" + ai.toString(), ai.tileUpRightMerge(3, i) == canMerge);
				System.out.println("Test " + count + " passed");
				System.out.println("   - returned " + canMerge + "\n" + ai.toString());
			}
		}
	}

	public static List<int[][]> getDefaultStates() {
		
		List<int[][]> states = new ArrayList<int[][]>();
		
		int[][] state = new int[4][4];
		
		//top two tiles are not a match
		state[2][0] = 64;
		state[2][1] = 128;
		state[2][2] = 16;
		states.add(state);
		
		state = new int[4][4];
		state[2][0] = 64;
		state[2][1] = 128;
		state[2][3] = 16;
		states.add(state);
		
		//middle two tiles are not a match
		state = new int[4][4];
		state[2][1] = 64;
		state[2][2] = 128;
		state[2][0] = 16;
		states.add(state);
		
		state = new int[4][4];
		state[2][1] = 64;
		state[2][2] = 128;
		state[2][3] = 16;
		states.add(state);
		
		//bottom two tiles are not a match
		state = new int[4][4];
		state[2][2] = 64;
		state[2][3] = 128;
		state[2][0] = 16;
		states.add(state);
		
		state = new int[4][4];
		state[2][2] = 64;
		state[2][3] = 128;
		state[2][1] = 16;
		states.add(state);
		
		//top two tiles are not a match but can merge
		state = new int[4][4];
		state[2][0] = 64;
		state[2][1] = 64;
		state[2][2] = 16;
		states.add(state);
		
		state = new int[4][4];
		state[2][0] = 64;
		state[2][1] = 64;
		state[2][3] = 16;
		states.add(state);
		
		//middle two tiles are not a match but can merge
		state = new int[4][4];
		state[2][1] = 64;
		state[2][2] = 64;
		state[2][0] = 16;
		states.add(state);
		
		state = new int[4][4];
		state[2][1] = 64;
		state[2][2] = 64;
		state[2][3] = 16;
		states.add(state);
		
		//bottom two tiles are not a match but can merge
		state = new int[4][4];
		state[2][2] = 64;
		state[2][3] = 64;
		state[2][0] = 16;
		states.add(state);
		
		state = new int[4][4];
		state[2][2] = 64;
		state[2][3] = 64;
		state[2][1] = 16;
		states.add(state);
		
		//all three tiles are the same
		state = new int[4][4];
		state[2][0] = 16;
		state[2][1] = 16;
		state[2][2] = 16;
		states.add(state);
		
		state = new int[4][4];
		state[2][1] = 16;
		state[2][2] = 16;
		state[2][3] = 16;
		states.add(state);
		
		//top two tiles can merge to make a match
		state = new int[4][4];
		state[2][0] = 8;
		state[2][1] = 8;
		state[2][2] = 64;
		states.add(state);
		
		state = new int[4][4];
		state[2][0] = 8;
		state[2][1] = 8;
		state[2][3] = 64;
		states.add(state);
		
		//middle two tiles can merge to make a match
		state = new int[4][4];
		state[2][1] = 8;
		state[2][2] = 8;
		state[2][0] = 64;
		states.add(state);
		
		state = new int[4][4];
		state[2][1] = 8;
		state[2][2] = 8;
		state[2][3] = 64;
		states.add(state);
		
		//bottom two tiles can merge to make a match
		state = new int[4][4];
		state[2][2] = 8;
		state[2][3] = 8;
		state[2][0] = 64;
		states.add(state);
		
		state = new int[4][4];
		state[2][2] = 8;
		state[2][3] = 8;
		state[2][1] = 64;
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
		
		//top two tiles are not a match
		params.get(0)[1] = false;
		params.get(1)[1] = false;
		
		//middle two tiles are not a match
		params.get(2)[1] = true;
		params.get(3)[1] = false;
		
		//bottom two tiles are not a match
		params.get(4)[1] = true;
		params.get(5)[1] = true;
		
		//top two tiles are not a match but can merge
		params.get(6)[1] = false;
		params.get(7)[1] = false;
		
		//middle two tiles are not a match but can merge
		params.get(8)[1] = true;
		params.get(9)[1] = false;
		
		//bottom two tiles are not a match but can merge
		params.get(10)[1] = true;
		params.get(11)[1] = true;
		
		//all three tiles are the same
		params.get(12)[1] = false;
		params.get(13)[1] = false;
		
		//top two tiles can merge to make a match
		params.get(14)[1] = true;
		params.get(15)[1] = true;
		
		//middle two tiles can merge to make a match
		params.get(16)[1] = false;
		params.get(17)[1] = true;
		
		//bottom two tiles can merge to make a match
		params.get(18)[1] = false;
		params.get(19)[1] = false;
		
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
		
		//top two tiles are not a match
		params.get(0)[1] = false;
		params.get(1)[1] = false;
		
		//middle two tiles are not a match
		params.get(2)[1] = false;
		params.get(3)[1] = false;
		
		//bottom two tiles are not a match
		params.get(4)[1] = false;
		params.get(5)[1] = false;
		
		//top two tiles are not a match but can merge
		params.get(6)[1] = true;
		params.get(7)[1] = true;
		
		//middle two tiles are not a match but can merge
		params.get(8)[1] = false;
		params.get(9)[1] = true;
		
		//bottom two tiles are not a match but can merge
		params.get(10)[1] = false;
		params.get(11)[1] = false;
		
		//all three tiles are the same
		params.get(12)[1] = true;
		params.get(13)[1] = true;
		
		//top two tiles can merge to make a match
		params.get(14)[1] = false;
		params.get(15)[1] = false;
		
		//middle two tiles can merge to make a match
		params.get(16)[1] = true;
		params.get(17)[1] = false;
		
		//bottom two tiles can merge to make a match
		params.get(18)[1] = true;
		params.get(19)[1] = true;
		
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
		
		//top two tiles are not a match
		params.get(0)[1] = true;
		params.get(1)[1] = true;
		
		//middle two tiles are not a match
		params.get(2)[1] = false;
		params.get(3)[1] = true;
		
		//bottom two tiles are not a match
		params.get(4)[1] = false;
		params.get(5)[1] = false;
		
		//top two tiles are not a match but can merge
		params.get(6)[1] = false;
		params.get(7)[1] = false;
		
		//middle two tiles are not a match but can merge
		params.get(8)[1] = false;
		params.get(9)[1] = false;
		
		//bottom two tiles are not a match but can merge
		params.get(10)[1] = false;
		params.get(11)[1] = false;
		
		//all three tiles are the same
		params.get(12)[1] = false;
		params.get(13)[1] = false;
		
		//top two tiles can merge to make a match
		params.get(14)[1] = false;
		params.get(15)[1] = false;
		
		//middle two tiles can merge to make a match
		params.get(16)[1] = false;
		params.get(17)[1] = false;
		
		//bottom two tiles can merge to make a match
		params.get(18)[1] = false;
		params.get(19)[1] = false;
		
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
		
		return params;
	}
}






