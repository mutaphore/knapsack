import java.util.*;

public class KnapsackGreedy {

	private int n, C, maxVal, weight;
	private ArrayList<Item> items;
	private ArrayList<Item> solution;

	public KnapsackGreedy(ArrayList<Item> items, int n, int C) {
		double ratio;
		int i, j;

		this.items = new ArrayList<Item>();
		this.n = n;
      	this.C = C;
      	this.weight = 0;
      	this.maxVal = 0;

      	//Create the sorted array of items (by decreasing order of v/w)
      	for (i = 0; i < n; i++) {
      		ratio = items.get(i).value / items.get(i).weight;
      		for (j = 0; j < this.items.size(); j++) {
      			if (ratio > this.items.get(j).value / this.items.get(j).weight)
      				break;
      		}
      		this.items.add(j, items.get(i));
      	}
	}

	//Build the solution by selecting the next item that fit
	public void construct() {
		int i, j, index;

		solution = new ArrayList<Item>();

		for (i = 0; i < n; i++) {
			if (weight + items.get(i).weight <= C) {
				weight += items.get(i).weight;
				maxVal += items.get(i).value;
				index = items.get(i).index;
				//Insert item into solution so index is in ascending order
				for (j = 0; j < solution.size(); j++) {
					if (index < solution.get(j).index)
						break;
				}
				solution.add(j, items.get(i));
			}
		}
	}

	public void printSolution() {
		Iterator<Item> itr = solution.iterator();

		System.out.println("Greedy solution (not necessarily optimal): " +
		 "value = " + maxVal + ", weight = " + weight);
		
		while (itr.hasNext())
			System.out.print("<" + itr.next().index + "> ");
      	System.out.println();
	}

}