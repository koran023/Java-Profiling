package treeshuffle.asgraph;

import static org.junit.Assert.*;

import org.junit.Test;


public class GraphSubtreeSwapTest {

	@Test
	public void swapAtRoot() {
		Tree a = Tree.parseTree("+ 3 * 4 5");
		Tree b = Tree.parseTree("/ - 8 9 6");
		Tree expectedChild = Tree.parseTree("/ - 8 9 6");
		Tree child = a.swapWith(b, 0);
		assertEquals(expectedChild, child);
	}

}
