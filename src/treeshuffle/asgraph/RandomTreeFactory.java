package treeshuffle.asgraph;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RandomTreeFactory {

	private static final boolean USE_CACHE = true;
	private Map<Tree, Tree> trees = new HashMap<Tree, Tree>();

	private static final double LEAF_PROBABILITY = 0.1;
	private static final int MAX_DEPTH = 8;
	private Random rand = new Random();
	private static final int NUM_LEAVES = 4;

	private char[] operators = { '+', '-', '*', '/' };

	public Tree buildRandomTree() {
		return doBuildRandomTree(MAX_DEPTH);
	}

	private Tree doBuildRandomTree(int maxDepth) {
		Tree result;
		if (maxDepth == 0 || rand.nextDouble() < LEAF_PROBABILITY) {
			result = new Leaf(rand.nextInt(NUM_LEAVES));
		} else {
			char operator = randomOperator();
			Tree left = doBuildRandomTree(maxDepth-1);
			Tree right = doBuildRandomTree(maxDepth-1);
			result = new Node(operator, left, right);
		}
		if (USE_CACHE) {
			/*
			 * The following four lines cache constructed (sub)trees so that
			 * there are never two different copies in memory of the same
			 * tree, i.e., two trees in member with the same nodes.
			 */
			if (trees.get(result) == null) {
				trees.put(result, result);
			}
			return trees.get(result);
		} else {
			return result;
		}
	}

	private char randomOperator() {
		int index = rand.nextInt(operators .length);
		return operators[index];
	}

}
