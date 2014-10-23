package lambda;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

import utils.Person;

public class StreamExamples {

	private final Consumer<Object> print = System.out::println;
	private final IntConsumer intPrint = System.out::println;

	private static Person[] persons = new Person[] { new Person(1, "Destiny"),
			new Person(2, "Aslan"), new Person(3, "Kira"),
			new Person(4, "Rezel") };

	// 0.072s
	@Test
	public void demoReadingImperatively() throws IOException {
		String contents = new String(
				Files.readAllBytes(Paths
						.get("C:\\Users\\Ruixiang Jiang\\Downloads\\A sample book.txt")),
				StandardCharsets.UTF_8);

		// Split into words; nonletters are delimiters
		List<String> words = Arrays.asList(contents.split("[\\P{L}]+"));

		int count = 0;
		for (String w : words) {
			if (w.length() > 5)
				count++;
		}

		System.out.println(count);
	}

	// 0.105s
	@Test
	public void demoReadingDeclaratively() throws IOException {
		String contents = new String(
				Files.readAllBytes(Paths
						.get("C:\\Users\\Ruixiang Jiang\\Downloads\\A sample book.txt")),
				StandardCharsets.UTF_8);

		// Split into words; nonletters are delimiters
		List<String> words = Arrays.asList(contents.split("[\\P{L}]+"));

		long count = words.stream().filter(w -> w.length() > 5).count();

		System.out.println(count);
	}

	/**
	 * Demostrate the usage of new API splitAsStream in Pattern class of RE.
	 * @throws IOException
	 */
	@Test
	public void demoSplitAsStream() throws IOException {
		String contents = new String(
				Files.readAllBytes(Paths
						.get("C:\\Users\\Ruixiang Jiang\\Downloads\\A sample book.txt")),
				StandardCharsets.UTF_8);

		Stream<String> words = Pattern.compile("[\\P{L}]+").splitAsStream(
				contents);

		System.out.println(words.filter(w -> w.length() > 5).count());
	}

	/**
	 * Demo the usage of new API: Files.lines(path)
	 * Since Stream implements the AutoClosable, so it can be used in try-with-resources
	 * @throws IOException
	 */
	@Test
	public void demolinesInFiles() throws IOException {
		// since the Stream is based on File reading, the stream should be closed after using
		// Refer to: http://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html
		try (Stream<String> lines = Files
				.lines(Paths
						.get("C:\\Users\\Ruixiang Jiang\\Downloads\\A sample book.txt"))) {
			System.out.println(lines.count());
		}
	}

	// 0.320s
	@Test
	public void demoReadingDeclarativelyInParallel() throws IOException {
		String contents = new String(
				Files.readAllBytes(Paths
						.get("C:\\Users\\Ruixiang Jiang\\Downloads\\A sample book.txt")),
				StandardCharsets.UTF_8);

		// Split into words; nonletters are delimiters
		List<String> words = Arrays.asList(contents.split("[\\P{L}]+"));

		// only difference lies here
		long count = words.parallelStream().filter(w -> w.length() > 5).count();

		System.out.println(count);
	}

	// 0.202s
	@Test
	public void demoStreamOf() throws IOException {
		String contents = new String(
				Files.readAllBytes(Paths
						.get("C:\\Users\\Ruixiang Jiang\\Downloads\\A sample book.txt")),
				StandardCharsets.UTF_8);

		Stream<String> words = Stream.of(contents.split("[\\P{L}]+"));

		System.out.println(words.filter(w -> w.length() > 5).count());
	}

	@SuppressWarnings("unused")
	@Test
	public void demoStreamEmpty() {
		Stream<String> empty = Stream.<String> empty();

		// same with above, using the generic type inference
		Stream<String> empty2 = Stream.empty();
	}

	/**
	 * generate() is one of the two static methods to make infinite streams, another is iterate()
	 * it receives a Supplier<T> as the data source.
	 */
	@Test
	public void demoStreamGenerate() {
		Stream<String> echos = Stream.<String> generate(() -> "Echo");

		echos.limit(5).forEach(System.out::println);

		Stream<Double> randoms = Stream.<Double> generate(Math::random);

		randoms.limit(5).forEach(System.out::println);
	}

