package algo;

import java.util.stream.Stream;

public class StockingParallel {

	public static StockInfo findHighPriced(final Stream<String> symbols) {
		return symbols.map(StockingUtils::getPrice)
				.filter(StockingUtils.isPriceLessThan(500))
				.reduce(StockingUtils::pickHigh).get();
	}

	public static void main(String[] args) {
		long currentTime = System.currentTimeMillis();

		System.out.println("High priced under $500 is "
				+ findHighPriced(Tickers.symbols.parallelStream()));
		System.out.println("Time Duration: "
				+ (System.currentTimeMillis() - currentTime) / 1000.0 + "s");

//		High priced under $500 is ticker: AMZN price: 313.180
//		Time Duration: 5.621s
	}

}
