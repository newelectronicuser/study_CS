package com.codewithmosh.collections;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ================================================================
 *  CollectionDemo — Interview-Ready Java Collection Interface Cheat-Sheet
 * ================================================================
 *
 *  The Collection<E> interface is the ROOT of the List, Set, and Queue
 *  hierarchies. Every operation here works on ANY concrete collection.
 *
 *  Topics covered:
 *   1.  Core CRUD — add / remove / contains / size / isEmpty / clear
 *   2.  Bulk operations — addAll / removeAll / retainAll / containsAll
 *   3.  toArray() — converting a collection to an array
 *   4.  Iterating — for-each, iterator, forEach()
 *   5.  removeIf() — functional bulk removal (Java 8+)
 *   6.  Equals & identity (== vs equals)
 *   7.  Sorting any Collection via stream
 *   8.  Unmodifiable & synchronized wrappers
 *   9.  Collection → Stream pipeline
 *  10.  Defensive copying
 */
public class CollectionDemo {

    // =============================================================
    //  MAIN entry point
    // =============================================================
    public static void show() {
        System.out.println("\n--- 1. Core CRUD ---");
        coreCrud();

        System.out.println("\n--- 2. Bulk Operations ---");
        bulkOperations();

        System.out.println("\n--- 3. toArray() ---");
        toArrayDemo();

        System.out.println("\n--- 4. Iteration ---");
        iterationDemo();

        System.out.println("\n--- 5. removeIf() ---");
        removeIfDemo();

        System.out.println("\n--- 6. Equals & Identity ---");
        equalsAndIdentity();

        System.out.println("\n--- 7. Sorting via Stream ---");
        sortingViaStream();

        System.out.println("\n--- 8. Unmodifiable & Synchronized Wrappers ---");
        wrappersDemo();

        System.out.println("\n--- 9. Collection → Stream ---");
        streamPipelineDemo();

        System.out.println("\n--- 10. Defensive Copying ---");
        defensiveCopyDemo();
    }

    // =============================================================
    //  1. Core CRUD (methods on Collection<E>)
    // =============================================================
    private static void coreCrud() {
        // Use Collection<E> as the variable type when you only need the root API
        Collection<String> col = new ArrayList<>();

        // add() — returns true if the collection changed
        col.add("apple");
        col.add("banana");
        boolean changed = col.add("cherry");
        System.out.println("add returned: " + changed);   // true
        System.out.println("Collection:   " + col);       // [apple, banana, cherry]

        // size() / isEmpty()
        System.out.println("size:    " + col.size());     // 3
        System.out.println("isEmpty: " + col.isEmpty());  // false

        // contains() — O(n) for List; O(1) avg for HashSet
        System.out.println("contains 'banana': " + col.contains("banana")); // true
        System.out.println("contains 'grape':  " + col.contains("grape"));  // false

        // remove(Object) — removes the FIRST occurrence (List) or the element (Set)
        boolean removed = col.remove("banana");
        System.out.println("remove 'banana': " + removed + " | col: " + col);

        // clear()
        col.clear();
        System.out.println("After clear, isEmpty: " + col.isEmpty()); // true
    }

    // =============================================================
    //  2. Bulk Operations
    // =============================================================
    private static void bulkOperations() {
        Collection<String> col = new ArrayList<>(List.of("a", "b", "c", "d"));

        // Collections.addAll — varargs convenience
        Collections.addAll(col, "e", "f");
        System.out.println("After addAll: " + col); // [a, b, c, d, e, f]

        // addAll(Collection) — append another collection
        col.addAll(List.of("g", "h"));
        System.out.println("After addAll(list): " + col);

        // containsAll — true only if ALL elements are present
        System.out.println("containsAll [a,b]: " + col.containsAll(List.of("a", "b"))); // true
        System.out.println("containsAll [a,z]: " + col.containsAll(List.of("a", "z"))); // false

        // removeAll — removes every element that appears in the argument collection
        col.removeAll(List.of("e", "f", "g", "h"));
        System.out.println("After removeAll: " + col); // [a, b, c, d]

        // retainAll — keeps ONLY elements that appear in the argument collection
        col.retainAll(List.of("b", "c"));
        System.out.println("After retainAll [b,c]: " + col); // [b, c]
    }

