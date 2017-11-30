package treeshuffle.asgraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

	private static final double ONE_MILLION = 1000000.0;
	private static final double ONE_BILLION = 1000000000.0;
	private static final int NUM_ADDITIONAL_SIZE_LOOPS = 25;

	public static void main(String[] args) {
		int numTrees = 20000;
		if (args.length > 0) {
			numTrees = Integer.parseInt(args[0]);
		}
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Hit return when you're ready to start the program.");
		scanner.nextLine();
		
		long startTime = System.nanoTime();
		
		printHeapSize();
		
		Tree[] trees = new Tree[numTrees];
		for (int i=0; i<numTrees; ++i) {
			trees[i] = Tree.randomTree();
		}

		printHeapSize();
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		System.out.println("Construction time = " + duration / ONE_BILLION + " seconds");
		startTime = endTime;

		int totalSize = 0;
		List<Integer> sizes = new ArrayList<Integer>();
		for (Tree tree : trees) {
			Integer size = tree.size();
			totalSize += size;
			sizes.add(size);
		}

		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("Size time = " + duration / ONE_BILLION + " seconds");
		startTime = endTime;

		System.out.println("The average tree size was " + totalSize * 1.0 / numTrees);
		Collections.sort(sizes);
		System.out.println("The minimum tree size was " + sizes.get(0));
		System.out.println("The 25th percentile for size was " + sizes.get(numTrees/4));
		System.out.println("The median size was " + sizes.get(numTrees/2));
		System.out.println("The 75th percentile for size was " + sizes.get(3*numTrees/4));
		System.out.println("The maximum tree size was " + sizes.get(numTrees-1));
		
		printHeapSize();
		
		for (int i=0; i<NUM_ADDITIONAL_SIZE_LOOPS; ++i) {
			totalSize = 0;
			for (Tree tree : trees) {
				Integer size = tree.size();
				totalSize += size;
			}			
		}

		endTime = System.nanoTime();
		duration = endTime - startTime;
		System.out.println("Additional size loops time = " + duration / ONE_BILLION + " seconds");
		startTime = endTime;

		printHeapSize();
}

	private static void printHeapSize() {
		long heapSize = Runtime.getRuntime().totalMemory();
	    System.out.println("Heap Size= " + heapSize / ONE_MILLION + " Mb");	
	}

}
