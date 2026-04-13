package com.codewithmosh.generics;

import java.util.function.Supplier;

/**
 * ================================================================
 *  GenericsDemo — Interview-Ready Java Generics Cheat-Sheet
 * ================================================================
 *
 *  Existing project classes used:
 *    - List            — Object-based "poor" list (pre-generics style)
 *    - GenericList<T>  — Type-safe generic list implementing Iterable<T>
 *    - KeyValuePair<K,V>— Two-parameter generic class
 *    - User            — Implements Comparable<User>; sorted by points
 *    - Instructor      — Extends User (for wildcard demos)
 *    - Utils           — Static generic methods: max(), print(), printUsers()
 *
 *  Topics covered:
 *   1.  Object-based list (pre-generics pitfall)
 *   2.  Generic class — GenericList<T>
 *   3.  Generic class with multiple type params — KeyValuePair<K,V>
 *   4.  Generic method — Utils.max(), Utils.print()
 *   5.  Bounded type parameter — <T extends Comparable<T>>
 *   6.  GenericList<T> as Iterable — for-each loop
 *   7.  Upper bounded wildcard — <? extends User>
 *   8.  Lower bounded wildcard — <? super Instructor>
 *   9.  Unbounded wildcard — <?>
 *  10.  PECS principle demonstrated
 *  11.  Invariance — why GenericList<Instructor> ≠ GenericList<User>
 *  12.  Type erasure consequences
 *  13.  Generic method returning a lambda (Supplier<T>)
 *  14.  Common runtime exception gotchas
 */
public class GenericsDemo {

    // =============================================================
    //  MAIN entry point
    // =============================================================
    public static void show() {
        System.out.println("\n--- 1. Object-based List (pre-generics pitfall) ---");
        objectBasedListDemo();

        System.out.println("\n--- 2. GenericList<T> — type-safe ---");
        genericListDemo();

        System.out.println("\n--- 3. KeyValuePair<K,V> — multiple type params ---");
        keyValuePairDemo();

        System.out.println("\n--- 4. Generic methods (Utils.max, Utils.print) ---");
        genericMethodsDemo();

        System.out.println("\n--- 5. Bounded type parameter ---");
        boundedTypeDemo();

        System.out.println("\n--- 6. GenericList<T> as Iterable (for-each) ---");
        iterableDemo();

        System.out.println("\n--- 7. Upper bounded wildcard (? extends User) ---");
        upperBoundedWildcardDemo();

        System.out.println("\n--- 8. Lower bounded wildcard (? super Instructor) ---");
        lowerBoundedWildcardDemo();

        System.out.println("\n--- 9. Unbounded wildcard (?) ---");
        unboundedWildcardDemo();

        System.out.println("\n--- 10. PECS principle ---");
        pecsDemo();

        System.out.println("\n--- 11. Invariance ---");
        invarianceDemo();

        System.out.println("\n--- 12. Type erasure ---");
        typeErasureDemo();

        System.out.println("\n--- 13. Generic factory (Supplier<T>) ---");
        genericFactoryDemo();
    }

    // =============================================================
    //  1. Object-based List — the PRE-GENERICS pitfall
    //     No compile-time safety; must cast; bugs discovered at runtime
    // =============================================================
    private static void objectBasedListDemo() {
        List list = new List();      // uses Object[] internally

        list.add("Alice");
        list.add(42);                // accidentally added an int — no compile error!

        // Explicit cast required every time you retrieve
        String name = (String) list.get(0);  // OK
        System.out.println("Name: " + name);

        try {
            String bad = (String) list.get(1); // ClassCastException at RUNTIME
        } catch (ClassCastException e) {
            System.out.println("Pre-generics bug: ClassCastException caught at runtime!");
            System.out.println("→ Generics move this error to compile time.");
        }
    }

