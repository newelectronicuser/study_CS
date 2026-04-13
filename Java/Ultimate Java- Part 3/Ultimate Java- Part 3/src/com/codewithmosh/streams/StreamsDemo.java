package com.codewithmosh.streams;

import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

/**
 * ============================================================
 *  StreamsDemo — Interview-Ready Java Streams Cheat-Sheet
 * ============================================================
 *
 *  Topics covered:
 *   1.  Creating streams (7 ways)
 *   2.  map() vs flatMap()
 *   3.  filter()
 *   4.  Slicing  — limit / skip / takeWhile / dropWhile
 *   5.  sorted() & Comparator chaining
 *   6.  distinct()
 *   7.  peek()  — debugging
 *   8.  Simple reducers — count / min / max / findFirst / findAny
 *   9.  allMatch / anyMatch / noneMatch
 *  10.  reduce()
 *  11.  Collectors — toList / toSet / toMap / joining
 *  12.  groupingBy  (single-level + downstream collectors)
 *  13.  partitioningBy
 *  14.  Primitive streams — IntStream / LongStream / DoubleStream
 *  15.  Parallel streams
 *  16.  Optional chaining after stream operations
 */
public class StreamsDemo {

    // -----------------------------------------------------------------
    //  Shared sample data
    // -----------------------------------------------------------------
    private static List<Movie> movies() {
        return List.of(
            new Movie("Inception",       800, Genre.THRILLER),
            new Movie("The Dark Knight", 950, Genre.THRILLER),
            new Movie("Avengers",        700, Genre.ACTION),
            new Movie("Iron Man",        650, Genre.ACTION),
            new Movie("Home Alone",      500, Genre.COMEDY),
            new Movie("Elf",             480, Genre.COMEDY),
            new Movie("Interstellar",    870, Genre.THRILLER)
        );
    }

    // =================================================================
    //  MAIN entry point — calls every demo section
    // =================================================================
    public static void show() {

        System.out.println("\n=== 1. CREATING STREAMS ===");
        creatingStreams();

        System.out.println("\n=== 2. MAP vs FLAT-MAP ===");
        mappingElements();

        System.out.println("\n=== 3. FILTERING ===");
        filteringElements();

        System.out.println("\n=== 4. SLICING (limit / skip / takeWhile / dropWhile) ===");
        slicingElements();

        System.out.println("\n=== 5. SORTING ===");
        sortingElements();

        System.out.println("\n=== 6. DISTINCT ===");
        distinctElements();

        System.out.println("\n=== 7. PEEK (debugging) ===");
        peekingElements();

        System.out.println("\n=== 8. SIMPLE REDUCERS (count / min / max / findFirst / findAny) ===");
        simpleReducers();

        System.out.println("\n=== 9. MATCHING (allMatch / anyMatch / noneMatch) ===");
        matchingElements();

        System.out.println("\n=== 10. REDUCE ===");
        reducingStream();

        System.out.println("\n=== 11. COLLECTORS (toList / toSet / toMap / joining) ===");
        collectorsDemo();

        System.out.println("\n=== 12. GROUPING BY ===");
        groupingByDemo();

        System.out.println("\n=== 13. PARTITIONING BY ===");
        partitioningByDemo();

        System.out.println("\n=== 14. PRIMITIVE STREAMS (IntStream / LongStream / DoubleStream) ===");
        primitiveStreams();

        System.out.println("\n=== 15. PARALLEL STREAMS ===");
        parallelStreams();

        System.out.println("\n=== 16. OPTIONAL CHAINING ===");
        optionalChaining();
    }

    // =================================================================
    //  1. CREATING STREAMS
    // =================================================================
    private static void creatingStreams() {
        // a) From a Collection
        List<String> names = List.of("Alice", "Bob", "Charlie");
        names.stream().forEach(System.out::println);

        // b) From an array
        int[] arr = {1, 2, 3};
        Arrays.stream(arr).forEach(System.out::println);

        // c) Stream.of() — arbitrary elements
        Stream.of("x", "y", "z").forEach(System.out::println);

        // d) Stream.generate() — infinite, needs limit()
        Stream.generate(Math::random)
              .limit(3)
              .forEach(n -> System.out.printf("random: %.4f%n", n));

        // e) Stream.iterate() — seed + UnaryOperator
        Stream.iterate(0, n -> n + 2)
              .limit(5)
              .forEach(n -> System.out.print(n + " ")); // 0 2 4 6 8
        System.out.println();

        // f) Stream.iterate() — Java 9 with predicate (replaces takeWhile pattern)
        Stream.iterate(1, n -> n <= 10, n -> n + 1)
              .forEach(n -> System.out.print(n + " ")); // 1 2 3 … 10
        System.out.println();

        // g) IntStream.range / rangeClosed
        IntStream.range(1, 4).forEach(n -> System.out.print(n + " ")); // 1 2 3
        System.out.println();
        IntStream.rangeClosed(1, 4).forEach(n -> System.out.print(n + " ")); // 1 2 3 4
        System.out.println();
    }

