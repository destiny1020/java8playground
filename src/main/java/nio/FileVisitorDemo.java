package nio;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import org.junit.Test;

public class FileVisitorDemo {

	@Test
	public void demoSearchFile() throws IOException {
		Path searchFile = Paths.get("1.avi");
		AbstractSearch walk = new FullMatchingSearch(searchFile);
		EnumSet<FileVisitOption> opts = EnumSet
				.of(FileVisitOption.FOLLOW_LINKS);

		Path targetStore = Paths.get("Z:/Movies");
		Files.walkFileTree(targetStore, opts, Integer.MAX_VALUE, walk);

		if (!walk.found) {
			System.out.println("Not found.");
		}
	}

	@Test
	public void demoSearchFileGlob() throws IOException {
		AbstractSearch walk = new GlobSearch("1*");
		EnumSet<FileVisitOption> opts = EnumSet
				.of(FileVisitOption.FOLLOW_LINKS);

		Path targetStore = Paths.get("Z:/Movies");
		Files.walkFileTree(targetStore, opts, Integer.MAX_VALUE, walk);
	}
}

class GlobSearch extends AbstractSearch {

	public GlobSearch(String glob) {
		super(glob);
	}

	@Override
	public void search(Path file) throws IOException {
		Path name = file.getFileName();
		if (name != null && matcher.matches(name)) {
			System.out.println("Searched file was found: " + name + " in "
					+ file.toRealPath().toString());
		}
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		search(file);
		return FileVisitResult.CONTINUE;
	}
}

class FullMatchingSearch extends AbstractSearch {

	public FullMatchingSearch(Path searchedFile) {
		super(searchedFile);
	}

	@Override
	public void search(Path file) throws IOException {
		Path name = file.getFileName();
		if (name != null && name.equals(searchedFile)) {
			System.out.println("Searched file was found: " + searchedFile
					+ " in " + file.toRealPath().toString());
			found = true;
		}
	}

}

abstract class AbstractSearch implements FileVisitor<Path> {

	protected Path searchedFile;
	protected PathMatcher matcher;
	public boolean found;

	public AbstractSearch(Path searchedFile) {
		this.searchedFile = searchedFile;
		this.found = false;
	}

	public AbstractSearch(String glob) {
		matcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);
	}

	public abstract void search(Path file) throws IOException;

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		search(file);
		if (!found) {
			return FileVisitResult.CONTINUE;
		} else {
			return FileVisitResult.TERMINATE;
		}
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc)
			throws IOException {
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc)
			throws IOException {
		System.out.println("Visited: " + dir);
		return FileVisitResult.CONTINUE;
	}

}
