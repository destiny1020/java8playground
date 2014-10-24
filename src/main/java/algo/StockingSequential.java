package algo;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class StockingSequential {

	public static void main(String[] args) {
		long currentTime = System.currentTimeMillis();
		StockInfo highPriced = new StockInfo("", BigDecimal.ZERO);
		final Predicate<StockInfo> isPriceLessThan500 = StockingUtils
				.isPriceLessThan(500);

		for (String symbol : Tickers.symbols) {
			StockInfo stockInfo = StockingUtils.getPrice(symbol);
			if (isPriceLessThan500.test(stockInfo))
				highPriced = StockingUtils.pickHigh(highPriced, stockInfo);
		}

		System.out.println("High priced under $500 is " + highPriced);
		System.out.println("Time Duration: "
				+ (System.currentTimeMillis() - currentTime) / 1000.0 + "s");
		
		// High priced under $500 is ticker: AMZN price: 313.180
		// Time Duration: 24.325s
	}

}
