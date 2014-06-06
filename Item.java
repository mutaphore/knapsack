import java.util.*;

public class Item implements Comparable<Item> {

	public int index;
	public int value;
	public int weight;

	public Item(int i, int v, int w) {
		index = i;
		value = v;
		weight = w;
	}

	public int compareTo(Item other) {
		if (this.index == other.index)
			return 0;
		else if (this.index < other.index)
			return -1;
		else
			return 1;
	}
}