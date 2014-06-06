import java.util.*;
import java.math.*;

public class KnapsackDynamic {

	private int n, C, maxVal, weight;
	private ArrayList<Item> items;
	private ArrayList<Item> solution;
	private int[][] table;

	public KnapsackDynamic(ArrayList<Item> items, int n, int C) {
		this.items = items;
		this.n = n;
      	this.C = C;
      	this.maxVal = 0;
      	this.weight = 0;
      	this.table = new int[n+1][C+1];
	}

	//Create the dynamic programming table
	public void createTable() {
		int i, j;
		Item item;

		//Initialize boundary values to 0
		for (i = 0; i < n + 1; i++)
			table[i][0] = 0;
		for (j = 0; j < C + 1; j++)
			table[0][j] = 0;

		//Construct table values
		for (i = 1; i < n + 1; i++) {
			for (j = 1; j < C + 1; j++) {
				item = items.get(i - 1);
				if (j - item.weight >= 0)
					table[i][j] = Math.max(table[i-1][j], 
					 item.value + table[i-1][j-item.weight]);
				else	
					table[i][j] = table[i-1][j];
			}
			
		}

	}

	//Backtrack through table values to get solution
	public void backtrack() {
		int i = n, j = C, k;
		Item item;

		solution = new ArrayList<Item>();

		while (i != 0 && j != 0) {
			if (table[i][j] == table[i-1][j])
				i = i - 1;	//Backtrack
			else {
				//Insert item into solution so index is in ascending order
				item = items.get(i - 1);
				for (k = 0; k < solution.size(); k++) {
					if (item.index < solution.get(k).index)
						break;
				}
				solution.add(k, item);
				maxVal += item.value;
				weight += item.weight;

				//Backtrack
				j = j - item.weight;
				i = i - 1;
			}
		}
	}

	public void printSolution() {
		Iterator<Item> itr = solution.iterator();

		System.out.println("Dynamic Programming solution: " +
		 "value = " + maxVal + ", weight = " + weight);
		
		while (itr.hasNext())
			System.out.print("<" + itr.next().index + "> ");
      	System.out.println();
	}

}