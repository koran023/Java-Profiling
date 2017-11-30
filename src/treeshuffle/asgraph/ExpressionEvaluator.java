package treeshuffle.asgraph;

public class ExpressionEvaluator implements TreeVisitor<Integer> {

	@Override
	public Integer visitLeaf(Leaf leaf) {
		return leaf.value();
	}

	@Override
	public Integer visitNode(Node node) {
		int leftValue = node.getLeft().acceptVisitor(this);
		int rightValue = node.getRight().acceptVisitor(this);
		char operator = node.getOperator();
		switch (operator) {
			case '+':
				return leftValue + rightValue;
			case '-':
				return leftValue - rightValue;
			case '*':
				return leftValue * rightValue;
			case '/':
				return leftValue / rightValue;
		}
		throw new IllegalArgumentException("Unknown operator: <" + operator + ">");
	}

}
