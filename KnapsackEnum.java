import java.util.*;
import java.math.*;

public class KnapsackEnum {

	private int n, C, maxVal, weight;
	private ArrayList<Item> items;
	private ArrayList<Item> solution;
	private ArrayList<ArrayList<Item>> candidates;

	public KnapsackEnum(ArrayList<Item> items, int n, int C) {
		this.items = items;
		this.n = n;
      this.C = C;		
		this.candidates = new ArrayList<ArrayList<Item>>((int)Math.pow(2, n));
		this.maxVal = 0;
	}

	//Generate all possible candidate solutions
	public void generate() {

   	for (int i = 0; i < Math.pow(2, n); i++) {
   		candidates.add(i, new ArrayList<Item>());
   		for (int bit = 0; bit < n; bit++) {
   			if ((i & 1 << bit) > 0)
   				candidates.get(i).add(items.get(bit));
   		}
   	}
	}

	//Find the best feasible solution by searching through all candidates
	public void search() {
		int maxNdx = 0, totVal, totWeight;

		for (int i = 0; i < Math.pow(2, n); i++) {
			totWeight = 0;
			totVal = 0;
			for (int j = 0; j < candidates.get(i).size(); j++) {
				totWeight += candidates.get(i).get(j).weight;
				totVal += candidates.get(i).get(j).value;
			}
			if (totWeight <= C && totVal > maxVal) {
				maxVal = totVal;
				weight = totWeight;
				maxNdx = i;
			}
		}
		solution = candidates.get(maxNdx);
	}

	//Print items in the solution set
	public void printSolution() {
		Iterator<Item> itr = solution.iterator();

		System.out.println("Using Brute force the best feasible solution found: " +
		 "value = " + maxVal + ", weight = " + weight);
		
		while (itr.hasNext())
			System.out.print("<" + itr.next().index + "> ");
      System.out.println();
	}

}