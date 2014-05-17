package nio;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class PathsOperationsDemo {

	/**
	 * Demo how to use the path.resolve().
	 */
	@Test
	public void demoResolve() {
		Path path_from = Paths.get("C:/Test/1.jpg");
		Path path_to = Paths.get("photos");

		Path resolved = path_from.resolve(path_to);
		System.out.println(resolved);
	}
}
