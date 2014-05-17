package nio;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

public class CreatingLinksTest {

	@Test
	public void demoCreateSymLink() {
		Path link = FileSystems.getDefault().getPath("C:/Test", "movie.avi");
		Path target = FileSystems.getDefault()
				.getPath("Z:/Movies", "movie.avi");
		try {
			Files.createSymbolicLink(link, target);
		} catch (IOException | UnsupportedOperationException
				| SecurityException e) {
			if (e instanceof SecurityException) {
				System.err.println("Permission denied!");
			}
			if (e instanceof UnsupportedOperationException) {
				System.err.println("An unsupported operation was detected!");
			}
			if (e instanceof IOException) {
				System.err.println("An I/O error occurred!");
			}
			System.err.println(e);
		}
	}

	/**
	 * Demo how to create hard link.
	 *
	 * NOTICE: Hard links cannot be created across different drivers.
	 * So, if the link is located in D:/, then an exception will be thrown.
	 */
	@Test
	public void demoCreateHardLink() {
		Path link = FileSystems.getDefault().getPath("C:/Test", "1.jpg");
		Path target = FileSystems.getDefault().getPath("C:/Cover/", "1.jpg");
		try {
			Files.createLink(link, target);
			System.out.println("The link was successfully created!");
		} catch (IOException | UnsupportedOperationException
				| SecurityException e) {
			if (e instanceof SecurityException) {
				System.err.println("Permission denied!");
			}
			if (e instanceof UnsupportedOperationException) {
				System.err.println("An unsupported operation was detected!");
			}
			if (e instanceof IOException) {
				System.err.println("An I/O error occured!");
			}
			System.err.println(e);
		}
	}

	/**
	 * Demo how to get the Path object of the link target.
	 */
	@Test
	public void demoTraceSymLink() {
		Path link = FileSystems.getDefault().getPath("C:/Test", "movie.avi");
		Path target = FileSystems.getDefault()
				.getPath("Z:/Movies", "movie.avi");
		try {
			Files.createSymbolicLink(link, target);
			Path linkedpath = Files.readSymbolicLink(link);
			System.out.println(linkedpath.toString());
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