	/**
	 * iterate() is one of the two static methods to make infinite streams, another is generate()
	 * it receives two parameters, the first one is the seed, the second one is a UnaryOperator<T>
	 * UnaryOperator<T> is a specialization of the Function<T, R>
	 */
	@Test
	public void demoStreamIterate() {
		Stream<BigInteger> sequence = Stream.<BigInteger> iterate(
				BigInteger.ONE, n -> n.add(BigInteger.ONE));

		sequence.limit(10).forEach(System.out::println);
	}

	/**
	 * Demostrate the usage of flatMap.
	 */
	@Test
	public void demoFlatMap() {
		final Function<String, Stream<Character>> characterStream = str -> str
				.chars().mapToObj(i -> (char) i);

		final List<String> names = Arrays.asList("Tom", "Jim", "Green");

		// ordinary mapping, strings -> Stream of Stream<Character>
		Stream<Stream<Character>> map1 = names.stream().map(characterStream);
		map1.forEach(stream -> stream.forEach(System.out::println));

		// flat mapping, strings -> Stream<Character>
		Stream<Character> map2 = names.stream().flatMap(characterStream);
		map2.forEach(System.out::println);
	}

	/**
	 * Demonstrate how to use limit. Cutting infinite streams down to size.
	 */
	@Test
	public void demoLimit() {
		Stream.generate(Math::random).limit(100).forEach(System.out::println);
	}

	/**
	 * Demonstrate how to use skip, the first N elements will be skipped.
	 */
	@Test
	public void demoSkip() {
		Stream.<Integer> iterate(1, n -> n + 1).skip(10).limit(20)
				.forEach(System.out::println);
	}

	/**
	 * Demo how to use the static method concat to concatenate two streams.
	 */
	@Test
	public void demoConcat() {
		Stream.concat(
				Stream.<Integer> iterate(1, n -> n + 1).skip(10).limit(20),
				Stream.<Integer> iterate(1, n -> n + 1).skip(50).limit(20))
				.forEach(System.out::println);
	}

	/**
	 * Demo how to use peek method in Stream API, this method's main goal is debugging.
	 * Must call the last forEach as a terminal operation to see the effects of peek.
	 *
	 * After a terminal operation has been applied, the stream ceases to be usable.
	 *
	 * The peek method will not cease the usage of the underlying Stream, but the terminal methods like
	 * forEach and forEachOrdered will. So if you want to reuse the stream, peek maybe your choice.
	 * Since peek and forEach accepts the same Consumer<T>.
	 */
	@Test
	public void demoPeek() {
		Stream.concat(
				Stream.<Integer> iterate(1, n -> n + 1).skip(10).limit(20),
				Stream.<Integer> iterate(1, n -> n + 1).skip(50).limit(20))
				.map(i -> i * 2)
				.peek(e -> System.out.println("Mapped value: " + e))
				.forEach(System.out::println);
	}

	/**
	 * Demo how to use distinct, it is a STATEFUL transformation since distinct needs to
	 * remember the previous elements.
	 */
	@Test
	public void demoDistinct() {
		Stream.<String> of("a", "b", "a", "a").distinct().forEach(print);
	}

	/**
	 * Demo how to use sorted in two ways, it is a STATEFUL transformation since sorting
	 * needs to remember all the previous elements.
	 *
	 * After a terminal operation has been applied, the stream ceases to be usable.
	 *
	 * At the same time, the usage of Comparator.comparing and Comparator.reversed has been shown.
	 */
	@Test
	public void demoSorted() {
		// sort by the str1.compareTo(str2) alphabetically
		Stream.of("Mike", "Jim", "Green", "Valkyrie").sorted().forEach(print);

		System.out.println();

		// underhood: (c1, c2) ->
		// keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2))
		Stream.of("Mike", "Jim", "Green", "Valkyrie")
				.sorted(Comparator.comparing(String::length)).forEach(print);

		System.out.println();

		Stream.of("Mike", "Jim", "Green", "Valkyrie")
				.sorted(Comparator.comparing(String::length).reversed())
				.forEach(print);
	}