    // =============================================================
    //  2. GenericList<T> — type-safe, compile-time checked
    //     T is replaced with the actual type at compile time.
    //     Internally uses (T[]) new Object[10] — common generic array workaround.
    // =============================================================
    private static void genericListDemo() {
        // String list — compiler enforces only Strings
        GenericList<String> names = new GenericList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Carol");
        // names.add(42);  // ← COMPILE ERROR — caught immediately

        String first = names.get(0); // no cast needed
        System.out.println("First name: " + first);

        // Integer list — same class, different type argument
        GenericList<Integer> scores = new GenericList<>();
        scores.add(95);
        scores.add(80);
        scores.add(72);

        int top = scores.get(0);
        System.out.println("Top score: " + top);

        // User list — works with any reference type
        GenericList<User> users = new GenericList<>();
        users.add(new User(100));
        users.add(new User(250));
        System.out.println("User: " + users.get(1));
    }

    // =============================================================
    //  3. KeyValuePair<K,V> — two independent type parameters
    //     Each parameter is resolved independently at the call site
    // =============================================================
    private static void keyValuePairDemo() {
        // K=String, V=Integer
        KeyValuePair<String, Integer> score  = new KeyValuePair<>("Alice", 95);
        System.out.println("Pair (String, Integer): created");

        // K=Integer, V=Boolean
        KeyValuePair<Integer, Boolean> flag  = new KeyValuePair<>(1, true);
        System.out.println("Pair (Integer, Boolean): created");

        // K=String, V=User
        KeyValuePair<String, User> entry     = new KeyValuePair<>("user1", new User(200));
        System.out.println("Pair (String, User): created");

        // Real-world analogy: Map.Entry<K,V> is exactly this pattern
        System.out.println("→ Map.Entry<K,V> in the JCF uses the same two-param pattern");
    }

    // =============================================================
    //  4. Generic Methods — Utils.max() and Utils.print()
    //     Type parameter declared BEFORE the return type: <T> T method(...)
    //     Type argument is INFERRED at the call site.
    // =============================================================
    private static void genericMethodsDemo() {
        // Utils.max<T extends Comparable<T>> — works for any Comparable
        int    maxInt  = Utils.max(3, 7);
        double maxDbl  = Utils.max(3.14, 2.71);
        String maxStr  = Utils.max("apple", "cherry"); // lexicographic
        User   maxUser = Utils.max(new User(100), new User(250)); // uses User.compareTo

        System.out.println("max(3,7):              " + maxInt);
        System.out.println("max(3.14,2.71):        " + maxDbl);
        System.out.println("max(apple,cherry):     " + maxStr);
        System.out.println("max(User100,User250):  " + maxUser);

        // Utils.print<K,V> — two independent type params, for-print only
        Utils.print("name",  "Alice");   // K=String, V=String
        Utils.print("score", 95);        // K=String, V=Integer
        Utils.print(1,       true);      // K=Integer, V=Boolean

        // Explicit type argument (rarely needed — inference handles it normally)
        String result = GenericsDemo.<String>identity("explicit type arg");
        System.out.println("Explicit type arg: " + result);
    }

    /** Identity function — returns input unchanged; shows generic method anatomy. */
    public static <T> T identity(T value) { return value; }

    // =============================================================
    //  5. Bounded Type Parameter — <T extends Comparable<T>>
    //     Compiler guarantees T has compareTo() — so we can call it safely.
    //     Without the bound `extends Comparable<T>`, compareTo() would not compile.
    // =============================================================
    private static void boundedTypeDemo() {
        // Works because Integer, Double, String all implement Comparable
        System.out.println("max ints:    " + Utils.max(10, 30));
        System.out.println("max doubles: " + Utils.max(1.1, 9.9));
        System.out.println("max strings: " + Utils.max("mango", "apple"));
        System.out.println("max users:   " + Utils.max(new User(50), new User(150)));

        // Multiple bounds: class first, then interfaces
        // <T extends Number & Comparable<T>>  ← Number is the class bound
        System.out.println("\n→ Multiple bounds syntax: <T extends Number & Comparable<T>>");
        System.out.println("  Class bound must come FIRST, interfaces after &");
    }

