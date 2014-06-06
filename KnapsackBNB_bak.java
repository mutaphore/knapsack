import java.util.*;

public class KnapsackBNB {

	private int n, C, maxVal, weight;
	private ArrayList<Item> items;
	private ArrayList<Item> solution;
	private PriorityQueue<Node> pq;	//Priority queue of state-space nodes
	private Node root;

	//Node in the state-space tree
	public class Node implements Comparable<Node>{
		ArrayList<Item> solution;	//Partial or complete solution at node
		int w;	//total weight at the current selection
		int v;	//total value at the current selection
		int i;	//The item number currently at
		double ub;	//Upper bound calculated from v + (W − w)(v_i+1/w_i+1)
		
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
		double ratio;
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
      		ratio = (double)items.get(i).value / (double)items.get(i).weight;
      		for (j = 1; j < this.items.size(); j++) {
      			if (ratio > (double)this.items.get(j).value / 
      			 (double)this.items.get(j).weight)
      				break;
      		}
      		this.items.add(j, items.get(i));
      	}
      	//Initialize root
      	root = new Node();
      	root.w = 0;
      	root.v = 0;
      	root.ub =C * (double)items.get(0).value / (double)items.get(0).weight;
      	root.i = 0;
	}

	//Insert item into solution so index is in ascending order
	private void addToSolution(ArrayList<Item> solution, int i) {
		int j, index = items.get(i).index;

		for (j = 0; j < solution.size(); j++) {
			if (index < solution.get(j).index)
				break;
		}
		solution.add(j, items.get(i));	
	}


	private double bound(Node node) {
		double ub = node.v;

		if (node.w > C)
			return 0;
		else if (node.i + 1 <= n)
			ub = (double)node.v + (double)(C - node.w) * 
		     ((double)items.get(node.i+1).value / 
		      (double)items.get(node.i+1).weight);

		return ub;
	}

	public void branchAndBound() {
		Node cur, left, right;

		pq.add(root);
		solution = new ArrayList<Item>();

		while (pq.size() > 0) {
			cur = pq.poll();
			if (cur.ub > maxVal) {
				//Left child includes the item
				left = new Node();
				if (cur.solution != null)
					left.solution = new ArrayList<Item>(cur.solution);
				else
					left.solution = new ArrayList<Item>();
				left.i = cur.i + 1;
				left.w = cur.w + items.get(left.i).weight;
				left.v = cur.v + items.get(left.i).value;
				addToSolution(left.solution, left.i);
				left.ub = bound(left);

				//Add to solution only if greater than max value and fits
				if (left.w <= C && left.v > maxVal) {
					maxVal = left.v;
					weight = left.w;
					solution = left.solution;
				}
				if (left.ub > maxVal)
					pq.add(left);

				//Right child does not include item
				right = new Node();
				if (cur.solution != null)
					right.solution = new ArrayList<Item>(cur.solution);
				else
					right.solution = new ArrayList<Item>();

				right.i = cur.i + 1;
				right.w = cur.w;
				right.v = cur.v;
				right.ub = bound(right);
					
				if (right.ub > maxVal)
					pq.add(right);
			}
		}
	}
	
	public void printSolution() {
		Iterator<Item> itr = solution.iterator();

		System.out.println("Using Branch and Bound the best feasible solution found: " +
		 "value = " + maxVal + ", weight = " + weight);
		
		while (itr.hasNext())
			System.out.print("<" + itr.next().index + "> ");
      	System.out.println();
	}
}