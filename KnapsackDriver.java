import java.util.*;
import java.io.*;

public class KnapsackDriver {
	
	public static ArrayList<Item> items;
	public static int n, C;

	public static void main(String args[]) {
		KnapsackEnum knap_enum;
		KnapsackGreedy knap_greedy;
		KnapsackDynamic knap_dynamic;
		KnapsackBNB knap_bnb;

		readFile();

		// //Find solution by exhaustive search
		// knap_enum = new KnapsackEnum(items, n, C);
		// knap_enum.generate();
		// knap_enum.search();
		// knap_enum.printSolution();

		//Find solution by greedy, items sorted by highest v to w ratio first
		knap_greedy = new KnapsackGreedy(items, n, C);
		knap_greedy.construct();
		knap_greedy.printSolution();

		//Find solution by dynamic programming
		knap_dynamic = new KnapsackDynamic(items, n, C);
		knap_dynamic.createTable();
		knap_dynamic.backtrack();
		knap_dynamic.printSolution();

		//Find solution with branch and bound
		knap_bnb = new KnapsackBNB(items, n, C);
		knap_bnb.branchAndBound();
		knap_bnb.printSolution();
	}

	//Read in contents of the file
	public static void readFile() {
		Scanner sc = new Scanner(System.in), fileIn;
      String fileName;
      Item item;
        
		try {
			System.out.print("Enter input file name: ");
			fileName = sc.next();
			fileIn = new Scanner(new FileInputStream(new File(fileName)));
			
			//Read in file contents
			items = new ArrayList<Item>();
			n = fileIn.nextInt();
			for (int i = 0; i < n; i++) {
				item = new Item(fileIn.nextInt(), fileIn.nextInt(), fileIn.nextInt());
				items.add(item);
			}
			C = fileIn.nextInt();
		}
		catch (FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(0);
		}
	}
}