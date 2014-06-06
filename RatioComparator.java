import java.util.*;

public class RatioComparator implements Comparator<Item> {

	public int compare(Item a, Item b) {
		float r1, r2;

		r1 = (float)a.value / (float)a.weight;
		r2 = (float)b.value / (float)b.weight;
		if (r1 == r2)
			return 0;
		else
			return r1 > r2 ? -1 : 1;
	}
}