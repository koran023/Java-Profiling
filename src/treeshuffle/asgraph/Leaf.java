package treeshuffle.asgraph;

public class Leaf extends Tree {

	private int value;

	public Leaf(int value) {
		this.value = value;
	}
	
	@Override
	public boolean isLeaf() {
		return true;
	}
	
	public int value() {
		return value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Leaf)) {
			return false;
		}
		Leaf other = (Leaf) o;
		return value == other.value;
	}
	
	@Override
	public int hashCode() {
		return value;
	}

	@Override
	public <T> T doAcceptVisitor(TreeVisitor<T> visitor) {
		return visitor.visitLeaf(this);
	}

}
