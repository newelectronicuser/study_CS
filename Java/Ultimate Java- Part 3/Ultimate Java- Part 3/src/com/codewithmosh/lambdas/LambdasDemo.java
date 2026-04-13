package com.codewithmosh.lambdas;

import java.util.*;
import java.util.function.*;

/**
 * ================================================================
 *  LambdasDemo — Interview-Ready Lambda & Functional Interface Sheet
 * ================================================================
 *
 *  Topics covered:
 *   1.  Functional Interface — custom (Printer) vs anonymous inner class
 *   2.  Lambda syntax — expression vs block, type inference
 *   3.  Variable capture — effectively final rule
 *   4.  Method references — 4 kinds
 *   5.  Consumer<T>        — accept(), andThen()
 *   6.  BiConsumer<T,U>    — two-argument consumer
 *   7.  Supplier<T>        — get(), lazy evaluation
 *   8.  Function<T,R>      — apply(), andThen(), compose(), identity()
 *   9.  BiFunction<T,U,R>  — two-argument function
 *  10.  Predicate<T>       — test(), and(), or(), negate(), not()
 *  11.  BiPredicate<T,U>   — two-argument predicate
 *  12.  UnaryOperator<T>   — extends Function<T,T>
 *  13.  BinaryOperator<T>  — extends BiFunction<T,T,T>
 *  14.  Primitive variants — IntFunction, ToIntFunction, IntUnaryOperator …
 *  15.  Function composition pipeline — real-world example
 *  16.  Returning lambdas from methods (Higher-order functions)
 *  17.  Passing lambdas as parameters
 */
public class LambdasDemo {

    // =============================================================
    //  MAIN entry point
    // =============================================================
    public static void show() {
        System.out.println("\n--- 1. Custom Functional Interface (Printer) ---");
        customFunctionalInterface();

        System.out.println("\n--- 2. Lambda Syntax ---");
        lambdaSyntax();

        System.out.println("\n--- 3. Variable Capture ---");
        variableCapture();

        System.out.println("\n--- 4. Method References ---");
        methodReferences();

        System.out.println("\n--- 5. Consumer<T> ---");
        consumerDemo();

        System.out.println("\n--- 6. BiConsumer<T,U> ---");
        biConsumerDemo();

        System.out.println("\n--- 7. Supplier<T> ---");
        supplierDemo();

        System.out.println("\n--- 8. Function<T,R> ---");
        functionDemo();

        System.out.println("\n--- 9. BiFunction<T,U,R> ---");
        biFunctionDemo();

        System.out.println("\n--- 10. Predicate<T> ---");
        predicateDemo();

        System.out.println("\n--- 11. BiPredicate<T,U> ---");
        biPredicateDemo();

        System.out.println("\n--- 12. UnaryOperator<T> ---");
        unaryOperatorDemo();

        System.out.println("\n--- 13. BinaryOperator<T> ---");
        binaryOperatorDemo();

        System.out.println("\n--- 14. Primitive Specialisations ---");
        primitiveVariantsDemo();

        System.out.println("\n--- 15. Function Composition Pipeline ---");
        compositionPipeline();

        System.out.println("\n--- 16. Returning Lambdas (Higher-order Functions) ---");
        returningLambdas();

        System.out.println("\n--- 17. Passing Lambdas as Parameters ---");
        passingLambdas();
    }

    // =============================================================
    //  1. Custom Functional Interface
    //     Printer is a @FunctionalInterface with a single abstract method
    // =============================================================
    private static void customFunctionalInterface() {
        // a) Old style — anonymous inner class
        Printer anonPrinter = new Printer() {
            @Override
            public void print(String message) {
                System.out.println("[Anon] " + message);
            }
        };
        anonPrinter.print("Hello from anonymous class");

        // b) Named implementation class (ConsolePrinter)
        Printer consolePrinter = new ConsolePrinter();
        consolePrinter.print("Hello from ConsolePrinter");

        // c) Lambda — most concise, same result
        Printer lambdaPrinter = message -> System.out.println("[Lambda] " + message);
        lambdaPrinter.print("Hello from lambda");

        // d) Method reference (System.out::println matches void print(String))
        Printer refPrinter = System.out::println;
        refPrinter.print("Hello from method reference");
    }