    // =============================================================
    //  6. GenericList<T> implements Iterable<T> — for-each support
    //     The inner ListIterator class makes this work.
    // =============================================================
    private static void iterableDemo() {
        GenericList<String> languages = new GenericList<>();
        languages.add("Java");
        languages.add("Kotlin");
        languages.add("Scala");
        languages.add("Groovy");

        // Enhanced for-each works because GenericList implements Iterable<T>
        System.out.print("Languages: ");
        for (String lang : languages) {      // calls languages.iterator() internally
            System.out.print(lang + " ");
        }
        System.out.println();

        // Same with Users
        GenericList<User> users = new GenericList<>();
        users.add(new User(100));
        users.add(new User(200));
        System.out.print("Users: ");
        for (User u : users) System.out.print(u + " ");
        System.out.println();
    }

    // =============================================================
    //  7. Upper Bounded Wildcard — ? extends User
    //     Accept GenericList<User> OR GenericList<Instructor> (any subtype of User)
    //     READ ONLY — cannot add (compiler can't verify the exact subtype)
    // =============================================================
    private static void upperBoundedWildcardDemo() {
        GenericList<User>       users       = new GenericList<>();
        GenericList<Instructor> instructors = new GenericList<>();

        users.add(new User(100));
        instructors.add(new Instructor(300));

        // Utils.printUsers accepts GenericList<? extends User>
        // → works with both User and Instructor lists
        Utils.printUsers(users);        // GenericList<User>       — accepted ✅
        Utils.printUsers(instructors);  // GenericList<Instructor> — accepted ✅

        // Our own method using the same pattern
        printAllUsers(users);
        printAllUsers(instructors);
    }

    /** Upper bounded wildcard — accepts any GenericList of User or its subtypes. */
    private static void printAllUsers(GenericList<? extends User> list) {
        // Can READ elements as User (the upper bound)
        Object first = list.get(0); // safe — we know it's at least a User
        System.out.println("printAllUsers → " + first);

        // Cannot ADD — we don't know the exact subtype
        // list.add(new User(50));       // ← COMPILE ERROR
        // list.add(new Instructor(50)); // ← COMPILE ERROR
    }

    // =============================================================
    //  8. Lower Bounded Wildcard — ? super Instructor
    //     Accept GenericList<Instructor>, GenericList<User>, GenericList<Object>
    //     WRITE — can add Instructor (or subtypes); read only as Object
    // =============================================================
    private static void lowerBoundedWildcardDemo() {
        GenericList<User>   userList   = new GenericList<>();
        GenericList<Object> objectList = new GenericList<>();

        // Can ADD Instructor safely — list is guaranteed to hold at least Instructor
        addInstructors(userList);    // GenericList<User>   — ? super Instructor ✅
        addInstructors(objectList);  // GenericList<Object> — ? super Instructor ✅

        System.out.println("Lower wildcard: added Instructors to User and Object lists");
    }

    /** Lower bounded wildcard — can write Instructor into any list that holds User or above. */
    private static void addInstructors(GenericList<? super Instructor> list) {
        list.add(new Instructor(500));  // safe — list holds at least Instructor
        list.add(new Instructor(600));

        // Reading: can only guarantee Object — specific type is unknown
        Object obj = list.get(0);  // safe only as Object
        // User user = list.get(0); // ← COMPILE ERROR — might not be User
    }

    // =============================================================
    //  9. Unbounded Wildcard — ?
    //     Accept any parameterised type.
    //     Read as Object only. Cannot add (except null).
    //     Use when the logic is type-agnostic.
    // =============================================================
    private static void unboundedWildcardDemo() {
        GenericList<String>  strings  = new GenericList<>();
        GenericList<Integer> integers = new GenericList<>();
        GenericList<User>    users    = new GenericList<>();

        strings.add("hello"); strings.add("world");
        integers.add(1);  integers.add(2);
        users.add(new User(99));

        // Same method handles all three — type doesn't matter
        printSize(strings);
        printSize(integers);
        printSize(users);
    }

    /** Unbounded wildcard — only needs to know it's a GenericList; doesn't care about T. */
    private static void printSize(GenericList<?> list) {
        Object first = list.get(0); // can only read as Object
        System.out.println("First element (as Object): " + first);
    }

