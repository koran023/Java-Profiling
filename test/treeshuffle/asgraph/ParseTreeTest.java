package treeshuffle.asgraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ParseTreeTest {
	
	@Test
	public void parseSingletonTest() {
		String s = "5";
		Integer expectedSize = new Integer(1);
		Integer expectedValue = new Integer(5);
		Tree t = Tree.parseTree(s);
		assertTrue(t.isLeaf());
		assertEquals(expectedSize, t.size());
		assertEquals(expectedValue, t.evaluateExpression());
	}
	
	@Test
	public void parseSmallTreeTest() {
		String s = "+ 5 8";
		int expectedSize = 3;
		int expectedValue = 13;
		constructAndCheckTree(s, expectedSize, expectedValue);
	}
	
	@Test
	public void parseImbalancedTreeTest() {
		String s = "+ * 3 7 5";
		int expectedSize = 5;
		int expectedValue = 26;
		constructAndCheckTree(s, expectedSize, expectedValue);
	}
	
	@Test
	public void parseLargerTreeTest() {
		// ((14 / 5) + 8) * (3 - 6) = (2 + 8) * (3 - 6) = 10*-3 = -30
		String s = "* + / 14 5 8 - 3 6";
		int expectedSize = 9;
		int expectedValue = -30;
		constructAndCheckTree(s, expectedSize, expectedValue);
	}

	private void constructAndCheckTree(String s, Integer expectedSize,
			Integer expectedValue) {
		Tree t = Tree.parseTree(s);
		assertFalse(t.isLeaf());
		assertEquals(expectedSize, t.size());
		assertEquals(expectedValue, t.evaluateExpression());
	}
}
