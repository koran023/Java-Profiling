package treeshuffle.asgraph;

public class TreeParser {

	private String[] parts;
	private int cursor;

	public TreeParser(String s) {
		parts = s.split(" ");
	}

	public Tree parseTree() {
		cursor = 0;
		return doParseTree();
	}
	
	private Tree doParseTree() {
		String element = parts[cursor];
		if (Character.isDigit(element.charAt(0))) {
			++cursor;
			return new Leaf(Integer.parseInt(element));
		} else {
			char operator = element.charAt(0);
			++cursor;
			Tree left = doParseTree();
			Tree right = doParseTree();
			Node node = new Node(operator, left, right);
			return node;
		}
	}

}
