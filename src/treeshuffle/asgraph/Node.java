package treeshuffle.asgraph;

public class Node extends Tree {

	private Tree left;
	private Tree right;
	private char operator;

	public Node(char operator, Tree left, Tree right) {
		this.operator = operator;
		this.left = left;
		this.right = right;
	}
	
	public char getOperator() {
		return operator;
	}

	public Tree getLeft() {
		return left;
	}
	
	public Tree getRight() {
		return right;
	}

	public boolean isLeaf() {
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Node)) {
			return false;
		}
		Node other = (Node) o;
		return operator == other.operator && left.equals(other.left) && right.equals(other.right);
	}

	@Override
	public int hashCode() {
		 return operator + left.hashCode() + right.hashCode();
	}

	@Override
	public <T> T doAcceptVisitor(TreeVisitor<T> visitor) {
		return visitor.visitNode(this);
	}

}