    // =================================================================
    //  2. MAP vs FLAT-MAP
    // =================================================================
    private static void mappingElements() {
        // map() — one-to-one transform
        List<String> nameLengths = List.of("alice", "bob", "charlie")
            .stream()
            .map(String::toUpperCase)         // String → String
            .collect(Collectors.toList());
        System.out.println("Uppercase: " + nameLengths);

        // map() on domain objects
        List<String> titles = movies().stream()
            .map(Movie::getTitle)
            .collect(Collectors.toList());
        System.out.println("Titles: " + titles);

        // flatMap() — one-to-many: flatten nested lists
        List<List<Integer>> nested = List.of(
            List.of(1, 2, 3),
            List.of(4, 5),
            List.of(6)
        );
        List<Integer> flat = nested.stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        System.out.println("Flat list: " + flat); // [1,2,3,4,5,6]

        // flatMap() practical: extract all words from sentences
        List<String> sentences = List.of("Hello World", "Java Streams");
        List<String> words = sentences.stream()
            .flatMap(s -> Arrays.stream(s.split(" ")))
            .collect(Collectors.toList());
        System.out.println("Words: " + words); // [Hello, World, Java, Streams]
    }

    // =================================================================
    //  3. FILTERING
    // =================================================================
    private static void filteringElements() {
        // Filter movies with > 700 likes
        movies().stream()
            .filter(m -> m.getLikes() > 700)
            .map(Movie::getTitle)
            .forEach(System.out::println);

        // Chain multiple filters (same as using &&)
        long count = movies().stream()
            .filter(m -> m.getGenre() == Genre.THRILLER)
            .filter(m -> m.getLikes() > 800)
            .count();
        System.out.println("Popular thrillers: " + count);

        // Filter non-blank strings (common interview question)
        List<String> data = List.of("Alice", "", "  ", "Bob");
        List<String> clean = data.stream()
            .filter(s -> !s.isBlank())
            .collect(Collectors.toList());
        System.out.println("Non-blank: " + clean);
    }

    // =================================================================
    //  4. SLICING
    // =================================================================
    private static void slicingElements() {
        // limit() — top-N (pagination first page)
        System.out.println("First 3 movies:");
        movies().stream()
            .limit(3)
            .map(Movie::getTitle)
            .forEach(System.out::println);

        // skip() — pagination: skip first page
        System.out.println("Skip 3 movies:");
        movies().stream()
            .skip(3)
            .map(Movie::getTitle)
            .forEach(System.out::println);

        // limit + skip → page(pageNumber, pageSize)
        int pageSize = 2, pageNumber = 2;
        System.out.println("Page " + pageNumber + ":");
        movies().stream()
            .skip((long)(pageNumber - 1) * pageSize)
            .limit(pageSize)
            .map(Movie::getTitle)
            .forEach(System.out::println);

        // takeWhile() — Java 9+ stops when predicate is false
        Stream.of(2, 4, 6, 7, 8, 10)
              .takeWhile(n -> n % 2 == 0)
              .forEach(n -> System.out.print(n + " ")); // 2 4 6
        System.out.println();

        // dropWhile() — Java 9+ drops elements until predicate fails
        Stream.of(1, 2, 3, 10, 11, 12)
              .dropWhile(n -> n < 10)
              .forEach(n -> System.out.print(n + " ")); // 10 11 12
        System.out.println();
    }

    // =================================================================
    //  5. SORTING
    // =================================================================
    private static void sortingElements() {
        // Natural order
        List<Integer> sorted = Stream.of(5, 1, 4, 2, 3)
            .sorted()
            .collect(Collectors.toList());
        System.out.println("Natural: " + sorted);

        // Reverse order
        List<Integer> desc = Stream.of(5, 1, 4, 2, 3)
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
        System.out.println("Desc: " + desc);

        // Sort by field (likes ascending)
        System.out.println("Movies by likes ↑:");
        movies().stream()
            .sorted(Comparator.comparingInt(Movie::getLikes))
            .map(m -> m.getTitle() + " (" + m.getLikes() + ")")
            .forEach(System.out::println);

        // Multi-key sort: genre asc, then likes desc
        System.out.println("Movies by genre then likes ↓:");
        movies().stream()
            .sorted(Comparator.comparing(Movie::getGenre)
                              .thenComparingInt(Movie::getLikes)
                              .reversed())
            .map(m -> m.getGenre() + " | " + m.getTitle())
            .forEach(System.out::println);
    }

