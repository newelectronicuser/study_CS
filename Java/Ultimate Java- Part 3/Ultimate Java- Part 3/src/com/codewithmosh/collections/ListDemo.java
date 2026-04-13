package com.codewithmosh.collections;

import java.util.*;

/**
 * ================================================================
 *  ListDemo — Interview-Ready Java List Cheat-Sheet
 * ================================================================
 *
 *  Topics covered:
 *   1.  ArrayList — CRUD, indexed access, capacity
 *   2.  LinkedList — addFirst/addLast/Deque usage
 *   3.  Comparable — natural ordering via Customer.compareTo()
 *   4.  Comparator — external ordering (lambda & EmailComparator)
 *   5.  Iterator & ListIterator — safe removal during traversal
 *   6.  subList — view slicing
 *   7.  Collections utilities — sort, reverse, shuffle, frequency, disjoint
 *   8.  List.of() / List.copyOf() — immutable lists (Java 9+)
 *   9.  ArrayList vs LinkedList — when to use which
 */
public class ListDemo {

    // =============================================================
    //  MAIN entry point
    // =============================================================
    public static void show() {
        System.out.println("\n--- 1. ArrayList CRUD ---");
        arrayListCrud();

        System.out.println("\n--- 2. LinkedList (Deque usage) ---");
        linkedListDemo();

        System.out.println("\n--- 3. Comparable (Natural Ordering) ---");
        comparableDemo();

        System.out.println("\n--- 4. Comparator (Custom Ordering) ---");
        comparatorDemo();

        System.out.println("\n--- 5. Iterator & ListIterator ---");
        iteratorDemo();

        System.out.println("\n--- 6. subList ---");
        subListDemo();

        System.out.println("\n--- 7. Collections Utilities ---");
        collectionsUtilitiesDemo();

        System.out.println("\n--- 8. Immutable Lists ---");
        immutableListDemo();
    }

    // =============================================================
    //  1. ArrayList — CRUD, indexed access
    // =============================================================
    private static void arrayListCrud() {
        // ArrayList: dynamic array, O(1) random access, O(n) insert/delete in middle
        List<String> list = new ArrayList<>();

        // add() — O(1) amortized (resize is O(n) but rare)
        list.add("Java");
        list.add("Python");
        list.add("Go");

        // add at index — shifts elements right: O(n)
        list.add(1, "Kotlin");
        System.out.println("After add at index 1: " + list); // [Java, Kotlin, Python, Go]

        // get() — O(1)
        System.out.println("get(0): " + list.get(0));

        // set() — O(1)
        list.set(0, "Java 21");
        System.out.println("After set(0): " + list);

        // remove by index — O(n) (shifts left)
        list.remove(2);
        System.out.println("After remove(2): " + list);

        // remove by value — O(n)
        list.remove("Go");
        System.out.println("After remove(\"Go\"): " + list);

        // indexOf / lastIndexOf
        Collections.addAll(list, "Java 21", "Rust", "Java 21");
        System.out.println("indexOf(\"Java 21\"): " + list.indexOf("Java 21"));
        System.out.println("lastIndexOf(\"Java 21\"): " + list.lastIndexOf("Java 21"));

        // contains, size, isEmpty
        System.out.println("contains(\"Rust\"): " + list.contains("Rust"));
        System.out.println("size: " + list.size());
        System.out.println("isEmpty: " + list.isEmpty());

        // clear
        list.clear();
        System.out.println("After clear, isEmpty: " + list.isEmpty());
    }

    // =============================================================
    //  2. LinkedList — doubly-linked, also implements Deque
    // =============================================================
    private static void linkedListDemo() {
        // LinkedList: O(1) add/remove at both ends, O(n) random access
        LinkedList<String> ll = new LinkedList<>();
        ll.add("B");
        ll.add("C");

        ll.addFirst("A");  // O(1)
        ll.addLast("D");   // O(1)
        System.out.println("LinkedList: " + ll); // [A, B, C, D]

        System.out.println("peekFirst: " + ll.peekFirst()); // A (no remove)
        System.out.println("peekLast:  " + ll.peekLast());  // D

        System.out.println("removeFirst: " + ll.removeFirst()); // A
        System.out.println("removeLast:  " + ll.removeLast());  // D
        System.out.println("After removes: " + ll); // [B, C]

        // Used as a Stack (LIFO)
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(1); stack.push(2); stack.push(3);
        System.out.println("Stack pop: " + stack.pop()); // 3 (LIFO)

        // Used as a Queue (FIFO)
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(1); queue.offer(2); queue.offer(3);
        System.out.println("Queue poll: " + queue.poll()); // 1 (FIFO)
    }

    // =============================================================
    //  3. Comparable — natural ordering
    //     Customer implements Comparable<Customer> (sorted by name)
    // =============================================================
    private static void comparableDemo() {
        var customers = new ArrayList<>(List.of(
            new Customer("Zara",  "z@mail.com"),
            new Customer("Alice", "a@mail.com"),
            new Customer("Bob",   "b@mail.com")
        ));

        // Collections.sort() uses compareTo() → sorts by name (natural order)
        Collections.sort(customers);
        System.out.println("Sorted by name (natural): " + customers); // [Alice, Bob, Zara]

        // list.sort() — same as Collections.sort(list, null)
        customers.sort(null);
        System.out.println("list.sort(null): " + customers);
    }