    // =============================================================
    //  3. toArray()
    // =============================================================
    private static void toArrayDemo() {
        Collection<String> col = List.of("x", "y", "z");

        // toArray() — returns Object[]; requires casting
        Object[] raw = col.toArray();
        System.out.println("Object[]: " + Arrays.toString(raw));

        // toArray(T[] a) — returns typed array; preferred
        String[] typed = col.toArray(new String[0]); // pass empty array — JVM sizes it
        System.out.println("String[]: " + Arrays.toString(typed));

        // toArray(IntFunction) — Java 11+, cleanest syntax
        String[] typed11 = col.toArray(String[]::new);
        System.out.println("String[] (Java 11 method ref): " + Arrays.toString(typed11));
    }

    // =============================================================
    //  4. Iteration — three styles
    // =============================================================
    private static void iterationDemo() {
        Collection<String> col = new ArrayList<>(List.of("one", "two", "three", "four"));

        // a) Enhanced for-each (Iterable)
        System.out.print("for-each: ");
        for (String s : col) System.out.print(s + " ");
        System.out.println();

        // b) Iterator — allows safe removal during traversal
        Iterator<String> it = col.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if (s.length() > 3) it.remove(); // safe: removes "three" and "four"
        }
        System.out.println("After iterator remove (len>3): " + col); // [one, two]

        // c) forEach() — lambda, Java 8+ (for-each under the hood)
        col.forEach(s -> System.out.print("[" + s + "] "));
        System.out.println();