    // =============================================================
    //  2. Lambda Syntax
    // =============================================================
    private static void lambdaSyntax() {
        // Expression lambda (no braces, implicit return)
        Function<Integer, Integer> square   = n -> n * n;
        Function<Integer, Integer> squareFn = (Integer n) -> n * n; // explicit type
        System.out.println("square(5): " + square.apply(5));

        // Block lambda (braces, explicit return)
        Function<Integer, Integer> factorial = n -> {
            int result = 1;
            for (int i = 2; i <= n; i++) result *= i;
            return result;
        };
        System.out.println("factorial(5): " + factorial.apply(5));

        // No parameters
        Runnable greet = () -> System.out.println("Hello from Runnable lambda");
        greet.run();

        // Multiple parameters (parentheses required)
        BinaryOperator<Integer> add = (a, b) -> a + b;
        System.out.println("add(3,4): " + add.apply(3, 4));
    }

    // =============================================================
    //  3. Variable Capture
    //     Local variables must be effectively final (assigned only once)
    // =============================================================
    private static void variableCapture() {
        // Effectively final — never reassigned after first assignment
        int factor = 3;
        Function<Integer, Integer> triple = n -> n * factor; // captures 'factor'
        System.out.println("triple(7): " + triple.apply(7));

        // Instance/static fields can be freely mutated inside a lambda
        // because the lambda captures `this` (or the class), not a local copy

        // Closure over loop variable — must copy to effectively-final local
        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final int captured = i; // copy → effectively final
            tasks.add(() -> System.out.println("Task " + captured));
        }
        tasks.forEach(Runnable::run); // Task 0, Task 1, Task 2

