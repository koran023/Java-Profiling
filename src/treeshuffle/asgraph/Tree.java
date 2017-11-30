package treeshuffle.asgraph;

import java.util.HashMap;
import java.util.Map;

public abstract class Tree {
	
	private static final boolean USE_CACHE = true;

	public int totalTrees = 0;
	public Tree() {
		++totalTrees;
	}
	
	private static RandomTreeFactory randomTreeFactory = new RandomTreeFactory();

	private Map<TreeVisitor<?>, Object> cache = new HashMap<TreeVisitor<?>, Object>();
	private SizeVisitor sizeVisitor = new SizeVisitor();
	private ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
	
	public static Tree parseTree(String s) {
		TreeParser parser = new TreeParser(s);
		return parser.parseTree();
	}
	
	public static Tree randomTree() {
		return randomTreeFactory.buildRandomTree();
	}

	public abstract boolean isLeaf();
	
	public Integer size() {
		return acceptVisitor(sizeVisitor);
	}
	
	public Integer evaluateExpression() {
		return acceptVisitor(expressionEvaluator);
	}

	public Tree swapWith(Tree other, int swapPosition) {
		return other;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T acceptVisitor(TreeVisitor<T> visitor) {
		if (USE_CACHE) {
			T result = (T) cache.get(visitor);
			if (result == null) {
				result = doAcceptVisitor(visitor);
				cache.put(visitor, result);
			}
			return result;
		} else {
			return doAcceptVisitor(visitor);
		}
	}

	public abstract <T> T doAcceptVisitor(TreeVisitor<T> visitor);

}
