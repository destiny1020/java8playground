package lambda;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import building.blocks.CrawlerTaskStatistics;
import building.blocks.PictureCrawlingTask;

public class PictureDownloader {

	public static void main(String[] args) {
		List<Pair<String, String>> pairs = Arrays
				.asList(Pair
						.of("http://comic.sun0769.com/eEditor8965po0d9efsfs0df/UploadFile/2012423113222494.jpg",
								"unicorn-1.jpg"),
						Pair.of("http://img0.imgtn.bdimg.com/it/u=746176184,343283404&fm=23&gp=0.jpg",
								"unicorn-2.jpg"),
						Pair.of("http://img1.chinaface.com/middle/11235qVJbKUfNVGEaqS7S8UMCFDq.jpg",
								"unicorn-3.jpg"),
						Pair.of("http://www.kumi.cn/photo/9e/12/c2/9e12c25b1607d2b4.jpg",
								"unicorn-4.jpg"),
						Pair.of("http://pic.baike.soso.com/p/20130222/20130222033949-1957166711.jpg",
								"unicorn-5.jpg"),
						Pair.of("http://www.sinaimg.cn/dy/slidenews/21_img/2012_17/2252_904135_695566.jpg",
								"unicorn-6.jpg"),
						Pair.of("http://ps3.tgbus.com/UploadFiles/201203/20120321103315274.jpg",
								"unicorn-7.jpg"));

		// This is for the statistics during the downloading
		CrawlerTaskStatistics stats = new CrawlerTaskStatistics(
				new LinkedBlockingQueue<>(), new LinkedBlockingQueue<>());

		List<PictureCrawlingTask> tasks = PictureCrawlingTask.constructTasks(
				pairs, stats);

		// delegate the downloading to the underlying ForkJoinPool
		List<String> destinations = tasks.parallelStream()
				.map(PictureCrawlingTask::call).collect(Collectors.toList());

		System.out.println("Finished");

		System.out.println(destinations);
	}
}
