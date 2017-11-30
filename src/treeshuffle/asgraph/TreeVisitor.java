package treeshuffle.asgraph;

public interface TreeVisitor<T> {

	T visitLeaf(Leaf leaf);

	T visitNode(Node node);

}
