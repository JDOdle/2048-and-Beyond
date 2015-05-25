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
public class OneTileMergeTests {

	private static AI2048 ai;
	private boolean canMerge;
	private static int count = 0;
	
	public OneTileMergeTests(int[][] state, boolean canMerge) {
		
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
		
		for (int i = 0; i < 4; i++) {
			if (ai.getTile(3,i) == 16) {
				assertTrue("\nTest " + count + " returned " + !canMerge + "\n" + ai.toString(), ai.tileUpRightMerge(3, i) == canMerge);
				System.out.println("Test " + count + " passed");
				System.out.println("   - returned " + canMerge + "\n" + ai.toString());
			}
		}
		
		count++;
	}

	public static List<int[][]> getDefaultStates() {
		
		List<int[][]> states = new ArrayList<int[][]>();
		
		int[][] state = new int[4][4];
		
		state[2][0] = 16;
		states.add(state);
		
		state = new int[4][4];
		state[2][1] = 16;
		states.add(state);
		
		state = new int[4][4];
		state[2][2] = 16;
		states.add(state);
		
		state = new int[4][4];
		state[2][3] = 16;
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
			Object[] o = {states.get(i), true};
			params.add(o);
		}
		
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