    // =============================================================
    //  10. PECS — Producer Extends, Consumer Super
    //      A helpful mnemonic for choosing wildcards:
    //      • Producer (you READ from it)  → use ? extends T
    //      • Consumer (you WRITE into it) → use ? super T
    // =============================================================
    private static void pecsDemo() {
        GenericList<Instructor> source = new GenericList<>();
        source.add(new Instructor(100));
        source.add(new Instructor(200));

        GenericList<User> dest = new GenericList<>();

        // copy: source is a Producer (we read from it) → ? extends Instructor
        //       dest   is a Consumer (we write into it) → ? super Instructor
        copy(source, dest);
        System.out.println("PECS copy: dest[0] = " + dest.get(0));
        System.out.println("PECS copy: dest[1] = " + dest.get(1));
    }

    /**
     * Copies all elements from src to dest.
     * src  is a Producer → ? extends T  (we only read from it)
     * dest is a Consumer → ? super T    (we only write into it)
     */
    private static <T> void copy(GenericList<? extends T> src,
                                  GenericList<? super T>   dest) {
        for (int i = 0; i < 2; i++) {
            dest.add(src.get(i));  // read from producer, write to consumer
        }
    }

    // =============================================================
    //  11. Invariance — GenericList<Instructor> is NOT-A GenericList<User>
    //      Even though Instructor IS-A User, the CONTAINERS are not compatible.
    //      This is why wildcards exist.
    // =============================================================
    private static void invarianceDemo() {
        GenericList<Instructor> instructors = new GenericList<>();
        instructors.add(new Instructor(300));

        // Invariance: cannot assign to GenericList<User>
        // GenericList<User> users = instructors; // ← COMPILE ERROR

        // Why? If it were allowed, this would be possible:
        // users.add(new User(0));              // adds a plain User to an Instructor list!
        // Instructor i = instructors.get(1);   // ClassCastException at runtime

        // Solution: use wildcards
        GenericList<? extends User> safeRef = instructors; // ✅ read-only view
        User u = safeRef.get(0); // safe — read as User
        System.out.println("Invariance: safe read via wildcard → " + u);
    }

    // =============================================================
    //  12. Type Erasure — what the JVM actually sees at runtime
    // =============================================================
    @SuppressWarnings("unchecked")
    private static void typeErasureDemo() {
        GenericList<String>  strList = new GenericList<>();
        GenericList<Integer> intList = new GenericList<>();

        // Both are the SAME class at runtime — generic info is erased
        System.out.println("Same class at runtime: "
            + (strList.getClass() == intList.getClass())); // true

        // Cannot use instanceof with parameterised type
        // if (strList instanceof GenericList<String>) { } // ← COMPILE ERROR
        if (strList instanceof GenericList<?>) {           // ✅ — unbounded wildcard OK
            System.out.println("instanceof GenericList<?> works");
        }

        // Generic array workaround — used inside GenericList<T> itself
        // T[] arr = new T[10];            // ← COMPILE ERROR — type erased
        Object[] arr = new Object[10];     // actual bytecode after erasure
        System.out.println("Generic array workaround: Object[] at runtime");
    }

    // =============================================================
    //  13. Generic Factory — returning lambdas / Supplier<T>
    //      Useful when you cannot do `new T()` (type erased).
    //      Pass a Supplier<T> to create instances instead.
    // =============================================================
    private static void genericFactoryDemo() {
        // Use Supplier<T> as a factory to create instances
        GenericList<User> users = createList(() -> new User(100));
        GenericList<Instructor> instructors = createList(() -> new Instructor(500));

        System.out.println("Factory User:       " + users.get(0));
        System.out.println("Factory Instructor: " + instructors.get(0));
    }

    /**
     * Cannot do `new T()` directly — type is erased. Pass a Supplier instead.
     * This is the standard workaround for generic instantiation.
     */
    private static <T> GenericList<T> createList(Supplier<T> factory) {
        GenericList<T> list = new GenericList<>();
        list.add(factory.get()); // factory creates the instance
        return list;
    }
}