	/**
	 * Show the usage of max reduction operation and Optional type.
	 */
	@Test
	public void demoMaxAndOptional() {
		Optional<String> max = Stream.of("Mike", "Jim", "Green", "Valkyrie")
				.max(Comparator.comparing(String::length));
		max.ifPresent(print);

		Optional<String> max2 = Stream.of("Mike", "Jim", "Green", "Valkyrie")
				.max(String::compareTo);
		max2.ifPresent(print);
	}

	/**
	 * Show the usage of findFirst and Optional type.
	 */
	@Test
	public void demoFindFirstAndOptional() {
		Optional<String> firstName = Stream
				.of("Mike", "Michelle", "Green", "Valkyrie")
				.filter(name -> name.startsWith("M")).findFirst();

		firstName.ifPresent(print);
	}

	/**
	 * Show the usage of findAny and Optional Type.
	 *
	 * NOTICE: The result for each invocation with the combination of parallel() and findAny()
	 * maybe different.
	 */
	@Test
	public void demoFindAnyAndOptional() {
		Optional<String> anyName = Stream
				.of("Mike", "Michelle", "Green", "Valkyrie").parallel()
				.filter(name -> name.startsWith("M")).findAny();

		anyName.ifPresent(print);
	}

	/**
	 * Show the usage of anyMatch, allMatch and noneMatch, they will receive a Predicate as the parameter,
	 * so they act like a filter. It can be combined with parallel as well to accelerate the process.
	 */
	@Test
	public void demoMatching() {
		boolean anyMatch = Stream.of("Mike", "Michelle", "Green", "Valkyrie")
				.parallel().anyMatch(name -> name.startsWith("Q"));
		System.out.println(anyMatch);

		boolean allMatch = Stream.of("Mike", "Michelle", "Green", "Valkyrie")
				.parallel().allMatch(name -> name.startsWith("Q"));
		System.out.println(allMatch);

		boolean noneMatch = Stream.of("Mike", "Michelle", "Green", "Valkyrie")
				.parallel().noneMatch(name -> name.startsWith("Q"));
		System.out.println(noneMatch);
	}

	/**
	 * Demo the correct usage of Optional<T>, the key points are ifPresent, orElse, orElseGet and orElseThrow.
	 *
	 * ifPresent receives a Consumer.
	 * orElse receives a parameter in target type.
	 * orElseGet receives a Supplier.
	 * orElseThrow receives a Supplier for getting exception.
	 */
	@Test(expected = NoSuchElementException.class)
	public void demoOptional() {
		Optional<String> name = Optional.of("Mike");
		name.ifPresent(print);

		Optional<String> emptyName = Optional.empty();
		System.out
				.println("Default Value: " + emptyName.orElse("Default Name"));

		Optional<String> emptyName2 = Optional.<String> empty();
		String defaultName = emptyName2.orElseGet(() -> String.valueOf(Math
				.random()));
		System.out.println("Default Value From Supplier: " + defaultName);

		// throw an exception
		emptyName.orElseThrow(NoSuchElementException::new);
	}

	/**
	 * Optional in tandem with Stream.map.
	 */
	@Test
	public void demoOptionalWithMap() {
		final Function<Double, Optional<Double>> inverse = x -> x == 0 ? Optional
				.empty() : Optional.of(1 / x);
		final Function<Double, Optional<Double>> square = x -> x < 0 ? Optional
				.empty() : Optional.of(Math.sqrt(x));

		List<Double> doubles = Arrays.<Double> asList(4.0, 3.0, 0.0, -1.0, 2.0);

		doubles.stream().map(inverse)
				.peek(x -> System.out.println("Inversed: " + x)).forEach(print);

		doubles.stream().map(square)
				.peek(x -> System.out.println("Squared: " + x)).forEach(print);
	}

	/**
	 * The Optional.flatMap method works in the same way as Stream.flatMap if you consider an optional value
	 * to be a stream of size zero or one.
	 *
	 * This method is similar to map(Function), but the provided mapper is one whose result is already an Optional,
	 * and if invoked, flatMap does not wrap it with an additional Optional.
	 */
	@Test
	public void demoOptionalWithFlatMap() {
		final Function<Double, Optional<Double>> inverse = x -> x == 0 ? Optional
				.empty() : Optional.of(1 / x);
		final Function<Double, Optional<Double>> square = x -> x < 0 ? Optional
				.empty() : Optional.of(Math.sqrt(x));

		Optional<Double> d = Optional.<Double> of(-4.0);
		Optional<Double> answer = d.flatMap(inverse).flatMap(square);

		// Optional.empty
		System.out.println(answer);

		d = Optional.<Double> of(4.0);
		answer = d.flatMap(inverse).flatMap(square);

		// Optional[0.5]
		System.out.println(answer);
	}

