package building.blocks;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.tuple.Pair;

public class CrawlerTaskStatistics {

	private final LinkedBlockingQueue<Pair<String, String>> redirectURLs;
	private final LinkedBlockingQueue<Pair<String, String>> failedURLs;

	public CrawlerTaskStatistics(
			LinkedBlockingQueue<Pair<String, String>> redirectURLs,
			LinkedBlockingQueue<Pair<String, String>> failedURLs) {
		this.redirectURLs = redirectURLs;
		this.failedURLs = failedURLs;
	}

	public LinkedBlockingQueue<Pair<String, String>> getRedirectURLs() {
		return redirectURLs;
	}

	public LinkedBlockingQueue<Pair<String, String>> getFailedURLs() {
		return failedURLs;
	}

}
