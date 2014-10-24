package algo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RodCutterLambda {

	final List<Integer> prices;

	public RodCutterLambda(final List<Integer> pricesForLength) {
		prices = pricesForLength;
	}

	public int maxProfit(final int rodLength) {
		return Memoizer
				.callMemoized(
						(final Function<Integer, Integer> func,
								final Integer length) -> {
							int profit = (length <= prices.size()) ? prices
									.get(length - 1) : 0;
							for (int i = 1; i < length; i++) {
								int profitWhenCut = func.apply(i)
										+ func.apply(length - i);
								if (profit < profitWhenCut) {
									profit = profitWhenCut;
								}
							}

							return profit;
						}, rodLength);
	}

	public static void main(String[] args) {
		final List<Integer> priceValues = Arrays.asList(2, 1, 1, 2, 2, 2, 1, 8,
				9, 15);
		final RodCutterLambda rodCutter = new RodCutterLambda(priceValues);

		long currentTime = System.currentTimeMillis();
		System.out.println(rodCutter.maxProfit(5));
		System.out.println("Duration: "
				+ (System.currentTimeMillis() - currentTime) / 1000.0);
//
//		currentTime = System.currentTimeMillis();
//		System.out.println(rodCutter.maxProfit(22));
//		System.out.println("Duration: "
//				+ (System.currentTimeMillis() - currentTime) / 1000.0);

		//		10
		//		Duration: 0.116
		//		44
		//		Duration: 0.001

	}

	static class Memoizer {
		public static <T, R> R callMemoized(
				final BiFunction<Function<T, R>, T, R> func, final T input) {
			Function<T, R> memoized = new Function<T, R>() {

				private final Map<T, R> store = new HashMap<>();

				@Override
				public R apply(T input) {
					R result = store.computeIfAbsent(input,
							key -> func.apply(this, key));
//					System.out.println(String.format("Result: %s, for: %s", result, input));
					return result;
				}
			};

			return memoized.apply(input);
		}
	}

}