	/**
	 * In general, if the reduce method has a reduction operation op,
	 * the reduction yields v0 op v1 op v2 op ...,
	 * where we write vi op vi + 1 for the function call op(vi, vi + 1).
	 *
	 * The operation should be associative: It shouldn’t matter in which order you combine the elements.
	 * In math notation, (x op y) op z = x op (y op z).
	 *
	 * This allows efficient reduction with parallel streams.
	 */
	@Test
	public void demoReduceSum() {
		Stream<Integer> ints = Stream.of(1, 2, 3, 4, 5);
		Optional<Integer> sum = ints.reduce(Integer::sum);

		sum.ifPresent(print);
	}

	/**
	 * BiFunction, abstracts the operation of receiving two parameters and return one result.
	 * So it has three generic types defined.
	 *
	 * BinaryOperator, a specialization of BiFunction, which the three generic types are the same.
	 * So it has only one generic type.
	 *
	 * The ACCUMULATOR is for gathering the partial results.
	 * The COMBINER is for combining all the partial results accumulated. It is used when the Parallel
	 * mechanism is enabled.
	 *
	 * But as it is, this kind of REDUCE operation is seldom used. We can always use something as below to replace:
	 *
	 * Optional<Integer> sum = names.mapToInt(String::length).sum();
	 */
	@Test
	public void demoReduceWithAccumulatorAndCombiner() {
		Stream<String> names = Stream.of("Mike", "Lily", "Michelle", "Destiny");

		final BiFunction<Integer, String, Integer> accumulator = (total, name) -> total
				+ name.length();
		final BinaryOperator<Integer> combiner = (x, y) -> x + y;

		Integer sum = names.parallel().reduce(0, accumulator, combiner);
		System.out.println(sum);
	}

	/**
	 * Show how to collect the results into array.
	 * Since it is not possible to create a generic array at runtime,
	 * the expression stream.toArray() returns an Object[] array.
	 *
	 * If you want an array of the correct type, pass in the array constructor, like below.
	 */
	@Test
	public void demoCollectToArrays() {
		Stream<String> names = Stream.of("Mike", "Lily", "Michelle", "Destiny");

		String[] upperNames = names.map(String::toUpperCase).toArray(
				String[]::new);
		Arrays.asList(upperNames).stream().forEach(print);
	}

	/**
	 * Show how the general collect works.
	 *
	 * It should receive THREE parameters:
	 * 1. Supplier, to provide a container for each thread.
	 *    Since sometimes the stream is handled concurrently, so each thread should be provided with its own container,
	 *    to overcome the problems of synchronization.
	 * 2. Accumulator, to tell the working thread how to operate on the container provided by Supplier.
	 * 3. Combiner, to tell the threads how to merge their partial results into the final one.
	 *
	 * But as it is, this method is somehow low-level, and should be replace by something like below:
	 *    stream.parallel().collect(Collectors.toSet())
	 *    OR
	 *    stream.parallel().collect(Collectors.toList())
	 */
	@Test
	public void demoGeneralCollect() {
		Stream<String> names = Stream.of("Mike", "Lily", "Michelle", "Destiny",
				"Destiny");

		Set<String> collected = names.parallel().collect(HashSet::new,
				Set::add, Set::addAll);

		collected.stream().forEach(print);
	}

	/**
	 * Show how to use the joining methods in the helper class Collectors.
	 */
	@Test
	public void demoCollectJoining() {
		final List<String> names = Arrays.asList("Mike", "Lily", "Michelle",
				"Destiny", "Destiny");

		// without delimiter between names
		String joinedNames = names.stream().collect(Collectors.joining());
		System.out.println(joinedNames);

		joinedNames = names.stream().collect(Collectors.joining(", "));
		System.out.println(joinedNames);
	}