        // d) forEach with method reference
        col.forEach(System.out::println);
    }

    // =============================================================
    //  5. removeIf() — functional bulk removal (Java 8+)
    //     Preferred over an iterator loop for conditional removal
    // =============================================================
    private static void removeIfDemo() {
        Collection<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        // Remove all even numbers
        nums.removeIf(n -> n % 2 == 0);
        System.out.println("Odds only: " + nums); // [1, 3, 5, 7, 9]

        // Remove blank strings
        Collection<String> data = new ArrayList<>(Arrays.asList("Alice", "", "  ", "Bob", null));
        data.removeIf(s -> s == null || s.isBlank());
        System.out.println("Non-blank: " + data); // [Alice, Bob]

        // Remove domain-object by condition
        Collection<Customer> customers = new ArrayList<>(List.of(
            new Customer("Alice", "alice@gmail.com"),
            new Customer("Bob",   "bob@yahoo.com"),
            new Customer("Carol", "carol@gmail.com")
        ));
        customers.removeIf(c -> c.getEmail().endsWith("@yahoo.com"));
        System.out.println("Without yahoo: " + customers); // [Alice, Carol]
    }

    // =============================================================
    //  6. Equals & Identity
    // =============================================================
    private static void equalsAndIdentity() {
        Collection<String> a = new ArrayList<>(List.of("1", "2", "3"));
        Collection<String> b = new ArrayList<>(List.of("1", "2", "3"));
        Collection<String> c = new HashSet<>(List.of("1", "2", "3"));

        // == checks reference identity (are they the same object?)
        System.out.println("a == b (identity):  " + (a == b)); // false

        // equals() checks structural equality (same elements in same order for List)
        System.out.println("a.equals(b) (List): " + a.equals(b)); // true — same type + same order

        // List.equals(Set) is always false — different contract
        System.out.println("a.equals(c) (List vs Set): " + a.equals(c)); // false

        // hashCode — equal collections must have equal hash codes
        System.out.println("a.hashCode() == b.hashCode(): " + (a.hashCode() == b.hashCode())); // true
    }

    // =============================================================
    //  7. Sorting any Collection via Stream
    // =============================================================
    private static void sortingViaStream() {
        Collection<Customer> customers = new HashSet<>(List.of(
            new Customer("Zara",  "z@mail.com"),
            new Customer("Alice", "a@mail.com"),
            new Customer("Bob",   "b@mail.com")
        ));

        // Sets have no sort; convert via stream and collect
        List<Customer> sortedByName = customers.stream()
            .sorted()                                        // uses Comparable (name)
            .collect(Collectors.toList());
        System.out.println("Sorted by name: " + sortedByName);

        List<Customer> sortedByEmail = customers.stream()
            .sorted(Comparator.comparing(Customer::getEmail))
            .collect(Collectors.toList());
        System.out.println("Sorted by email: " + sortedByEmail);
    }

    // =============================================================
    //  8. Unmodifiable & Synchronized Wrappers
    // =============================================================
    private static void wrappersDemo() {
        Collection<String> mutable = new ArrayList<>(List.of("a", "b", "c"));

        // Unmodifiable — read-only VIEW (still reflects mutations to original)
        Collection<String> readOnly = Collections.unmodifiableCollection(mutable);
        System.out.println("Unmodifiable: " + readOnly);
        // readOnly.add("d"); // → UnsupportedOperationException

        mutable.add("d"); // the underlying list is still mutable
        System.out.println("ReadOnly sees mutation: " + readOnly); // [a, b, c, d]

        // For a true immutable snapshot use List.copyOf():
        Collection<String> snapshot = List.copyOf(mutable);
        mutable.add("e");
        System.out.println("Snapshot unchanged: " + snapshot); // [a, b, c, d]

        // Synchronized wrapper — thread-safe via single global lock (legacy)
        Collection<String> syncCol = Collections.synchronizedCollection(new ArrayList<>());
        syncCol.add("safe");
        // Iteration still requires explicit sync:
        synchronized (syncCol) {
            for (String s : syncCol) System.out.println("sync: " + s);
        }
    }

    // =============================================================
    //  9. Collection → Stream pipeline
    // =============================================================
    private static void streamPipelineDemo() {
        Collection<Customer> customers = List.of(
            new Customer("Alice", "alice@gmail.com"),
            new Customer("Bob",   "bob@yahoo.com"),
            new Customer("Carol", "carol@gmail.com"),
            new Customer("Dave",  "dave@gmail.com")
        );

        // filter + map → collect
        List<String> gmailNames = customers.stream()
            .filter(c -> c.getEmail().endsWith("@gmail.com"))
            .map(Customer::toString)
            .sorted()
            .collect(Collectors.toList());
        System.out.println("Gmail customers: " + gmailNames);

        // count
        long count = customers.stream()
            .filter(c -> c.getEmail().contains("gmail"))
            .count();
        System.out.println("Gmail count: " + count);

        // anyMatch / allMatch / noneMatch
        System.out.println("anyMatch yahoo: " + customers.stream()
            .anyMatch(c -> c.getEmail().endsWith("@yahoo.com")));
        System.out.println("noneMatch null: " + customers.stream()
            .noneMatch(c -> c.getEmail() == null));

        // joining names as CSV
        String csv = customers.stream()
            .map(Customer::toString)
            .collect(Collectors.joining(", "));
        System.out.println("Names CSV: " + csv);
    }

    // =============================================================
    //  10. Defensive Copying — prevent external mutation
    // =============================================================
    private static void defensiveCopyDemo() {
        // BAD: storing the caller's list directly — caller can mutate your field
        class BadService {
            private final Collection<String> items;
            BadService(Collection<String> items) {
                this.items = items; // aliased! caller's changes affect this
            }
        }

        // GOOD: make a defensive copy in the constructor
        class GoodService {
            private final Collection<String> items;
            GoodService(Collection<String> items) {
                this.items = List.copyOf(items); // immutable snapshot
            }
            Collection<String> getItems() {
                return Collections.unmodifiableCollection(items); // safe read-only view
            }
        }

        List<String> original = new ArrayList<>(List.of("a", "b", "c"));
        GoodService svc = new GoodService(original);
        original.add("d"); // does NOT affect svc
        System.out.println("Service items (unchanged): " + svc.getItems()); // [a, b, c]
    }
}
