package nio;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;

import building.blocks.StreamHelper;

public class OperateFilesAndDirDemo {

	/**
	 * Demo how to list root directories in Java 7 NIO.2 Style
	 */
	@Test
	public void demoListRootDirectories() {
		Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();
		for (Path name : dirs) {
			System.out.println(name);
		}

		System.out.println();

		// Java 8 FP Style
		// since there is no direct API for converting Iterable to Stream
		// you can encapsulate the boilerplate
		// Refer to:
		// http://stackoverflow.com/questions/20310209/how-to-perform-stream-functions-on-an-iterable
		StreamHelper.stream(dirs).forEach(System.out::println);
	}

	/**
	 * Demo how to get the array of Files for all root folders.
	 */
	@Test
	public void demoListRootFiles() {
		// Old Java 6 Style
		File[] roots = File.listRoots();
		for (File root : roots) {
			System.out.println(root);
		}
	}

	/**
	 * Demo how to list the entire contents under certain path.
	 */
	@Test
	public void demoListingContentImperatively() {
		// Java 7, try-with-resource style
		Path path = Paths.get("Z:/Movies");
		// no filter applied
		System.out.println("\nNo filter applied:");
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
			for (Path file : ds) {
				System.out.println(file.getFileName());
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * Demo how to iterate the contents under a certain path in FP style.
	 */
	@Test
	public void demoListingContentDeclaratively() throws IOException {
		// Java 8, FP style
		Path path = Paths.get("Z:/Movies");
		// no filter applied
		System.out.println("\nNo filter applied:");

		Stream<Path> pathStream = StreamHelper.stream(Files
				.newDirectoryStream(path));

		pathStream.forEach(p -> System.out.println(p.getFileName()));
	}

	/**
	 * Demo how to iterate the contents by using Glob pattern.
	 */
	@Test
	public void demoListingContentWithGlob() throws IOException {
		Path path = Paths.get("Z:/Movies");
		// glob pattern applied
		System.out.println("\nGlob pattern applied:");

		Stream<Path> pathStream = StreamHelper.stream(Files.newDirectoryStream(
				path, "*川*"));

		pathStream.forEach(p -> System.out.println(p.getFileName()));
	}

	/**
	 * Demo how to iterate the contents by using Filter.
	 */
	@Test
	public void demoListingContentWithFilter() throws IOException {
		Path path = Paths.get("Z:/Movies");
		// filter applied
		System.out.println("\nFilter applied:");

		Stream<Path> pathStream = StreamHelper.stream(Files.newDirectoryStream(
				path, p -> p.getFileName().toString().contains("川")));

		pathStream.forEach(System.out::println);
	}

	/**
	 * Demo how to write lines to a file.
	 */
	@Test
	public void demoWritingToFile() {
		Path path = Paths.get("C:/Test", "wiki.txt");

		Charset charset = Charset.forName("UTF-8");

		// line breaks will be automatically inserted
		List<String> lines = Arrays.asList("\n", "Line 1", "Line 2", "Line 3");

		try {
			Files.write(path, lines, charset, StandardOpenOption.APPEND);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * Demo how to read lines from a file.
	 */
	@Test
	public void demoReadingFileIntoLines() {
		Path path = Paths.get("C:/Test", "wiki.txt");

		Charset charset = Charset.forName("UTF-8");

		try {
			Files.readAllLines(path, charset).stream()
					.forEach(System.out::println);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * Demo how to create a temporary directory.
	 *
	 * For Windows, it will create directories under Users\%User%\AppData\Local\Temp
	 * This path can be obtained by: String default_tmp = System.getProperty("java.io.tmpdir");
	 */
	@Test
	public void demoCreateTmpDir() {
		String tmp_dir_prefix = "nio_";
		try {
			// passing null prefix
			Path tmp_1 = Files.createTempDirectory(null);
			System.out.println("TMP: " + tmp_1.toString());
			// set a prefix
			Path tmp_2 = Files.createTempDirectory(tmp_dir_prefix);
			System.out.println("TMP: " + tmp_2.toString());
		} catch (IOException e) {
			System.err.println(e);
		}

		// of course, can designate a tmp directory rather than the default
		Path basedir = FileSystems.getDefault().getPath("C:/Test/tmp/");
		try {
			// create a tmp directory in the base dir
			Path tmp = Files.createTempDirectory(basedir, null);
			System.out.println("TMP: " + tmp.toString());
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * Demo how to create temporary files.
	 */
	@Test
	public void testCreateTmpFile() {
		String tmp_file_prefix = "prefix_";
		String tmp_file_sufix = "_suffix";
		try {
			// passing null prefix/suffix
			Path tmp_1 = Files.createTempFile(null, null);
			System.out.println("TMP: " + tmp_1.toString());
			// set a prefix and a suffix
			Path tmp_2 = Files.createTempFile(tmp_file_prefix, tmp_file_sufix);
			System.out.println("TMP: " + tmp_2.toString());
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}
