package com.codewithmosh.collections;

import java.util.*;

/**
 * ================================================================
 *  SetDemo — Interview-Ready Java Set Cheat-Sheet
 * ================================================================
 *
 *  Topics covered:
 *   1.  HashSet — basics, no duplicates, no order
 *   2.  LinkedHashSet — insertion-order preserved
 *   3.  TreeSet — sorted order, NavigableSet operations
 *   4.  Set math — union / intersection / difference / symmetric difference
 *   5.  equals() & hashCode() contract for custom objects in a Set
 *   6.  Set.of() — immutable sets (Java 9+)
 *   7.  Interview patterns — duplicate detection, unique elements
 */
public class SetDemo {

    // =============================================================
    //  MAIN entry point
    // =============================================================
    public static void show() {
        System.out.println("\n--- 1. HashSet (unordered, O(1) ops) ---");
        hashSetDemo();

        System.out.println("\n--- 2. LinkedHashSet (insertion order) ---");
        linkedHashSetDemo();

        System.out.println("\n--- 3. TreeSet (sorted, O(log n) ops) ---");
        treeSetDemo();

        System.out.println("\n--- 4. Set Math Operations ---");
        setMathDemo();

        System.out.println("\n--- 5. Immutable Sets ---");
        immutableSetDemo();

        System.out.println("\n--- 6. Interview Patterns ---");
        interviewPatternsDemo();
    }

    // =============================================================
    //  1. HashSet — backed by HashMap, O(1) avg for add/remove/contains
    // =============================================================
    private static void hashSetDemo() {
        Set<String> set = new HashSet<>();

        // add() — returns false if element already present
        System.out.println("add \"A\": " + set.add("A")); // true
        System.out.println("add \"B\": " + set.add("B")); // true
        System.out.println("add \"A\": " + set.add("A")); // false — duplicate ignored

        set.add("C");
        System.out.println("Set: " + set); // order is NOT guaranteed

        // contains — O(1) average
        System.out.println("contains \"B\": " + set.contains("B")); // true
        System.out.println("contains \"Z\": " + set.contains("Z")); // false

        // remove
        set.remove("B");
        System.out.println("After remove \"B\": " + set);

        // size, isEmpty
        System.out.println("size: " + set.size());
        System.out.println("isEmpty: " + set.isEmpty());

        // Iteration (order unpredictable)
        for (String s : set) System.out.print(s + " ");
        System.out.println();

        // HashSet allows ONE null
        set.add(null);
        System.out.println("Set with null: " + set);
    }

    // =============================================================
    //  2. LinkedHashSet — maintains insertion order
    //     Same O(1) performance as HashSet but deterministic iteration
    // =============================================================
    private static void linkedHashSetDemo() {
        Set<String> linked = new LinkedHashSet<>();
        linked.add("banana");
        linked.add("apple");
        linked.add("cherry");
        linked.add("apple"); // duplicate — ignored

        // Iteration is always in insertion order
        System.out.println("LinkedHashSet: " + linked); // [banana, apple, cherry]

        // Practical: deduplicate while preserving order
        List<String> words = List.of("the", "cat", "sat", "on", "the", "mat", "on");
        Set<String> unique = new LinkedHashSet<>(words);
        System.out.println("Unique in order: " + unique); // [the, cat, sat, on, mat]
    }

    // =============================================================
    //  3. TreeSet — Red-Black Tree, always sorted, O(log n) ops
    //     Implements NavigableSet (extends SortedSet)
    //     Custom objects MUST implement Comparable or provide a Comparator
    // =============================================================
    private static void treeSetDemo() {
        // Natural ordering
        TreeSet<Integer> ts = new TreeSet<>();
        ts.add(5); ts.add(2); ts.add(8); ts.add(1); ts.add(4);
        System.out.println("TreeSet (sorted): " + ts); // [1, 2, 4, 5, 8]

        // SortedSet views
        System.out.println("first: " + ts.first()); // 1
        System.out.println("last:  " + ts.last());  // 8
        System.out.println("headSet(5, excl): " + ts.headSet(5));          // [1, 2, 4]
        System.out.println("tailSet(4, incl):  " + ts.tailSet(4));         // [4, 5, 8]
        System.out.println("subSet(2,5):       " + ts.subSet(2, 5));       // [2, 4]

        // NavigableSet operations
        System.out.println("floor(3):   " + ts.floor(3));   // 2 (largest ≤ 3)
        System.out.println("ceiling(3): " + ts.ceiling(3)); // 4 (smallest ≥ 3)
        System.out.println("lower(4):   " + ts.lower(4));   // 2 (strictly < 4)
        System.out.println("higher(4):  " + ts.higher(4));  // 5 (strictly > 4)

        // Descending order
        System.out.println("descendingSet: " + ts.descendingSet()); // [8, 5, 4, 2, 1]

        // poll first/last (remove and return)
        System.out.println("pollFirst: " + ts.pollFirst()); // 1
        System.out.println("pollLast:  " + ts.pollLast());  // 8
        System.out.println("After polls: " + ts);            // [2, 4, 5]

        // TreeSet with custom Comparator (sort strings by length)
        TreeSet<String> byLength = new TreeSet<>(
            Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder())
        );
        byLength.add("banana"); byLength.add("fig"); byLength.add("apple"); byLength.add("kiwi");
        System.out.println("By length: " + byLength); // [fig, kiwi, apple, banana]