        // The following would NOT compile (i is not effectively final):
        // tasks.add(() -> System.out.println(i)); // ← compile error
    }

    // =============================================================
    //  4. Method References — 4 kinds
    // =============================================================
    private static void methodReferences() {
        List<String> words = List.of("banana", "apple", "cherry");

        // a) Static method reference:  ClassName::staticMethod
        //    equivalent lambda: (x, y) -> Math.max(x, y)
        BinaryOperator<Integer> max = Math::max;
        System.out.println("Math::max(3,7): " + max.apply(3, 7));

        // b) Instance method of a specific object:  object::instanceMethod
        //    equivalent lambda: () -> "hello".toUpperCase()
        String prefix = "Hello";
        Supplier<String> greet = prefix::toUpperCase;
        System.out.println("prefix::toUpperCase: " + greet.get());

        // c) Instance method of an arbitrary object:  ClassName::instanceMethod
        //    equivalent lambda: s -> s.toLowerCase()
        Function<String, String> lower = String::toLowerCase;
        words.stream().map(lower).forEach(System.out::println);

        // d) Constructor reference:  ClassName::new
        //    equivalent lambda: () -> new ArrayList<>()
        Supplier<List<String>> listFactory = ArrayList::new;
        List<String> newList = listFactory.get();
        newList.add("created via constructor reference");
        System.out.println(newList);

        // e) Instance method with arg (BiFunction style)
        BiFunction<String, String, Boolean> startsWith = String::startsWith;
        System.out.println("'apple'.startsWith('app'): " + startsWith.apply("apple", "app"));
    }

    // =============================================================
    //  5. Consumer<T>  — void accept(T t)
    //     Use when you need a side-effect (print, save, send)
    // =============================================================
    private static void consumerDemo() {
        Consumer<String> print  = s -> System.out.println("Print:  " + s);
        Consumer<String> upper  = s -> System.out.println("Upper:  " + s.toUpperCase());
        Consumer<String> length = s -> System.out.println("Length: " + s.length());

        print.accept("hello");

        // andThen — chain consumers; all run on the SAME input in order
        Consumer<String> pipeline = print.andThen(upper).andThen(length);
        pipeline.accept("Java");
        // Print:  Java
        // Upper:  JAVA
        // Length: 4

        // Practical: forEach uses Consumer
        List<Integer> nums = List.of(1, 2, 3, 4, 5);
        nums.forEach(n -> System.out.print(n + " "));
        System.out.println();
    }

    // =============================================================
    //  6. BiConsumer<T,U>  — void accept(T t, U u)
    // =============================================================
    private static void biConsumerDemo() {
        BiConsumer<String, Integer> entry = (k, v) ->
            System.out.println(k + " → " + v);

        entry.accept("Alice", 95);

        // andThen on BiConsumer
        BiConsumer<String, Integer> log  = (k, v) -> System.out.println("[LOG] " + k + "=" + v);
        BiConsumer<String, Integer> both = entry.andThen(log);
        both.accept("Bob", 80);

        // Practical: Map.forEach uses BiConsumer
        Map<String, Integer> scores = Map.of("Alice", 95, "Bob", 80);
        scores.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    // =============================================================
    //  7. Supplier<T>  — T get()
    //     Use for lazy evaluation / factory / default value generation
    // =============================================================
    private static void supplierDemo() {
        // Basic
        Supplier<Double> random = Math::random;
        System.out.println("Random: " + random.get());

        // Lazy instantiation — object created only when get() is called
        Supplier<List<String>> listFactory = ArrayList::new;
        List<String> list = listFactory.get();
        list.add("lazy");
        System.out.println("Lazy list: " + list);

        // Practical: Optional.orElseGet uses Supplier (avoids creating object if not needed)
        Optional<String> opt = Optional.empty();
        String value = opt.orElseGet(() -> "computed default");
        System.out.println("orElseGet: " + value);

        // Custom: getOrCreate pattern
        Supplier<String> expensiveOp = () -> {
            // imagine a DB call here
            return "result from DB";
        };
        System.out.println("DB result (lazy): " + expensiveOp.get());
    }

    // =============================================================
    //  8. Function<T,R>  — R apply(T t)
    //     andThen: f.andThen(g)  → g(f(x))
    //     compose:  f.compose(g) → f(g(x))
    //     identity: Function.identity() → x -> x
    // =============================================================
    private static void functionDemo() {
        Function<String, Integer> length = String::length;
        System.out.println("length('hello'): " + length.apply("hello")); // 5

        // andThen — pipe output of f into g
        Function<Integer, Integer> doubled = length.andThen(n -> n * 2);
        System.out.println("length then double 'Java': " + doubled.apply("Java")); // 8

        // compose — run g first, then f
        Function<Integer, Integer> add1  = n -> n + 1;
        Function<Integer, Integer> times3 = n -> n * 3;

        // andThen: (1+1)*3 = 6
        System.out.println("add1.andThen(times3).apply(1): " + add1.andThen(times3).apply(1));

        // compose: (1*3)+1 = 4
        System.out.println("add1.compose(times3).apply(1): " + add1.compose(times3).apply(1));

        // identity — useful as a no-op placeholder
        Function<String, String> identity = Function.identity();
        System.out.println("identity('abc'): " + identity.apply("abc"));

        // Type-changing chain: String → Integer → Boolean
        Function<String, Integer> parse     = Integer::parseInt;
        Function<Integer, Boolean> isEven   = n -> n % 2 == 0;
        Function<String, Boolean> isEvenStr = parse.andThen(isEven);
        System.out.println("'42' is even: " + isEvenStr.apply("42"));
        System.out.println("'15' is even: " + isEvenStr.apply("15"));
    }

    // =============================================================
    //  9. BiFunction<T,U,R>  — R apply(T t, U u)
    // =============================================================
    private static void biFunctionDemo() {
        BiFunction<String, Integer, String> repeat =
            (s, n) -> s.repeat(n);
        System.out.println("repeat('ha', 3): " + repeat.apply("ha", 3)); // hahaha

        // andThen on BiFunction
        BiFunction<Integer, Integer, Integer> multiply = (a, b) -> a * b;
        Function<Integer, String> toStr = n -> "Result=" + n;
        BiFunction<Integer, Integer, String> multiplyThenStr = multiply.andThen(toStr);
        System.out.println("multiplyThenStr(4,5): " + multiplyThenStr.apply(4, 5));
    }

    // =============================================================
    //  10. Predicate<T>  — boolean test(T t)
    //      and(), or(), negate(), not() (Java 11+)
    // =============================================================
    private static void predicateDemo() {
        Predicate<String> isLong   = s -> s.length() > 5;
        Predicate<String> hasUpper = s -> s.chars().anyMatch(Character::isUpperCase);
        Predicate<String> isBlank  = String::isBlank;

        System.out.println("isLong('Java'):     " + isLong.test("Java"));       // false
        System.out.println("isLong('Antigrav'): " + isLong.test("Antigravity")); // true

        // and() — both must be true
        Predicate<String> longAndHasUpper = isLong.and(hasUpper);
        System.out.println("longAndHasUpper('HelloWorld'): " + longAndHasUpper.test("HelloWorld")); // true

        // or() — at least one must be true
        Predicate<String> longOrHasUpper = isLong.or(hasUpper);
        System.out.println("longOrHasUpper('Hi'): " + longOrHasUpper.test("Hi")); // true (has upper)

        // negate() — logical NOT
        Predicate<String> isShort = isLong.negate();
        System.out.println("isShort('Java'): " + isShort.test("Java")); // true

        // Predicate.not() — Java 11+ static helper (great for method refs)
        List<String> data = List.of("Alice", "", "  ", "Bob");
        data.stream()
            .filter(Predicate.not(String::isBlank))
            .forEach(System.out::println); // Alice, Bob

        // Practical: filter pipeline
        List<Integer> nums = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> gt5    = n -> n > 5;
        nums.stream()
            .filter(isEven.and(gt5))
            .forEach(n -> System.out.print(n + " ")); // 6 8 10
        System.out.println();
    }

    // =============================================================
    //  11. BiPredicate<T,U>  — boolean test(T t, U u)
    // =============================================================
    private static void biPredicateDemo() {
        BiPredicate<String, Integer> longerThan = (s, n) -> s.length() > n;
        System.out.println("'hello' longer than 3: " + longerThan.test("hello", 3)); // true
        System.out.println("'hi' longer than 3:    " + longerThan.test("hi", 3));    // false

        // and / or / negate work the same as Predicate
        BiPredicate<String, String> startsWith = String::startsWith;
        BiPredicate<String, String> endsWith   = String::endsWith;
        BiPredicate<String, String> startsOrEnds = startsWith.or(endsWith);
        System.out.println("'hello' starts or ends 'he': " + startsOrEnds.test("hello", "he")); // true
    }

    // =============================================================
    //  12. UnaryOperator<T>  — extends Function<T,T>
    //      Input and output are the SAME type
    // =============================================================
    private static void unaryOperatorDemo() {
        UnaryOperator<Integer> square    = n -> n * n;
        UnaryOperator<Integer> increment = n -> n + 1;
        UnaryOperator<String>  shout     = s -> s.toUpperCase() + "!";

        System.out.println("square(5):    " + square.apply(5));    // 25
        System.out.println("increment(5): " + increment.apply(5)); // 6

        // andThen chains — increment first, then square: (1+1)^2 = 4
        System.out.println("increment.andThen(square).apply(1): "
            + increment.andThen(square).apply(1)); // 4

        // compose — square first, then increment: (1^2)+1 = 2
        System.out.println("increment.compose(square).apply(1): "
            + increment.compose(square).apply(1)); // 2

        System.out.println("shout('java'): " + shout.apply("java")); // JAVA!

        // Practical: List.replaceAll uses UnaryOperator
        List<String> list = new ArrayList<>(List.of("hello", "world", "java"));
        list.replaceAll(String::toUpperCase);
        System.out.println("replaceAll toUpperCase: " + list);

        // UnaryOperator.identity()
        UnaryOperator<String> id = UnaryOperator.identity();
        System.out.println("identity('abc'): " + id.apply("abc"));
    }

    // =============================================================
    //  13. BinaryOperator<T>  — extends BiFunction<T,T,T>
    //      Two inputs and output are ALL the same type
    // =============================================================
    private static void binaryOperatorDemo() {
        BinaryOperator<Integer> add      = (a, b) -> a + b;
        BinaryOperator<Integer> multiply = (a, b) -> a * b;
        BinaryOperator<String>  concat   = (a, b) -> a + b;

        System.out.println("add(3,4): "      + add.apply(3, 4));        // 7
        System.out.println("multiply(3,4): " + multiply.apply(3, 4));   // 12
        System.out.println("concat: "        + concat.apply("Hello ", "World")); // Hello World

        // BinaryOperator.minBy / maxBy — returns a BinaryOperator that picks min / max
        BinaryOperator<Integer> minBy = BinaryOperator.minBy(Comparator.naturalOrder());
        BinaryOperator<Integer> maxBy = BinaryOperator.maxBy(Comparator.naturalOrder());
        System.out.println("minBy(3,7): " + minBy.apply(3, 7)); // 3
        System.out.println("maxBy(3,7): " + maxBy.apply(3, 7)); // 7

        // Practical: Stream.reduce uses BinaryOperator
        List<Integer> nums = List.of(1, 2, 3, 4, 5);
        int sum = nums.stream().reduce(0, Integer::sum);   // BinaryOperator
        System.out.println("reduce sum: " + sum);          // 15

        Optional<Integer> product = nums.stream().reduce((a, b) -> a * b);
        System.out.println("reduce product: " + product.orElse(0)); // 120
    }

    // =============================================================
    //  14. Primitive Specialisations — avoid boxing/unboxing overhead
    // =============================================================
    private static void primitiveVariantsDemo() {
        // IntFunction<R>      — int → R
        IntFunction<String> intToStr = n -> "Number " + n;
        System.out.println(intToStr.apply(42));

        // ToIntFunction<T>    — T → int
        ToIntFunction<String> strLen = String::length;
        System.out.println("length of 'hello': " + strLen.applyAsInt("hello"));

        // IntUnaryOperator    — int → int (no boxing at all)
        IntUnaryOperator doubleIt = n -> n * 2;
        System.out.println("double(7): " + doubleIt.applyAsInt(7));

        // IntBinaryOperator   — (int, int) → int
        IntBinaryOperator addInts = (a, b) -> a + b;
        System.out.println("addInts(3,4): " + addInts.applyAsInt(3, 4));

        // IntPredicate        — int → boolean
        IntPredicate isEven = n -> n % 2 == 0;
        System.out.println("isEven(4): " + isEven.test(4));
        System.out.println("isEven(5): " + isEven.test(5));

        // IntConsumer         — int → void
        IntConsumer printInt = System.out::println;
        printInt.accept(100);

        // IntSupplier         — () → int
        IntSupplier roll = () -> (int)(Math.random() * 6) + 1;
        System.out.println("dice roll: " + roll.getAsInt());

        // ToIntBiFunction<T,U> — (T,U) → int
        ToIntBiFunction<String, String> compareLen =
            (a, b) -> Integer.compare(a.length(), b.length());
        System.out.println("compareLen('hi','hello'): " + compareLen.applyAsInt("hi", "hello"));
    }

    // =============================================================
    //  15. Function Composition Pipeline — real-world example
    //      "User registration pipeline"
    // =============================================================
    private static void compositionPipeline() {
        // Step 1: trim whitespace
        UnaryOperator<String> trim    = String::trim;

        // Step 2: lowercase
        UnaryOperator<String> toLower = String::toLowerCase;

        // Step 3: validate non-empty
        Predicate<String> nonEmpty = s -> !s.isEmpty();

        // Step 4: format as username
        UnaryOperator<String> addPrefix = s -> "user_" + s;

        // Compose as a Function pipeline using andThen
        Function<String, String> sanitize = trim.andThen(toLower).andThen(addPrefix);

        List<String> rawInputs = List.of("  Alice  ", "BOB", "", "  Carol");
        for (String input : rawInputs) {
            String trimmed = trim.apply(input);
            if (nonEmpty.test(trimmed)) {
                System.out.println("Registered: " + sanitize.apply(input));
            } else {
                System.out.println("Skipped empty input.");
            }
        }

        // String formatting pipeline combining multiple Function.andThen calls
        Function<String, String> formatPipeline =
            ((Function<String, String>) String::trim)
                .andThen(String::toUpperCase)
                .andThen(s -> "\"" + s + "\"");

        System.out.println(formatPipeline.apply("   hello world   ")); // "HELLO WORLD"
    }

    // =============================================================
    //  16. Returning Lambdas — Higher-order functions
    //      A method returns a lambda / functional interface
    // =============================================================
    private static void returningLambdas() {
        // Factory that returns a Predicate
        Predicate<Integer> greaterThan5  = greaterThan(5);
        Predicate<Integer> greaterThan10 = greaterThan(10);

        System.out.println("7 > 5?:  " + greaterThan5.test(7));   // true
        System.out.println("7 > 10?: " + greaterThan10.test(7));  // false

        // Factory that returns a multiplier Function
        Function<Integer, Integer> triple = multiplierOf(3);
        Function<Integer, Integer> quadruple = multiplierOf(4);
        System.out.println("triple(5):    " + triple.apply(5));    // 15
        System.out.println("quadruple(5): " + quadruple.apply(5)); // 20

        // Compose returned functions
        Function<Integer, Integer> tripleThenQuadruple = triple.andThen(quadruple);
        System.out.println("triple then quadruple(2): " + tripleThenQuadruple.apply(2)); // 24
    }

    /** Returns a Predicate that tests if n > threshold. */
    private static Predicate<Integer> greaterThan(int threshold) {
        return n -> n > threshold; // threshold is effectively final — captured
    }

    /** Returns a Function that multiplies by factor. */
    private static Function<Integer, Integer> multiplierOf(int factor) {
        return n -> n * factor;
    }

    // =============================================================
    //  17. Passing Lambdas as Parameters
    //      Method accepts a functional interface — caller decides behaviour
    // =============================================================
    private static void passingLambdas() {
        // a) Pass a Consumer to a "process" utility
        List<String> names = List.of("Alice", "Bob", "Carol");
        processAll(names, s -> System.out.println("Hello, " + s + "!"));

        // b) Pass a Predicate to a custom filter
        List<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println("Evens: "    + filter(nums, n -> n % 2 == 0));
        System.out.println("Gt5:   "    + filter(nums, n -> n > 5));

        // c) Pass a Function to a "transform" utility
        System.out.println("Doubled: "  + transform(nums, n -> n * 2));
        System.out.println("Squared: "  + transform(nums, n -> n * n));

        // d) Pass a Comparator (which extends BiFunction-like interface)
        List<String> words = new ArrayList<>(List.of("banana", "kiwi", "apple", "fig"));
        sort(words, Comparator.comparingInt(String::length));
        System.out.println("By length: " + words);
    }

    /** General-purpose iterate-and-consume. */
    private static <T> void processAll(List<T> list, Consumer<T> action) {
        for (T item : list) action.accept(item);
    }

    /** Returns a new list with only the elements matching the predicate. */
    private static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : list) if (predicate.test(item)) result.add(item);
        return result;
    }

    /** Returns a new list where every element has been transformed. */
    private static <T, R> List<R> transform(List<T> list, Function<T, R> mapper) {
        List<R> result = new ArrayList<>();
        for (T item : list) result.add(mapper.apply(item));
        return result;
    }

    /** Sorts the list in-place using the given comparator. */
    private static <T> void sort(List<T> list, Comparator<T> comparator) {
        list.sort(comparator);
    }
}