    // =============================================================
    //  4. Comparator — custom / multiple orderings
    // =============================================================
    private static void comparatorDemo() {
        var customers = new ArrayList<>(List.of(
            new Customer("Zara",  "z@mail.com"),
            new Customer("Alice", "c@mail.com"),
            new Customer("Bob",   "a@mail.com")
        ));

        // a) Named Comparator class (EmailComparator)
        customers.sort(new EmailComparator());
        System.out.println("Sorted by email (EmailComparator): " + customers);

        // b) Lambda comparator
        customers.sort((c1, c2) -> c1.getEmail().compareTo(c2.getEmail()));
        System.out.println("Sorted by email (lambda): " + customers);

        // c) Comparator.comparing() — method reference
        customers.sort(Comparator.comparing(Customer::getEmail));
        System.out.println("Sorted by email (comparing()): " + customers);

        // d) Reversed
        customers.sort(Comparator.comparing(Customer::getEmail).reversed());
        System.out.println("Sorted by email reversed: " + customers);

        // e) Multi-key: sort by name length, then alphabetically
        var words = new ArrayList<>(List.of("banana", "kiwi", "apple", "fig"));
        words.sort(Comparator.comparingInt(String::length)
                             .thenComparing(Comparator.naturalOrder()));
        System.out.println("Multi-key sort: " + words); // [fig, kiwi, apple, banana]
    }

    // =============================================================
    //  5. Iterator & ListIterator — safe modification during traversal
    // =============================================================
    private static void iteratorDemo() {
        List<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));

        // Remove even numbers safely during iteration
        // (for-each throws ConcurrentModificationException if you remove)
        Iterator<Integer> it = nums.iterator();
        while (it.hasNext()) {
            if (it.next() % 2 == 0) it.remove(); // safe remove
        }
        System.out.println("After removing evens: " + nums); // [1, 3, 5]

        // ListIterator — bidirectional; can also set/add
        List<String> items = new ArrayList<>(List.of("a", "b", "c"));
        ListIterator<String> lit = items.listIterator();
        while (lit.hasNext()) {
            String val = lit.next();
            lit.set(val.toUpperCase()); // replace during traversal
        }
        System.out.println("After toUpperCase: " + items); // [A, B, C]

        // Traverse backwards
        while (lit.hasPrevious()) System.out.print(lit.previous() + " "); // C B A
        System.out.println();
    }

    // =============================================================
    //  6. subList — a live view (backed by the original list)
    // =============================================================
    private static void subListDemo() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D", "E"));

        // subList(fromIndex inclusive, toIndex exclusive)
        List<String> view = list.subList(1, 4);
        System.out.println("subList(1,4): " + view); // [B, C, D]

        // Modifications to subList reflect in the original
        view.clear();
        System.out.println("Original after subList.clear(): " + list); // [A, E]

        // Practical: take a copy to avoid aliasing issues
        List<String> copy = new ArrayList<>(list.subList(0, list.size()));
        System.out.println("Copy of full list: " + copy);
    }

    // =============================================================
    //  7. Collections Utilities
    // =============================================================
    private static void collectionsUtilitiesDemo() {
        List<Integer> nums = new ArrayList<>(List.of(3, 1, 4, 1, 5, 9, 2, 6));

        Collections.sort(nums);
        System.out.println("sorted: " + nums);

        Collections.reverse(nums);
        System.out.println("reversed: " + nums);

        System.out.println("min: " + Collections.min(nums)); // 1
        System.out.println("max: " + Collections.max(nums)); // 9

        Collections.shuffle(nums);
        System.out.println("shuffled: " + nums);

        // frequency — count occurrences
        List<String> words = List.of("a", "b", "a", "c", "a");
        System.out.println("frequency of 'a': " + Collections.frequency(words, "a")); // 3

        // disjoint — true if no common elements
        System.out.println("disjoint: " + Collections.disjoint(List.of(1, 2), List.of(3, 4))); // true

        // nCopies — create a list of N copies of a value
        List<String> repeated = Collections.nCopies(4, "Java");
        System.out.println("nCopies: " + repeated); // [Java, Java, Java, Java]

        // unmodifiableList — read-only wrapper
        List<String> mutable = new ArrayList<>(List.of("x", "y"));
        List<String> readonly = Collections.unmodifiableList(mutable);
        // readonly.add("z"); // → UnsupportedOperationException
        System.out.println("unmodifiable: " + readonly);

        // binarySearch — list must be sorted first!
        List<Integer> sorted = new ArrayList<>(List.of(1, 3, 5, 7, 9));
        int idx = Collections.binarySearch(sorted, 7);
        System.out.println("binarySearch(7): index=" + idx); // 3
    }

    // =============================================================
    //  8. Immutable Lists (Java 9+)
    // =============================================================
    private static void immutableListDemo() {
        // List.of() — immutable, no nulls allowed, fixed size
        List<String> immutable = List.of("A", "B", "C");
        System.out.println("List.of: " + immutable);
        // immutable.add("D"); // → UnsupportedOperationException

        // List.copyOf() — makes an immutable copy of an existing collection
        List<String> mutable = new ArrayList<>(List.of("X", "Y"));
        List<String> copy = List.copyOf(mutable);
        mutable.add("Z"); // does NOT affect copy
        System.out.println("Original mutable: " + mutable);
        System.out.println("Immutable copy:   " + copy); // [X, Y]

        // Arrays.asList() — fixed-size but MUTABLE (set() works, add/remove throws)
        List<String> fixedSize = Arrays.asList("P", "Q", "R");
        fixedSize.set(0, "P!"); // allowed
        System.out.println("Arrays.asList (after set): " + fixedSize);
        // fixedSize.add("S"); // → UnsupportedOperationException
    }
}