    // =================================================================
    //  6. DISTINCT
    // =================================================================
    private static void distinctElements() {
        List<Integer> nums = List.of(1, 2, 2, 3, 3, 3, 4);
        List<Integer> unique = nums.stream()
            .distinct()
            .collect(Collectors.toList());
        System.out.println("Unique: " + unique); // [1, 2, 3, 4]

        // Distinct genres from movies (uses equals/hashCode of enum)
        List<Genre> genres = movies().stream()
            .map(Movie::getGenre)
            .distinct()
            .collect(Collectors.toList());
        System.out.println("Genres: " + genres);
    }

    // =================================================================
    //  7. PEEK — debugging intermediate state
    // =================================================================
    private static void peekingElements() {
        // peek() lets you inspect elements without consuming the stream
        List<String> result = movies().stream()
            .filter(m -> m.getLikes() > 700)
            .peek(m -> System.out.println("[after filter] " + m.getTitle()))
            .map(Movie::getTitle)
            .peek(t -> System.out.println("[after map]   " + t))
            .collect(Collectors.toList());
        System.out.println("Result: " + result);
    }

    // =================================================================
    //  8. SIMPLE REDUCERS
    // =================================================================
    private static void simpleReducers() {
        // count()
        long n = movies().stream()
            .filter(m -> m.getGenre() == Genre.ACTION)
            .count();
        System.out.println("Action movies: " + n);

        // min() / max()
        Optional<Movie> least = movies().stream()
            .min(Comparator.comparingInt(Movie::getLikes));
        least.ifPresent(m -> System.out.println("Least liked: " + m.getTitle()));

        Optional<Movie> most = movies().stream()
            .max(Comparator.comparingInt(Movie::getLikes));
        most.ifPresent(m -> System.out.println("Most liked: " + m.getTitle()));

        // findFirst() — deterministic; use with filter for "find one"
        Optional<Movie> firstThriller = movies().stream()
            .filter(m -> m.getGenre() == Genre.THRILLER)
            .findFirst();
        firstThriller.ifPresent(m -> System.out.println("First thriller: " + m.getTitle()));

        // findAny() — non-deterministic; faster with parallel streams
        Optional<Movie> anyComedy = movies().stream()
            .filter(m -> m.getGenre() == Genre.COMEDY)
            .findAny();
        anyComedy.ifPresent(m -> System.out.println("Any comedy: " + m.getTitle()));
    }

    // =================================================================
    //  9. MATCHING
    // =================================================================
    private static void matchingElements() {
        // allMatch — every element satisfies the predicate?
        boolean allHaveGenre = movies().stream()
            .allMatch(m -> m.getGenre() != null);
        System.out.println("All have genre: " + allHaveGenre); // true

        // anyMatch — at least one element satisfies?
        boolean hasBlockbuster = movies().stream()
            .anyMatch(m -> m.getLikes() > 900);
        System.out.println("Any blockbuster (>900 likes): " + hasBlockbuster);

        // noneMatch — no element satisfies?
        boolean noFlop = movies().stream()
            .noneMatch(m -> m.getLikes() < 100);
        System.out.println("No flop (<100 likes): " + noFlop);
    }

    // =================================================================
    //  10. REDUCE
    // =================================================================
    private static void reducingStream() {
        List<Integer> nums = List.of(1, 2, 3, 4, 5);

        // reduce(identity, accumulator)
        int sum = nums.stream().reduce(0, Integer::sum);
        System.out.println("Sum: " + sum); // 15

        // reduce(accumulator) → Optional (no identity)
        Optional<Integer> product = nums.stream()
            .reduce((a, b) -> a * b);
        product.ifPresent(p -> System.out.println("Product: " + p)); // 120

        // Sum of specific field (map first, then reduce)
        int totalLikes = movies().stream()
            .map(Movie::getLikes)
            .reduce(0, Integer::sum);
        System.out.println("Total likes: " + totalLikes);

        // Equivalent using mapToInt + sum (preferred for primitives)
        int totalLikes2 = movies().stream()
            .mapToInt(Movie::getLikes)
            .sum();
        System.out.println("Total likes (primitive): " + totalLikes2);
    }

