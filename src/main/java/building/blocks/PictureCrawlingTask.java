package building.blocks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Legacy Code, which is used during the JDK 6.
 *
 * Since Callable is following the Functional Interface's Specification,
 * it can be used in Lambda Expression without any changes.
 */
public class PictureCrawlingTask implements Callable<String> {

	private static final int RETRY_TIMES = 10;
	private static final int CONNECT_TIMEOUT_MILLIS = 1500;
	private static final int READ_TIMEOUT_MILLIS = 2000;

	private String url;
	private String filePath;

	private final CrawlerTaskStatistics stat;

	private PictureCrawlingTask(CrawlerTaskStatistics stat) {
		this.stat = stat;
	}

	public PictureCrawlingTask(Pair<String, String> tuple,
			CrawlerTaskStatistics stat) {
		this(stat);
		this.url = tuple.getLeft();
		this.filePath = tuple.getRight();
	}

	public PictureCrawlingTask(String url, String filePath,
			CrawlerTaskStatistics stat) {
		this(stat);
		this.url = url;
		this.filePath = filePath;
	}

	@Override
	public String call() {
		URL urlObj;
		try {
			urlObj = new URL(url);
		} catch (MalformedURLException e) {
			String errorMsg = "Malformed URL: " + url;
			System.err.println(errorMsg);
			e.printStackTrace();
			return "";
		}

		String threadName = Thread.currentThread().getName();

		HttpURLConnection conn;
		int retryCount = 0;
		while (retryCount < RETRY_TIMES) {
			try {
				conn = (HttpURLConnection) urlObj.openConnection();
				conn.setInstanceFollowRedirects(false);
				conn.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
				conn.setReadTimeout(READ_TIMEOUT_MILLIS);

				String msg = String.format("[%s] Reading URL %s, Time %d",
						threadName, url, retryCount + 1);
				System.out.println(msg);
				if (String.valueOf(conn.getResponseCode()).startsWith("3")) {
					msg = String
							.format("%s will result in a redirect operation, abort it.",
									url);
					System.out.println(msg);
					stat.getRedirectURLs().offer(Pair.of(url, filePath));
					return "";
				}

				BufferedImage image = null;
				image = ImageIO.read(conn.getInputStream());
				ImageIO.write(image, "jpg", new File(filePath));

				msg = String.format(
						"[%s] Crawl Image Successful, URL: %s --- Path: %s",
						threadName, url, filePath);
				System.out.println(msg);
				// return directly after saving successfully
				return filePath;
			} catch (IOException e) {
				retryCount++;
				String errorMsg = String
						.format("[%s] IO Exception --- %s, happened when trying to save image from url: %s to file: %s",
								threadName, e.getMessage(), url, filePath);
				System.out.println(errorMsg);
			}
		}

		stat.getFailedURLs().offer(Pair.of(url, filePath));
		return "";
	}

	public static List<PictureCrawlingTask> constructTasks(
			List<Pair<String, String>> taskTuples, CrawlerTaskStatistics stat) {
		List<PictureCrawlingTask> tasks = new LinkedList<PictureCrawlingTask>();
		for (Pair<String, String> t : taskTuples) {
			tasks.add(new PictureCrawlingTask(t, stat));
		}

		return tasks;
	}
}