        // TreeSet with custom objects (uses Comparable — Customer sorts by name)
        TreeSet<Customer> customers = new TreeSet<>();
        customers.add(new Customer("Zara",  "z@mail.com"));
        customers.add(new Customer("Alice", "a@mail.com"));
        customers.add(new Customer("Bob",   "b@mail.com"));
        System.out.println("Customer TreeSet: " + customers); // [Alice, Bob, Zara]
    }

    // =============================================================
    //  4. Set Math Operations
    // =============================================================
    private static void setMathDemo() {
        Set<Integer> a = new HashSet<>(Set.of(1, 2, 3, 4, 5));
        Set<Integer> b = new HashSet<>(Set.of(4, 5, 6, 7, 8));

        // Union — all elements in EITHER set
        Set<Integer> union = new HashSet<>(a);
        union.addAll(b);
        System.out.println("Union:           " + new TreeSet<>(union)); // [1,2,3,4,5,6,7,8]

        // Intersection — only elements in BOTH sets
        Set<Integer> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        System.out.println("Intersection:    " + new TreeSet<>(intersection)); // [4, 5]

        // Difference — elements in A but NOT in B
        Set<Integer> difference = new HashSet<>(a);
        difference.removeAll(b);
        System.out.println("A minus B:       " + new TreeSet<>(difference)); // [1, 2, 3]

        // Symmetric difference — elements in EITHER but NOT BOTH
        Set<Integer> symDiff = new HashSet<>(union);
        symDiff.removeAll(intersection);
        System.out.println("Symmetric diff:  " + new TreeSet<>(symDiff)); // [1, 2, 3, 6, 7, 8]

        // isSubset — does A contain all of B?
        Set<Integer> sub = Set.of(4, 5);
        System.out.println("sub {4,5} ⊆ a? " + new HashSet<>(a).containsAll(sub)); // true

        // disjoint — do A and B share NO elements?
        Set<Integer> c = Set.of(10, 11);
        System.out.println("a disjoint c?   " + Collections.disjoint(a, c)); // true
    }

    // =============================================================
    //  5. Immutable Sets (Java 9+)
    // =============================================================
    private static void immutableSetDemo() {
        // Set.of() — immutable, no duplicates, no nulls
        Set<String> immutable = Set.of("A", "B", "C");
        System.out.println("Set.of: " + immutable);
        // immutable.add("D"); // → UnsupportedOperationException

        // Set.copyOf() — immutable copy of a mutable set
        Set<String> mutable = new HashSet<>(Set.of("X", "Y"));
        Set<String> copy = Set.copyOf(mutable);
        mutable.add("Z"); // does NOT affect copy
        System.out.println("Set.copyOf: " + copy); // {X, Y}
    }

    // =============================================================
    //  6. Interview Patterns
    // =============================================================
    private static void interviewPatternsDemo() {
        // a) Check for duplicates in O(n)
        int[] arr = {1, 2, 3, 4, 2};
        Set<Integer> seen = new HashSet<>();
        boolean hasDuplicate = false;
        for (int n : arr) {
            if (!seen.add(n)) { hasDuplicate = true; break; }
        }
        System.out.println("Has duplicate: " + hasDuplicate); // true

        // b) First non-repeating character
        String s = "aabbcde";
        Set<Character> repeated = new HashSet<>();
        Set<Character> unique   = new LinkedHashSet<>();
        for (char c : s.toCharArray()) {
            if (!repeated.contains(c)) {
                if (!unique.add(c)) { unique.remove(c); repeated.add(c); }
            }
        }
        char firstUnique = unique.isEmpty() ? '_' : unique.iterator().next();
        System.out.println("First unique char in \"" + s + "\": " + firstUnique); // c

        // c) Two-sum — check if any two numbers sum to target
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        Set<Integer> complements = new HashSet<>();
        boolean found = false;
        for (int n : nums) {
            if (complements.contains(target - n)) { found = true; break; }
            complements.add(n);
        }
        System.out.println("Two-sum " + target + " found: " + found); // true

        // d) Longest consecutive sequence — O(n) with HashSet
        int[] seq = {100, 4, 200, 1, 3, 2};
        Set<Integer> numSet = new HashSet<>();
        for (int n : seq) numSet.add(n);
        int longest = 0;
        for (int n : numSet) {
            if (!numSet.contains(n - 1)) { // start of a sequence
                int len = 1;
                while (numSet.contains(++n)) len++;
                longest = Math.max(longest, len);
            }
        }
        System.out.println("Longest consecutive in "
            + Arrays.toString(seq) + ": " + longest); // 4 (1,2,3,4)

        // e) Remove duplicates from a List preserving order
        List<Integer> withDups = List.of(3, 1, 4, 1, 5, 9, 2, 6, 5, 3);
        List<Integer> noDups = new ArrayList<>(new LinkedHashSet<>(withDups));
        System.out.println("Deduped list (ordered): " + noDups);
    }
}