    // =================================================================
    //  11. COLLECTORS
    // =================================================================
    private static void collectorsDemo() {
        // toList()
        List<String> titles = movies().stream()
            .map(Movie::getTitle)
            .collect(Collectors.toList());
        System.out.println("Title list: " + titles);

        // toUnmodifiableList() / List.copyOf() — immutable
        List<String> immutable = movies().stream()
            .map(Movie::getTitle)
            .collect(Collectors.toUnmodifiableList());
        System.out.println("Immutable list size: " + immutable.size());

        // toSet() — removes duplicates
        Set<Genre> genreSet = movies().stream()
            .map(Movie::getGenre)
            .collect(Collectors.toSet());
        System.out.println("Genre set: " + genreSet);

        // toMap(keyMapper, valueMapper)
        Map<String, Integer> titleToLikes = movies().stream()
            .collect(Collectors.toMap(Movie::getTitle, Movie::getLikes));
        System.out.println("Title→Likes: " + titleToLikes);

        // toMap() with merge function to handle duplicate keys
        Map<Genre, Integer> genreMaxLikes = movies().stream()
            .collect(Collectors.toMap(
                Movie::getGenre,
                Movie::getLikes,
                Integer::max          // keep the max when genre already mapped
            ));
        System.out.println("Genre→MaxLikes: " + genreMaxLikes);

        // joining() — string concatenation
        String csv = movies().stream()
            .map(Movie::getTitle)
            .collect(Collectors.joining(", "));
        System.out.println("CSV: " + csv);

        String wrapped = movies().stream()
            .map(Movie::getTitle)
            .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("Wrapped: " + wrapped);

        // counting() as downstream
        Map<Genre, Long> countPerGenre = movies().stream()
            .collect(Collectors.groupingBy(Movie::getGenre, Collectors.counting()));
        System.out.println("Count per genre: " + countPerGenre);

        // summarizingInt
        IntSummaryStatistics stats = movies().stream()
            .collect(Collectors.summarizingInt(Movie::getLikes));
        System.out.printf("Likes → min=%d max=%d avg=%.1f%n",
            stats.getMin(), stats.getMax(), stats.getAverage());
    }

    // =================================================================
    //  12. GROUPING BY
    // =================================================================
    private static void groupingByDemo() {
        // Simple grouping — SQL: SELECT genre, list(movies) GROUP BY genre
        Map<Genre, List<Movie>> byGenre = movies().stream()
            .collect(Collectors.groupingBy(Movie::getGenre));
        byGenre.forEach((g, list) ->
            System.out.println(g + " → " + list.stream().map(Movie::getTitle).toList()));

        // Downstream: count
        Map<Genre, Long> counts = movies().stream()
            .collect(Collectors.groupingBy(
                Movie::getGenre,
                Collectors.counting()
            ));
        System.out.println("Counts: " + counts);

        // Downstream: mapping → collect titles per genre
        Map<Genre, List<String>> titlesByGenre = movies().stream()
            .collect(Collectors.groupingBy(
                Movie::getGenre,
                Collectors.mapping(Movie::getTitle, Collectors.toList())
            ));
        System.out.println("Titles by genre: " + titlesByGenre);

        // Downstream: sum of likes per genre
        Map<Genre, Integer> likesByGenre = movies().stream()
            .collect(Collectors.groupingBy(
                Movie::getGenre,
                Collectors.summingInt(Movie::getLikes)
            ));
        System.out.println("Likes by genre: " + likesByGenre);

        // Multi-level grouping (genre → likes bucket)
        Map<Genre, Map<String, List<Movie>>> multilevel = movies().stream()
            .collect(Collectors.groupingBy(
                Movie::getGenre,
                Collectors.groupingBy(m -> m.getLikes() >= 700 ? "Popular" : "Average")
            ));
        multilevel.forEach((g, inner) ->
            inner.forEach((bucket, list) ->
                System.out.println(g + " / " + bucket + " → " +
                    list.stream().map(Movie::getTitle).toList())));
    }

