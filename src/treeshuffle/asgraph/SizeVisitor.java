package treeshuffle.asgraph;

public class SizeVisitor implements TreeVisitor<Integer> {

	@Override
	public Integer visitLeaf(Leaf leaf) {
		return 1;
	}

	@Override
	public Integer visitNode(Node node) {
		return 1 + node.getLeft().acceptVisitor(this) + node.getRight().acceptVisitor(this);
	}

}
