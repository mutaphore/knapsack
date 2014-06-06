import java.util.*;

public class KnapsackBNB {

	private int n, C, maxVal, weight;
	private ArrayList<Item> items;
	private char[] solution;
	private PriorityQueue<Node> pq;	//Priority queue of state-space nodes
	private Node root;

	//Node in the state-space tree
	public class Node implements Comparable<Node> {
		char[] solution;	//Partial or complete solution at node
		int w;	//total weight at the current selection
		int v;	//total value at the current selection
		int i;	//The item number currently at
		float ub;	//Upper bound calculated from v + (W − w)(v_i+1/w_i+1)
		
		//Priority given to highest upper bound
		public int compareTo(Node other) {

			if (this.ub == other.ub)
				return 0;
			else if (this.ub < other.ub)
				return 1;
			else
				return -1;
		}
	}

	public KnapsackBNB(ArrayList<Item> items, int n, int C) {
		float ratio;
		int i, j;

		this.items = new ArrayList<Item>();
		this.pq = new PriorityQueue<Node>();
		this.n = n;
      	this.C = C;
      	this.maxVal = 0;
      	this.weight = 0;
      
		//Create the sorted array of items (by decreasing order of v/w)
		this.items.add(0, null);
      	for (i = 0; i < n; i++) {
      		ratio = (float)items.get(i).value / (float)items.get(i).weight;
      		for (j = 1; j < this.items.size(); j++) {
      			if (ratio > (float)this.items.get(j).value / 
      			 (float)this.items.get(j).weight)
      				break;
      		}
      		this.items.add(j, items.get(i));
      	}
      	//Initialize and add root to priority queue
      	root = new Node();
      	root.w = 0;
      	root.v = 0;
      	root.ub =C * (float)items.get(0).value / (float)items.get(0).weight;
      	root.i = 0;
      	pq.add(root);
	}

	//Calculate bound for node
	private float bound(Node node) {
		float ub = node.v;

		if (node.w > C)
			return 0;
		else if (node.i + 1 <= n)
			ub = (float)node.v + (float)(C - node.w) * 
		     ((float)items.get(node.i+1).value / 
		      (float)items.get(node.i+1).weight);

		return ub;
	}

	public void branchAndBound() {
		Node cur, left, right;

		while (pq.size() > 0) {
			cur = pq.poll();
			if (cur.ub > maxVal) {
				//Left child includes the item
				left = new Node();
				left.solution = new char[n+1];
				if (cur.solution != null)
					System.arraycopy(cur.solution, 0, left.solution, 0, n + 1);
				left.i = cur.i + 1;
				left.w = cur.w + items.get(left.i).weight;
				left.v = cur.v + items.get(left.i).value;
				left.solution[left.i] = 1;
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
				right.solution = new char[n+1];
				if (cur.solution != null)
					System.arraycopy(cur.solution, 0, right.solution, 0, n + 1);
				right.i = cur.i + 1;
				right.w = cur.w;
				right.v = cur.v;
				right.ub = bound(right);
					
				if (right.ub > maxVal)
					pq.offer(right);
			}
			//System.gc();
		}
	}
	
	//Prints solution
	public void printSolution() {
		ArrayList<Item> sorted = new ArrayList<Item>();
		Iterator<Item> itr;

		for (int i = 1; i < n + 1; i++) {
			if (solution[i] == 1)
				sorted.add(items.get(i));
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