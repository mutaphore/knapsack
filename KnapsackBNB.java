import java.util.*;

public class KnapsackBNB {

	private int n, C, maxVal, weight;
	private Item[] items;
	private char[] solution;
	private PriorityQueue<Node> pq;	//Priority queue of state-space nodes
	private Node root;

	//Node in the state-space tree
	public class Node implements Comparable<Node> {
		char[] solution;	//Each letter represents if item is taken or not
		int w;				//total weight at the current selection
		int v;				//total value at the current selection
		int i;				//the item number currently at
		float ub;			//Upper bound of node
		
		public int compareTo(Node other) {

			if (this.ub == other.ub)
				return 0;
			else if (this.ub < other.ub)
				return 1;	//Priority given to highest upper bound
			else
				return -1;
		}
	}

	public KnapsackBNB(ArrayList<Item> items, int n, int C) {
		Item[] temp = new Item[n];
		float ratio;
		int i, j;

		this.items = new Item[n+1];
		this.pq = new PriorityQueue<Node>();
		this.n = n;
      	this.C = C;
      	this.maxVal = 0;
      	this.weight = 0;
      
		//Create the sorted array of items (by decreasing order of v/w)
		System.arraycopy(items.toArray(), 0, temp, 0, n);
		Arrays.sort(temp, new RatioComparator());
		System.arraycopy(temp, 0, this.items, 1, n);
		this.items[0] = null;	//Root item is empty

      	//Initialize and add root to priority queue
      	root = new Node();
      	root.w = 0;
      	root.v = 0;
      	root.ub =C * (float)this.items[1].value / (float)this.items[1].weight;
      	root.i = 0;
      	pq.add(root);
	}

	//Calculate bound for node
	private float bound(Node node) {
		float ub = node.v;
		int j, totWeight = node.w;

		if (node.w > C)
			return 0;	//0 upper bound will not be queued
		else {
			//Calculate max # of remaining items that can fit in knapsack
			for (j = node.i + 1; totWeight <= C && j < n + 1; j++) {
				totWeight += items[j].weight;
				ub += items[j].value;
			}
			//Add max possible value from the remainder capacity
			if (j < n + 1)
				ub += (float)(C - totWeight) * ((float)items[j].value / 
		      	 (float)items[j].weight);
			return ub;
		}
	}

	//Add a solution byte at the i'th position
	private char[] addSolution(char[] src, char[] dst, int i) {

		dst = new char[i+1];
		dst[i] = 1;
		if (src != null)
			System.arraycopy(src, 0, dst, 0, src.length);
			
		return dst;
	}

	//Copies a solution to new array and return it
	private char[] copySolution(char[] src) {
		char[] copy = null;

		if (src != null) {
			copy = new char[src.length];
			System.arraycopy(src, 0, copy, 0, src.length);			
		}
		return copy;
	}

	//Branch and bound the state tree to arrive at solution
	public void branchAndBound() {
		Node cur, left, right;
		long startTime, endTime;
		int runTime = 5;	//Runtime limit in minutes

		//Set start and end times
		startTime = endTime = System.currentTimeMillis();

		while (pq.size() > 0 && endTime - startTime < 1000 * 60 * runTime) {
			cur = pq.poll();
			if (cur.ub > maxVal) {
				//Left child includes the item
				left = new Node();
				left.i = cur.i + 1;
				left.w = cur.w + items[left.i].weight;
				left.v = cur.v + items[left.i].value;
				left.solution = addSolution(cur.solution, left.solution, left.i);
				left.ub = bound(left);

				//Add to solution only if greater than max value and fits
				if (left.w <= C && left.v > maxVal) {
					maxVal = left.v;
					weight = left.w;
					solution = left.solution;
				}
				if (left.ub > maxVal)	
					pq.offer(left);

				//Right child does not include item
				right = new Node();
				right.i = cur.i + 1;
				right.w = cur.w;
				right.v = cur.v;
				right.solution = copySolution(cur.solution);
				right.ub = bound(right);
				if (right.ub > maxVal)
					pq.offer(right);
			}
			endTime = System.currentTimeMillis();
		}
	}
	
	//Prints solution in order of increaing index value
	public void printSolution() {
		ArrayList<Item> sorted = new ArrayList<Item>();
		Iterator<Item> itr;

		for (int i = 1; i < solution.length; i++) {
			if (solution[i] == 1)
				sorted.add(items[i]);
		}
		Collections.sort(sorted);
		itr = sorted.iterator();

		System.out.println("Using Branch and Bound the best feasible solution found: " +
		 "value = " + maxVal + ", weight = " + weight);
		while (itr.hasNext())
			System.out.print("<" + itr.next().index + "> ");
      	System.out.println();
	}
}