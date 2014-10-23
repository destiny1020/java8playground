package algo;

import java.util.Arrays;
import java.util.List;

public class RodCutter {

	final List<Integer> prices;

	public RodCutter(final List<Integer> pricesForLength) {
		prices = pricesForLength;
	}

	public int maxProfit(final int length) {
		int profit = (length <= prices.size()) ? prices.get(length - 1) : 0;
		for (int i = 1; i < length; i++) {
			int priceWhenCut = maxProfit(i) + maxProfit(length - i);
			if (profit < priceWhenCut)
				profit = priceWhenCut;
		}
		return profit;
	}

	public static void main(String[] args) {
		final List<Integer> priceValues = Arrays.asList(2, 1, 1, 2, 2, 2, 1, 8,
				9, 15);
		final RodCutter rodCutter = new RodCutter(priceValues);

		long currentTime = System.currentTimeMillis();
		System.out.println(rodCutter.maxProfit(5));
		System.out.println("Duration: "
				+ (System.currentTimeMillis() - currentTime) / 1000.0);

		currentTime = System.currentTimeMillis();
		System.out.println(rodCutter.maxProfit(22));
		System.out.println("Duration: "
				+ (System.currentTimeMillis() - currentTime) / 1000.0);

		//		10
		//		Duration: 0.001
		//		44
		//		Duration: 34.612
	}

}