    // =================================================================
    //  13. PARTITIONING BY
    // =================================================================
    private static void partitioningByDemo() {
        // Always produces a Map<Boolean, List<T>>
        Map<Boolean, List<Movie>> partition = movies().stream()
            .collect(Collectors.partitioningBy(m -> m.getLikes() >= 700));

        System.out.println("Popular (≥700 likes):");
        partition.get(true).forEach(m -> System.out.println("  " + m.getTitle()));
        System.out.println("Average (<700 likes):");
        partition.get(false).forEach(m -> System.out.println("  " + m.getTitle()));

        // Downstream: counting partitions
        Map<Boolean, Long> partitionCount = movies().stream()
            .collect(Collectors.partitioningBy(
                m -> m.getGenre() == Genre.THRILLER,
                Collectors.counting()
            ));
        System.out.println("Thriller/Non-thriller count: " + partitionCount);
    }

    // =================================================================
    //  14. PRIMITIVE STREAMS — avoid boxing overhead
    // =================================================================
    private static void primitiveStreams() {
        // IntStream.range / rangeClosed
        int sumTo100 = IntStream.rangeClosed(1, 100).sum();
        System.out.println("Sum 1–100: " + sumTo100); // 5050

        // average() → OptionalDouble
        OptionalDouble avg = IntStream.of(10, 20, 30).average();
        avg.ifPresent(a -> System.out.printf("Avg: %.1f%n", a)); // 20.0

        // summaryStatistics
        IntSummaryStatistics stats = IntStream.of(3, 1, 4, 1, 5, 9)
            .summaryStatistics();
        System.out.println("Max: " + stats.getMax());
        System.out.println("Min: " + stats.getMin());
        System.out.println("Sum: " + stats.getSum());

        // mapToInt from Object stream — avoids boxing
        int totalLikes = movies().stream()
            .mapToInt(Movie::getLikes)
            .sum();
        System.out.println("Total likes (IntStream): " + totalLikes);

        // mapToObj — primitive stream back to Object stream
        String result = IntStream.rangeClosed(1, 5)
            .mapToObj(Integer::toString)
            .collect(Collectors.joining("-")); // 1-2-3-4-5
        System.out.println("Joined: " + result);

        // LongStream
        long factorial = LongStream.rangeClosed(1, 10)
            .reduce(1L, (a, b) -> a * b);
        System.out.println("10! = " + factorial);

        // DoubleStream
        double sumDoubles = DoubleStream.of(1.5, 2.5, 3.0).sum();
        System.out.println("Double sum: " + sumDoubles);
    }

    // =================================================================
    //  15. PARALLEL STREAMS
    // =================================================================
    private static void parallelStreams() {
        // parallelStream() — uses ForkJoinPool.commonPool()
        long parallelSum = IntStream.rangeClosed(1, 1_000_000)
            .parallel()
            .asLongStream()
            .sum();
        System.out.println("Parallel sum 1–1 000 000: " + parallelSum);

        // .parallel() can also be called on any stream
        List<String> titles = movies().parallelStream()
            .filter(m -> m.getLikes() > 600)
            .map(Movie::getTitle)
            .collect(Collectors.toList());
        System.out.println("Parallel filtered titles: " + titles);

        // NOTE: parallel streams share a global thread pool; for CPU-bound
        // tasks they help; for I/O-bound task use Executor/CompletableFuture.
    }

    // =================================================================
    //  16. OPTIONAL CHAINING (stream terminal returns Optional)
    // =================================================================
    private static void optionalChaining() {
        // map on Optional — transform value if present
        Optional<String> bestTitle = movies().stream()
            .max(Comparator.comparingInt(Movie::getLikes))
            .map(Movie::getTitle);
        System.out.println("Best movie: " + bestTitle.orElse("None"));

        // orElse / orElseGet / orElseThrow
        String title = movies().stream()
            .filter(m -> m.getLikes() > 10_000)
            .map(Movie::getTitle)
            .findFirst()
            .orElse("No blockbuster found");
        System.out.println(title);

        // ifPresentOrElse (Java 9+)
        movies().stream()
            .filter(m -> m.getGenre() == Genre.COMEDY)
            .findFirst()
            .ifPresentOrElse(
                m -> System.out.println("Found comedy: " + m.getTitle()),
                () -> System.out.println("No comedy found")
            );

        // stream() on Optional (Java 9+) — bridge Optional into a Stream
        long comedyCount = movies().stream()
            .filter(m -> m.getGenre() == Genre.COMEDY)
            .findFirst()
            .stream()                       // Optional<Movie> → Stream<Movie> (0 or 1)
            .count();
        System.out.println("Comedy found (0 or 1): " + comedyCount);

        // Null safety via Optional.ofNullable
        String nullableTitle = null;
        String safe = Optional.ofNullable(nullableTitle)
            .map(String::toUpperCase)
            .orElse("DEFAULT");
        System.out.println("Safe title: " + safe);
    }
}