	/**
	 * Show how to use the summary statictics for types: Integer, Long and Double.
	 *
	 * Below will just show the Integer, other two types are almost the same.
	 *
	 * For Collectors.summarizingInt method, it expects ToIntFunction Functional Interface,
	 * this FI will apply any passed in parameter to get its integer value.
	 */
	@Test
	public void demoSummaryStatistics() {
		Stream<Integer> numbersInt = Stream.of(1, 2, 3, 4, 5);

		IntSummaryStatistics summary = numbersInt.collect(Collectors
				.summarizingInt(Integer::intValue));

		System.out.println(summary.getCount());
		System.out.println(summary.getAverage());
		System.out.println(summary.getSum());
		System.out.println(summary.getMax());
		System.out.println(summary.getMin());
	}

	/**
	 * Demo how to use the Collectors.map method to get mapping structure based on objects.
	 *
	 * Since the value is itself in most cases, the second parameter should be Function.identity().
	 *
	 * Underhood, it will use toMap(keyMapper, valueMapper, throwingMerger(), HashMap::new).
	 * The throwingMerger() will not allow duplicated keys by throwing IllegalStateException.
	 */
	@Test
	public void demoCollectorsMap() {
		Map<Integer, Person> personMappings = Arrays.asList(persons).stream()
				.collect(Collectors.toMap(Person::getId, Function.identity()));

		System.out.println(personMappings);
	}

	/**
	 * Demo how to use the Collectors.mao method with Merger.
	 *
	 * The Merger is a BinaryOperator Functional Interface, which is a specialization of BiFunction.
	 * It will determine the behaviors when there are duplicated keys.
	 */
	@Test
	public void demoCollectorsMapWithMerger() {
		// override the original person whose id is 2
		persons[1] = new Person(1, "Fake Destiny");

		// replace the old record with the new one
		Map<Integer, Person> personMappings = Arrays
				.asList(persons)
				.stream()
				.collect(
						Collectors.toMap(Person::getId, Function.identity(), (
								existingValue, newValue) -> newValue));

		// {1=1-Fake Destiny, 3=3-Kira, 4=4-Rezel}
		System.out.println(personMappings);
	}

	/**
	 * Demo how to use the Collectors.toConcurrentMap. The underlying combiner is ConcurrentHashMap,
	 * while in Collectors.toMap, it is using HashMap.
	 *
	 * When using in a concurrent manner, shared the underlying ConcurrentMap is more efficient than
	 * maintaining threads' own ConcurrentMap.
	 */
	@Test
	public void demoCollectorsConcurrentMap() {

		ConcurrentMap<Integer, Person> sharedHolder = new ConcurrentHashMap<>();
		Supplier<ConcurrentMap<Integer, Person>> supplier = () -> sharedHolder;

		Map<Integer, Person> personMappings = Arrays
				.asList(persons)
				.parallelStream()
				.collect(
						Collectors.toConcurrentMap(Person::getId,
								Function.identity(),
								(exsitingValue, newValue) -> newValue, supplier));

		// {1=1-Destiny, 2=2-Aslan, 3=3-Kira, 4=4-Rezel}
		System.out.println(personMappings);
	}

	/**
	 * Demo how to use the Collectors.groupingBy to get a nested collection with:
	 * Key -> Set/List
	 *
	 * The first parameter passed in groupingBy is a Classifier. The returned values are the keys to the final mapping.
	 */
	@Test
	public void demoCollectorsGrouping() {
		Map<String, List<Locale>> localeMappings = Arrays
				.asList(Locale.getAvailableLocales()).stream()
				.collect(Collectors.groupingBy(Locale::getCountry));

		System.out.println(localeMappings.get(Locale.CHINA.getCountry()));
	}

	/**
	 * By default, the groupingBy will group the values to a list, but after assigning the Downstream Collector,
	 * the list can be replaced to set.
	 *
	 * Besides, the downstream collector can be other methods like: Collectors.counting, summingXXX, maxBy/minBy, etc.
	 *
	 * Pay special attention to the Collectors.mapping method, it can let us do some processing before collecting.
	 */
	@Test
	public void demoCollectorsGroupingWithDownStreamCollector() {
		List<Locale> locales = Arrays.asList(Locale.getAvailableLocales());

		Map<String, Set<Locale>> localeMappings = locales.stream().collect(
				Collectors.groupingBy(Locale::getCountry, Collectors.toSet()));

		System.out.println(localeMappings);

		// using counting() downstream collector
		Map<String, Long> localeInfo = locales.stream()
				.collect(
						Collectors.groupingBy(Locale::getCountry,
								Collectors.counting()));

		System.out.println(localeInfo);

		// using mapping to do some processing before using downstream collector
		Map<String, Set<String>> localeLanguageInfo = locales.stream().collect(
				Collectors.groupingBy(
						Locale::getCountry,
						Collectors.mapping(Locale::getLanguage,
								Collectors.toSet())));

		System.out.println(localeLanguageInfo);
	}

	/**
	 * Collectors.reduce is seldom necessary, just like Stream.reduce. It is relatively low-level API.
	 *
	 * There are always better solutions.
	 */
	@Test
	public void demoCollectorsWithReduction() {
		List<Locale> locales = Arrays.asList(Locale.getAvailableLocales());

		// The first identity should be empty String because this parameter
		// represents: "the type of the mapped values"
		// or, it means the initial value for the reduction
		Map<String, String> localesJoined = locales.stream().collect(
				Collectors.groupingBy(Locale::getCountry, Collectors.reducing(
						"", Locale::getLanguage, (x, y) -> x.length() == 0 ? y
								: x + ", " + y)));

		System.out.println(localesJoined);

		// since the above operation on downstream collector is really
		// convoluted
		// it should be replaced with more clear and concise operation, like
		// below
		Map<String, String> betterLocalesJoined = locales.stream().collect(
				Collectors.groupingBy(
						Locale::getCountry,
						Collectors.mapping(Locale::getLanguage,
								Collectors.joining(", "))));

		System.out.println(betterLocalesJoined);
	}

	/**
	 * When the Classifier for the groupingBy is effectively Predicate, the grouping behavior
	 * is in fact a partitioning behavior, so in this case, should use partitioningBy instead of
	 * groupingBy.
	 *
	 * Below demo will collect all locales with English available.
	 *
	 * Like groupingBy, the partitioningBy also supports the Downstream Collectors as illustrated above.
	 */
	@Test
	public void demoCollectorsPartitioning() {
		Map<Boolean, List<Locale>> localeMappings = Arrays
				.asList(Locale.getAvailableLocales())
				.stream()
				.collect(
						Collectors.partitioningBy((Locale l) -> l.getLanguage()
								.equals("en")));

		System.out.println(localeMappings.get(true));
	}

	/**
	 * Show a better way to generate sequence of Integers or Longs.
	 */
	@Test
	public void demoPrimitiveStreamRange() {
		IntStream zeroToNine = IntStream.range(0, 10);

		// the forEach method will accept a Function Interface of type
		// IntConsumer, which is not a generic type and dedicated to primitive
		// int.
		zeroToNine.forEach(intPrint);

		System.out.println();

		IntStream zeroToTen = IntStream.rangeClosed(0, 10);

		zeroToTen.forEach(intPrint);
	}

	/**
	 * Convert Object Stream to Primitive Stream.
	 */
	@Test
	public void demoConvertBetweenPrimitiveStream() {
		List<String> names = Arrays.asList("Destiny", "ReZEL", "Aegis",
				"Buster", "Strike");

		IntStream namesLengths = names.stream().mapToInt(String::length);

		namesLengths.forEach(intPrint);

		// convert IntStream to Object Stream, using the boxed method to convert
		// to Stream of Integers.
		List<String> convertedNames = IntStream.range(0, 10).boxed()
				.map(i -> "The " + i).collect(Collectors.toList());

		convertedNames.stream().forEach(print);
	}

	/**
	 * Demo the conception of Non-Interference.
	 * See http://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html#NonInterference for details.
	 */
	@Test
	public void demoNonInterference() {
		List<String> l = new ArrayList<>(Arrays.asList("one", "two"));
		Stream<String> sl = l.stream();

		// this operation is acceptable since it done before the terminal
		// operation
		l.add("three");

		// the terminal operation
		String s = sl.collect(Collectors.joining(" "));

		// not effective since the terminal operation has been executed
		l.remove(0);

		// one two three
		System.out.println(s);

		System.out.println(l);

	}

}
